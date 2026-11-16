package wow.structural.facade.subsystems;

import java.util.List;

import wow.creational.builder.Character;
import wow.creational.factorymethod.Dungeon;

/** Facade pattern - Subsystem: moves the party into a dungeon and back to the city. */
public class Teleporter {

    public void enter(List<Character> party, Dungeon dungeon) {
        String names = "";
        for (Character member : party) {
            if (!names.isEmpty()) {
                names = names + ", ";
            }
            names = names + member.getName();
        }
        System.out.println("[Teleport] " + names + " enter " + dungeon.getName());
        restoreAll(party);
    }

    public void returnToCity(List<Character> party) {
        String capital = party.get(0).getFaction().getCapital();
        System.out.println("[Teleport] The party returns to " + capital);
        restoreAll(party);
    }

    private void restoreAll(List<Character> party) {
        for (Character member : party) {
            member.restoreHealth();
        }
    }
}
