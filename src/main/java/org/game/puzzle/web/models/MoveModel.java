package org.game.puzzle.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@ToString
public class MoveModel implements Serializable {

    private static final long serialVersionUID = 4852398140042986573L;
    private boolean userHit;
    private int damage;
    private List<Position> route;
    private boolean finish;
    private boolean next;

    public MoveModel(boolean userHit, int damage, List<Position> route, boolean finish, boolean next) {
        this.userHit = userHit;
        this.damage = damage;
        this.route = route;
        this.finish = finish;
        this.next = next;
    }
}
