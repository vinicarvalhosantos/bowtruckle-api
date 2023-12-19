package br.com.santos.vinicius.bowtruckleapi.service;

import com.github.twitch4j.helix.domain.Game;

public interface StreamGameService {

    Game getGame(String gameId);

}
