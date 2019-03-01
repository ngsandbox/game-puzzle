package org.game.puzzle.web.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingListener;

@Getter
@Setter
@EqualsAndHashCode(of = "login")
@ToString
@Component
@Scope("session")
public class User implements HttpSessionBindingListener {
    private String login;
}
