package br.com.santos.vinicius.bowtruckleapi.event;

import br.com.santos.vinicius.bowtruckleapi.singleton.StreamSingleton;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwitchChannelGoLiveEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventSubscription = this.eventManager.onEvent(ChannelGoLiveEvent.class, event -> {
            StreamSingleton stream = StreamSingleton.getInstance();
            log.info(String.format("[%s] went live with title %s on game %s!",
                    event.getChannel().getName(), event.getStream().getTitle(), event.getStream().getGameId()));

            stream.goOnline(event);
        });
    }
}
