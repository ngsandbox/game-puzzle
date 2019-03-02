package org.game.puzzle.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan
@Configuration
public class DataBaseConfiguration {
}
