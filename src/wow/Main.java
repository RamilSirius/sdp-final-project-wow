package wow;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import wow.behavioral.observer.EventManager;
import wow.behavioral.observer.observers.AchievementTracker;
import wow.behavioral.observer.observers.CombatLog;
import wow.behavioral.observer.observers.GuildAnnouncer;
import wow.behavioral.strategy.strategies.MasterLooterLoot;
import wow.behavioral.strategy.strategies.RandomRollLoot;
import wow.behavioral.strategy.strategies.RoundRobinLoot;
import wow.creational.builder.Character;
import wow.creational.builder.CharacterBuilder;
import wow.creational.builder.attributes.HeroClass;
import wow.creational.builder.attributes.Race;
import wow.creational.factorymethod.Dungeon;
import wow.creational.factorymethod.Enemy;
import wow.creational.factorymethod.creators.Deadmines;
import wow.creational.factorymethod.creators.MoltenCore;
import wow.creational.factorymethod.creators.ShadowfangKeep;
import wow.creational.singleton.WorldServer;
import wow.structural.adapter.DiscordChatAdapter;
import wow.structural.adapter.InGameGuildChat;
import wow.structural.adapter.adaptee.DiscordWebhookClient;
import wow.structural.decorator.Weapon;
import wow.structural.decorator.decorators.CrusaderEnchantment;
import wow.structural.decorator.decorators.FieryEnchantment;
import wow.structural.decorator.decorators.SharpeningStone;
import wow.structural.facade.DungeonFacade;

/**
 * The only class outside the pattern packages: it connects all 8 patterns and tests them step by step.
 * "java -cp out wow.Main" runs every step at once, "java -cp out wow.Main step" waits for Enter after each step.
 */
public class Main {

    private static final String GUILD_NAME = "Sirius Raiders";
    private static final String DISCORD_URL = "https://discord.com/api/webhooks/sirius-raiders";
    private static final String DISCORD_BOT = "Sirius Bot";
    private static final long LOOT_SEED = 7; // fixed seed: the same dice rolls every run

    private static final Scanner INPUT = new Scanner(System.in);
    private static boolean stepByStep;
    private static int failedChecks;

    public static void main(String[] args) {
        stepByStep = args.length > 0 && args[0].equals("step");
        CombatLog combatLog = new CombatLog();
        AchievementTracker achievements = new AchievementTracker();
        subscribeObservers(combatLog, achievements);
        System.out.println("=== Azeroth Online - realm " + WorldServer.getInstance().getRealmName() + " ===");

        testSingleton();
        List<Character> party = testBuilder();
        testDecorator(party.get(0));
        testFactoryMethod();
        DungeonFacade facade = new DungeonFacade(new RoundRobinLoot());
        testFacade(facade, party);
        testStrategy(facade, party);
        testObserverAndAdapter(facade, party, achievements);
        printSummary(party, combatLog, achievements);
    }

    /** Observer + Adapter: every listener joins the one EventManager owned by the WorldServer singleton. */
    private static void subscribeObservers(CombatLog combatLog, AchievementTracker achievements) {
        GuildAnnouncer announcer = new GuildAnnouncer();
        announcer.addChannel(new InGameGuildChat(GUILD_NAME));
        announcer.addChannel(new DiscordChatAdapter(new DiscordWebhookClient(), DISCORD_URL, DISCORD_BOT));

        EventManager events = WorldServer.getInstance().getEvents();
        events.subscribe(combatLog);
        events.subscribe(achievements);
        events.subscribe(announcer);
    }

    private static void testSingleton() {
        step(1, "Singleton: one WorldServer for the whole game");
        WorldServer first = WorldServer.getInstance();
        WorldServer second = WorldServer.getInstance();
        System.out.println("[World] Realm: " + first.getRealmName());
        check("getInstance() always returns the same object", first == second);
    }

    private static List<Character> testBuilder() {
        step(2, "Builder: create heroes step by step (the observers already listen)");
        Character ramil = new CharacterBuilder().name("Ramil").race(Race.ORC).heroClass(HeroClass.WARRIOR)
                .guild(GUILD_NAME).title("the Kingslayer").build();
        List<Character> party = new ArrayList<>();
        party.add(ramil);
        party.add(createHero("Sylvanas", Race.UNDEAD, HeroClass.HUNTER));
        party.add(createHero("Baine", Race.TAUREN, HeroClass.PRIEST));
        party.add(createHero("Voljin", Race.TROLL, HeroClass.MAGE));
        for (Character member : party) {
            WorldServer.getInstance().login(member);
        }
        printParty(party);
        check("optional parts are set: " + ramil.getFullName(), ramil.getFullName().equals("Ramil the Kingslayer"));
        CharacterBuilder noName = new CharacterBuilder().name("").race(Race.ORC).heroClass(HeroClass.ROGUE);
        check("build() refuses a hero without a name", isRefused(noName));
        return party;
    }

