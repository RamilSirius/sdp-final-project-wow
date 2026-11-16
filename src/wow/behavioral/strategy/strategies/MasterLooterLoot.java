package wow.behavioral.strategy.strategies;

import java.util.List;

import wow.behavioral.strategy.LootStrategy;
import wow.creational.builder.Character;
import wow.structural.decorator.Weapon;

/** Strategy pattern - Concrete Strategy: the master looter takes the item. */
public class MasterLooterLoot implements LootStrategy {

    private final Character masterLooter;

    public MasterLooterLoot(Character masterLooter) {
        this.masterLooter = masterLooter;
    }

    @Override
    public String getName() {
        return "Master Looter (" + masterLooter.getName() + ")";
    }

    @Override
    public Character chooseWinner(List<Character> party, Weapon item) {
        if (party.contains(masterLooter)) {
            return masterLooter;
        }
        // the master looter left the party (or died in this run), so the first member takes the item
        Character leader = party.get(0);
        System.out.println("[Loot] " + masterLooter.getName() + " cannot loot now, "
                + leader.getName() + " takes the item");
        return leader;
    }
}
