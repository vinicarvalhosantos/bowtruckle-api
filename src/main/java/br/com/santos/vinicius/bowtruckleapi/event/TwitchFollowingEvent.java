package br.com.santos.vinicius.bowtruckleapi.event;

import com.github.twitch4j.pubsub.events.FollowingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwitchFollowingEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventManager.onEvent(FollowingEvent.class, followingEvent -> {
            log.info(String.format("%s followed the channel", followingEvent.getData().getUsername()));
        });
    }
}
