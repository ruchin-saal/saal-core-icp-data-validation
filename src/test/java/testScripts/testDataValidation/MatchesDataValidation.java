package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.matchesResponse.MatchesResponse;
import utilities.*;

import java.util.ArrayList;
import java.util.List;
@Tag("SanityTest")
public class MatchesDataValidation  extends BaseClass {
    private static final Logger logger = LogManager.getLogger(MatchesDataValidation.class);
    @Test
    @Title("Verify Match Table Sample Data")
    @Tag("Matches")
    public void matchTableSampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;
        ArrayList<String> api_matchid = new ArrayList<String>();
        ArrayList<String> api_label = new ArrayList<String>();
        ArrayList<String> api_date = new ArrayList<String>();
        ArrayList<String> api_dateutc = new ArrayList<String>();
        ArrayList<String> api_status = new ArrayList<String>();
        ArrayList<String> api_gameweek = new ArrayList<String>();
        ArrayList<String> api_roundid = new ArrayList<String>();
        ArrayList<String> api_competitionid = new ArrayList<String>();
        ArrayList<String> api_seasonid = new ArrayList<String>();
        TeamsDataValidation teamsDataValidation=new TeamsDataValidation();
        List<String> teamID=teamsDataValidation.getTeamID();
        int teamIDSize = GlbVar.teamIdSize.equals("all") ? teamID.size() : Integer.parseInt(GlbVar.teamIdSize);
        ArrayList<String> api_current_teamid=new ArrayList<>();
        logger.info("Fetching data for following teamID");
        for (int h = 0; h < teamIDSize; h++) {
            logger.info(teamID.get(h));
            api_current_teamid.add(teamID.get(h));
            String competition_endPoint = "/v3/teams/"+ teamID.get(h)+"/matches";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            MatchesResponse matchesResponse = response.as(MatchesResponse.class);
            for (int i = 0; i < matchesResponse.getMatches().size(); i++) {
                api_matchid.add(String.valueOf(matchesResponse.getMatches().get(i).getMatchId()));
                api_label.add(String.valueOf(matchesResponse.getMatches().get(i).getLabel()));
                api_date.add(String.valueOf(matchesResponse.getMatches().get(i).getDate()));
                api_dateutc.add(String.valueOf(matchesResponse.getMatches().get(i).getDateutc()));
                api_status.add(String.valueOf(matchesResponse.getMatches().get(i).getStatus()));
                api_gameweek.add(String.valueOf(matchesResponse.getMatches().get(i).getGameweek()));
                api_roundid.add(String.valueOf(matchesResponse.getMatches().get(i).getRoundId()));
                api_competitionid.add(String.valueOf(matchesResponse.getMatches().get(i).getCompetitionId()));
                api_seasonid.add(String.valueOf(matchesResponse.getMatches().get(i).getSeasonId()));
            }
        }

        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tableName = GlbVar.matches_tablename;
        String whereClauseColumnName="teamid";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tableName);
        logger.info("DB column names: " + columnNames);
        ArrayList<String> db_matchid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(0), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_label = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(1), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_date = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(2), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_dateutc = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(3), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_status = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(4), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_gameweek = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(5), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_roundid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(6), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_competitionid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(7), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_seasonid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(8), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_teamid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(9), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        logger.info("Executing assertion for the api_matchid column");
        AssertHelpers.softAssertTrue(api_matchid, db_matchid);
        logger.info("Executing assertion for the api_label column");
        AssertHelpers.softAssertTrue(api_label, db_label);
        logger.info("Executing assertion for the api_date column");
        db_date=GenericFun.changeDateFormat(db_date);
        AssertHelpers.softAssertTrue(GenericFun.removeDuplicateDataFromList(api_date), GenericFun.removeDuplicateDataFromList(db_date));
        logger.info("Executing assertion for the api_dateutc column");
        db_dateutc=GenericFun.changeDateFormat(db_dateutc);
        AssertHelpers.softAssertTrue(GenericFun.removeDuplicateDataFromList(api_dateutc), GenericFun.removeDuplicateDataFromList(db_dateutc));
        logger.info("Executing assertion for the api_status column");
        AssertHelpers.softAssertTrue(api_status, db_status);
        logger.info("Executing assertion for the api_gameweek column");
        AssertHelpers.softAssertTrue(api_gameweek, db_gameweek);
        logger.info("Executing assertion for the api_roundid column");
        AssertHelpers.softAssertTrue(api_roundid, db_roundid);
        logger.info("Executing assertion for the api_competitionid column");
        AssertHelpers.softAssertTrue(api_competitionid, db_competitionid);
        logger.info("Executing assertion for the api_seasonid column");
        AssertHelpers.softAssertTrue(api_seasonid, db_seasonid);
        logger.info("Executing assertion for the teamID column");
        AssertHelpers.softAssertTrue(api_current_teamid, GenericFun.removeDuplicateDataFromList(db_teamid));
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify Match Table Number Of Records Validation")
    @Tag("Matches")
    public void matchNumberOfRecordsValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;
        ArrayList<String> api_matchid = new ArrayList<String>();
