package br.com.santos.vinicius.bowtruckleapi.service;

import br.com.santos.vinicius.bowtruckleapi.model.dto.GameTimeDTO;
import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamGamesEntity;
import com.github.twitch4j.helix.domain.Game;

import java.time.LocalDate;
import java.util.List;

public interface StreamGameService {

    Game getGame(String gameId);

    void saveStreamGame(StreamGamesEntity streamGames);

    GameTimeDTO gameTime(String gameName, LocalDate from);

    GameTimeDTO currentGameTime();

}
