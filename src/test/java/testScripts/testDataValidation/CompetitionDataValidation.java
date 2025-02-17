package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.competitionResponse.CompetitionResponse;
import utilities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Tag("SanityTest")
public class CompetitionDataValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(CompetitionDataValidation.class);
    @Test
    @Title("Verify Competition table Sample Data")
    @Tag("Competition")
    public void competitionTableSampleDataAndNumberOfRecordValidation() throws Exception {
        Config.setConfigs();
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.competition_tablename;
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        System.out.println("DB column names: " + columnNames);
        ArrayList<String> db_competitionid =  tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(0));
        ArrayList<String> db_name = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(1));
        ArrayList<String> db_format = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(2));
        ArrayList<String> db_type = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(3));
        ArrayList<String> db_category = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(4));
        ArrayList<String> db_gender = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(5));
        ArrayList<String> db_divisionlevel = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(6));
        ArrayList<String> db_areaid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(7));

        //Fetching from API
        RestUtils restUtils = new RestUtils();
        String areaList=GlbVar.areaList;
        String[] areaArray = areaList.split(",\\s*");
        Response response;
        ArrayList<String> api_areaid = new ArrayList<String>();
        ArrayList<String> api_category = new ArrayList<String>();
        ArrayList<String> api_competitionid =  new ArrayList<String>();
        ArrayList<String> api_divisionlevel = new ArrayList<String>();
        ArrayList<String> api_format = new ArrayList<String>();
        ArrayList<String> api_gender = new ArrayList<String>();
        ArrayList<String> api_name = new ArrayList<String>();
        ArrayList<String> api_type =new ArrayList<String>();
        for (int h = 0; h < areaArray.length; h++) {
            String competition_endPoint = "/v3/competitions?areaId=" + areaArray[h];
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            CompetitionResponse competitionResponse = response.as(CompetitionResponse.class);
            for (int i = 0; i < competitionResponse.getCompetitions().size(); i++) {
                api_areaid.add(String.valueOf(competitionResponse.getCompetitions().get(i).getArea().getId()));
                api_category.add(String.valueOf(competitionResponse.getCompetitions().get(i).getCategory()));
                api_competitionid.add(String.valueOf(competitionResponse.getCompetitions().get(i).getWyId()));
                api_divisionlevel.add(String.valueOf(competitionResponse.getCompetitions().get(i).getDivisionLevel()));
                api_format.add(String.valueOf(competitionResponse.getCompetitions().get(i).getFormat()));
                api_gender.add(String.valueOf(competitionResponse.getCompetitions().get(i).getGender()));
                api_name.add(String.valueOf(competitionResponse.getCompetitions().get(i).getName()));
                api_type.add(String.valueOf(competitionResponse.getCompetitions().get(i).getType()));
            }
        }

        AssertHelpers.softAssertTrue(api_areaid,db_areaid);
        AssertHelpers.softAssertTrue(api_category,db_category);
        AssertHelpers.softAssertTrue(api_competitionid,db_competitionid);
        AssertHelpers.softAssertTrue(api_divisionlevel,db_divisionlevel);
        AssertHelpers.softAssertTrue(api_format,db_format);
        AssertHelpers.softAssertTrue(api_gender,db_gender);
        AssertHelpers.softAssertTrue(api_name,db_name);
        AssertHelpers.softAssertTrue(api_type,db_type);
        AssertHelpers.softAssertAll();
        }

    @Test
    @Title("Verify competition Table Duplicate Records")
    @Tag("Competition")
    public void competitionTableDuplicateRecords() throws Exception{
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata=new TrinoTableMetaData();
        ArrayList<String> columnNames=tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.competition_tablename);
        logger.info("Fetching duplicate value for 'competition' table and column name is '"+columnNames.get(0)+"'");
        ArrayList<String> db_competitionid =  tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.competition_tablename, columnNames.get(0), null,null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_competitionid);
    }


    /*To fetch Competition ID from other class*/
    public List<String> getCompetitionID() throws IOException {
        //Fetching from API
        Config.setConfigs();
        RestUtils restUtils = new RestUtils();
        String[] areaArray = GlbVar.areaList.split(",\\s*");
        Response response;
        List<String> api_competitionid = new ArrayList<String>();
        long startTime = System.currentTimeMillis();
        logger.info("Fetching the competition id");
        for (int h = 0; h < areaArray.length; h++) {
            String competition_endPoint = "/v3/competitions?areaId=" + areaArray[h];
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            CompetitionResponse competitionResponse = response.as(CompetitionResponse.class);
            for (int i = 0; i < competitionResponse.getCompetitions().size(); i++) {
                api_competitionid.add(String.valueOf(competitionResponse.getCompetitions().get(i).getWyId()));
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(">>>>Total Time in competition id collection: "+duration/1000);
        logger.info("ALL COMPETITION ID IS: "+api_competitionid);
        return api_competitionid;
    }
}


