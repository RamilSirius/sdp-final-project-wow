package wow.behavioral.strategy.strategies;

import java.util.List;

import wow.behavioral.strategy.LootStrategy;
import wow.creational.builder.Character;
import wow.structural.decorator.Weapon;

/** Strategy pattern - Concrete Strategy: party members get items in turn. */
public class RoundRobinLoot implements LootStrategy {

    private Character lastWinner; // null until the first item is given

    @Override
    public String getName() {
        return "Round Robin";
    }

    @Override
    public Character chooseWinner(List<Character> party, Weapon item) {
        // the next member after the last winner; indexOf gives -1 when he is not in the list, so we start at 0
        int index = party.indexOf(lastWinner) + 1;
        if (index >= party.size()) {
            index = 0; // wrap around to the first member
        }
        Character winner = party.get(index);
        System.out.println("[Loot] Round Robin: it is " + winner.getName() + "'s turn (member "
                + (index + 1) + " of " + party.size() + ")");
        lastWinner = winner;
        return winner;
    }
}
