package wow.creational.singleton;

import java.util.ArrayList;
import java.util.List;

import wow.behavioral.observer.EventManager;
import wow.behavioral.observer.EventType;
import wow.behavioral.observer.GameEvent;
import wow.creational.builder.Character;

/** Singleton pattern - Singleton: the one realm server of the whole game. */
public final class WorldServer {

    private static final WorldServer INSTANCE = new WorldServer();

    private static final String REALM_NAME = "Sirius";

    private final EventManager events = new EventManager();
    private final List<Character> onlinePlayers = new ArrayList<>();

    private WorldServer() {
        // private: nobody outside can call "new WorldServer()"
    }

    public static WorldServer getInstance() {
        return INSTANCE;
    }

    public String getRealmName() {
        return REALM_NAME;
    }

    public EventManager getEvents() {
        return events;
    }

    public List<Character> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers);
    }

    public void login(Character character) {
        onlinePlayers.add(character);
        System.out.println("[World] " + character.getName() + " logged in to realm " + REALM_NAME);
        String message = character.getFullName() + " entered the world";
        events.publish(new GameEvent(EventType.PLAYER_LOGGED_IN, character.getName(), message));
    }
}
