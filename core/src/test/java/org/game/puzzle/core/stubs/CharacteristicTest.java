package org.game.puzzle.core.stubs;

import lombok.Builder;
import org.game.puzzle.core.entities.Characteristic;
import org.game.puzzle.core.entities.species.Gender;

import static java.util.Optional.ofNullable;

@SuppressWarnings("unused")
public class CharacteristicTest {
    @Builder(builderMethodName = "testCharacterBuilder")
    public static Characteristic getCharacteristic(int level,
                                                   Integer endurance,
                                                   Integer perception,
                                                   Integer strength,
                                                   Integer charisma,
                                                   Integer intelligence,
                                                   Integer agility,
                                                   Integer luck) {
        Characteristic.CharacteristicBuilder builder = Characteristic.builder();
        builder.id("id" + level);
        builder.gender(Gender.NONE);
        builder.strength(ofNullable(strength).orElse(8));
        builder.perception(ofNullable(perception).orElse(9));
        builder.endurance(ofNullable(endurance).orElse(7));
        builder.charisma(ofNullable(charisma).orElse(6));
        builder.intelligence(ofNullable(intelligence).orElse(5));
        builder.agility(ofNullable(agility).orElse(4));
        builder.luck(ofNullable(luck).orElse(5));
        builder.experience(level * 100);
        builder.level(level);
        return builder.build();
    }
}
