package org.game.puzzle.core.stubs;

import lombok.NonNull;
import org.game.puzzle.core.dao.GameDAO;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Species;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestGameDao implements GameDAO {
    private final List<Species> speciesList = new ArrayList<>();

    @Override
    public String save(Species species) {
        speciesList.add(species);
        return species.getId();
    }

    @Override
    public Optional<Species> getById(String id) {
        return speciesList.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Species> getByLogin(String login) {
        return speciesList.stream()
                .filter(t -> t.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public String updateCharacteristic(@NonNull Characteristic characteristic) {
        return null;
    }

    @Override
    public void removeByLogin(String login) {
        speciesList.removeIf(species -> species.getLogin().equals(login));
    }
}
