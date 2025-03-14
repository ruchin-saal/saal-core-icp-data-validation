package utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.sql.Connection;
import io.trino.jdbc.TrinoResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class TrinoTableMetaData {
    private static final Logger logger = LogManager.getLogger(TrinoTableMetaData.class);
    public TrinoTableMetaData() {
        printConfigs();
    }

    public static ArrayList<String> fetchMetaData(String sqlQuery) throws Exception {
        System.out.println("************************************************");
        Config.setConfigs();
        printConfigs();
        ArrayList<String> tableMetaData = getMetaDataResult(sqlQuery);
        return tableMetaData;
    }

    public static ArrayList<String> getMetaDataResult(String sqlQuery) throws SQLException {
        //Connecting Trino to execute queries
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);
        // Check if resultSet is not null (query execution successful)
        ArrayList<String> sortedMetaData = new ArrayList<>();
        if (resultSet != null) {
            // Get metadata to fetch column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // Print column names
            for (int i = 1; i <= 1; i++) {
//                System.out.print(metaData.getColumnName(i) + "\t");
            }
//            System.out.println(); // New line after printing column names
            ArrayList<String> columnName = new ArrayList<>();
            // Print query results
            while (resultSet.next()) {
                for (int i = 1; i <= 1; i++) {
                    columnName.add(resultSet.getString(i));
                }
            }
            for (int i = 0; i < columnName.size() - 2; i++) {
                sortedMetaData.add(columnName.get(i));
            }
            Collections.sort(sortedMetaData);
//            System.out.println("NUMBERS OF COLUMNS IN DB: "+sortedMetaData.size());
//            System.out.println("COLUMNS NAMES IN DATABASE:");
//            System.out.println(sortedMetaData);


        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }
        resultSet.close();
        connection.close();
        return sortedMetaData;
    }


    public static ArrayList<String> getTableMetaDataByTableName(String catalogName, String schemaName, String tableName) throws SQLException {
        //Connecting Trino to execute queries
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
        String sqlQuery="SHOW COLUMNS FROM "+catalogName+"."+schemaName+"."+tableName;
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);
        // Check if resultSet is not null (query execution successful)
        ArrayList<String> sortedMetaData = new ArrayList<>();
        if (resultSet != null) {
            // Get metadata to fetch column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // Print column names
            ArrayList<String> columnName = new ArrayList<>();
            // Print query results
            while (resultSet.next()) {
                for (int i = 1; i <= 1; i++) {
                    columnName.add(resultSet.getString(i));
                }
            }
            for (int i = 0; i < columnName.size() - 2; i++) {
                sortedMetaData.add(columnName.get(i));
            }
//            Collections.sort(sortedMetaData);
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }
        resultSet.close();
        connection.close();
        return sortedMetaData;
    }


    public static void setAuthToken(String IDP_URL) throws Exception {
        String username = Config.username;
        String password = Config.password;
        Response response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("grant_type", "password")
                .formParam("username", username)
                .formParam("password", password)
                .formParam("client_id", Config.Client_NAME)
                .when()
                .post(IDP_URL);
        LoginResponse loginResponse = response.as(LoginResponse.class);
        Config.access_token = loginResponse.getAccess_token();
    }

    public static void printConfigs() {
        logger.info("=========[ Trino Host Name ] : " + Config.trino_host);
        logger.info("=========[ Trino Port Number ] : " + Config.trino_port);
        logger.info("=========[ Trino Username ] : " + Config.trino_username);
//        logger.info("=========[ Trino Password ] : " + Config.trino_password);
        logger.info("=======================================================\n");
    }


    public static ArrayList<String> fetchTableData(String catalogName, String schemaName, String tableName,String columnName) throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
//        System.out.println("Fetching record of '"+columnName+"' column");
        // Construct the SQL query to fetch records for the specified column
         String sqlQuery = "SELECT " + columnName + " FROM "+catalogName+"."+schemaName+"."+tableName;

        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnRecords = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                // Fetch the record for the specified column and add it to the list
                columnRecords.add(resultSet.getString(1));  // Assuming single column result
            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
