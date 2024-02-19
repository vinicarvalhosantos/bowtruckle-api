package br.com.santos.vinicius.bowtruckleapi.service.impl;

import br.com.santos.vinicius.bowtruckleapi.model.entity.GameEntity;
import br.com.santos.vinicius.bowtruckleapi.repository.GamesRepository;
import br.com.santos.vinicius.bowtruckleapi.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

    private GamesRepository repository;

    public GameServiceImpl(GamesRepository repository) {
        this.repository = repository;
    }


    @Override
    public void saveGame(GameEntity game) {
        log.info("Saving game informations.");
        this.repository.save(game);
    }

    @Override
    public GameEntity findGameById(Long gameId) {
        Optional<GameEntity> game = this.repository.findById(gameId);
        return game.orElse(null);

    }

    @Override
    public List<GameEntity> findGamesByName(String gameName) {
        return this.repository.getGameEntitiesByNameLike(gameName);
    }


}
