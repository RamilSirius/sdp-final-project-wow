package wow.behavioral.observer;

/** Observer pattern - Observer: anything that wants to hear about game events. */
public interface GameEventListener {

    void onEvent(GameEvent event);
}
