package br.com.santos.vinicius.bowtruckleapi.service;

import br.com.santos.vinicius.bowtruckleapi.model.dto.StreamTimeDTO;
import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamEntity;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StreamService {
    void goOnline(ChannelGoLiveEvent goLiveEvent);

    //void goOnline(String title, String gameId);

    @Deprecated
    void wentOffline();

    void wentOffline(LocalDateTime endedAt);

    void changeGame(ChannelChangeGameEvent changeGameEvent);

    void changeGame(String newGameId);

    Page<StreamEntity> findStreamsDetails(int page, int limit);

    List<StreamEntity> findStreamsByDateRange(LocalDate from, LocalDate to);

    StreamTimeDTO streamTime(LocalDate of);

    boolean checkStreamerIsOnline();

}
