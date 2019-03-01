package org.game.puzzle.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class WebGameApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebGameApplication.class, args);
    }
}

