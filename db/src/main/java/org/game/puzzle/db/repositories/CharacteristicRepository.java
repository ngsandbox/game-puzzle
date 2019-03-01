package org.game.puzzle.db.repositories;

import org.game.puzzle.db.entities.CharacteristicEntity;
import org.game.puzzle.db.entities.SpeciesEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public interface CharacteristicRepository extends CrudRepository<CharacteristicEntity, String>,
        JpaSpecificationExecutor<CharacteristicEntity> {

}
