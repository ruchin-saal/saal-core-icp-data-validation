package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSQLConnection {
    private static final Logger logger = LogManager.getLogger(PostgreSQLConnection.class);

    private static String POSTGRESQL_URL;
    private static Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    /**
     * Establishes a connection to PostgreSQL.
     *
     * @return
     */
    public static Connection connect() throws Exception {
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
        return conn;
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

    public int fetchNumberOfRecords() {
        int count = 0;
        String sql = "SELECT count(*) FROM " + Config.pg_schema_name + "." + Config.employeeTable + ";";  // Query to get the count of employees
        logger.info("POSTGRESQL::" + sql);

        try {
            Connection connection=connect();  // Step 1: Establish Connection
            // Execute the query and retrieve the count value
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                count = rs.getInt(1);  // Get the first column of the result set, which is the count
            }
        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
        } finally {
            closeConnection();  // Step 3: Close Connection
        }
        return count;
    }

    public List<String> fetchColumnNames() {
        List<String> columnNames = new ArrayList<>();
        String sql = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_schema = '" + Config.pg_schema_name + "' " +
                "AND table_name = '" + Config.employeeTable + "' " +
                "ORDER BY column_name;";

//        logger.info("POSTGRESQL FOR GETTING COLUMN NAMES:: " + sql);

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connect();  // Step 1: Establish Connection
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);  // Step 2: Execute Query

            if (resultSet != null) {
                while (resultSet.next()) {
                    columnNames.add(resultSet.getString("column_name")); // Fetch ONLY column_name
                }
            } else {
                logger.info("❌ ResultSet is null. Query execution failed.");
            }
        } catch (SQLException e) {
            logger.info("❌ Error executing query: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                logger.info("❌ Error closing resources: " + e.getMessage());
            }
        }

        return columnNames;
    }


    public static ArrayList<String> fetchDuplicateRecords(String columnNames) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        // Ensure columnNames is valid
        if (columnNames == null || columnNames.trim().isEmpty()) {
            throw new IllegalArgumentException("Column names cannot be null or empty");
        }

        // Construct SQL query to find duplicate records in PostgreSQL
        String sqlQuery = String.format(
                "SELECT %s, COUNT(*) AS duplicate_count " +
                        "FROM %s.%s " +
                        "GROUP BY %s " +
                        "HAVING COUNT(*) > 1",
                columnNames, Config.pg_schema_name, Config.employeeTable, columnNames
        );
        logger.info("<<<<<POSTGRESQL SQL FOR DUPLICATE RECORDS>>>>> " + sqlQuery);
        ArrayList<String> duplicateRecords = new ArrayList<>();
        try {
            // Establish Connection
            connection = connect();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

            if (resultSet != null) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount(); // Get number of columns in result

                while (resultSet.next()) {
                    StringBuilder rowRecord = new StringBuilder();

                    // Loop through all columns dynamically
                    for (int i = 1; i <= columnCount; i++) {
                        rowRecord.append(metaData.getColumnName(i)) // Column Name
                                .append(": ")
                                .append(resultSet.getString(i)) // Column Value
                                .append(" | ");
                    }

                    // Remove last " | " separator
                    if (rowRecord.length() > 3) {
                        rowRecord.setLength(rowRecord.length() - 3);
                    }

                    // Print the full row
//                    logger.info(rowRecord.toString());
                    // Store the full row record in the list
                    duplicateRecords.add(rowRecord.toString());
                }
            } else {
                logger.info("ResultSet is null. Query execution failed.");
            }
        } catch (SQLException e) {
            logger.info("❌ Error executing query: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Close resources
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return duplicateRecords;
    }
}
