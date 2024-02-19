package br.com.santos.vinicius.bowtruckleapi.repository;

import br.com.santos.vinicius.bowtruckleapi.model.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamesRepository extends JpaRepository<GameEntity, Long> {

    List<GameEntity> getGameEntitiesByNameLike(String gameName);


}
