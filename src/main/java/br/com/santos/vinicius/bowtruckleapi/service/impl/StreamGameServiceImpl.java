package br.com.santos.vinicius.bowtruckleapi.service.impl;

import br.com.santos.vinicius.bowtruckleapi.service.StreamGameService;
import br.com.santos.vinicius.bowtruckleapi.singleton.TwitchClientSingleton;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.GameList;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class StreamGameServiceImpl implements StreamGameService {

    @Override
    public Game getGame(String gameId) {
        TwitchClientSingleton twitch = TwitchClientSingleton.getInstance();
        GameList resultList = twitch.client.getHelix().getGames(null, Collections.singletonList(gameId), null, null).execute();

        return resultList.getGames().get(0);
    }
}
