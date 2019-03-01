package org.game.puzzle.db.repositories;

import org.game.puzzle.db.entities.SpeciesEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public interface SpeciesRepository extends CrudRepository<SpeciesEntity, String>,
        JpaSpecificationExecutor<SpeciesEntity> {

    @Query("select s from SpeciesEntity s where  (s.speciesId = :speciesId) ")
    @EntityGraph(value = "SpeciesEntity.victims", type = FETCH)
    Optional<SpeciesEntity> findVictims(@Param("speciesId") String speciesId);


    @Query("select s from SpeciesEntity s where  (s.login = :login) ")
    @EntityGraph(value = "SpeciesEntity.full", type = FETCH)
    Optional<SpeciesEntity> findByLoginIgnoreCase(@Param("login") String login);

}