    private static void testDecorator(Character hero) {
        step(3, "Decorator: enchant a weapon layer by layer");
        Weapon weapon = hero.getWeapon();
        int baseDamage = weapon.getDamage();
        printWeapon("Base weapon", weapon);
        weapon = new FieryEnchantment(weapon);
        printWeapon("+ Fiery Enchantment", weapon);
        weapon = new CrusaderEnchantment(weapon);
        printWeapon("+ Crusader Enchantment", weapon);
        weapon = new SharpeningStone(weapon);
        printWeapon("+ Sharpening Stone", weapon);
        hero.equip(weapon);
        System.out.println("[Enchant] " + hero.getName() + " equips it. His damage is now " + hero.getDamage());
        int bonus = FieryEnchantment.BONUS_DAMAGE + CrusaderEnchantment.BONUS_DAMAGE + SharpeningStone.BONUS_DAMAGE;
        check("three enchantments add " + bonus + " damage", weapon.getDamage() == baseDamage + bonus);
    }

    private static void testFactoryMethod() {
        step(4, "Factory Method: every dungeon creates its own enemies and boss loot");
        Dungeon[] dungeons = {new Deadmines(), new ShadowfangKeep(), new MoltenCore()};
        for (Dungeon dungeon : dungeons) {
            List<Enemy> enemies = dungeon.spawnEnemies();
            Enemy boss = enemies.get(enemies.size() - 1);
            Weapon loot = dungeon.dropBossLoot();
            System.out.println("[Dungeon] " + dungeon.getName() + ": " + dungeon.getTrashMobCount() + " x "
                    + enemies.get(0).getName() + ", boss " + boss.getName() + ", loot " + loot.getName()
                    + " (" + loot.getDamage() + " dmg)");
            check("the boss comes last in " + dungeon.getName(), boss.isBoss());
        }
    }

    private static void testFacade(DungeonFacade facade, List<Character> party) {
        step(5, "Facade: one call runs The Deadmines (Factory Method, Strategy, Observer, Adapter inside)");
        check("The Deadmines are cleared", facade.runDungeon(party, new Deadmines()));
        Character jaina = createHero("Jaina", Race.HUMAN, HeroClass.MAGE);
        WorldServer.getInstance().login(jaina);
        List<Character> mixedParty = new ArrayList<>(party);
        mixedParty.add(jaina);
        check("the facade refuses a Horde + Alliance party", !facade.runDungeon(mixedParty, new Deadmines()));
    }

    private static void testStrategy(DungeonFacade facade, List<Character> party) {
        step(6, "Strategy: change the loot method at runtime");
        facade.setLootStrategy(new RandomRollLoot(LOOT_SEED));
        System.out.println("[Loot] Loot method is now: " + facade.getLootStrategyName());
        check("Shadowfang Keep is cleared", facade.runDungeon(party, new ShadowfangKeep()));
        facade.setLootStrategy(new MasterLooterLoot(party.get(0)));
        System.out.println("[Loot] Loot method is now: " + facade.getLootStrategyName());
        check("The Deadmines are cleared again", facade.runDungeon(party, new Deadmines()));
    }

    private static void testObserverAndAdapter(DungeonFacade facade, List<Character> party,
                                               AchievementTracker achievements) {
        step(7, "Observer + Adapter: a wipe in Molten Core reaches the guild chat and Discord");
        check("a small party wipes in Molten Core", !facade.runDungeon(party, new MoltenCore()));
        check("the achievement tracker heard the wipe", achievements.getAchievements().contains("First Wipe"));
        check("'finally' brought everybody home with full health", hasFullHealth(party));
    }

    private static void printSummary(List<Character> party, CombatLog combatLog, AchievementTracker achievements) {
        step(8, "Summary: what the observers collected");
        for (String achievement : achievements.getAchievements()) {
            System.out.println("[Achievement] " + achievement);
        }
        System.out.println("[Combat Log] " + combatLog.size() + " events recorded");
        WorldServer world = WorldServer.getInstance();
        System.out.println("[World] " + world.getOnlinePlayers().size() + " players online in realm "
                + world.getRealmName());
        printParty(party);
        System.out.println();
        if (failedChecks == 0) {
            System.out.println("=== All checks passed ===");
        } else {
            System.out.println("=== " + failedChecks + " check(s) FAILED ===");
        }
    }

    // ----- helpers -----

    private static Character createHero(String name, Race race, HeroClass heroClass) {
        return new CharacterBuilder().name(name).race(race).heroClass(heroClass).build();
    }

    private static boolean isRefused(CharacterBuilder builder) {
        try {
            builder.build();
            return false;
        } catch (IllegalStateException e) {
            System.out.println("[Builder] Error: " + e.getMessage());
            return true;
        }
    }

    private static boolean hasFullHealth(List<Character> party) {
        for (Character member : party) {
            if (member.getCurrentHealth() != member.getMaxHealth()) {
                return false;
            }
        }
        return true;
    }

    private static void printParty(List<Character> party) {
        for (Character member : party) {
            System.out.println("[Party] " + member.describe());
        }
    }

    private static void printWeapon(String label, Weapon weapon) {
        System.out.println("[Enchant] " + label + ": " + weapon.getName() + " (" + weapon.getDamage() + " dmg)");
    }

    private static void check(String description, boolean passed) {
        if (passed) {
            System.out.println("[Check] OK: " + description);
        } else {
            failedChecks++;
            System.out.println("[Check] FAILED: " + description);
        }
    }

    /** Prints the step title. In "step" mode it first waits for Enter, so the defense can go step by step. */
    private static void step(int number, String title) {
        if (stepByStep && number > 1) {
            System.out.println("(press Enter for the next step)");
            if (INPUT.hasNextLine()) {
                INPUT.nextLine();
            }
        }
        System.out.println();
        System.out.println("--- Step " + number + ". " + title + " ---");
    }
}
