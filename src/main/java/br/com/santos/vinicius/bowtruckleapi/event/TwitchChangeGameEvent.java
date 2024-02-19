package br.com.santos.vinicius.bowtruckleapi.event;

import com.github.twitch4j.events.ChannelChangeGameEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TwitchChangeGameEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventSubscription = this.eventManager.onEvent(ChannelChangeGameEvent.class, changeGameEvent -> {

            log.info(String.format("Game changed! (%s)", changeGameEvent.getGameId()));

            this.streamService.changeGame(changeGameEvent);
        });
    }
}
