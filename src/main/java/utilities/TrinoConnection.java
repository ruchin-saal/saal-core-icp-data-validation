package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;

public class TrinoConnection {
    private static final Logger logger = LogManager.getLogger(TrinoConnection.class);
    public static Connection con;
    public static Statement stmt;

    static String trinoDriverClass = "io.trino.jdbc.TrinoDriver";

    static String connectionString = "jdbc:trino://" + Config.trino_host
            + ":" + Config.trino_port + "/tpch/sf1"
            + "?user=" + Config.trino_username
            + "&password=" + Config.trino_password
            + "&SSL=true";

//    static String connectionString = "jdbc:trino://" + Config.trino_host
//            + ":" + Config.trino_port + "/tpch?"
//            + "user=" + Config.trino_username
//            + "&password=" + Config.trino_password;

    public Connection connect_trino(String scale_factor) {
        connectionString = "jdbc:trino://" + Config.trino_host
                + ":" + Config.trino_port + "/"+Config.catalog_name+"/" + scale_factor + ""
                + "?user=" + Config.trino_username
                + "&password=" + Config.trino_password
                + "&SSL=true";
//        System.out.println("\n[ JDBC Connection String is ] -->> " + connectionString);
        try {
            Class.forName(trinoDriverClass);
            con = DriverManager.getConnection(connectionString);
            con.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public ResultSet executeSelectStatement(String Catalog_name, String Schema_name, String tableName, String limit) {
        ResultSet rs = null;
        String statementToExecute = "Select * From " +
                Catalog_name + "." +
                Schema_name + "." +
                tableName +
                " Limit " + limit;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(statementToExecute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void closeConnection() {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSetMetaData getTableColumnMetaDataFromTrino(String table_name) throws Exception {
        String statementToExecute = "Select * From " + table_name + " Limit 5";
        ResultSet resultSet = null;
        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(statementToExecute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet.getMetaData();
    }

    public ResultSet executeUseStatement(String Catalog_name, String Schema_name) {
        ResultSet rs = null;
        String statementToExecute = "USE " + Catalog_name + "." + Schema_name;
        System.out.println("Select Schema Query is -->> " + statementToExecute);
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(statementToExecute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet executeSelectQuery(String SqlQuery) {
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(SqlQuery);
        } catch (SQLException e) {
            logger.info("QUERY ISSUE OR TRINO CONNECTION LOSSED DUE TO NETWORK / VPN ISSUE");
            e.printStackTrace();
        }
        return rs;
    }
}
