package br.com.santos.vinicius.bowtruckleapi.event;

import br.com.santos.vinicius.bowtruckleapi.singleton.TwitchClientSingleton;
import com.github.philippheuer.events4j.api.domain.IEventSubscription;
import com.github.philippheuer.events4j.core.EventManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwitchEvent {

    protected IEventSubscription eventSubscription;

    protected EventManager eventManager;

    public TwitchEvent() {
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
}
