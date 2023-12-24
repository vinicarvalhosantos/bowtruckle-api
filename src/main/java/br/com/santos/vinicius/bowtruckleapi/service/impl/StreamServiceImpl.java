package br.com.santos.vinicius.bowtruckleapi.service.impl;

import br.com.santos.vinicius.bowtruckleapi.model.StreamGame;
import br.com.santos.vinicius.bowtruckleapi.model.StreamInfoModel;
import br.com.santos.vinicius.bowtruckleapi.service.StreamGameService;
import br.com.santos.vinicius.bowtruckleapi.service.StreamService;
import com.github.twitch4j.helix.domain.Game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StreamServiceImpl implements StreamService {

    StreamGameService streamGameService;

    public StreamServiceImpl(StreamGameService streamGameService) {
        this.streamGameService = streamGameService;
    }

    @Override
    public StreamInfoModel defineStreamInformations(String streamTitle, String gameId) {
        StreamGame streamGame = this.extractGameInformation(gameId);

        return new StreamInfoModel(streamTitle, streamGame, new ArrayList<>(List.of(streamGame)));
    }

    @Override
    public StreamInfoModel updateGameInStreamInformations(String newGameId, StreamInfoModel streamInfo) {
        StreamGame streamGame = this.extractGameInformation(newGameId);

        streamInfo.getPlayingGame().setEndedAt(LocalDateTime.now());
        streamInfo.setPlayingGame(streamGame);
        streamInfo.getStreamGameList().add(streamGame);

        return streamInfo;
    }

    private StreamGame extractGameInformation(String gameId) {
        Game game = streamGameService.getGame(gameId);

        return new StreamGame(game.getId(), game.getName(),
                game.getBoxArtUrl(144, 192), LocalDateTime.now());
    }
}
