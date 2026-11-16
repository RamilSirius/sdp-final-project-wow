package wow.behavioral.strategy;

import java.util.List;

import wow.creational.builder.Character;
import wow.structural.decorator.Weapon;

/** Strategy pattern - Strategy: a rule that decides who gets the loot. */
public interface LootStrategy {

    String getName();

    // item is passed so a strategy may look at it (e.g. staffs only for mages); our three strategies do not need it
    Character chooseWinner(List<Character> party, Weapon item);
}
