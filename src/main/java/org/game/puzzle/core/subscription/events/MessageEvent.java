package org.game.puzzle.core.subscription.events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.game.puzzle.core.subscription.Topics;

import java.io.Serializable;
import java.util.UUID;


@Setter
@Getter
@ToString
public class MessageEvent implements SubscriptionEvent, Serializable {

    private static final long serialVersionUID = 1697757597992954891L;

    private String id;
    private String login;
    private String message;
    private boolean last;

    private MessageEvent(String login, String message, boolean last) {
        this.id = UUID.randomUUID().toString();
        this.login = login;
        this.message = message;
        this.last = last;
    }

    public static MessageEvent of(String login, String message) {
        return of(login, message, false);
    }

    public static MessageEvent of(String login, String message, boolean last) {
        return new MessageEvent(login, message, last);
    }

    @Override
    public String getSourceName() {
        return Topics.MESSAGE_TOPIC;
    }
}