//        System.out.println(columnRecords);
        connection.close();
        return columnRecords;
    }

    public ArrayList<String> fetchTableDataColumnWise(String catalogName, String schemaName, String tableName) throws SQLException {
        //Fetching column names
        ArrayList<String> columnNames=getTableMetaDataByTableName(catalogName,schemaName,tableName);
        //Fetching column data
        ArrayList<String> fetchedColumnData=new ArrayList<>();
        for(int i=0;i<columnNames.size();i++)
        {
            ArrayList<String> columnData=fetchTableData(catalogName,schemaName,tableName,columnNames.get(i));
            for(int j=0;j<columnData.size();j++) {
                fetchedColumnData.add(columnData.get(j));
            }
            Collections.sort(fetchedColumnData);
            return fetchedColumnData;
        }
        return columnNames;
    }


    public ArrayList<String> fetchTableDataColumnsWise(String catalogName, String schemaName, String tableName,String columnName) throws SQLException {
        ArrayList<String> fetchedColumnData=new ArrayList<>();
        ArrayList<String> columnData=fetchTableData(catalogName,schemaName,tableName,columnName);
        for(int j=0;j<columnData.size();j++) {
            fetchedColumnData.add(columnData.get(j));
        }
        fetchedColumnData.removeIf(item -> item == null);
//        System.out.println(fetchedColumnData);
//        Collections.sort(fetchedColumnData);
        return fetchedColumnData;
    }

    public ArrayList<String> fetchObjectTypeTableDataColumnsWise(String catalogName, String schemaName, String tableName,String columnName) throws SQLException {
        ArrayList<String> fetchedColumnData=new ArrayList<String>();
        ArrayList<String> columnData=fetchTableData(catalogName,schemaName,tableName,columnName);
        for(int j=0;j<columnData.size();j++) {
            System.out.println("CONDITION 1: columnData.get(j).equals(null)==" +columnData.get(j).equals(null)+" at index: "+j+" ##DATA: "+columnData.get(j));
            System.out.println("Condtion 2: columnData.get(j).equals('')==" +columnData.get(j).equals("")+" at index: "+j+" ##DATA: "+columnData.get(j));
            if(columnData.get(j).equals(null) || columnData.get(j).equals(""))
            {
                fetchedColumnData.add("null");
            }
            else {
                fetchedColumnData.add(columnData.get(j));
            }
        }
//        Collections.sort(fetchedColumnDatas);
        return fetchedColumnData;
    }

    public ArrayList<String> fetchTableDataColumnsWiseWithoutSorting(String catalogName, String schemaName, String tableName,String columnName) throws SQLException {
        ArrayList<String> fetchedColumnData=new ArrayList<>();
        ArrayList<String> columnData=fetchTableData(catalogName,schemaName,tableName,columnName);
        for(int j=0;j<columnData.size();j++) {
            fetchedColumnData.add(columnData.get(j));
        }
        return fetchedColumnData;
    }

    public static ArrayList<String> fetchTableDataFromMultipleColumns(String catalogName, String schemaName, String tableName,String columnName1,String columnName2, String columnName3) throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
//        System.out.println("Fetching record of '"+columnName+"' column");
        // Construct the SQL query to fetch records for the specified column
        String sqlQuery;
        if(columnName2.equals(null) && columnName3.equals(null)) {
            sqlQuery = "SELECT " + columnName1 + " FROM " + catalogName + "." + schemaName + "." + tableName;
        } else if (!columnName1.equals(null) && !columnName2.equals(null) && columnName3.equals(null)) {
            sqlQuery = "SELECT " + columnName1 +", "+columnName2+ " FROM " + catalogName + "." + schemaName + "." + tableName;
        } else {
            sqlQuery = "SELECT " + columnName1 +", "+columnName2+", "+columnName3+ " FROM " + catalogName + "." + schemaName + "." + tableName;
        }

        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnRecords = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                // Fetch the record for the specified column and add it to the list
                if(columnName2.equals(null) && columnName3.equals(null)) {
                    columnRecords.add(resultSet.getString(1));
                } else if (!columnName1.equals(null) && !columnName2.equals(null) && columnName3.equals(null)) {
                    columnRecords.add(resultSet.getString(1)+" "+resultSet.getString(2));
                } else {
                    columnRecords.add(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3));
                }


            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
