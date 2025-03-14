package testScripts.testDataValidation;

import baseConfig.BaseClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import utilities.*;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTable extends BaseClass {
    private static final Logger logger = LogManager.getLogger(EmployeeTable.class);

    @Test
    public void test() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(Config.catalog_name, Config.schemas_name, Config.employee_Trino_Table);
        logger.info("DB column names: " + columnNames);
        ArrayList<String> dbIdValue = tableMetadata.fetchTableDataColumnsWise(Config.catalog_name, Config.schemas_name, Config.employee_Trino_Table, columnNames.get(0));
    }

    @Test
    public void validateColumnNamesAndDataType() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNamesAndDataType();
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, "ingested_at - timestamp(6)");
        logger.info("TRINO>>"+updatedTrinoListString);
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
        List<String> postgreSqlResults = postgreSQLConnection.fetchColumnNamesAndDataType();
        ArrayList<String> updatedPostgreSqlListString = GenericFun.removeItemFromList(trinoResult, "id - integer");
        logger.info("POSTGRESS>>"+updatedPostgreSqlListString);
        logger.info("Validation Result for Column Names and DataType");
        AssertHelpers.compareColumnNameAndDataTypeListElementWise(updatedPostgreSqlListString, updatedTrinoListString);
    }

    @Test
    public void validateNumberOfRecords() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        int trinoResult = tableMetadata.fetchNumberOfRecords();
        logger.info("TRINO>>"+trinoResult);
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
        int postgreSqlResults = postgreSQLConnection.fetchNumberOfRecords();
        logger.info("POSTGRESS>>"+postgreSqlResults);
        logger.info("Validation result for number of records in PostgreSQL and Trino");
        AssertHelpers.validateNumberOfRecords(postgreSqlResults, trinoResult);
        AssertHelpers.softAssertAll();
    }

    @Test
    public void duplicateRecordInTable() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNames();
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, "ingested_at");
          String columnNames=null;
        columnNames= String.valueOf(updatedTrinoListString);
        columnNames=GenericFun.removeTextFromString(columnNames,"[");
        columnNames=GenericFun.removeTextFromString(columnNames,"]");
        ArrayList<String> trinoDuplicateRecord=tableMetadata.duplicateRecords(columnNames);
 //        logger.info("TRINO DUPLICATE RECORD>>"+trinoDuplicateRecord);
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
        List<String> postgreSqlResults = postgreSQLConnection.fetchColumnNames();
        ArrayList<String> updatedPostgreSqlListString = GenericFun.removeItemFromList((ArrayList<String>) postgreSqlResults, "id");
//        logger.info("POSTGRESS>>"+updatedPostgreSqlListString);
        String postgreSQLColumnNames= String.valueOf(updatedPostgreSqlListString);
        columnNames=null;
        columnNames=GenericFun.removeTextFromString(postgreSQLColumnNames,"[");
        columnNames=GenericFun.removeTextFromString(columnNames,"]");
        ArrayList<String> postgreSqlDuplicateRecordList=postgreSQLConnection.fetchDuplicateRecords(columnNames);
//        logger.info("POSTGRESQL DUPLICATE RECORD>>"+postgreSqlDuplicateRecordList);
        logger.info("DUPLICATE RECORD CHECK IN TRINO");
        AssertHelpers.assertTrueSoft(trinoDuplicateRecord.size());
        logger.info("DUPLICATE RECORD CHECK IN POSTGRESQL");
        AssertHelpers.assertTrueSoft(postgreSqlDuplicateRecordList.size());
        AssertHelpers.softAssertAll();
    }
}






/*
 *
 * COLUMN VALIDATION: name and type validation: DONE
 * NUMBER OF RECORDS: DONE
 * DUPLICATE VALUE: DONE
 * SAMPLE DATA
 * PARAMETERIZATION
 * */