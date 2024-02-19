package br.com.santos.vinicius.bowtruckleapi.event;

import br.com.santos.vinicius.bowtruckleapi.service.StreamService;
import br.com.santos.vinicius.bowtruckleapi.singleton.TwitchClientSingleton;
import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import com.github.philippheuer.events4j.core.EventManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Slf4j
@Component
public class TwitchEvent {

    @Autowired
    StreamService streamService;

    @Autowired
    TwitchChangeGameEvent changeGameEvent;

    @Autowired
    TwitchChannelMessageEvent channelMessageEvent;

    @Autowired
    TwitchChannelGoOfflineEvent channelGoOfflineEvent;

    /*@Autowired
    TwitchCheersEvent cheersEvent;

    @Autowired
    TwitchFollowingEvent twitchFollowingEvent;

    @Autowired
    TwitchSubscriptionEvent twitchSubscriptionEvent;*/

    protected IEventSubscription eventSubscription;

    protected EventManager eventManager;

    protected Timer timer;

    private final List<TwitchEvent> onlineStreamEvents = new ArrayList<>();

    public void configure() {
        TwitchClientSingleton twitch = TwitchClientSingleton.getInstance();
        this.eventManager = twitch.client.getEventManager();
    }

    public void startListening(Class childClass) {
        log.info(String.format("Starting to listen for %s", childClass.getSimpleName()));
    }

    public void startListening() {
    }

    public void dispose() {
        if (eventSubscription != null) {
            eventSubscription.dispose();
            eventSubscription = null;
        }
    }

    protected void startOnlineStreamEvents() {
        configureOnlineStreamEvents();
        onlineStreamEvents.forEach(TwitchEvent::startListening);
    }

    protected void disposeOnlineStreamEvents() {
        onlineStreamEvents.forEach(TwitchEvent::dispose);
    }

    private void configureOnlineStreamEvents() {
        onlineStreamEvents.add(changeGameEvent);
        onlineStreamEvents.add(channelMessageEvent);
        onlineStreamEvents.add(channelGoOfflineEvent);
      /*  onlineStreamEvents.add(cheersEvent);
        onlineStreamEvents.add(twitchFollowingEvent);
        onlineStreamEvents.add(twitchSubscriptionEvent);*/
        onlineStreamEvents.forEach(TwitchEvent::configure);
    }
}