//        System.out.println(columnRecords);
        connection.close();
        return columnRecords;
    }

    public ArrayList<String> fetchDuplicateRecordsDataColumnsWise(String catalogName, String schemaName, String tableName,String columnName1, String columnName2, String columnName3) throws SQLException {
        ArrayList<String> fetchedColumnData=new ArrayList<>();
        ArrayList<String> columnData=fetchDuplicateRecordFromTable(catalogName,schemaName,tableName,columnName1,columnName2,columnName3);
        for(int j=0;j<columnData.size();j++) {
            fetchedColumnData.add(columnData.get(j));
        }
        fetchedColumnData.removeIf(item -> item == null);
//        System.out.println(fetchedColumnData);
//        Collections.sort(fetchedColumnData);
        return fetchedColumnData;
    }


    public static ArrayList<String> fetchDuplicateRecordFromTable(String catalogName, String schemaName, String tableName,String columnName1, String columnName2, String columnName3) throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
//        System.out.println("Fetching record of '"+columnName+"' column");
        // Construct the SQL query to fetch records for the specified column
        String sqlQuery;
        if(columnName2==null && columnName3==null) {
            sqlQuery = "SELECT " + columnName1 + ", COUNT(*) FROM " + catalogName + "." + schemaName + "." + tableName +
                    " Group by " + columnName1 + " having COUNT(*) >1";
            logger.info("DUPLICATE RECORD QUERY IS: \n" +sqlQuery);
        }else if(!(columnName1 ==null) && !(columnName2 ==null) && columnName3==null){
            sqlQuery = "SELECT " + columnName1 +", "+columnName2+ ", COUNT(*) FROM " + catalogName + "." + schemaName + "." + tableName +
                    " Group by " + columnName1 + ", "+columnName2+" having COUNT(*) >1";
            logger.info("DUPLICATE RECORD QUERY IS: \n" +sqlQuery);
        }else {
            sqlQuery = "SELECT " + columnName1 +", "+columnName2+", "+columnName3+ ", COUNT(*) FROM " + catalogName + "." + schemaName + "." + tableName +
                    " Group by " + columnName1 + ", "+columnName2+", "+columnName3+" having COUNT(*) >1";
            logger.info("DUPLICATE RECORD QUERY IS: \n" +sqlQuery);
        }

        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnRecords = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                // Fetch the record for the specified column and add it to the list
                columnRecords.add(resultSet.getString(1));  // Assuming single column result
            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
//        System.out.println(columnRecords);
        connection.close();
        return columnRecords;
    }


    public ArrayList<String> fetchTableDataSqlQueryWise(String sqlQuery) throws SQLException {
        ArrayList<String> fetchedColumnData=new ArrayList<>();
        ArrayList<String> columnData=fetchTableDataSQLQueryWise(sqlQuery);
        for(int j=0;j<columnData.size();j++) {
            fetchedColumnData.add(columnData.get(j));
        }
        fetchedColumnData.removeIf(item -> item == null);
        return fetchedColumnData;
    }

    public static ArrayList<String> fetchTableDataSQLQueryWise(String query) throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
        String sqlQuery = query;

        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnRecords = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                // Fetch the record for the specified column and add it to the list
                columnRecords.add(resultSet.getString(1));  // Assuming single column result
            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
//        System.out.println(columnRecords);
        connection.close();
        return columnRecords;
    }

    public String constructQuery(String catalogName, String schemaName, String tableName, String columnName,
                                 String whereClauseColumnName, String whereClauseValue){
        String sqlQuery="Select "+columnName+" from "+catalogName+"."+schemaName+"."+tableName+ " where "+whereClauseColumnName+" in("+whereClauseValue+")";
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"[","");
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"]","");
        logger.info("SQL Query is: "+sqlQuery);
        return sqlQuery;
    }

    public static ArrayList<String> fetchTableDataByQuery(String SQLQuery) throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
