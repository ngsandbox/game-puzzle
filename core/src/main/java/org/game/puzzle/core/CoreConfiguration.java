package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.game.puzzle.core.subscription.InMemorySubscriptionService;
import org.game.puzzle.core.subscription.SubscriptionService;
import org.game.puzzle.core.utils.Generator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties
@ComponentScan("org.game.puzzle.core")
public class CoreConfiguration {

    @ConditionalOnMissingBean(Generator.class)
    @Bean
    public Generator generator() {
        return new Generator();
    }

    @ConditionalOnMissingBean(SubscriptionService.class)
    @Bean
    public SubscriptionService getSubscriptionService() {
        return new InMemorySubscriptionService();
    }
}
