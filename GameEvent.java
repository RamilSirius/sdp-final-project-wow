package wow.behavioral.observer;

/** Observer pattern - the message the Subject sends to every Observer (cannot be changed). */
public class GameEvent {

    private final EventType type;
    private final String subject;
    private final String message;

    public GameEvent(EventType type, String subject, String message) {
        this.type = type;
        this.subject = subject;
        this.message = message;
    }

    public EventType getType() {
        return type;
    }

    /** Who the event is about: a player name or an enemy name. */
    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
