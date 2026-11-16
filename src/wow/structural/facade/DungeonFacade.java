package wow.structural.facade;

import java.util.List;

import wow.behavioral.strategy.LootStrategy;
import wow.behavioral.strategy.LootSystem;
import wow.creational.builder.Character;
import wow.creational.factorymethod.Dungeon;
import wow.creational.factorymethod.Enemy;
import wow.structural.facade.subsystems.CombatSystem;
import wow.structural.facade.subsystems.ExperienceService;
import wow.structural.facade.subsystems.Teleporter;

/** Facade pattern - Facade: one call runs a whole dungeon using all the subsystems. */
public class DungeonFacade {

    private static final int MAX_PARTY_SIZE = 5;

    private final Teleporter teleporter = new Teleporter();
    private final CombatSystem combat = new CombatSystem();
    private final LootSystem loot;
    private final ExperienceService experience = new ExperienceService();

    public DungeonFacade(LootStrategy initialStrategy) {
        this.loot = new LootSystem(initialStrategy);
    }

    public void setLootStrategy(LootStrategy strategy) {
        loot.setStrategy(strategy);
    }

    public String getLootStrategyName() {
        return loot.getStrategy().getName();
    }

    /** Returns true if the dungeon was cleared. */
    public boolean runDungeon(List<Character> party, Dungeon dungeon) {
        if (!canEnter(party)) {
            return false;
        }
        System.out.println("=== Dungeon run: " + dungeon.getName() + " ===");
        teleporter.enter(party, dungeon);
        try {
            for (Enemy enemy : dungeon.spawnEnemies()) {
                if (!combat.fight(party, enemy)) {
                    return false;
                }
            }
            loot.distribute(party, dungeon.dropBossLoot());
            experience.grant(party, dungeon.getExperienceReward());
            return true;
        } finally {
            // runs after a win AND after a wipe
            teleporter.returnToCity(party);
        }
    }

    private boolean canEnter(List<Character> party) {
        if (party.isEmpty() || party.size() > MAX_PARTY_SIZE) {
            System.out.println("[Dungeon] Cannot enter: a party needs 1 to " + MAX_PARTY_SIZE + " members");
            return false;
        }
        Character leader = party.get(0);
        for (Character member : party) {
            if (member.getFaction() != leader.getFaction()) {
                System.out.println("[Dungeon] Cannot enter: " + member.getName() + " is "
                        + member.getFaction().getDisplayName() + ", the party is "
                        + leader.getFaction().getDisplayName());
                return false;
            }
        }
        return true;
    }
}
