package br.com.santos.vinicius.bowtruckleapi.repository;

import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamGamesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StreamGamesRepository extends JpaRepository<StreamGamesEntity, Long> {

    /*@Query("""
        select new StreamGamesEntity(sgl.id, sgl.game, sgl.startedAt, sgl.endedAt)\s
        from StreamGamesEntity sgl\s
        where DATE(sgl.startedAt) = DATE(now()) and sgl.startedAt is null""")*/
    @Query(value = """
            select sgl.id, sgl.ended_at, sgl.started_at, sgl.game_id, sgl.stream_details_id
            from stream_games_list sgl
            cross join stream_details sd
            where sd.ended_at is null and sgl.ended_at is null and sd.game_id is not null""", nativeQuery = true)
    Optional<StreamGamesEntity> findCurrentGame();

    @Query("""
            select new StreamGamesEntity(sgl.id, sgl.game, sgl.startedAt, sgl.endedAt)\s
            from StreamGamesEntity sgl\s
            where sgl.game.id in(?1) and DATE(sgl.startedAt) = DATE(?2)""")
    List<StreamGamesEntity> findGamesPlayedByIdList(List<Long> gameIdList, LocalDate from);

}
