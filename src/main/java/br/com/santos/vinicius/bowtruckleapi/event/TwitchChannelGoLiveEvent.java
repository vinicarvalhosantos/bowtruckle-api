package br.com.santos.vinicius.bowtruckleapi.event;

import br.com.santos.vinicius.bowtruckleapi.singleton.StreamSingleton;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwitchChannelGoLiveEvent extends TwitchEvent {

    @Override
    public void startListening() {
        super.startListening(this.getClass());
        //streamService.goOnline("6- Zerando GTA V PELA PRIMEIRA VEZ !amazon !nuuvem", "509658");
        //this.startOnlineStreamEvents();

        if (this.streamService.checkStreamerIsOnline()) {
            this.startOnlineStreamEvents();
            StreamSingleton streamSingleton = StreamSingleton.getInstance();
            streamSingleton.isStreaming = true;
            return;
        }

        this.eventSubscription = this.eventManager.onEvent(ChannelGoLiveEvent.class, event -> {
            log.info(String.format("[%s] went live with title %s on game %s!",
                    event.getChannel().getName(), event.getStream().getTitle(), event.getStream().getGameId()));

            log.info("Checking if stream has dropped");
            if (this.timer != null) {
                this.timer.cancel();
                this.timer = null;
                this.startOnlineStreamEvents();
                return;
            }

            this.startOnlineStreamEvents();
            streamService.goOnline(event);
        });
    }
}
