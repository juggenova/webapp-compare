package com.juggenova.doodleclone.config;

import java.io.File;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import jakarta.annotation.PreDestroy;

@Configuration
@Profile("dev")
public class EmbeddedMariaDBConfig {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedMariaDBConfig.class);

    private static final String DATA_DIR = new File("./embedded-mariadb-data").getAbsolutePath();
    private static final String BASE_DIR = new File("./embedded-mariadb-base").getAbsolutePath();
    private static final int PORT = 3306;
    private static final String DB_NAME = "doodledb";

    private MariaDBServer mariaDBServer;

    @Bean
    public DataSource dataSource() {
        logger.info("Initializing embedded MariaDB datasource");
        logger.info("Using data directory: {}", DATA_DIR);
        logger.info("Using base directory: {}", BASE_DIR);
        logger.info("Using port: {}", PORT);
        logger.info("Using database name: {}", DB_NAME);

        try {
            // Create and initialize the MariaDB server
            mariaDBServer = new MariaDBServer(PORT, DATA_DIR, BASE_DIR, DB_NAME);

            // Configure the datasource to connect to the embedded MariaDB
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
            dataSource.setUrl("jdbc:mariadb://localhost:" + PORT + "/" + DB_NAME);
            dataSource.setUsername("root");
            dataSource.setPassword("");

            logger.info("Embedded MariaDB datasource initialized successfully");
            return dataSource;
        } catch (Exception e) {
            logger.error("Failed to initialize embedded MariaDB datasource: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize embedded MariaDB datasource", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        // Properly shut down the database when the application stops
        if (mariaDBServer != null) {
            mariaDBServer.stop();
        }
    }
}
