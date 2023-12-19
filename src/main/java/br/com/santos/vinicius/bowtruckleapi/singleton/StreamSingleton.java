package br.com.santos.vinicius.bowtruckleapi.singleton;

import br.com.santos.vinicius.bowtruckleapi.event.*;
import br.com.santos.vinicius.bowtruckleapi.model.StreamInfoModel;
import br.com.santos.vinicius.bowtruckleapi.service.StreamService;
import br.com.santos.vinicius.bowtruckleapi.service.impl.StreamGameServiceImpl;
import br.com.santos.vinicius.bowtruckleapi.service.impl.StreamServiceImpl;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StreamSingleton {

    public static StreamSingleton singleInstance = null;

    public boolean isStreaming = false;

    private final List<TwitchEvent> onlineStreamEvents = new ArrayList<>();

    private static final List<TwitchEvent> mainStreamEvents = new ArrayList<>();

    public StreamInfoModel streamInfo;

    private final Map<LocalDate, StreamInfoModel> streamMap = new LinkedHashMap<>();

    public static StreamSingleton getInstance() {
        if (singleInstance == null) {
            singleInstance = new StreamSingleton();
            configureMainStreamEvents();
        }

        return singleInstance;
    }

    public void wentOffline() {
        onlineStreamEvents.forEach(TwitchEvent::dispose);

        streamInfo.getPlayingGame().setEndedAt(LocalDateTime.now());
        streamInfo.getStreamGameList().add(streamInfo.getPlayingGame());
        streamInfo.setPlayingGame(null);
        streamInfo.setEndedAt(LocalDateTime.now());
        streamMap.put(LocalDate.now(), streamInfo);

        streamInfo = null;
    }

    public void goOnline(ChannelGoLiveEvent goLiveEvent) {
        if (streamMap.get(LocalDate.now()) != null) {
            streamInfo = streamMap.get(LocalDate.now());
        }

        if (streamInfo != null) {
            log.error("Stream information is not null, something went wrong when stream went offline.");
            return;
        }

        if (onlineStreamEvents.isEmpty()) {
            configureOnlineStreamEvents();
        }

        onlineStreamEvents.forEach(TwitchEvent::startListening);
        isStreaming = true;

        String gameId = goLiveEvent.getStream().getGameId();
        String streamTitle = goLiveEvent.getStream().getTitle();

        StreamService streamService = new StreamServiceImpl(new StreamGameServiceImpl());

        streamInfo = streamService.defineStreamInformations(streamTitle, gameId);
    }

    public void changeGame(ChannelChangeGameEvent changeGameEvent) {
        StreamService streamService = new StreamServiceImpl(new StreamGameServiceImpl());

        String gameId = changeGameEvent.getGameId();

        streamInfo = streamService.updateGameInStreamInformations(gameId, this.streamInfo);
    }

    public void startMainStreamEvents() {
        mainStreamEvents.forEach(TwitchEvent::startListening);
    }

    private void configureOnlineStreamEvents() {
        onlineStreamEvents.add(new TwitchChangeGameEvent());
        onlineStreamEvents.add(new TwitchChannelMessageEvent());
    }

    private static void configureMainStreamEvents() {
        mainStreamEvents.add(new TwitchChannelGoLiveEvent());
        mainStreamEvents.add(new TwitchChannelGoOfflineEvent());
    }

}
