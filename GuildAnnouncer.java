package wow.behavioral.observer.observers;

import java.util.ArrayList;
import java.util.List;

import wow.behavioral.observer.GameEvent;
import wow.behavioral.observer.GameEventListener;
import wow.structural.adapter.ChatChannel;

/** Observer pattern - Concrete Observer: sends important events to all chat channels. */
public class GuildAnnouncer implements GameEventListener {

    private final List<ChatChannel> channels = new ArrayList<>();

    public void addChannel(ChatChannel channel) {
        channels.add(channel);
    }

    @Override
    public void onEvent(GameEvent event) {
        switch (event.getType()) {
            case BOSS_DEFEATED:
            case PARTY_WIPED:
            case LOOT_RECEIVED:
            case LEVEL_UP:
                announce(event.getMessage());
                break;
            default:
                break; // logins and trash kills are not important for the guild
        }
    }

    private void announce(String text) {
        for (ChatChannel channel : channels) {
            channel.sendMessage(text);
        }
    }
}
