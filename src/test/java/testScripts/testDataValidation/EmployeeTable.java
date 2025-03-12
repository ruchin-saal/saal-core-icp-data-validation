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
//        PostgreSQLConnection postgreSQLConnection=new PostgreSQLConnection();
//        List<String> results = postgreSQLConnection.runDatabaseOperations();
//        for (String row : results) {
//            System.out.println(row);
//        }

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
}






/*
 *
 * COLUMN VALIDATION: name and type validation: DONE
 *
 * NUMBER OF RECORDS
 * METADATA
 * DUPLICATE VALUE
 * SAMPLE DATA
 * */