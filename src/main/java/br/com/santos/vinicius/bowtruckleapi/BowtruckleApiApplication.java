package br.com.santos.vinicius.bowtruckleapi;

import br.com.santos.vinicius.bowtruckleapi.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class BowtruckleApiApplication implements InitializingBean {

    private final TwitchChannelGoLiveEvent channelGoLiveEvent;

    private static final List<TwitchEvent> mainStreamEvents = new ArrayList<>();

    public BowtruckleApiApplication(TwitchChannelGoLiveEvent channelGoLiveEvent) {
        this.channelGoLiveEvent = channelGoLiveEvent;
    }

    public static void main(String[] args) {
        SpringApplication.run(BowtruckleApiApplication.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startMainStreamEvents();
    }

    private void startMainStreamEvents() {
        configureMainStreamEvents();
        mainStreamEvents.forEach(TwitchEvent::startListening);
    }
    private void configureMainStreamEvents() {
        mainStreamEvents.add(channelGoLiveEvent);
        mainStreamEvents.forEach(TwitchEvent::configure);
    }
}
