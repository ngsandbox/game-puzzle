package org.game.puzzle.web;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.TestConfiguration;
import org.game.puzzle.WebGameApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = WebGameApplication.class)
@AutoConfigureWebTestClient(timeout = "36000")
class WebGamePuzzleApplicationTests {

//    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ApplicationContext context;

//    @BeforeEach
//    public void setup() {
//        webTestClient = WebTestClient
//                .bindToApplicationContext(this.context)
//                .configureClient()
//                .build();
//    }
//
//    @Test
//    void callTasksList() {
//        webTestClient
//                .get().uri("/api/v1/game")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                // and use the dedicated DSL to test assertions against the response
//                .expectStatus().isOk()
//                .expectBody(Void.class);
//    }
}

