package wow.structural.adapter.adaptee;

/** Adapter pattern - Adaptee: a pretend third-party Discord library that we cannot change. */
public class DiscordWebhookClient {

    /** Its API does not match ChatChannel: it wants a URL and a JSON body. No real network here. */
    public void postJson(String webhookUrl, String jsonBody) {
        System.out.println("[Discord] POST " + webhookUrl + " " + jsonBody);
    }
}