//        System.out.println("Fetching record of '"+columnName+"' column");
        // Construct the SQL query to fetch records for the specified column
        String sqlQuery = SQLQuery;
        logger.info("SQL Query: "+sqlQuery);
        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnRecords = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                // Fetch the record for the specified column and add it to the list
                columnRecords.add(resultSet.getString(1));  // Assuming single column result
            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
//        System.out.println(columnRecords);
        connection.close();
        return columnRecords;
    }

    public String constructQueryWithDecimalCasting(String catalogName, String schemaName, String tableName, String columnName,
                                 String whereClauseColumnName, String whereClauseValue){
        String sqlQuery="Select cast("+columnName+" as decimal(15,1)) from "+catalogName+"."+schemaName+"."+tableName+ " where "+whereClauseColumnName+" in("+whereClauseValue+")";
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"[","");
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"]","");
        logger.info("SQL Query is: "+sqlQuery);
        return sqlQuery;
    }

    public String constructQueryWithTwoClause(String catalogName, String schemaName, String tableName, String columnName,
                                 String whereClauseColumnName1, String whereClauseValue1, String whereClauseColumnName2, String whereClauseValue2){
        String sqlQuery="Select "+columnName+" from "+catalogName+"."+schemaName+"."+tableName+
                " where "+whereClauseColumnName1+" in("+whereClauseValue1+")"+ " AND "+whereClauseColumnName2+" in("+whereClauseValue2+")";
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"[","");
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"]","");
        logger.info("SQL Query is: "+sqlQuery);
        return sqlQuery;
    }

    public String constructQueryWithTwoClauseAndWithDecimalCasting(String catalogName, String schemaName, String tableName, String columnName,
                                              String whereClauseColumnName1, String whereClauseValue1, String whereClauseColumnName2, String whereClauseValue2){
        String sqlQuery="Select cast("+columnName+"  as decimal(15,2)) from "+catalogName+"."+schemaName+"."+tableName+
                " where "+whereClauseColumnName1+" in("+whereClauseValue1+")"+ " AND "+whereClauseColumnName2+" in("+whereClauseValue2+")";
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"[","");
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"]","");
        logger.info("SQL Query is: "+sqlQuery);
        return sqlQuery;
    }


    public String constructQueryWithTwoColumnAndTwoClauseAndWithDecimalCasting(String catalogName, String schemaName, String tableName, String columnName1,String columnName2,
                                                                   String whereClauseColumnName1, String whereClauseValue1, String whereClauseColumnName2, String whereClauseValue2){
        String sqlQuery="Select cast("+columnName1+","+columnName2+"  as decimal(15,2)) from "+catalogName+"."+schemaName+"."+tableName+
                " where "+whereClauseColumnName1+" in("+whereClauseValue1+")"+ " AND "+whereClauseColumnName2+" in("+whereClauseValue2+")";
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"[","");
        sqlQuery=GenericFun.replaceAnyValue(sqlQuery,"]","");
        logger.info("SQL Query is: "+sqlQuery);
        return sqlQuery;
    }

    public static ArrayList<String> fetchTwoColumnsTableDataByQueryFrom(String SQLQuery) throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
//        System.out.println("Fetching record of '"+columnName+"' column");
        // Construct the SQL query to fetch records for the specified column
        String sqlQuery = SQLQuery;
        logger.info("SQL Query: "+sqlQuery);
        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnRecords = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                // Fetch the record for the specified column and add it to the list
                columnRecords.add(resultSet.getString(1)+","+resultSet.getString(2));  // Assuming single column result
            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
