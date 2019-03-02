package org.game.puzzle.web.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class ErrorDetails implements Serializable {

    private static final long serialVersionUID = 2735225803128625332L;
    private Date timestamp;
    private String field;
    private String details;

    public ErrorDetails(@NonNull Date timestamp, @NonNull String field, @NonNull String details) {
        this.timestamp = timestamp;
        this.field = field;
        this.details = details;
    }
}
