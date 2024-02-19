package br.com.santos.vinicius.bowtruckleapi.repository;

import br.com.santos.vinicius.bowtruckleapi.model.entity.StreamEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StreamRepository extends JpaRepository<StreamEntity, Long> {

    StreamEntity findByStreamDateIsAndEndedAtIsNull(LocalDate date);

    @NotNull Page<StreamEntity> findAll(@NotNull Pageable pageable);

    List<StreamEntity> findAllByStreamDateBetween(LocalDate from, LocalDate to);

    List<StreamEntity> findByStreamDateIs(LocalDate localDate);

}
