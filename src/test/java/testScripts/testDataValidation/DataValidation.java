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
        OracleConnection oracleConnection = new OracleConnection();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        List<String> oracleResults = oracleConnection.fetchColumnNamesAndDataType(oracleTableName);
        ArrayList<String> updatedoracleListString = GenericFun.removeItemFromList((ArrayList<String>) oracleResults, Config.removeOracleColumnAndDataType);
        logger.info("Oracle Table Column Names and Datatype>>" + updatedoracleListString);
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNamesAndDataType(trinoTableName);
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, Config.removeTrinoColumnAndDataType);
        logger.info("TRINO Table Column Names and Datatype>>" + updatedTrinoListString);
        logger.info("Validation result for 'Table Column names and Datatype' for Oracle Table:: "+ oracleTableName+ " and Trino for Table:: "+trinoTableName);
        AssertHelpers.compareColumnNameAndDataTypeListElementWise(updatedoracleListString, updatedTrinoListString);
    }


    @ParameterizedTest
    @MethodSource("provideTableNames")
    public void validateNumberOfRecords(String trinoTableName, String oracleTableName) throws Exception {
        Config.setConfigs();
        OracleConnection oracleConnection = new OracleConnection();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        int oracleResults = oracleConnection.fetchNumberOfRecords(oracleTableName);
        logger.info("ORACLE Number of Records >> " + oracleResults);
        int trinoResult = tableMetadata.fetchNumberOfRecords(trinoTableName);
        logger.info("TRINO Number of Records >> " + trinoResult);
        logger.info("Validation result for 'Number Of Records' for Oracle Table:: "+ oracleTableName+ " and Trino for Table:: "+trinoTableName);
        AssertHelpers.validateNumberOfRecords(oracleResults, trinoResult);
        AssertHelpers.softAssertAll();
    }

    @ParameterizedTest
    @MethodSource("provideTableNames")
    public void duplicateRecordInTable(String trinoTableName, String oracleTableName) throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        OracleConnection oracleConnection = new OracleConnection();
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNames(trinoTableName);
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, Config.removeTrinoColumn);
        String columnNames = null;
        columnNames = String.valueOf(updatedTrinoListString);
        columnNames = GenericFun.removeTextFromString(columnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        ArrayList<String> trinoDuplicateRecord = tableMetadata.duplicateRecords(columnNames, trinoTableName);
        //        logger.info("TRINO DUPLICATE RECORD>>"+trinoDuplicateRecord);
        List<String> oracleResults = oracleConnection.fetchColumnNames(oracleTableName);
        ArrayList<String> updatedoracleListString = GenericFun.removeItemFromList((ArrayList<String>) oracleResults, Config.removeOracleColumn);
//        logger.info("POSTGRESS>>"+updatedoracleListString);
        String oracleColumnNames = String.valueOf(updatedoracleListString);
        columnNames = null;
        columnNames = GenericFun.removeTextFromString(oracleColumnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        columnNames=GenericFun.updateListItems(columnNames, "\"\"");
        ArrayList<String> oracleDuplicateRecordList = oracleConnection.fetchDuplicateRecords(columnNames, oracleTableName);
//        logger.info("oracle DUPLICATE RECORD>>"+oracleDuplicateRecordList);
        logger.info("DUPLICATE RECORD CHECK IN TRINO TABLE:: "+trinoTableName);
        AssertHelpers.assertTrueSoft(trinoDuplicateRecord.size());
        logger.info("DUPLICATE RECORD CHECK IN ORACLE TABLE:: "+oracleTableName);
        AssertHelpers.assertTrueSoft(oracleDuplicateRecordList.size());
        AssertHelpers.softAssertAll();
    }

    @ParameterizedTest
    @MethodSource("provideTableNames")
    public void sampleData(String trinoTableName, String oracleTableName) throws Exception {
        Config.setConfigs();
        String columnNames = null;
        OracleConnection oracleConnection = new OracleConnection();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        List<String> oracleResults = oracleConnection.fetchColumnNames(oracleTableName);
        ArrayList<String> updatedoracleListString = GenericFun.removeItemFromList((ArrayList<String>) oracleResults, Config.removeOracleColumn);
        String oracleColumnNames = String.valueOf(updatedoracleListString);
        columnNames = null;
        columnNames = GenericFun.removeTextFromString(oracleColumnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        String columnNames_1=GenericFun.updateListItems(columnNames, "\"\"");
        ArrayList<String> oracleRecordWithColumnName = (ArrayList<String>) oracleConnection.fetchRecordWithColumnName(columnNames_1, oracleTableName, Config.limit);
        ArrayList<Object> oracleSampleData = new ArrayList<Object>();
        for (int i = 0; i < oracleRecordWithColumnName.size(); i++) {
            oracleSampleData.add(oracleConnection.fetchSampleRecord(columnNames_1, oracleTableName, oracleRecordWithColumnName.get(i)));
        }
//        logger.info("oracle SAMPLE RECORDS"+oracleSampleData);
        ArrayList<String> trinoResult = tableMetadata.fetchColumnNames(trinoTableName);
        ArrayList<String> updatedTrinoListString = GenericFun.removeItemFromList(trinoResult, Config.removeTrinoColumn);
        columnNames = String.valueOf(updatedTrinoListString);
        columnNames = GenericFun.removeTextFromString(columnNames, "[");
        columnNames = GenericFun.removeTextFromString(columnNames, "]");
        ArrayList<Object> trinoSampleData = new ArrayList<Object>();
        for (int i = 0; i < oracleRecordWithColumnName.size(); i++) {
            trinoSampleData.add(tableMetadata.fetchSampleRecords(columnNames, trinoTableName, oracleRecordWithColumnName.get(i)));
        }
//        logger.info("TRINO SAMPLE RECORDS"+trinoSampleData);
        logger.info("Validation result for 'Sample Data' for Oracle Table:: "+ oracleTableName+ " and Trino for Table:: "+trinoTableName);
        AssertHelpers.assertTrueCompareListElementWiseSoftAssertion(oracleSampleData, trinoSampleData);
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