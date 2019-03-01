package org.game.puzzle.db;


import org.game.puzzle.core.GameException;

public class DbException extends GameException {
    public DbException(String message) {
        super(message);
    }

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}
