package com.juggenova.doodleclone.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfiguration;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

/**
 * Allows reopening of an existing MariaDB data folder
 * Based on YadaMariaDB from YadaFramework
 */
public class CustomMariaDB extends DB {
    private static final Logger logger = LoggerFactory.getLogger(CustomMariaDB.class);

    protected CustomMariaDB(DBConfiguration config) {
        super(config);
    }

    /**
     * This factory method is the mechanism for opening an existing embedded database for use. This
     * method assumes that the database has already been prepared for use.
     *
     * @param config Configuration of the embedded instance
     * @return a new DB instance
     * @throws ManagedProcessException if something fatal went wrong
     */
    public static DB openEmbeddedDB(DBConfiguration config) throws ManagedProcessException {
        logger.info("Opening existing embedded database with config: baseDir={}, dataDir={}, libDir={}",
            config.getBaseDir(), config.getDataDir(), config.getLibDir());

        // Ensure directories exist
        File baseDir = new File(config.getBaseDir());
        if (!baseDir.exists()) {
            logger.info("Creating base directory: {}", baseDir);
            baseDir.mkdirs();
        }

        File dataDir = new File(config.getDataDir());
        if (!dataDir.exists()) {
            logger.info("Creating data directory: {}", dataDir);
            dataDir.mkdirs();
        }

        File libDir = new File(config.getLibDir());
        if (!libDir.exists()) {
            logger.info("Creating lib directory: {}", libDir);
            libDir.mkdirs();
        }

        CustomMariaDB db = new CustomMariaDB(config);
        db.prepareDirectories();
        return db;
    }

    /**
     * This factory method is the mechanism for opening an existing embedded database for use. This
     * method assumes that the database has already been prepared for use with default
     * configuration, allowing only for specifying port.
     *
     * @param port the port to start the embedded database on
     * @return a new DB instance
     * @throws ManagedProcessException if something fatal went wrong
     */
    public static DB openEmbeddedDB(int port) throws ManagedProcessException {
        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        config.setPort(port);
        return openEmbeddedDB(config.build());
    }
}
