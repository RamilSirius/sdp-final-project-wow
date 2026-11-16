package wow.structural.adapter;

/** Adapter pattern - a normal Target implementation: the guild chat inside the game. */
public class InGameGuildChat implements ChatChannel {

    private final String guildName;

    public InGameGuildChat(String guildName) {
        this.guildName = guildName;
    }

    @Override
    public void sendMessage(String text) {
        System.out.println("[Guild] <" + guildName + "> " + text);
    }
}
