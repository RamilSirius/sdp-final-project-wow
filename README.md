# Azeroth Online - SDP Final Project (World of Warcraft MMORPG)

Team final project for the **Software Design Patterns** course (Astana IT University, group SE-2510).

## The story

Welcome to the realm **Sirius**. Heroes are created, they join a Horde party - Ramil, Sylvanas, Baine and Voljin -
enchant their weapons and go into dungeons. The party fights trash mobs and a boss, the boss drops loot,
everybody gains experience. The guild chat and a Discord channel hear about every boss kill, every item and every wipe.
The first living member of the party takes the hits of the enemies, so the leader is a Warrior (the tank).
Some places are too dangerous: in **Molten Core** a small party always wipes.

The game is a console simulator written in plain Java 17 (no libraries, no build tool).
It uses **8 design patterns** of all three types that work together. Every team member implemented at least two of them.

## Team

| Team member | Patterns | Packages |
|-------------|----------|----------|
| Symbat Tunggushbay | Singleton, Builder, Factory Method | `creational/singleton`, `creational/builder`, `creational/factorymethod` |
| Yerkhan Ibragim | Decorator, Adapter, Observer | `structural/decorator`, `structural/adapter`, `behavioral/observer` |
| Ramil Zhunussov | Facade, Strategy + `Main` | `structural/facade`, `behavioral/strategy`, `Main.java` |

Every member committed his or her own packages to this repository.

## Patterns

| Type | Pattern | Author | Package | Where it is used |
|------|---------|--------|---------|------------------|
| Creational | Singleton | Symbat | `creational/singleton` | `WorldServer` is the one realm server of the game. It owns the `EventManager`, so the subsystems publish events through `WorldServer.getInstance().getEvents()` |
| Creational | Builder | Symbat | `creational/builder` | `CharacterBuilder` creates a `Character` step by step: required name, race, class; optional guild, title, level, weapon. `build()` checks the data. The constructor of `Character` is package-private, so a hero can only be created through the builder |
| Creational | Factory Method | Symbat | `creational/factorymethod` | The abstract `Dungeon` (creator) calls `createTrashMob()`, `createBoss()` and `createBossLoot()` in `spawnEnemies()`; every dungeon in `creators/` decides what to create. Products: `Enemy` and the boss loot `Weapon` (Shadowfang Keep drops an already enchanted weapon) |
| Structural | Decorator | Yerkhan | `structural/decorator` | Enchantments in `decorators/` wrap a `Weapon` and add damage. They can be stacked |
| Structural | Adapter | Yerkhan | `structural/adapter` | The game sends messages to any `ChatChannel`. `DiscordChatAdapter` turns them into calls of `DiscordWebhookClient` (`adaptee/`), which plays a third-party library we cannot edit |
| Structural | Facade | Ramil | `structural/facade` | `DungeonFacade.runDungeon(party, dungeon)` runs the whole dungeon with one call, using the classes in `subsystems/` and the loot context from the Strategy package |
| Behavioral | Strategy | Ramil | `behavioral/strategy` | `LootSystem` (context) asks the current `LootStrategy` who gets the boss loot. The strategies in `strategies/` can be changed at runtime through the facade |
| Behavioral | Observer | Yerkhan | `behavioral/observer` | `EventManager` (subject) sends every `GameEvent` to the observers in `observers/`: the combat log, the achievements and the guild announcer |

## Project structure

Every pattern has its own package, grouped by pattern type; the sub-folders follow the roles of the pattern.
`Main` is the only class outside the pattern packages.

