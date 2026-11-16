package wow.behavioral.observer.observers;

import wow.behavioral.observer.GameEvent;
import wow.behavioral.observer.GameEventListener;

import java.util.LinkedHashSet;
import java.util.Set;

/** Observer pattern - Concrete Observer: unlocks achievements when special events happen. */
public class AchievementTracker implements GameEventListener {

    // LinkedHashSet: no duplicates and keeps the unlock order
    private final Set<String> achievements = new LinkedHashSet<>();

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case BOSS_DEFEATED:
                unlock("Boss Slayer: " + event.getSubject());
                break;
            case PARTY_WIPED:
                unlock("First Wipe");
                break;
            case LEVEL_UP:
                unlock("Getting Stronger: " + event.getSubject());
                break;
            default:
                break;
        }
    }

    private void unlock(String name) {
        // add() returns false if the achievement is already unlocked
        if (achievements.add(name)) {
            System.out.println("[Achievement] Unlocked: " + name);
        }
    }

    public Set<String> getAchievements() {
        return new LinkedHashSet<>(achievements);
    }
}
