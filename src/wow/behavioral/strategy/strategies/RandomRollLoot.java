package wow.behavioral.strategy.strategies;

import java.util.List;
import java.util.Random;

import wow.behavioral.strategy.LootStrategy;
import wow.creational.builder.Character;
import wow.structural.decorator.Weapon;

/** Strategy pattern - Concrete Strategy: everybody rolls 1-100, the highest roll wins. */
public class RandomRollLoot implements LootStrategy {

    private static final int MAX_ROLL = 100;

    private final Random random;

    public RandomRollLoot() {
        this.random = new Random();
    }

    /** With a seed the rolls are the same every run (useful for the demo). */
    public RandomRollLoot(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public String getName() {
        return "Random Roll";
    }

    @Override
    public Character chooseWinner(List<Character> party, Weapon item) {
        Character winner = null;
        int bestRoll = 0;
        for (Character member : party) {
            int roll = random.nextInt(MAX_ROLL) + 1;
            System.out.println("[Loot] " + member.getName() + " rolls " + roll);
            if (roll > bestRoll) { // on a tie the earlier member keeps the item
                bestRoll = roll;
                winner = member;
            }
        }
        return winner;
    }
}