//        System.out.println(columnRecords);
        connection.close();
        return columnRecords;
    }

    public static ArrayList<String> getTableMetaDataByTableName() throws SQLException {
        //Connecting Trino to execute queries
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
        String sqlQuery="SHOW COLUMNS FROM "+Config.catalog_name+"."+Config.schemas_name+"."+Config.catalog_name;
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);
        // Check if resultSet is not null (query execution successful)
        ArrayList<String> sortedMetaData = new ArrayList<>();
        if (resultSet != null) {
            // Get metadata to fetch column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // Print column names
            ArrayList<String> columnName = new ArrayList<>();
            // Print query results
            while (resultSet.next()) {
                for (int i = 1; i <= 1; i++) {
                    columnName.add(resultSet.getString(i));
                }
            }
            for (int i = 0; i < columnName.size() - 2; i++) {
                sortedMetaData.add(columnName.get(i));
            }
//            Collections.sort(sortedMetaData);
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }
        resultSet.close();
        connection.close();
        return sortedMetaData;
    }

    public static ArrayList<String> fetchColumnNamesAndDataType() throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;
        String sqlQuery = String.format(
                "SELECT column_name, data_type FROM %s.information_schema.columns WHERE table_schema = '%s' AND table_name = '%s' ORDER BY column_name",
                Config.catalog_name, Config.schemas_name, Config.employee_Trino_Table
        );
        logger.info("TRINO SQL:: "+sqlQuery);
        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnRecords = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                String columnName = resultSet.getString(1); // Fetch column name
                String dataType = resultSet.getString(2); // Fetch data type
                columnRecords.add(columnName + " - " + dataType);  // Store both column and data type
            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
        return columnRecords;
    }

    public static int fetchNumberOfRecords() throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);

        String sqlQuery = String.format(
                "SELECT COUNT(*) FROM %s.%s.%s",
                Config.catalog_name, Config.schemas_name, Config.employee_Trino_Table
        );
        logger.info("TRINO SQL:: " + sqlQuery);

        // Execute the query
        TrinoResultSet resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        int rowCount = 0;
        if (resultSet != null && resultSet.next()) {
            rowCount = resultSet.getInt(1); // Fetch count
        } else {
            System.out.println("ResultSet is null or empty. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
        return rowCount; // Return the count of rows
    }

    public static ArrayList<String> fetchColumnNames() throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;

        String sqlQuery = String.format(
                "SELECT column_name FROM %s.information_schema.columns WHERE table_schema = '%s' AND table_name = '%s' ORDER BY column_name",
                Config.catalog_name, Config.schemas_name, Config.employee_Trino_Table
        );

//        logger.info("TRINO SQL FOR GETTING COLUMN NAAMES OF TABLE:: " + sqlQuery);
        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);

        ArrayList<String> columnNames = new ArrayList<>();
        if (resultSet != null) {
            while (resultSet.next()) {
                String columnName = resultSet.getString(1); // Fetch only column name
                columnNames.add(columnName);  // Store only column name
            }
        } else {
            System.out.println("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
        return columnNames;
    }

    public static ArrayList<String> duplicateRecords(String columnNames) throws SQLException {
        TrinoConnection trinoConnection = new TrinoConnection();
        Connection connection = trinoConnection.connect_trino(Config.catalog_name);
        connection.setAutoCommit(true);
        TrinoResultSet resultSet = null;

        // Ensure columnNames is valid
        if (columnNames == null || columnNames.trim().isEmpty()) {
            throw new IllegalArgumentException("Column names cannot be null or empty");
        }

        // Construct SQL query to find duplicate records
        String sqlQuery = String.format(
                "SELECT %s, COUNT(*) AS duplicate_count " +
                        "FROM %s.%s.%s " +
                        "GROUP BY %s " +
                        "HAVING COUNT(*) > 1",
                columnNames, Config.catalog_name, Config.schemas_name, Config.employee_Trino_Table, columnNames
        );

        logger.info("<<<<<TRINO SQL FOR DUPLICATE RECORDS>>>>>" + sqlQuery);

        // Execute the query
        resultSet = (TrinoResultSet) trinoConnection.executeSelectQuery(sqlQuery);
        ArrayList<String> duplicateRecords = new ArrayList<>();
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
//                logger.info(rowRecord.toString());

                // Store the full row record in the list
                duplicateRecords.add(rowRecord.toString());
            }
        } else {
            logger.info("ResultSet is null. Query execution failed.");
        }

        // Close resultSet and connection
        if (resultSet != null) {
            resultSet.close();
        }
        if (connection != null) {
            connection.close();
        }
        return duplicateRecords;
    }





}