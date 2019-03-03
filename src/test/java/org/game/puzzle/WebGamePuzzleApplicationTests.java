package org.game.puzzle;

import org.game.puzzle.core.entities.species.Gender;
import org.game.puzzle.web.models.Position;
import org.game.puzzle.web.models.SpeciesStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = WebGameApplication.class)
public class WebGamePuzzleApplicationTests {


    @Autowired
    private TestRestTemplate template;


    @Test
    public void checkAcces200() {
        ResponseEntity<String> result = getTemplate()
                .getForEntity("/game", String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void checkFight() {
        SpeciesStats stats = SpeciesStats.builder()
                .gender(Gender.FEMALE)
                .strength(6)
                .perception(7)
                .endurance(8)
                .charisma(9)
                .intelligence(5)
                .agility(6)
                .luck(7)
                .level(7)
                .life(100)
                .steps(8)
                .build();
        ResponseEntity<String> register = getTemplate()
                .postForEntity("/api/v1/species", stats, String.class);
        assertEquals(HttpStatus.OK, register.getStatusCode());
        String cookie = register.getHeaders().getFirst("Set-Cookie");
        ResponseEntity<String> apiResult = nextRequest(null, HttpMethod.GET, "/api/v1/species/spring", cookie, String.class);
        assertEquals(HttpStatus.OK, apiResult.getStatusCode());

        ResponseEntity<String> fightResult = nextRequest(null, HttpMethod.POST, "/fight", cookie, String.class);
        assertEquals(HttpStatus.OK, fightResult.getStatusCode());

        ResponseEntity<String> gotoResult = nextRequest(new Position(5, 5), HttpMethod.POST, "/api/v1/goto", cookie, String.class);
        assertEquals(HttpStatus.OK, gotoResult.getStatusCode());
        assertNotNull(gotoResult.getBody());
    }

    private <T> ResponseEntity<T> nextRequest(Object body, HttpMethod method, String url, String cookie, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie);
        RequestEntity<Object> request = new RequestEntity<>(body, headers, method, URI.create(url));
        return getTemplate().exchange(request, clazz);
    }

    private TestRestTemplate getTemplate() {
        return template.withBasicAuth("spring", "secret");
    }
}

