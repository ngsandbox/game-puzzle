package org.game.puzzle.db;


import org.game.puzzle.core.exceptions.GameException;

public class DbException extends GameException {
    public DbException(String message) {
        super(message);
    }
}
