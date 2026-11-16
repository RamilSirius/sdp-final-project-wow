package wow.structural.adapter;

import wow.structural.adapter.adaptee.DiscordWebhookClient;

/** Adapter pattern - Adapter: lets the game send ChatChannel messages to Discord. */
public class DiscordChatAdapter implements ChatChannel {

    private final DiscordWebhookClient client;
    private final String webhookUrl;
    private final String botName;

    public DiscordChatAdapter(DiscordWebhookClient client, String webhookUrl, String botName) {
        this.client = client;
        this.webhookUrl = webhookUrl;
        this.botName = botName;
    }

    @Override
    public void sendMessage(String text) {
        // translate our simple call into the call the Discord library understands
        String json = "{\"username\":\"" + escape(botName) + "\",\"content\":\"" + escape(text) + "\"}";
        client.postJson(webhookUrl, json);
    }

    /** JSON needs a backslash before \ and ", and line breaks and tabs written as \n, \r and \t. */
    private String escape(String text) {
        String result = text.replace("\\", "\\\\"); // must be first, or it would double the other backslashes
        result = result.replace("\"", "\\\"");
        result = result.replace("\n", "\\n");
        result = result.replace("\r", "\\r");
        return result.replace("\t", "\\t");
    }
}
