package br.com.santos.vinicius.bowtruckleapi.service;

import br.com.santos.vinicius.bowtruckleapi.model.entity.GameEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameService {

    void saveGame(GameEntity game);

    GameEntity findGameById(Long gameId);

    List<GameEntity> findGamesByName(String gameName);

}
