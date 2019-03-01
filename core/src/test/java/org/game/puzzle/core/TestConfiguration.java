package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.dao.GameDAO;
import org.game.puzzle.core.stubs.TestGameDao;
import org.game.puzzle.core.stubs.TestGenerator;
import org.game.puzzle.core.subscription.InMemorySubscriptionService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.utils.Generator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class TestConfiguration {

    @Primary
    @Bean
    public Generator generator() {
        return new TestGenerator();
    }

    @Bean
    public SubscriptionService getSubscriptionService() {
        return new InMemorySubscriptionService();
    }

    @Bean
    public GameDAO gameDAO(){
        return new TestGameDao();
    }
}