```
src/wow
|-- Main.java                         // the only class outside: connects the patterns and tests all of them
|-- creational
|   |-- singleton
|   |   `-- WorldServer.java          // Singleton
|   |-- builder
|   |   |-- CharacterBuilder.java     // Builder
|   |   |-- Character.java            // Product
|   |   `-- attributes                // parts of the product
|   |       |-- Faction.java          //   enum: Alliance / Horde with capital city
|   |       |-- Race.java             //   enum: race -> faction
|   |       `-- HeroClass.java        //   enum: class stats and starter weapon
|   `-- factorymethod
|       |-- Dungeon.java              // Creator (declares the factory methods)
|       |-- Enemy.java                // Product
|       `-- creators                  // Concrete Creators
|           |-- Deadmines.java
|           |-- ShadowfangKeep.java
|           `-- MoltenCore.java
|-- structural
|   |-- decorator
|   |   |-- Weapon.java               // Component
|   |   |-- BaseWeapon.java           // Concrete Component
|   |   |-- WeaponEnchantment.java    // Base Decorator
|   |   `-- decorators                // Concrete Decorators
|   |       |-- FieryEnchantment.java
|   |       |-- CrusaderEnchantment.java
|   |       `-- SharpeningStone.java
|   |-- adapter
|   |   |-- ChatChannel.java          // Target
|   |   |-- InGameGuildChat.java      // a normal Target implementation
|   |   |-- DiscordChatAdapter.java   // Adapter
|   |   `-- adaptee
|   |       `-- DiscordWebhookClient.java // Adaptee (pretend third-party library)
|   `-- facade
|       |-- DungeonFacade.java        // Facade
|       `-- subsystems                // Subsystems
|           |-- Teleporter.java
|           |-- CombatSystem.java
|           `-- ExperienceService.java
`-- behavioral
    |-- strategy
    |   |-- LootStrategy.java         // Strategy
    |   |-- LootSystem.java           // Context (also used by the Facade)
    |   `-- strategies                // Concrete Strategies
    |       |-- RoundRobinLoot.java
    |       |-- RandomRollLoot.java
    |       `-- MasterLooterLoot.java
    `-- observer
        |-- EventManager.java         // Subject
        |-- GameEventListener.java    // Observer
        |-- GameEvent.java            // the data sent to the observers
        |-- EventType.java            // kinds of events
        `-- observers                 // Concrete Observers
            |-- CombatLog.java
            |-- AchievementTracker.java
            `-- GuildAnnouncer.java   // also feeds the Adapter
```

## How the patterns work together

What happens in one call of `DungeonFacade.runDungeon(party, dungeon)`:

1. **Facade** checks the party: 1-5 members, all from the same faction (heroes were made by the **Builder**).
2. `Teleporter` moves the party into the dungeon and restores health.
3. The dungeon creates its enemies with its **Factory Methods** (`spawnEnemies()`).
4. `CombatSystem` fights every enemy. The damage of a hero includes the weapon damage,
   so **Decorator** enchantments make the party stronger.
5. Every kill, boss kill or wipe is published as a `GameEvent` through the **Singleton** `WorldServer`
   to the **Observers**: `CombatLog`, `AchievementTracker` and `GuildAnnouncer`.
6. `GuildAnnouncer` sends the important events to the guild chat and, through the **Adapter**, to Discord.
7. After the boss, `LootSystem` asks the current **Strategy** which living member gets the boss loot (created by a factory method).
8. `ExperienceService` gives XP; every level up is an event too.
9. A `finally` block always teleports the party back to the capital - after a win and after a wipe.

## Clean code and SOLID

- **S - Single Responsibility:** each class does one job: `Teleporter` only moves the party, `CombatSystem` only fights,
  `ExperienceService` only gives XP, `CombatLog` only records events.
- **O - Open/Closed:** a new dungeon, enchantment, loot method, observer or chat channel is one new class.
  `DungeonFacade`, `LootSystem`, `EventManager` and `GuildAnnouncer` do not change.
- **L - Liskov Substitution:** an enchanted weapon can be used everywhere a `Weapon` is expected;
  every `Dungeon` subclass works in `runDungeon`.
- **I - Interface Segregation:** small interfaces with one or two methods: `ChatChannel`, `GameEventListener`,
  `LootStrategy`, `Weapon`.
- **D - Dependency Inversion:** high-level classes depend on interfaces, not on concrete classes:
  `LootSystem` on `LootStrategy`, `GuildAnnouncer` on `ChatChannel`, `EventManager` on `GameEventListener`.
- **KISS:** plain Java, no frameworks, short methods; the builder is a simple fluent builder without a separate Director.
- **DRY:** shared code lives in one place: the dungeon loop is written once in `Dungeon.spawnEnemies()`,
  the delegation is written once in `WeaponEnchantment`, the party check once in `DungeonFacade.canEnter()`.
- **Clean code:** meaningful names, constants instead of magic numbers (`MAX_PARTY_SIZE`, `MAX_ROUNDS`, `BONUS_DAMAGE`),
  no `if`/`switch` on the type of a strategy, private fields and immutable events.

## Report

The full report (architecture, every pattern with code and UML, screenshots, testing, conclusion and further work):
[docs/SDP_Final_Project_Report.pdf](docs/SDP_Final_Project_Report.pdf).

## UML diagrams

PlantUML sources and PNGs are in `docs/uml/`: an overview of the whole project, two sequence diagrams
(`seq-run-dungeon`, `seq-events`) and one class diagram per pattern.

## Run

```bash
javac -d out $(find src -name "*.java")       # macOS / Linux
javac -d out -sourcepath src src/wow/Main.java # the same, also works in Windows cmd and PowerShell
java -cp out wow.Main                          # runs all 8 steps and the checks
java -cp out wow.Main step                     # waits for Enter after every step (for the defense)
```

In IntelliJ IDEA: open the folder and run `Main`.

`Main` tests every pattern in its own step and prints a `[Check]` line for every expected result.
The last line is `=== All checks passed ===`.

## Sample of the output

Shortened: `...` marks left-out lines.

```
--- Step 1. Singleton: one WorldServer for the whole game ---
[World] Realm: Sirius
[Check] OK: getInstance() always returns the same object
...
--- Step 3. Decorator: enchant a weapon layer by layer ---
[Enchant] Base weapon: Worn Shortsword (6 dmg)
[Enchant] + Fiery Enchantment: Worn Shortsword + Fiery (18 dmg)
[Enchant] + Crusader Enchantment: Worn Shortsword + Fiery + Crusader (38 dmg)
[Enchant] + Sharpening Stone: Worn Shortsword + Fiery + Crusader + Sharpened (43 dmg)
[Enchant] Ramil equips it. His damage is now 65
[Check] OK: three enchantments add 37 damage

