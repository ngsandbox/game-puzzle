package org.game.puzzle.db.repositories;

import org.game.puzzle.db.entities.CharacteristicEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicRepository extends CrudRepository<CharacteristicEntity, String>,
        JpaSpecificationExecutor<CharacteristicEntity> {



}
