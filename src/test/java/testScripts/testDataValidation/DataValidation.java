package testScripts.testDataValidation;

import baseConfig.BaseClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utilities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DataValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(DataValidation.class);


    public static Stream<Arguments> provideTableNames() throws IOException {
        Config.setConfigs();
        String[] trinoTables = Config.trino_TableNames.split("\\s*,\\s*");
        String[] oracleTables = Config.oracle_TableNames.split("\\s*,\\s*");
        // Ensure both arrays have the same length
        if (trinoTables.length != oracleTables.length) {
            throw new IllegalArgumentException("Mismatch in table count between Trino and Oracle");
        }
        return Stream.iterate(0, i -> i + 1)
                .limit(trinoTables.length)
                .map(i -> Arguments.of(trinoTables[i], oracleTables[i]));
    }

    @ParameterizedTest
    @MethodSource("provideTableNames")
    public void validateColumnNamesAndDataType(String trinoTableName, String oracleTableName) throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNamesAndDataType(trinoTableName);
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, "ingested_at - timestamp(6)");
        logger.info("TRINO>>" + updatedTrinoListString);
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
        List<String> postgreSqlResults = postgreSQLConnection.fetchColumnNamesAndDataType(oracleTableName);
        ArrayList<String> updatedPostgreSqlListString = GenericFun.removeItemFromList(trinoResult, "id - integer");
        logger.info("POSTGRESS>>" + updatedPostgreSqlListString);
        logger.info("Validation Result for Column Names and DataType");
        AssertHelpers.compareColumnNameAndDataTypeListElementWise(updatedPostgreSqlListString, updatedTrinoListString);
    }


    @ParameterizedTest
    @MethodSource("provideTableNames")
    public void validateNumberOfRecords(String trinoTableName, String oracleTableName) throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        int trinoResult = tableMetadata.fetchNumberOfRecords(trinoTableName);
        logger.info("TRINO Number of Records >> " + trinoResult);
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
        int postgreSqlResults = postgreSQLConnection.fetchNumberOfRecords(oracleTableName);
        logger.info("POSTGRESQL Number of Records >> " + postgreSqlResults);
        logger.info("Validation result for number of records in PostgreSQL and Trino for table: " + oracleTableName);
        AssertHelpers.validateNumberOfRecords(postgreSqlResults, trinoResult);
        AssertHelpers.softAssertAll();
    }

    @ParameterizedTest
    @MethodSource("provideTableNames")
    public void duplicateRecordInTable(String trinoTableName, String oracleTableName) throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNames(trinoTableName);
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, "ingested_at");
        String columnNames = null;
        columnNames = String.valueOf(updatedTrinoListString);
        columnNames = GenericFun.removeTextFromString(columnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        ArrayList<String> trinoDuplicateRecord = tableMetadata.duplicateRecords(columnNames, trinoTableName);
        //        logger.info("TRINO DUPLICATE RECORD>>"+trinoDuplicateRecord);
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
        List<String> postgreSqlResults = postgreSQLConnection.fetchColumnNames(oracleTableName);
        ArrayList<String> updatedPostgreSqlListString = GenericFun.removeItemFromList((ArrayList<String>) postgreSqlResults, "id");
//        logger.info("POSTGRESS>>"+updatedPostgreSqlListString);
        String postgreSQLColumnNames = String.valueOf(updatedPostgreSqlListString);
        columnNames = null;
        columnNames = GenericFun.removeTextFromString(postgreSQLColumnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        ArrayList<String> postgreSqlDuplicateRecordList = postgreSQLConnection.fetchDuplicateRecords(columnNames, oracleTableName);
//        logger.info("POSTGRESQL DUPLICATE RECORD>>"+postgreSqlDuplicateRecordList);
        logger.info("DUPLICATE RECORD CHECK IN TRINO");
        AssertHelpers.assertTrueSoft(trinoDuplicateRecord.size());
        logger.info("DUPLICATE RECORD CHECK IN POSTGRESQL");
        AssertHelpers.assertTrueSoft(postgreSqlDuplicateRecordList.size());
        AssertHelpers.softAssertAll();
    }

    @ParameterizedTest
    @MethodSource("provideTableNames")
    public void sampleData(String trinoTableName, String oracleTableName) throws Exception {
        Config.setConfigs();
        String columnNames = null;
        PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
        List<String> postgreSqlResults = postgreSQLConnection.fetchColumnNames(oracleTableName);
        ArrayList<String> updatedPostgreSqlListString = GenericFun.removeItemFromList((ArrayList<String>) postgreSqlResults, "id");
        String postgreSQLColumnNames = String.valueOf(updatedPostgreSqlListString);
        columnNames = null;
        columnNames = GenericFun.removeTextFromString(postgreSQLColumnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        ArrayList<String> postgreSQLrecordWithColumnName = (ArrayList<String>) postgreSQLConnection.fetchRecordWithColumnName(columnNames, oracleTableName, "5");
        ArrayList<Object> postgreSQLSampleData = new ArrayList<Object>();
        for (int i = 0; i < postgreSQLrecordWithColumnName.size(); i++) {
            postgreSQLSampleData.add(postgreSQLConnection.fetchSampleRecord(columnNames, oracleTableName, postgreSQLrecordWithColumnName.get(i)));
        }
//        logger.info("POSTGRESQL SAMPLE RECORDS"+postgreSQLSampleData);
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNames(trinoTableName);
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, "ingested_at");
        columnNames = String.valueOf(updatedTrinoListString);
        columnNames = GenericFun.removeTextFromString(columnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        ArrayList<Object> trinoSampleData = new ArrayList<Object>();
        for (int i = 0; i < postgreSQLrecordWithColumnName.size(); i++) {
            trinoSampleData.add(tableMetadata.fetchSampleRecords(columnNames, trinoTableName, postgreSQLrecordWithColumnName.get(i)));
        }
//        logger.info("TRINO SAMPLE RECORDS"+trinoSampleData);
        logger.info("Comparing the Oracle Table:: " + oracleTableName + " with Trino Table:: " + trinoTableName);
        AssertHelpers.assertTrueCompareListElementWiseSoftAssertion(postgreSQLSampleData, trinoSampleData);

    }
}

/*
 *
 * COLUMN VALIDATION: name and type validation: DONE
 * NUMBER OF RECORDS: DONE
 * DUPLICATE VALUE: DONE
 * SAMPLE DATA: DONE
 * PARAMETERIZATION: DONE
 * */