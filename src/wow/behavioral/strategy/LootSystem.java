package wow.behavioral.strategy;

import java.util.ArrayList;
import java.util.List;

import wow.behavioral.observer.EventType;
import wow.behavioral.observer.GameEvent;
import wow.creational.builder.Character;
import wow.creational.singleton.WorldServer;
import wow.structural.decorator.Weapon;

/** Strategy pattern - Context: gives loot using the current strategy (DungeonFacade uses it as a subsystem). */
public class LootSystem {

    private LootStrategy strategy;

    public LootSystem(LootStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(LootStrategy strategy) {
        this.strategy = strategy;
    }

    public LootStrategy getStrategy() {
        return strategy;
    }

    public void distribute(List<Character> party, Weapon item) {
        System.out.println("[Loot] " + item.getName() + " (" + item.getDamage() + " dmg) dropped. Loot method: "
                + strategy.getName());
        Character winner = strategy.chooseWinner(livingMembers(party), item);
        winner.receiveLoot(item);
        String message = winner.getName() + " receives " + item.getName();
        GameEvent event = new GameEvent(EventType.LOOT_RECEIVED, winner.getName(), message);
        WorldServer.getInstance().getEvents().publish(event);
    }

    /** Dead members cannot loot. After a win at least one member is alive (the party hits first). */
    private List<Character> livingMembers(List<Character> party) {
        List<Character> living = new ArrayList<>();
        for (Character member : party) {
            if (member.isAlive()) {
                living.add(member);
            }
        }
        return living;
    }
}
