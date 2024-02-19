package br.com.santos.vinicius.bowtruckleapi.event;

import br.com.santos.vinicius.bowtruckleapi.task.StreamIdleTask;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Slf4j
@Component
public class TwitchChannelGoOfflineEvent extends TwitchEvent {

    private final int DELAY_IN_MINUTES = 10;

    @Override
    public void startListening() {
        super.startListening(this.getClass());

        this.eventSubscription = this.eventManager.onEvent(ChannelGoOfflineEvent.class, event -> {
            log.info(String.format("[%s] just went offline!", event.getChannel().getName()));

            if (timer == null) {
                this.disposeOnlineStreamEvents();
                this.timer = new Timer();
                long delay = (1000 * 60) * DELAY_IN_MINUTES;
                timer.schedule(new StreamIdleTask(timer, this.streamService), delay);
            }
        });
    }

}
