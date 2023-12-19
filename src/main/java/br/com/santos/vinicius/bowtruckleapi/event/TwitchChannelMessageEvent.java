package br.com.santos.vinicius.bowtruckleapi.event;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwitchChannelMessageEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventSubscription = this.eventManager.onEvent(ChannelMessageEvent.class, messageEvent -> {
            /*log.info(String.format("[Tier %s, %s Months]%s: %s", messageEvent.getSubscriptionTier(), messageEvent.getSubscriberMonths(),
                    messageEvent.getUser().getName(), messageEvent.getMessage()));*/

        });
    }
}
