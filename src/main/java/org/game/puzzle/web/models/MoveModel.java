package org.game.puzzle.web.models;

import lombok.Getter;
import lombok.ToString;
import org.game.puzzle.core.entities.grid.Coordinate;

import java.util.List;


@Getter
@ToString
public class MoveModel {
    private final boolean userHit;
    private final int damage;
    private final List<Coordinate> route;
    private final boolean finish;
    private final boolean next;

    public MoveModel(boolean userHit, int damage, List<Coordinate> route, boolean finish, boolean next) {
        this.userHit = userHit;
        this.damage = damage;
        this.route = route;
        this.finish = finish;
        this.next = next;
    }
}
