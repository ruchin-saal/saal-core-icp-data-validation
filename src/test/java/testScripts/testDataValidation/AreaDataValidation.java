package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import responseModels.areaResponse.AreaResponse;
import utilities.*;

import java.util.ArrayList;

@ExtendWith(SerenityJUnit5Extension.class)
@Tag("SanityTest")
public class AreaDataValidation  extends BaseClass {
    private static final Logger logger = LogManager.getLogger(AreaDataValidation.class);
    @Test
    @Title("Verify sample data of Area Table")
    @Tag("AreaTable")
    public void areaTableSampleDataAndNumberOfRecordValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils=new RestUtils();
        Response response=restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, GlbVar.areaTable_endPoint);
        Assertions.assertEquals(200, response.getStatusCode(), "API '"+ GlbVar.api_baseUrl +GlbVar.areaTable_endPoint+"' is down");
        AreaResponse areaResponse = response.as(AreaResponse.class);
        ArrayList<String> apiIdValue = new ArrayList<String>();
        ArrayList<String> apiAlpha2codeValue = new ArrayList<String>();
        ArrayList<String> apiAlpha3codeValue = new ArrayList<String>();
        ArrayList<String> apiNameValue = new ArrayList<String>();
        for (int i = 0; i < areaResponse.getAreas().size(); i++) {
            apiIdValue.add(String.valueOf(areaResponse.getAreas().get(i).getId()));
            apiAlpha2codeValue.add(String.valueOf(areaResponse.getAreas().get(i).getAlpha2code())+"--"+String.valueOf(areaResponse.getAreas().get(i).getName()));
            apiAlpha3codeValue.add(String.valueOf(areaResponse.getAreas().get(i).getAlpha3code()));
            apiNameValue.add(String.valueOf(areaResponse.getAreas().get(i).getName()));
        }

        //Replacing some country code as per dev team request
        apiAlpha2codeValue=GenericFun.replaceTextInArrayListElement(apiAlpha2codeValue, "SC--Scotland","GB--Scotland");
        apiAlpha2codeValue=GenericFun.replaceTextInArrayListElement(apiAlpha2codeValue, "EN--England","GB--England");
        apiAlpha2codeValue=GenericFun.replaceTextInArrayListElement(apiAlpha2codeValue, "LS--East Timor","TP--East Timor");
        apiAlpha2codeValue=GenericFun.replaceTextInArrayListElement(apiAlpha2codeValue, "WA--Wales","GB--Wales");
        apiAlpha2codeValue=GenericFun.replaceTextInArrayListElement(apiAlpha2codeValue, "NI--Northern Ireland","GB--Northern Ireland");
        apiAlpha2codeValue=GenericFun.removeTextAfterAnyValue(apiAlpha2codeValue,"--");

        //DB DATA
        TrinoTableMetaData tableMetadata=new TrinoTableMetaData();
        ArrayList<String> columnNames=tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.area_tablename);
        logger.info("DB column names: "+columnNames);
        ArrayList<String> dbIdValue =  tableMetadata.fetchTableDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.area_tablename, columnNames.get(0));
        ArrayList<String> dbAlpha2codeValue = tableMetadata.fetchTableDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.area_tablename, columnNames.get(1));
        ArrayList<String> dbAlpha3codeValue = tableMetadata.fetchTableDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.area_tablename, columnNames.get(2));
        ArrayList<String> dbNameValue = tableMetadata.fetchTableDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.area_tablename, columnNames.get(3));

        logger.info("Executing for dbIdValue");
        AssertHelpers.softAssertTrue(apiIdValue, dbIdValue);
        logger.info("Executing for dbAlpha2codeValue");
        AssertHelpers.softAssertTrue(apiAlpha2codeValue, dbAlpha2codeValue);
        logger.info("Executing for dbAlpha3codeValue");
        AssertHelpers.softAssertTrue(apiAlpha3codeValue, dbAlpha3codeValue);
        logger.info("Executing for dbNameValue");
        AssertHelpers.softAssertTrue(apiNameValue, dbNameValue);
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify duplicate record of Area Table")
    @Tag("AreaTable")
    public void areaTableDuplicateRecords() throws Exception{
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata=new TrinoTableMetaData();
        ArrayList<String> columnNames=tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.area_tablename);
        logger.info("Fetching duplicate value for 'area' table and column name is '"+columnNames.get(0)+"'");
        ArrayList<String> dbIdValue =  tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.area_tablename, columnNames.get(0), null,null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(dbIdValue);
    }
}


