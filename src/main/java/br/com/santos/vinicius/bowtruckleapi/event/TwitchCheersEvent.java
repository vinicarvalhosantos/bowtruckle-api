package br.com.santos.vinicius.bowtruckleapi.event;

import com.github.twitch4j.pubsub.events.ChannelBitsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwitchCheersEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventManager.onEvent(ChannelBitsEvent.class, (bitsEvent -> {
            log.info(String.format("%s sent %d cheers", bitsEvent.getData().getUserName(), bitsEvent.getData().getTotalBitsUsed()));
        }));
    }
}
