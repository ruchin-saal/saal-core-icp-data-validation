package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLConnection {
    private static final Logger logger = LogManager.getLogger(PostgreSQLConnection.class);

    private static String POSTGRESQL_URL;
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    /**
     * Establishes a connection to PostgreSQL.
     */
    public void connect() throws Exception {
        Config.setConfigs();
        POSTGRESQL_URL = "jdbc:postgresql://" + Config.postgresqlHost + ":" + Config.port + "/" + Config.database;

        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection
            conn = DriverManager.getConnection(POSTGRESQL_URL, Config.dbUsername, Config.dbPassword);
            System.out.println("✅ Connection established successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error connecting to database: " + e.getMessage());
            throw e;
        }
    }

    public List<String> executeQuery(String sql) {
        List<String> results = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            // Fetch Result Data
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                String dataType = rs.getString("data_type");

                // Format the result as "column_name - data_type"
                results.add(columnName + " - " + dataType);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error executing query: " + e.getMessage());
        }
        return results;
    }


    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
            System.out.println("✅ Connection closed.");
        } catch (SQLException e) {
            System.err.println("❌ Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Calls all methods in sequence to connect, execute query, and close the connection.
     * Returns the query result as a List<String>.
     */
    public List<String> runDatabaseOperations() {
        List<String> queryResult = new ArrayList<>();
        String sql = "SELECT * FROM " +Config.pg_schema_name+"."+ Config.employeeTable + " LIMIT 10;";
        try {
            connect();               // Step 1: Establish Connection
            queryResult = executeQuery(sql);  // Step 2: Execute Query
        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
        } finally {
            closeConnection(); // Step 3: Close Connection
        }
        return queryResult;
    }

    public List<String> fetchColumnNamesAndDataType() {
        List<String> queryResult = new ArrayList<>();
        String sql = "SELECT column_name, data_type FROM information_schema.columns " +
                "WHERE table_schema = '"+Config.pg_schema_name+"' AND table_name = '"+Config.employeeTable+"' ORDER BY column_name;";
        logger.info("POSTGRESQL::"+sql);
        try {
            connect();               // Step 1: Establish Connection
            queryResult = executeQuery(sql);  // Step 2: Execute Query
        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
        } finally {
            closeConnection(); // Step 3: Close Connection
        }
        return queryResult;
    }


}
