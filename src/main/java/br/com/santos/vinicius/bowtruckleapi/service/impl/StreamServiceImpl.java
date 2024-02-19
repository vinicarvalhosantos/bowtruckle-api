package br.com.santos.vinicius.bowtruckleapi.service.impl;

import br.com.santos.vinicius.bowtruckleapi.exception.BadRequestException;
import br.com.santos.vinicius.bowtruckleapi.exception.InternalServerErrorException;
import br.com.santos.vinicius.bowtruckleapi.exception.NotFoundException;
import br.com.santos.vinicius.bowtruckleapi.model.dto.StreamTimeDTO;
import br.com.santos.vinicius.bowtruckleapi.model.entity.GameEntity;
import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamEntity;
import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamGamesEntity;
import br.com.santos.vinicius.bowtruckleapi.repository.StreamRepository;
import br.com.santos.vinicius.bowtruckleapi.service.GameService;
import br.com.santos.vinicius.bowtruckleapi.service.StreamGameService;
import br.com.santos.vinicius.bowtruckleapi.service.StreamService;
import br.com.santos.vinicius.bowtruckleapi.singleton.StreamSingleton;
import br.com.santos.vinicius.bowtruckleapi.singleton.TwitchClientSingleton;
import br.com.santos.vinicius.bowtruckleapi.util.DateTimeUtils;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.StreamList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class StreamServiceImpl implements StreamService {

    private final StreamGameService streamGameService;

    private final GameService gameService;

    private final StreamRepository repository;

    public StreamServiceImpl(StreamGameService streamGameService, GameService gameService, StreamRepository repository) {
        this.streamGameService = streamGameService;
        this.gameService = gameService;
        this.repository = repository;
    }

    @Override
    public void goOnline(ChannelGoLiveEvent goLiveEvent) {
        this.goOnline(goLiveEvent.getStream().getTitle(), goLiveEvent.getStream().getGameId());
    }

    @Deprecated
    @Override
    public void wentOffline() {
        StreamSingleton streamSingleton = StreamSingleton.getInstance();
        streamSingleton.isStreaming = true;

        StreamEntity stream = this.getStreamOfTheDay();
        this.endLastGame(stream);

        stream.setPlayingGame(null);
        stream.setEndedAt(LocalDateTime.now());

        this.repository.save(stream);
    }

    @Override
    public void wentOffline(LocalDateTime endedAt) {
        StreamSingleton streamSingleton = StreamSingleton.getInstance();
        streamSingleton.isStreaming = false;

        StreamEntity stream = this.getStreamOfTheDay();
        this.endLastGame(stream, endedAt);

        stream.setPlayingGame(null);
        stream.setEndedAt(endedAt);

        this.repository.save(stream);
    }

    @Override
    public void changeGame(ChannelChangeGameEvent changeGameEvent) {
        log.info("Updating stream details.");
        StreamEntity stream = updateGameInStreamInformations(changeGameEvent.getGameId());

        log.info("Saving stream details updated.");
        this.repository.save(stream);
    }

    @Override
    public void changeGame(String newGameId) {
        log.info("Updating stream details.");
        StreamEntity stream = updateGameInStreamInformations(newGameId);

        log.info("Saving stream details updated.");
        this.repository.save(stream);
    }

    @Override
    public Page<StreamEntity> findStreamsDetails(int page, int limit) {

        return this.repository.findAll(PageRequest.of((page - 1), limit, Sort.by("streamDate").ascending()));
    }

    @Override
    public List<StreamEntity> findStreamsByDateRange(LocalDate from, LocalDate to) {
        long differenceInDays = ChronoUnit.DAYS.between(from, to);
        if (differenceInDays > 365) {
            throw new BadRequestException(String.format("The range should be 365 days max. (%s)", differenceInDays));
        }
        return this.repository.findAllByStreamDateBetween(from, to);
    }

    @Override
    public StreamTimeDTO streamTime(LocalDate of) {
        List<StreamEntity> streamEntityList = this.repository.findByStreamDateIs(of);

        if (streamEntityList.isEmpty()) {
            throw new NotFoundException("Any stream was found for this date!");
        }

        StreamEntity stream = streamEntityList.get(streamEntityList.size() - 1);
        LocalDateTime endedAt = this.collectHigherEndedAt(streamEntityList);
        LocalDateTime startedAt = this.collectHigherStartedAt(streamEntityList);
        long totalSeconds = ChronoUnit.SECONDS.between(startedAt, endedAt);
        StreamTimeDTO streamTimeDTO = DateTimeUtils.formatTimeFromSeconds(totalSeconds).toStreamTimeDto();
        streamTimeDTO.setId(stream.getId());
        streamTimeDTO.setTitle(stream.getTitle());
        streamTimeDTO.setPlayingGame(stream.getPlayingGame());

        return streamTimeDTO;
    }

    @Override
    public boolean checkStreamerIsOnline() {
        StreamList streamList = this.getStreamList();

        if (streamList.getStreams().isEmpty()) {
            return false;
        }

        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime startedAt = LocalDateTime.ofInstant(streamList.getStreams().get(0).getStartedAtInstant(), zoneId);
        LocalDate streamDate = LocalDate.ofInstant(streamList.getStreams().get(0).getStartedAtInstant(), zoneId);

        String gameId = streamList.getStreams().get(0).getGameId();
        String title = streamList.getStreams().get(0).getTitle();
        StreamEntity stream = this.repository.findByStreamDateIsAndEndedAtIsNull(streamDate);

        if (stream == null) {
            stream = this.defineStreamInformations(title, Long.parseLong(gameId), startedAt);
            stream.setStreamDate(streamDate);
            stream.setStartedAt(startedAt);
            this.repository.save(stream);
            return true;

        }

        //StreamEntity stream = stream.get(0);
        if (!stream.getTitle().equals(title)) {
            stream.setTitle(title);
            this.repository.save(stream);
        }

        if (stream.getPlayingGame().getId() == Long.parseLong(gameId)) {
            return true;
        }

        this.updateGameInStreamInformations(gameId, stream);

        this.repository.save(stream);

        return true;
    }

    private LocalDateTime collectHigherEndedAt(List<StreamEntity> streamEntities) {
        List<LocalDateTime> endedAtList = streamEntities.stream().map(StreamEntity::getEndedAt).toList();
        if (endedAtList.contains(null)) {
            return LocalDateTime.now();
        }

        return endedAtList.stream().max(LocalDateTime::compareTo).orElse(LocalDateTime.now());
    }

    private LocalDateTime collectHigherStartedAt(List<StreamEntity> streamEntities) {
        List<LocalDateTime> startedAtList = streamEntities.stream().map(StreamEntity::getStartedAt).toList();

        return startedAtList.stream().min(LocalDateTime::compareTo).orElse(LocalDateTime.now());
    }

    private void goOnline(String title, String gameId) {
        StreamSingleton streamSingleton = StreamSingleton.getInstance();
        streamSingleton.isStreaming = true;

        Long longGameId = Long.parseLong(gameId);

        log.info("Collecting stream details.");
        StreamEntity stream = defineStreamInformations(title, longGameId);

        log.info("Saving stream details.");
        this.repository.save(stream);
    }

    private StreamEntity defineStreamInformations(String streamTitle, Long gameId) {
        StreamEntity stream = new StreamEntity();

        GameEntity game = collectGameInformation(gameId.toString());

        StreamGamesEntity streamGames = new StreamGamesEntity();
        streamGames.setGame(game);
        this.streamGameService.saveStreamGame(streamGames);

        stream.setTitle(streamTitle);
        stream.setPlayingGame(game);
        stream.setStreamGamesList(new ArrayList<>(List.of(streamGames)));
        return stream;
    }

    private StreamEntity defineStreamInformations(String streamTitle, Long gameId, LocalDateTime startedAt) {
        StreamEntity stream = new StreamEntity();

        GameEntity game = collectGameInformation(gameId.toString());

        StreamGamesEntity streamGames = new StreamGamesEntity();
        streamGames.setGame(game);
        streamGames.setStartedAt(startedAt);
        this.streamGameService.saveStreamGame(streamGames);

        stream.setTitle(streamTitle);
        stream.setPlayingGame(game);
        stream.setStreamGamesList(new ArrayList<>(List.of(streamGames)));
        stream.setStartedAt(startedAt);
        return stream;
    }

    private StreamEntity updateGameInStreamInformations(String newGameId) {
        StreamEntity stream = this.getStreamOfTheDay();

        return this.updateGameInStreamInformations(newGameId, stream);
    }

    private StreamEntity updateGameInStreamInformations(String newGameId, StreamEntity stream) {
        if (stream.getPlayingGame().getId() == Long.parseLong(newGameId)) {
            return stream;
        }

        this.endLastGame(stream);

        GameEntity game = this.collectGameInformation(newGameId);

        StreamGamesEntity streamGames = new StreamGamesEntity();
        streamGames.setGame(game);
        this.streamGameService.saveStreamGame(streamGames);

        stream.setPlayingGame(game);
        stream.getStreamGamesList().add(streamGames);

        return stream;
    }

    private GameEntity collectGameInformation(String gameId) {
        log.info("Collecting game informations from the database.");
        GameEntity game = this.gameService.findGameById(Long.parseLong(gameId));
        if (game != null) {
            return game;
        }

        log.info("Game does not exists in the database.\nCollecting game informations from twitch.");
        Game twitchGame = streamGameService.getGame(gameId);
        Long id = Long.parseLong(twitchGame.getId());

        game = new GameEntity(id, twitchGame.getName(), twitchGame.getBoxArtUrl(144, 192));
        this.gameService.saveGame(game);

        return game;
    }

    protected StreamEntity getStreamOfTheDay() {
        LocalDate todayDate = LocalDate.now();
        if (DateTimeUtils.isDayBreak()) {
            todayDate = todayDate.minusDays(1);
        }
        StreamEntity stream = this.repository.findByStreamDateIsAndEndedAtIsNull(todayDate);
        if (stream == null) {
            log.error("Something went wrong, there are no stream details for this date! ({})", LocalDate.now());
            throw new InternalServerErrorException(String.format("Something went wrong, there are no stream details for this date! (%s)", LocalDate.now()));
        }

        return stream;
    }

    private void endLastGame(StreamEntity stream) {
        this.endLastGame(stream, LocalDateTime.now());
    }

    private void endLastGame(StreamEntity stream, LocalDateTime endedAt) {
        StreamGamesEntity lastGame = stream.getStreamGamesList().stream().filter(streamGames -> streamGames.getEndedAt() == null).toList().get(0);
        lastGame.setEndedAt(endedAt);

        this.streamGameService.saveStreamGame(lastGame);
    }

    private StreamList getStreamList() {
        TwitchClientSingleton twitch = TwitchClientSingleton.getInstance();
        List<String> channelIdList = new ArrayList<>(Collections.singleton(twitch.channelId));
        return twitch.client.getHelix().getStreams(null, null, null, null, null, null, channelIdList, null).execute();
    }

}
