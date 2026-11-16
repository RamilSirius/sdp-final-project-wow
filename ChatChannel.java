package wow.structural.adapter;

/** Adapter pattern - Target: the interface our game uses to send chat messages. */
public interface ChatChannel {

    void sendMessage(String text);
}
