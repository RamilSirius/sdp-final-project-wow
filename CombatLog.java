package wow.behavioral.observer.observers;

import wow.behavioral.observer.GameEvent;
import wow.behavioral.observer.GameEventListener;

import java.util.ArrayList;
import java.util.List;

/** Observer pattern - Concrete Observer: prints and remembers every event. */
public class CombatLog implements GameEventListener {

    private final List<String> history = new ArrayList<>();

    @Override
    public void onEvent(GameEvent event) {
        history.add(event.getMessage());
        System.out.println("[Combat Log] " + event.getMessage());
    }

    public int size() {
        return history.size();
    }
}
