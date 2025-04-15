package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OracleConnection_PostgreSQLConnection_Backuup {
    private static final Logger logger = LogManager.getLogger(OracleConnection_PostgreSQLConnection_Backuup.class);

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
        POSTGRESQL_URL = "jdbc:postgresql://" + Config.oracleHost + ":" + Config.port + "/" + Config.database;

        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection
            conn = DriverManager.getConnection(POSTGRESQL_URL, Config.dbUsername, Config.dbPassword);
//            logger.info("✅ POSTGRESQL Connection established successfully!");
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
//            logger.info("✅ POSTGRESQL Connection closed successfully");
        } catch (SQLException e) {
            logger.info("❌ Error closing connection: " + e.getMessage());
        }
    }

    public List<String> fetchColumnNamesAndDataType(String postgreSQLTableDetails) {
        List<String> queryResult = new ArrayList<>();
        String schemaName = (String) GenericFun.getTableDetails(postgreSQLTableDetails, "schema");
        String tableName = (String) GenericFun.getTableDetails(postgreSQLTableDetails, "table");
        String sql = "SELECT column_name, data_type FROM information_schema.columns " +
                "WHERE table_schema = '" + schemaName + "' AND table_name = '" + tableName + "' ORDER BY column_name;";
        logger.info("ORACLE::" + sql);
        try {
            connect();               // Step 1: Establish Connection
            queryResult = executeQuery(sql);  // Step 2: Execute Query
        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
        } finally {
            closeConnection(); // Step 3: Close Connection
        }
        Collections.sort(queryResult);
        return queryResult;
    }

    public int fetchNumberOfRecords(String pgTableName) {
        int count = 0;
        String sql = "SELECT count(*) FROM " + pgTableName + ";";  // Query to get the count of employees
        logger.info("Oracle SQL Query:::: " + sql);
        try {
            Connection connection = connect();  // Step 1: Establish Connection
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

    public List<String> fetchColumnNames(String postgreSQLTableDetails) {
        List<String> columnNames = new ArrayList<>();
        String schemaName = (String) GenericFun.getTableDetails(postgreSQLTableDetails, "schema");
        String tableName = (String) GenericFun.getTableDetails(postgreSQLTableDetails, "table");
        String sql = "SELECT column_name FROM information_schema.columns " +
                "WHERE table_schema = '" + schemaName + "' " +
                "AND table_name = '" + tableName + "' " +
                "ORDER BY column_name;";
        logger.info("Oracle FOR GETTING COLUMN NAMES:: " + sql);
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
        Collections.sort(columnNames);
        return columnNames;
    }

    public static ArrayList<String> fetchDuplicateRecords(String columnNames, String postgreSQLTableDetails) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String schemaName = (String) GenericFun.getTableDetails(postgreSQLTableDetails, "schema");
        String tableName = (String) GenericFun.getTableDetails(postgreSQLTableDetails, "table");
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
                columnNames, schemaName, tableName, columnNames
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

    public List<String> fetchRecordWithColumnName(String columnNames, String table_Name, String limitRange) {
        List<String> queryResult = new ArrayList<>();
        String schemaName = (String) GenericFun.getTableDetails(table_Name, "schema");
        String tableName = (String) GenericFun.getTableDetails(table_Name, "table");
        String sql = String.format(
                "SELECT DISTINCT %s FROM %s LIMIT %s",
                columnNames, table_Name, limitRange
        );
        logger.info("Oracle FETCH DATA::" + sql);
        try {
            connect();               // Step 1: Establish Connection
            queryResult = executeQueryForRecords(sql);  // Step 2: Execute Query
        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
        } finally {
            closeConnection(); // Step 3: Close Connection
        }
        return queryResult;
    }

    public List<String> fetchSampleRecord(String columnName, String table_Name, String whereClause) {
        List<String> queryResult = new ArrayList<>();
        String schemaName = (String) GenericFun.getTableDetails(table_Name, "schema");
        String tableName = (String) GenericFun.getTableDetails(table_Name, "table");
        String sql = String.format(
                "SELECT %s FROM %s WHERE %s LIMIT 1",
                columnName, table_Name, whereClause
        );

        logger.info("Oracle::" + sql);
        try {
            connect();               // Step 1: Establish Connection
            queryResult = executeQueryForRecords(sql);  // Step 2: Execute Query
        } catch (Exception e) {
            System.err.println("❌ An error occurred: " + e.getMessage());
        } finally {
            closeConnection(); // Step 3: Close Connection
        }
        return queryResult;
    }

    public List<String> executeQueryForRecords(String sql) {
        List<String> results = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Get metadata to dynamically fetch column count
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Process each row dynamically
            while (rs.next()) {
                StringBuilder rowRecord = new StringBuilder();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);  // Get column name
                    int columnType = metaData.getColumnType(i);    // Get column type
                    String columnValue = rs.getString(i);         // Get column value

                    // Append column name and value
                    rowRecord.append(columnName).append("=");

                    // Format value based on data type
                    if (isNumericOrBooleanOrDateType(columnType)) {
                        rowRecord.append(columnValue);  // No quotes for numbers, booleans, and dates
                    } else {
                        rowRecord.append("'").append(columnValue).append("'");  // Add quotes for text types
                    }

                    // Append separator but only if it's NOT the last column
                    if (i < columnCount) {
                        rowRecord.append(" AND ");
                    }
                }

                results.add(rowRecord.toString());
            }
        } catch (SQLException e) {
            System.err.println("❌ Error executing query: " + e.getMessage());
        }

        return results;
    }

    /**
     * Helper method to check if a column type is numeric, boolean, or date.
     * No quotes should be added for these types.
     */
    private static boolean isNumericOrBooleanOrDateType(int columnType) {
        return columnType == Types.INTEGER ||      // INTEGER
                columnType == Types.BIGINT ||       // BIGINT
                columnType == Types.SMALLINT ||     // SMALLINT
                columnType == Types.TINYINT ||      // TINYINT
                columnType == Types.DECIMAL ||      // DECIMAL
                columnType == Types.DOUBLE ||       // DOUBLE
                columnType == Types.FLOAT ||        // FLOAT
                columnType == Types.REAL ||         // REAL
                columnType == Types.NUMERIC ||      // NUMERIC
                columnType == Types.BIT ||          // BOOLEAN (In some DBs)
                columnType == Types.BOOLEAN ||      // BOOLEAN
                columnType == Types.DATE ||         // DATE
                columnType == Types.TIME ||         // TIME
                columnType == Types.TIMESTAMP ||    // TIMESTAMP
                columnType == Types.TIMESTAMP_WITH_TIMEZONE || // TIMESTAMP WITH TIME ZONE
                columnType == Types.OTHER;          // UUID in some databases
    }
}
