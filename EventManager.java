package wow.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/** Observer pattern - Subject: keeps the listeners and sends every event to all of them. */
public class EventManager {

    private final List<GameEventListener> listeners = new ArrayList<>();

    public void subscribe(GameEventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(GameEvent event) {
        // loop over a copy, so a listener may unsubscribe while we are sending
        List<GameEventListener> copy = new ArrayList<>(listeners);
        for (GameEventListener listener : copy) {
            listener.onEvent(event);
        }
    }
}
