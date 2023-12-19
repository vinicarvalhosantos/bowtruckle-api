package br.com.santos.vinicius.bowtruckleapi.event;

import br.com.santos.vinicius.bowtruckleapi.singleton.StreamSingleton;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwitchChannelGoOfflineEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventSubscription = this.eventManager.onEvent(ChannelGoOfflineEvent.class, event -> {
            log.info(String.format("[%s] just went offline!", event.getChannel().getName()));
            StreamSingleton stream = StreamSingleton.getInstance();
            stream.wentOffline();
        });
    }

}
