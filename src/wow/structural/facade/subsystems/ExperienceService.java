package wow.structural.facade.subsystems;

import java.util.List;

import wow.behavioral.observer.EventType;
import wow.behavioral.observer.GameEvent;
import wow.creational.builder.Character;
import wow.creational.singleton.WorldServer;

/** Facade pattern - Subsystem: gives experience to the party after a cleared dungeon. */
public class ExperienceService {

    public void grant(List<Character> party, int xp) {
        System.out.println("[XP] Each member gains " + xp + " XP");
        for (Character member : party) {
            int oldLevel = member.getLevel();
            int levelsGained = member.gainExperience(xp);
            // one LEVEL_UP event for every level the member gained
            for (int level = oldLevel + 1; level <= oldLevel + levelsGained; level++) {
                String message = member.getName() + " reached level " + level;
                GameEvent event = new GameEvent(EventType.LEVEL_UP, member.getName(), message);
                WorldServer.getInstance().getEvents().publish(event);
            }
        }
    }
}
