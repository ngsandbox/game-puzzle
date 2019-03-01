package org.game.puzzle.stubs;

import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Gender;

@SuppressWarnings("unused")
public class CharacteristicStub {
    public static Characteristic getCharacteristic(int level) {
        Characteristic.CharacteristicBuilder builder = Characteristic.builder();
        builder.id("id" + level);
        builder.gender(Gender.NONE);
        builder.strength(8);
        builder.perception(9);
        builder.endurance(7);
        builder.charisma(6);
        builder.intelligence(5);
        builder.agility(4);
        builder.luck(5);
        builder.experience(level * 100);
        builder.level(level);
        return builder.build();
    }
}
