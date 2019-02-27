package org.game.puzzle.core.subscription.events;

import lombok.Getter;
import lombok.ToString;
import org.game.puzzle.core.subscription.Topics;

import java.io.Serializable;


@Getter
@ToString
public class MessageEvent implements SubscriptionEvent, Serializable {

    private static final long serialVersionUID = 1697757597992954891L;

    private String message;
    private boolean last;

    private MessageEvent(String message, boolean last) {
        this.message = message;
        this.last = last;
    }

    public static MessageEvent of(String message) {
        return of(message, false);
    }

    public static MessageEvent of(String message, boolean last) {
        return new MessageEvent(message, last);
    }

    @Override
    public String getSourceName() {
        return Topics.MESSAGE_TOPIC;
    }
}