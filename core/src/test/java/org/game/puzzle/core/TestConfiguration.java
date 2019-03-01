package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.services.ArenaService;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.stubs.TestGenerator;
import org.game.puzzle.core.subscription.InMemorySubscriptionService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.utils.Generator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration()
@EnableAutoConfiguration
@Profile("core-test")
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
    public CharacteristicService characteristicService(GameProperties properties,
                                                       SubscriptionService subscriptionService,
                                                       Generator generator) {
        return new CharacteristicService(properties, subscriptionService, generator);
    }

    @Bean
    public ArenaService arenaService(GameProperties properties,
                                     SubscriptionService subscriptionService,
                                     Generator generator) {
        return new ArenaService(properties, subscriptionService, generator);
    }

}
