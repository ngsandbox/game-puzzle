package org.game.puzzle.core;

import lombok.extern.slf4j.Slf4j;
import org.game.puzzle.core.configs.GameProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties//(GameProperties.class)
@ComponentScan("org.game.puzzle.core")
public class CommonConfiguration {
}
