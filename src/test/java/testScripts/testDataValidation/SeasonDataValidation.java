package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.seasonResponse.SeasonResponse;
import utilities.AssertHelpers;
import utilities.Config;
import utilities.RestUtils;
import utilities.TrinoTableMetaData;
import java.io.IOException;
import java.util.*;

@Tag("SanityTest")
public class SeasonDataValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(SeasonDataValidation.class);
    @Test
    @Title("Verify Season Table Sample Data And Number Of Record Validation")
    @Tag("Season")
    public void seasonTableSampleDataAndNumberOfRecordValidation() throws Exception {
        Config.setConfigs();
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.season_tablename;
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        System.out.println("DB column names: " + columnNames);
        ArrayList<String> db_seasonid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(0));
        ArrayList<String> db_name = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(1));
        ArrayList<String> db_startdate = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(2));
        ArrayList<String> db_enddate =  tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(3));
        ArrayList<String> db_active = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(4));
        ArrayList<String> db_competitionid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(5));

        //Fetching from API
        RestUtils restUtils = new RestUtils();
        String[] areaArray = GlbVar.competitionIdList.split(",\\s*");
        Response response;
        ArrayList<String> api_active = new ArrayList<String>();
        ArrayList<String> api_competitionid = new ArrayList<String>();
        ArrayList<String> api_enddate =  new ArrayList<String>();
        ArrayList<String> api_name = new ArrayList<String>();
        ArrayList<String> api_seasonid = new ArrayList<String>();
        ArrayList<String> api_startdate = new ArrayList<String>();
        long startTime = System.currentTimeMillis();
        for (int h = 0; h < areaArray.length; h++) {
            String competition_endPoint = "/v3/competitions/"+ areaArray[h]+"/seasons";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            SeasonResponse seasonResponse = response.as(SeasonResponse.class);
            for (int i = 0; i < seasonResponse.getSeasons().size(); i++) {
                api_active.add(String.valueOf(seasonResponse.getSeasons().get(i).getSeason().isActive()));
                api_competitionid.add(String.valueOf(seasonResponse.getSeasons().get(i).getSeason().getCompetitionId()));
                api_enddate.add(String.valueOf(seasonResponse.getSeasons().get(i).getSeason().getEndDate()));
                api_name.add(String.valueOf(seasonResponse.getSeasons().get(i).getSeason().getName()));
                api_seasonid.add(String.valueOf(seasonResponse.getSeasons().get(i).getSeasonId()));
                api_startdate.add(seasonResponse.getSeasons().get(i).getSeason().getStartDate());
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(">>>>Total Time in API data collection: "+duration/1000);
        AssertHelpers.softAssertTrue(api_active, db_active);
        AssertHelpers.softAssertTrue(api_competitionid, db_competitionid);
        AssertHelpers.softAssertTrue(api_enddate, db_enddate);
        AssertHelpers.softAssertTrue(api_name, db_name);
        AssertHelpers.softAssertTrue(api_seasonid, db_seasonid);
        AssertHelpers.softAssertTrue(api_startdate, db_startdate);
        AssertHelpers.softAssertAll();
    }


    @Test
    @Title("Verify Season Table Duplicate Records")
    @Tag("Season")
    public void seasonTableDuplicateRecords() throws Exception{
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata=new TrinoTableMetaData();
        ArrayList<String> columnNames=tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.season_tablename);
        logger.info("Fetching duplicate value for 'season' table and column name is '"+columnNames.get(0)+"'");
        ArrayList<String> db_seasonid =  tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.season_tablename, columnNames.get(0), null,null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_seasonid);
    }


    /*To fetch season id from other class*/
    public List<String> getSeasonID() throws IOException {
        //Fetching from API
        Config.setConfigs();
        RestUtils restUtils = new RestUtils();
        String[] areaArray = GlbVar.competitionIdList.split(",\\s*");
        Response response;
        List<String> api_seasonid = new ArrayList<String>();
        long startTime = System.currentTimeMillis();
        for (int h = 0; h < areaArray.length; h++) {
            String competition_endPoint = "/v3/competitions/"+ areaArray[h]+"/seasons";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            SeasonResponse seasonResponse = response.as(SeasonResponse.class);
            for (int i = 0; i < seasonResponse.getSeasons().size(); i++) {
                api_seasonid.add(String.valueOf(seasonResponse.getSeasons().get(i).getSeasonId()));
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Collections.sort(api_seasonid);
        System.out.println(">>>>Total Time in season ID collection: "+duration/1000);
        return api_seasonid;
    }

    /*To fetch season id from other class*/
    public List<String> getSeasonIDForParticularCompetitionsID(String competitionID, int competitionIDSize) throws IOException {
        //Fetching from API
        Config.setConfigs();
        RestUtils restUtils = new RestUtils();
        String[] competitionArray = competitionID.split(",\\s*");
        Response response;
        List<String> api_seasonid = new ArrayList<String>();
        long startTime = System.currentTimeMillis();
//        logger.info("Following are the seasonID on the basis of competitionID");
        for (int h = 0; h < competitionIDSize; h++) {
            String competition_endPoint = "/v3/competitions/"+ competitionArray[h]+"/seasons";
//            logger.info("For CompetitionId:"+competitionArray[h]+" flowing are the seasonID");
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            SeasonResponse seasonResponse = response.as(SeasonResponse.class);
            for (int i = 0; i < seasonResponse.getSeasons().size(); i++) {
                api_seasonid.add(competitionArray[h]+"<<competitionID__seasonID>>"+String.valueOf(seasonResponse.getSeasons().get(i).getSeasonId()));
//                logger.info(competitionArray[h]+"<<competitionID__seasonID>>"+String.valueOf(seasonResponse.getSeasons().get(i).getSeasonId()));
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        Collections.sort(api_seasonid);
        System.out.println(">>>>Total Time in season ID collection: "+duration/1000);
        return api_seasonid;
    }

//    public Map<String, List<String>> getSeasonIDForParticularCompetitionsID(String competitionID, int competitionIDSize) throws IOException {
//        // Fetching from API
//        Config.setConfigs();
//        RestUtils restUtils = new RestUtils();
//        String[] competitionArray = competitionID.split(",\\s*");
//        Response response;
//        Map<String, List<String>> apiSeasonMap = new HashMap<>();
//        long startTime = System.currentTimeMillis();
//        logger.info("Following are the seasonIDs on the basis of competitionID");
//
//        for (int h = 0; h < competitionIDSize; h++) {
//            String competition_endPoint = "/v3/competitions/" + competitionArray[h] + "/seasons";
//            logger.info("For CompetitionId:"+competitionArray[h]+" following are the seasonID");
//            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
//            Assertions.assertEquals(200, response.getStatusCode());
//            SeasonResponse seasonResponse = response.as(SeasonResponse.class);
//            String key = competitionArray[h];
//
//            // Check if the key already exists in the map, if not initialize a new list
//            List<String> seasonIds = apiSeasonMap.getOrDefault(key, new ArrayList<>());
//            logger.info("For CompetitionId:"+competitionArray[h]+" flowing are the teamsID");
//            for (int i = 0; i < seasonResponse.getSeasons().size(); i++) {
//                // Fetching seasonID as value
//                String value = String.valueOf(seasonResponse.getSeasons().get(i).getSeasonId());
//                logger.info(seasonResponse.getSeasons().get(i).getSeasonId());
//                // Add value to the list
//                seasonIds.add(value);
//            }
//
//            // Put the list of season IDs back into the map
//            apiSeasonMap.put(key, seasonIds);
//        }
//        System.out.println(apiSeasonMap);
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - startTime;
//
//        System.out.println(">>>>Total Time in season ID collection: " + duration / 1000);
//        logger.info("Key is competitionID and Value is a list of seasonIDs");
//        return apiSeasonMap;
//    }
}
