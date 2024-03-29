package br.com.santos.vinicius.bowtruckleapi.singleton;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

public class TwitchClientSingleton {

    public static TwitchClientSingleton singleInstance = null;

    public final TwitchClient client;

    public final String channelId;

    private TwitchClientSingleton(TwitchClient client) {
        this.client = client;
        this.channelId = "36413513";
    }

    public static TwitchClientSingleton getInstance() {
        if (singleInstance == null) {
            TwitchClient twitchClient = buildTwitchClient();
            singleInstance = new TwitchClientSingleton(twitchClient);
        }
        return singleInstance;
    }

    private static TwitchClient buildTwitchClient() {
        final String clientId = System.getenv("TWITCH_CLIENT_ID");
        final String clientSecret = System.getenv("TWITCH_CLIENT_SECRET");
        final String accessToken = System.getenv("TWITCH_OAUTH_CLIENT");
        final String channel = "mrfalll";
        final OAuth2Credential credential = new OAuth2Credential(channel, accessToken);

        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withDefaultAuthToken(credential)
                .withEnableChat(true)
                .withEnableHelix(true)
                .withEnableEventSocket(true)
                .build();

        twitchClient.getClientHelper().enableStreamEventListener(channel);
        twitchClient.getChat().joinChannel(channel);

        return twitchClient;
    }
}
