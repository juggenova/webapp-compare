package com.juggenova.doodleclone.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

/**
 * MariaDB server manager based on YadaMariaDBServer from YadaFramework
 */
public class MariaDBServer {
    // Static block to ensure the MySQL driver is loaded
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not found", e);
        }
    }
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private DB db;
    private Integer port;
    private String dataDir;
    private String baseDir;
    private String dbName;

    /**
     * Creates a new MariaDBServer with the specified configuration
     * @param port the port to use
     * @param dataDir the data directory
     * @param baseDir the base directory
     * @param dbName the database name
     */
    public MariaDBServer(int port, String dataDir, String baseDir, String dbName) {
        this.port = port;
        this.dataDir = dataDir;
        this.baseDir = baseDir;
        this.dbName = dbName;

        try {
            initialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start the embedded MariaDB", e);
        }
    }

    /**
     * Initializes the MariaDB server
     */
    private void initialize() throws Exception {
        logger.info("Initializing MariaDB server with data directory: {}, base directory: {}", dataDir, baseDir);

        // Create directories if they don't exist
        File dataDirFile = new File(dataDir);
        dataDirFile.mkdirs();
        if (!dataDirFile.isDirectory() || !dataDirFile.canWrite()) {
            throw new RuntimeException("Can't write embedded database to folder " + dataDirFile);
        }

        File baseDirFile = new File(baseDir);
        baseDirFile.mkdirs();
        if (!baseDirFile.isDirectory() || !baseDirFile.canWrite()) {
            throw new RuntimeException("Can't write embedded database to folder " + baseDirFile);
        }

        // Create lib directory
        File libDirFile = new File(baseDirFile, "lib");
        libDirFile.mkdirs();
        if (!libDirFile.isDirectory() || !libDirFile.canWrite()) {
            throw new RuntimeException("Can't write to lib directory " + libDirFile);
        }

        logger.info("Using directories: data={}, base={}, lib={}",
            dataDirFile.getAbsolutePath(),
            baseDirFile.getAbsolutePath(),
            libDirFile.getAbsolutePath());

        // Configure the database
        DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
        configBuilder.setBaseDir(baseDirFile.getAbsolutePath());
        configBuilder.setDataDir(dataDirFile.getAbsolutePath());
        configBuilder.setPort(port);
        configBuilder.setLibDir(libDirFile.getAbsolutePath());

        // If the base folder is empty install the db, otherwise open it
        String[] filesInBaseDir = baseDirFile.list();
        boolean install = filesInBaseDir == null || filesInBaseDir.length <= 1; // No files or only lib directory

        try {
            if (install) {
                logger.info("Installing new database in {}", baseDir);
                db = DB.newEmbeddedDB(configBuilder.build());
                db.start();
            } else {
                logger.info("Opening existing database in {}", baseDir);
                db = CustomMariaDB.openEmbeddedDB(configBuilder.build());

                // Check if database process is running already
                try {
                    Connection connection = openConnection();
                    connection.close();
                    // Yes it's running.
                    logger.info("Attaching to running database instance");
                } catch (Exception e) {
                    // Not running so start it
                    logger.info("Starting database process");
                    db.start();
                }
            }

            logger.info("Using embedded MariaDB on localhost:{} with data folder {}", port, dataDirFile.getAbsolutePath());
            logger.info("Database login as root with empty password");

            // Create the database if it doesn't exist
            ensureSchema();
        } catch (Exception e) {
            logger.error("Failed to initialize MariaDB: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize MariaDB", e);
        }
    }

    /**
     * Creates the database schema when it doesn't exist
     */
    private void ensureSchema() throws SQLException {
        Connection connection = openConnection();
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            logger.debug("Database schema {} created or already exists", dbName);
        } finally {
            connection.close();
        }
    }

    /**
     * Opens a connection to the database
     */
    private Connection openConnection() throws SQLException {
        try {
            // Explicitly load the MySQL driver
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL driver not found", e);
            throw new SQLException("MySQL driver not found", e);
        }

        // Use a direct JDBC URL instead of getting it from the DB configuration
        String jdbcUrl = "jdbc:mariadb://localhost:" + port + "/";
        logger.info("Connecting to database with URL: {}", jdbcUrl);
        Connection connection = DriverManager.getConnection(jdbcUrl, "root", ""); // Connecting with default root privileges
        return connection;
    }

    /**
     * Gets the database instance
     */
    public DB getDatabase() {
        return db;
    }

    /**
     * Gets the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Stops the database
     */
    public void stop() {
        if (db != null) {
            try {
                db.stop();
                logger.info("MariaDB server stopped");
            } catch (ManagedProcessException e) {
                logger.error("Failed to stop MariaDB server", e);
            }
        }
    }
}
