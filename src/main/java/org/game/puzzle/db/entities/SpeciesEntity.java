package org.game.puzzle.db.entities;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.entities.species.SpeciesType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "speciesId")
@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name = "SpeciesEntity.victims", attributeNodes = @NamedAttributeNode("victims")),
        @NamedEntityGraph(name = "SpeciesEntity.full",
                attributeNodes = {@NamedAttributeNode("characteristic"), @NamedAttributeNode("victims")})
})
public class SpeciesEntity {
    @Id
    @Column(name = "speciesId")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String speciesId;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "type", nullable = false)
    private SpeciesType type;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CharacteristicEntity characteristic;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "winner")
    private List<SpeciesEntity> victims;

    @ManyToOne(fetch = FetchType.LAZY)
    private SpeciesEntity winner;


    public SpeciesEntity(String speciesId,
                         @NonNull String login,
                         @NonNull SpeciesType type,
                         @NonNull CharacteristicEntity characteristic) {
                         //@NonNull List<SpeciesEntity> victims) {
        this.speciesId = speciesId;
        this.login = login;
        this.type = type;
        this.characteristic = characteristic;
    }
}
