package br.com.santos.vinicius.bowtruckleapi.event;

import br.com.santos.vinicius.bowtruckleapi.singleton.StreamSingleton;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TwitchChangeGameEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventSubscription = this.eventManager.onEvent(ChannelChangeGameEvent.class, changeGameEvent -> {
            StreamSingleton stream = StreamSingleton.getInstance();
            log.info(String.format("Game changed! (%s)", changeGameEvent.getGameId()));

            stream.changeGame(changeGameEvent);
        });
    }
}
