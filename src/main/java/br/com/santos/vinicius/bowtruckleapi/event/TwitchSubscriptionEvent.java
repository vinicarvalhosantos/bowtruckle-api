package br.com.santos.vinicius.bowtruckleapi.event;

import com.github.twitch4j.pubsub.events.ChannelSubscribeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwitchSubscriptionEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventManager.onEvent(ChannelSubscribeEvent.class, channelSubscribeEvent -> {
            log.info(String.format("%s subscribed for %d months", channelSubscribeEvent.getData().getUserName(), channelSubscribeEvent.getData().getCumulativeMonths()));
        });
    }
}
