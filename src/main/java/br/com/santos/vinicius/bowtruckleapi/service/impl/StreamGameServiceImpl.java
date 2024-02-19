package br.com.santos.vinicius.bowtruckleapi.service.impl;

import br.com.santos.vinicius.bowtruckleapi.exception.BadRequestException;
import br.com.santos.vinicius.bowtruckleapi.exception.InternalServerErrorException;
import br.com.santos.vinicius.bowtruckleapi.exception.NotFoundException;
import br.com.santos.vinicius.bowtruckleapi.model.dto.GameTimeDTO;
import br.com.santos.vinicius.bowtruckleapi.model.entity.GameEntity;
import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamGamesEntity;
import br.com.santos.vinicius.bowtruckleapi.repository.StreamGamesRepository;
import br.com.santos.vinicius.bowtruckleapi.service.GameService;
import br.com.santos.vinicius.bowtruckleapi.service.StreamGameService;
import br.com.santos.vinicius.bowtruckleapi.singleton.StreamSingleton;
import br.com.santos.vinicius.bowtruckleapi.singleton.TwitchClientSingleton;
import br.com.santos.vinicius.bowtruckleapi.util.DateTimeUtils;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.GameList;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class StreamGameServiceImpl implements StreamGameService {

    private final StreamGamesRepository repository;

    private final GameService gameService;

    public StreamGameServiceImpl(StreamGamesRepository repository, GameService gameService) {
        this.repository = repository;
        this.gameService = gameService;
    }

    @Override
    public Game getGame(String gameId) {
        TwitchClientSingleton twitch = TwitchClientSingleton.getInstance();
        GameList resultList = twitch.client.getHelix().getGames(null, Collections.singletonList(gameId), null, null).execute();

        return resultList.getGames().get(0);
    }

    @Override
    public void saveStreamGame(StreamGamesEntity streamGames) {
        this.repository.save(streamGames);
    }

    @Override
    public GameTimeDTO gameTime(String gameName, LocalDate from) {
        List<StreamGamesEntity> streamGameList = getStreamGameByName(gameName, from);
        long totalSeconds = 0;
        for (StreamGamesEntity streamGames : streamGameList) {
            totalSeconds += ChronoUnit.SECONDS.between(streamGames.getStartedAt(), streamGames.getEndedAt());
        }
        GameTimeDTO gameTime = DateTimeUtils.formatTimeFromSeconds(totalSeconds).toGameTimeDto();
        gameTime.setId(streamGameList.get(0).getGame().getId());
        gameTime.setName(streamGameList.get(0).getGame().getName());

        return gameTime;
    }

    @Override
    public GameTimeDTO currentGameTime() {
        StreamSingleton streamSingleton = StreamSingleton.getInstance();
        if (!streamSingleton.isStreaming) {
            throw new BadRequestException("Not in live.");
        }

        Optional<StreamGamesEntity> streamGamesOptional = this.repository.findCurrentGame();
        if (streamGamesOptional.isEmpty()) {
            throw new InternalServerErrorException("Something went wrong when tried to get current game time.");
        }

        StreamGamesEntity streamGame = streamGamesOptional.get();
        long totalSeconds = ChronoUnit.SECONDS.between(streamGame.getStartedAt(), LocalDateTime.now());
        GameTimeDTO gameTime = DateTimeUtils.formatTimeFromSeconds(totalSeconds).toGameTimeDto();
        gameTime.setId(streamGame.getGame().getId());
        gameTime.setName(streamGame.getGame().getName());

        return gameTime;
    }

    private List<StreamGamesEntity> getStreamGameByName(String gameName, LocalDate from) {
        List<GameEntity> gameList = this.gameService.findGamesByName(gameName);
        if (gameList == null || gameList.isEmpty()) {
            throw new NotFoundException("Any game was found with that name.");
        }

        List<Long> gameIdList = gameList.stream().map(GameEntity::getId).toList();
        List<StreamGamesEntity> streamGameList = this.repository.findGamesPlayedByIdList(gameIdList, from);

        if (streamGameList == null || streamGameList.isEmpty()) {
            throw new NotFoundException("Any games was played today with that name.");
        }

        return streamGameList;
    }


}