--- Step 4. Factory Method: every dungeon creates its own enemies and boss loot ---
[Dungeon] The Deadmines: 3 x Defias Pirate, boss Edwin VanCleef, loot Cruel Barb (30 dmg)
[Check] OK: the boss comes last in The Deadmines
[Dungeon] Shadowfang Keep: 3 x Shadowfang Worgen, boss Archmage Arugal, loot Shadowfang + Fiery (42 dmg)
[Check] OK: the boss comes last in Shadowfang Keep
...
--- Step 5. Facade: one call runs The Deadmines (Factory Method, Strategy, Observer, Adapter inside) ---
=== Dungeon run: The Deadmines ===
[Teleport] Ramil, Sylvanas, Baine, Voljin enter The Deadmines
...
[Combat] Edwin VanCleef is defeated in round 5
[Combat Log] The party has defeated Edwin VanCleef!
[Achievement] Unlocked: Boss Slayer: Edwin VanCleef
[Guild] <Sirius Raiders> The party has defeated Edwin VanCleef!
[Discord] POST https://discord.com/api/webhooks/sirius-raiders {"username":"Sirius Bot","content":"The party has defeated Edwin VanCleef!"}
[Loot] Cruel Barb (30 dmg) dropped. Loot method: Round Robin
[Loot] Round Robin: it is Ramil's turn (member 1 of 4)
...
[Teleport] The party returns to Orgrimmar
[Check] OK: The Deadmines are cleared
[World] Jaina logged in to realm Sirius
[Combat Log] Jaina entered the world
[Dungeon] Cannot enter: Jaina is Alliance, the party is Horde
[Check] OK: the facade refuses a Horde + Alliance party
...
--- Step 7. Observer + Adapter: a wipe in Molten Core reaches the guild chat and Discord ---
=== Dungeon run: Molten Core ===
...
[Combat] The party is wiped out after 13 rounds
[Combat Log] The party was wiped out by Molten Giant
[Achievement] Unlocked: First Wipe
[Guild] <Sirius Raiders> The party was wiped out by Molten Giant
[Discord] POST https://discord.com/api/webhooks/sirius-raiders {"username":"Sirius Bot","content":"The party was wiped out by Molten Giant"}
[Teleport] The party returns to Orgrimmar
[Check] OK: a small party wipes in Molten Core
[Check] OK: the achievement tracker heard the wipe
[Check] OK: 'finally' brought everybody home with full health
...
=== All checks passed ===
```
