package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.teamsResponse.TeamsResponse;
import utilities.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Tag("SanityTest")
public class TeamsDataValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(TeamsDataValidation.class);
    @Test
    @Title("Verify Teams Table sample data")
    @Tag("Teams")
    public void teamsSampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        String[] competionArray = GlbVar.competitionIdList.split(",\\s*");
        int competionIDSize;
        if(GlbVar.competitionIdSize.equals("all")){
            competionIDSize=competionArray.length;
        }else {
            competionIDSize= Integer.parseInt(GlbVar.competitionIdSize);
        }
        Response response;
        ArrayList<String> api_areaid = new ArrayList<String>();
        ArrayList<String> api_category = new ArrayList<String>();
        ArrayList<String> api_city =  new ArrayList<String>();
        ArrayList<String> api_competitionid = new ArrayList<String>();
        ArrayList<String> api_gender = new ArrayList<String>();
        ArrayList<String> api_imagedataurl = new ArrayList<String>();
        ArrayList<String> api_name = new ArrayList<String>();
        ArrayList<String> api_officialname = new ArrayList<String>();
        ArrayList<String> api_teamid = new ArrayList<String>();
        ArrayList<String> api_type = new ArrayList<String>();
        List<String> final_competitionID = new ArrayList<String>();
        for (int h = 0; h < competionIDSize; h++) {
            logger.info(competionArray[h]);
            final_competitionID.add(competionArray[h]);
            String competition_endPoint = "/v3/competitions/"+ competionArray[h]+"/teams";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            TeamsResponse teamsResponse = response.as(TeamsResponse.class);
            for (int i = 0; i < teamsResponse.getTeams().size(); i++) {
                api_areaid.add(String.valueOf(teamsResponse.getTeams().get(i).getArea().getId()));
                api_category.add(String.valueOf(teamsResponse.getTeams().get(i).getCategory()));
                api_city.add(String.valueOf(teamsResponse.getTeams().get(i).getCity()));
                api_gender.add(String.valueOf(teamsResponse.getTeams().get(i).getGender()));
                api_imagedataurl.add(String.valueOf(teamsResponse.getTeams().get(i).getImageDataURL()));
                api_name.add(String.valueOf(teamsResponse.getTeams().get(i).getName()));
                api_officialname.add(String.valueOf(teamsResponse.getTeams().get(i).getOfficialName()));
                api_teamid.add(String.valueOf(teamsResponse.getTeams().get(i).getWyId()));
                api_type.add(String.valueOf(teamsResponse.getTeams().get(i).getType()));
            }
        }
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.teams_tablename;
        String whereClauseColumnName="competitionid";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        logger.info("DB column names: " + columnNames);

        ArrayList<String> db_teamid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(0), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_name = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(1), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_officialname = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(2), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_city =  tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(3), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_type = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(4), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_category = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(5), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_gender = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(6), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_imagedataurl = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(7), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_areaid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(8), whereClauseColumnName, String.valueOf(final_competitionID)));
        ArrayList<String> db_competitionid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(9), whereClauseColumnName, String.valueOf(final_competitionID)));
        logger.info("Executing for the db_areaid column");
        AssertHelpers.softAssertTrue(api_areaid, db_areaid);
        logger.info("Executing for the db_category column");
        AssertHelpers.softAssertTrue(api_category, db_category);
        logger.info("Executing for the db_city column");
        AssertHelpers.softAssertTrue(api_city, db_city);
        logger.info("Executing for the db_gender column");
        AssertHelpers.softAssertTrue(api_gender, db_gender);
        logger.info("Executing for the db_imagedataurl column");
        AssertHelpers.softAssertTrue(api_imagedataurl, db_imagedataurl);
        logger.info("Executing for the db_name column");
        AssertHelpers.softAssertTrue(api_name, db_name);
        logger.info("Executing for the db_officialname column");
        AssertHelpers.softAssertTrue(api_officialname, db_officialname);
        logger.info("Executing for the db_teamid column");
        AssertHelpers.softAssertTrue(api_teamid, db_teamid);
        logger.info("Executing for the db_type column");
        AssertHelpers.softAssertTrue(api_type, db_type);
        logger.info("Executing for the db_competitionid column");
        AssertHelpers.softAssertTrue(final_competitionID, GenericFun.removeDuplicateDataFromList(db_competitionid));
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify Teams Table Duplicate Records")
    @Tag("Teams")
    public void teamsTableDuplicateRecords() throws Exception{
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata=new TrinoTableMetaData();
        ArrayList<String> columnNames=tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.teams_tablename);
        logger.info("Fetching duplicate value for 'teams' table and column name is '"+columnNames.get(0)+"' and "+columnNames.get(9));
        ArrayList<String> db_teamid =  tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.teams_tablename
                , columnNames.get(0), columnNames.get(9),null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_teamid);
    }

    @Test
    @Title("Verify Teams Table number of records")
    @Tag("Teams")
    public void teamsNumberOfRecordsValidation() throws Exception {
        Config.setConfigs();
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.teams_tablename;
        String columnName="(teamid, competitionid) as TeamAndCompetitionID";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        System.out.println("DB column names: " + columnNames);
        String sqlQuery = "Select " + columnName + " from " + GlbVar.catalog_name + "."
                + GlbVar.schemas_name + "." + GlbVar.teams_tablename;
        ArrayList<String> db_teamid = tableMetadata.fetchTableDataByQuery(sqlQuery);

        //Fetching from API
        RestUtils restUtils = new RestUtils();
        String[] competionArray = GlbVar.competitionIdList.split(",\\s*");
        Response response;
        ArrayList<String> api_teamid = new ArrayList<String>();
        for (int h = 0; h < competionArray.length; h++) {
            String competition_endPoint = "/v3/competitions/"+ competionArray[h]+"/teams";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            TeamsResponse teamsResponse = response.as(TeamsResponse.class);
            for (int i = 0; i < teamsResponse.getTeams().size(); i++) {
                api_teamid.add("{"+String.valueOf(teamsResponse.getTeams().get(i).getWyId())+", "+competionArray[h]+"}");
            }
        }
        AssertHelpers.softAssertTrue(api_teamid, db_teamid);
        logger.info("Missing data in DB: "+GenericFun.missingDataInSecondList(api_teamid,db_teamid));
        AssertHelpers.softAssertAll();
    }

    /*To fetch Competition ID from other class*/
    public List<String> getTeamID() throws IOException {
        //Fetching from API
        Config.setConfigs();
        RestUtils restUtils = new RestUtils();
        String[] competionArray = GlbVar.competitionIdList.split(",\\s*");
        Response response;
        ArrayList<String> api_teamid = new ArrayList<String>();
        logger.info("Fetching all team ID");
        long startTime = System.currentTimeMillis();
        for (int h = 0; h < competionArray.length; h++) {
            String competition_endPoint = "/v3/competitions/"+ competionArray[h]+"/teams";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            if(response.statusCode()==200){
//            Assertions.assertEquals(200, response.getStatusCode());
            TeamsResponse teamsResponse = response.as(TeamsResponse.class);
            for (int i = 0; i < teamsResponse.getTeams().size(); i++) {
                api_teamid.add(String.valueOf(teamsResponse.getTeams().get(i).getWyId()));
            }
         }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(">>>>Total Time in Team ID collection: "+duration/1000);
        logger.info("All Team id is:" +api_teamid);
        return api_teamid;
    }

    /*To fetch Competition ID from other class*/
    public List<String> getTeamIDByStaticCompetitionID(String competitionIdList, int competitionIDSize) throws IOException {
        //Fetching from API
        Config.setConfigs();
        RestUtils restUtils = new RestUtils();
        String[] competionArray = competitionIdList.split(",\\s*");
        Response response;
        ArrayList<String> api_teamid = new ArrayList<String>();
        long startTime = System.currentTimeMillis();
//        logger.info("Fetching Team ID on the basis of competition following competition id");
        for (int h = 0; h < competitionIDSize; h++) {
            String competition_endPoint = "/v3/competitions/"+ competionArray[h]+"/teams";
//            logger.info("For CompetitionId:"+competionArray[h]+" flowing are the teamsID");
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            TeamsResponse teamsResponse = response.as(TeamsResponse.class);
            for (int i = 0; i < teamsResponse.getTeams().size(); i++) {
                api_teamid.add(competionArray[h]+"<<competitionID__teamID>>"+String.valueOf(teamsResponse.getTeams().get(i).getWyId()));
//                logger.info(competionArray[h]+"<<competitionID__teamID>>"+String.valueOf(teamsResponse.getTeams().get(i).getWyId()));
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("All team ID on the basis of above competition id:" +api_teamid);
        System.out.println(">>>>Total Time in Team ID collection: "+duration/1000);
        return api_teamid;
    }
}
