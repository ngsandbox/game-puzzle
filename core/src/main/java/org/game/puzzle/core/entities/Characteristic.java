package org.game.puzzle.core.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Characteristic {
    /**
     * Unique identifier for species characteristic
     */
    private final String id;
    private final Gender gender;

    /**
     * Current species experience
     */
    private final long experience;

    /**
     * Current species level
     */
    private final int level;


    /**
     * Increases the damage you deal with melee weapons, like baseball bats and sledgehammers
     */
    private final int strength;

    /**
     * Perception primarily affects your weapon accuracy in V.A.T.S., Fallout's slow-motion auto-targeting system.
     * It also comes tied to perks rogue-type characters might enjoy, like lockpicking, pickpocketing, and explosives.
     * Perception also carries perks that will boost rifle and sniper skills.
     */
    private final int perception;

    /**
     * Endurance directly impacts your hit points and the amount you can sprint.
     * The perks tied to endurance includes all sorts of damage resistances,
     * the ability to eat a wider variety of foods and drinks (including human flesh) and the ability to take in healing from sunlight.
     */
    private final int endurance;

    /**
     * Charisma expands your toolset in the game's conversations. With a higher Charisma, you can persuade other
     * characters to do your bidding, and get reduced prices from merchants and vendors. Charisma perks enhance
     * your ability to command your followers, such as the much-advertised canine companion. You can also use
     * Charisma perks to improve your homestead, building supply lines and on-site stores.
     */
    private final int charisma;

    /**
     * Intelligence primarily increases the amount of EXP you gain from performing actions in the wasteland,
     * allowing you to level up faster. Intelligence perks pertain to crafting, hacking computers and even
     * robots, turning them into your allies. The highest perk allows you to become enraged at low health,
     * slowing down time and increasing your damage dealt.
     */
    private final int intelligence;

    /**
     * Agility enhances the frequency you can use V.A.T.S. targeting system, and also grants bonuses to sneaking.
     * Perks tied to Agility increase your abilities with one handed, silenced weapons, and also increase your
     * damage while attacking from stealth.
     */
    private final int agility;

    /**
     * Luck governs the frequency of critical hits, which deal increased damage.
     * It also gives you a higher chance to find quality items when looting containers throughout the wasteland.
     * Some of the Luck perks include further bonuses to crit chance, but also additional bonuses that occur at random.
     */
    private final int luck;

    @Builder
    public Characteristic(String id,
                          Gender gender,
                          int strength,
                          int perception,
                          int endurance,
                          int charisma,
                          int intelligence,
                          int agility,
                          int luck,
                          long experience,
                          int level) {
        this.id = id;
        this.gender = gender;
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.charisma = charisma;
        this.intelligence = intelligence;
        this.agility = agility;
        this.luck = luck;
        this.experience = experience;
        this.level = level;
    }
}