//        ArrayList<String> api_label = new ArrayList<String>();
//        ArrayList<String> api_date = new ArrayList<String>();
//        ArrayList<String> api_dateutc = new ArrayList<String>();
//        ArrayList<String> api_status = new ArrayList<String>();
//        ArrayList<String> api_gameweek = new ArrayList<String>();
//        ArrayList<String> api_roundid = new ArrayList<String>();
//        ArrayList<String> api_competitionid = new ArrayList<String>();
//        ArrayList<String> api_seasonid = new ArrayList<String>();
        TeamsDataValidation teamsDataValidation=new TeamsDataValidation();
        List<String> teamID=teamsDataValidation.getTeamID();
        for (int h = 0; h < teamID.size(); h++) {
            String competition_endPoint = "/v3/teams/"+ teamID.get(h)+"/matches";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            MatchesResponse matchesResponse = response.as(MatchesResponse.class);
            for (int i = 0; i < matchesResponse.getMatches().size(); i++) {
                api_matchid.add(String.valueOf(matchesResponse.getMatches().get(i).getMatchId()));
//                api_matchid.add(String.valueOf(matchesResponse.getMatches().get(i).getMatchId())+" "+teamID.get(h));
//                api_label.add(String.valueOf(matchesResponse.getMatches().get(i).getLabel()));
//                api_date.add(String.valueOf(matchesResponse.getMatches().get(i).getDate()));
//                api_dateutc.add(String.valueOf(matchesResponse.getMatches().get(i).getDateutc()));
//                api_status.add(String.valueOf(matchesResponse.getMatches().get(i).getStatus()));
//                api_gameweek.add(String.valueOf(matchesResponse.getMatches().get(i).getGameweek()));
//                api_roundid.add(String.valueOf(matchesResponse.getMatches().get(i).getRoundId()));
//                api_competitionid.add(String.valueOf(matchesResponse.getMatches().get(i).getCompetitionId()));
//                api_seasonid.add(String.valueOf(matchesResponse.getMatches().get(i).getSeasonId()));
            }
        }
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tableName = GlbVar.matches_tablename;
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tableName);
        logger.info("DB column names: " + columnNames);
        ArrayList<String> db_matchid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(0));
//        ArrayList<String> db_matchid = tableMetadata.fetchTableDataFromMultipleColumns(catalogName, schemaName, tableName, columnNames.get(0),columnNames.get(9),null);
//        ArrayList<String> db_label = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(1));
//        ArrayList<String> db_date = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(2));
//        ArrayList<String> db_dateutc = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(3));
//        ArrayList<String> db_status = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(4));
//        ArrayList<String> db_gameweek = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(5));
//        ArrayList<String> db_roundid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(6));
//        ArrayList<String> db_competitionid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(7));
//        ArrayList<String> db_seasonid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(8));
//        ArrayList<String> db_teamid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tableName, columnNames.get(9));

        logger.info("Executing assertion for the api_matchid column");
        AssertHelpers.softAssertTrue(api_matchid, db_matchid);
//        logger.info("Executing assertion for the api_label column");
//        AssertHelpers.softAssertTrue(api_label, db_label);
//        logger.info("Executing assertion for the api_date column");
//        AssertHelpers.softAssertTrue(api_date, db_date);
//        logger.info("Executing assertion for the api_dateutc column");
//        AssertHelpers.softAssertTrue(api_dateutc, db_dateutc);
//        logger.info("Executing assertion for the api_status column");
//        AssertHelpers.softAssertTrue(api_status, db_status);
//        logger.info("Executing assertion for the api_gameweek column");
//        AssertHelpers.softAssertTrue(api_gameweek, db_gameweek);
//        logger.info("Executing assertion for the api_roundid column");
//        AssertHelpers.softAssertTrue(api_roundid, db_roundid);
//        logger.info("Executing assertion for the api_competitionid column");
//        AssertHelpers.softAssertTrue(api_competitionid, db_competitionid);
//        logger.info("Executing assertion for the api_seasonid column");
//        AssertHelpers.softAssertTrue(api_seasonid, db_seasonid);
//        logger.info("Executing assertion for the teamID column");
//        AssertHelpers.softAssertTrue(teamID, db_teamid);
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify Match Table Duplicate Record Validation")
    @Tag("Matches")
    public void matchTableDuplicateRecords() throws Exception{
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata=new TrinoTableMetaData();
        ArrayList<String> columnNames=tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.matches_tablename);
        logger.info("Fetching duplicate value for 'teams' table and column name is '"+columnNames.get(0)+"', "+columnNames.get(9) +" and "+columnNames.get(8));
        ArrayList<String> db_teamid =  tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.matches_tablename
                , columnNames.get(0), columnNames.get(9),columnNames.get(8));
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_teamid);
    }

    public List<String> fetchMatchIdOnTeamIDBasis(List<String> teamID) throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;
        ArrayList<String> api_matchid = new ArrayList<String>();
        int teamIDSize = GlbVar.teamIdSize.equals("all") ? teamID.size() : Integer.parseInt(GlbVar.teamIdSize);
        ArrayList<String> api_current_teamid=new ArrayList<>();
        logger.info("Fetching data for following teamID");
        for (int h = 0; h < teamID.size(); h++) {
            logger.info(teamID.get(h));
            String competition_endPoint = "/v3/teams/"+ teamID.get(h)+"/matches";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            MatchesResponse matchesResponse = response.as(MatchesResponse.class);
            for (int i = 0; i < matchesResponse.getMatches().size(); i++) {
                api_matchid.add(String.valueOf(matchesResponse.getMatches().get(i).getMatchId()));
            }
        }
        logger.info("All Match ID is: "+api_matchid);
        return api_matchid;
    }
}