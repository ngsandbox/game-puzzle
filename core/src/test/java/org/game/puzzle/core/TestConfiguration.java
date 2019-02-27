package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.services.CharacteristicService;
import org.game.puzzle.core.stubs.TestGenerator;
import org.game.puzzle.core.subscription.InMemorySubscriptionService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.utils.Generator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.inject.Inject;

@Slf4j
@Configuration
@EnableConfigurationProperties(GameProperties.class)
@Profile("core-test")
public class TestConfiguration {

    @Inject
    public GameProperties properties;

    @Bean
    public Generator generator() {
        return new TestGenerator();
    }

    @Bean
    public SubscriptionService getSubscriptionService() {
        return new InMemorySubscriptionService();
    }

    @Bean
    public CharacteristicService characteristicService(SubscriptionService subscriptionService, Generator generator){
        return new CharacteristicService(properties, subscriptionService, generator);
    }

}
