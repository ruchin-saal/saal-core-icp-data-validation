package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.standingsResponse.StandingResponse;
import utilities.*;

import java.util.ArrayList;
import java.util.List;

@Tag("SanityTest")
public class StandingsDataValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(StandingsDataValidation.class);

    @Test
    @Title("Verify Standings Table sample data and number of records")
    @Tag("Standings")
    public void standingsSampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        SeasonDataValidation season = new SeasonDataValidation();
        List<String> seasonID = season.getSeasonID();
        logger.info("ALL SEASON ID IS: " + seasonID);
        Response response;
        List<String> api_competitionid = new ArrayList<String>();
        List<String> api_groupname = new ArrayList<String>();
        List<String> api_seasonid = new ArrayList<String>();
        List<String> api_teamid = new ArrayList<String>();
        List<String> api_totaldraws = new ArrayList<String>();
        List<String> api_totalgoalsagainst = new ArrayList<String>();
        List<String> api_totalgoalsfor = new ArrayList<String>();
        List<String> api_totallosses = new ArrayList<String>();
        List<String> api_totalplayed = new ArrayList<String>();
        List<String> api_totalpoints = new ArrayList<String>();
        List<String> api_totalwins = new ArrayList<String>();
        long startTime = System.currentTimeMillis();
        logger.info("Fetching data for following Season ID:");
        int seasonIDSize;
        if(GlbVar.seasonIDSize.equals("all")){
            seasonIDSize=seasonID.size();
        }else {
            seasonIDSize= Integer.parseInt(GlbVar.seasonIDSize);
        }
        List<String> final_seasonID = new ArrayList<String>();
        for (int h = 0; h < seasonIDSize; h++) {
            logger.info(seasonID.get(h));
            final_seasonID.add(seasonID.get(h));
            String standing_endPoint = "/v3/seasons/" + seasonID.get(h) + "/standings";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, standing_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            StandingResponse standingResponse = response.as(StandingResponse.class);
            if (standingResponse.getTeams() != null) {
                for (int i = 0; i < standingResponse.getTeams().size(); i++) {
                    api_competitionid.add(String.valueOf(standingResponse.getCompetitionId()));
                    api_groupname.add(String.valueOf(standingResponse.getTeams().get(i).getGroupName()));
                    api_seasonid.add(String.valueOf(standingResponse.getSeasonId()));
                    api_teamid.add(String.valueOf(standingResponse.getTeams().get(i).getTeamId()));
                    api_totaldraws.add(String.valueOf(standingResponse.getTeams().get(i).getTotalDraws()));
                    api_totalgoalsagainst.add(String.valueOf(standingResponse.getTeams().get(i).getTotalGoalsAgainst()));
                    api_totalgoalsfor.add(String.valueOf(standingResponse.getTeams().get(i).getTotalGoalsFor()));
                    api_totallosses.add(String.valueOf(standingResponse.getTeams().get(i).getTotalLosses()));
                    api_totalplayed.add(String.valueOf(standingResponse.getTeams().get(i).getTotalPlayed()));
                    api_totalpoints.add(String.valueOf(standingResponse.getTeams().get(i).getTotalPoints()));
                    api_totalwins.add(String.valueOf(standingResponse.getTeams().get(i).getTotalWins()));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(">>>>Total Time in groupName collection: " + duration / 1000);

        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.standings_tablename;
        String whereClauseColumnName="seasonid";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        logger.info("DB column names: " + columnNames);
        ArrayList<String> db_competitionid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(0), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_seasonid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(1), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_teamid =  tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(2), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_totalpoints = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(3), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_totalplayed = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(4), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_totalwins = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(5), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_totaldraws = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(6), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_totallosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(7), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_totalgoalsfor = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(8), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_totalgoalsagainst = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(9), whereClauseColumnName, String.valueOf(final_seasonID)));
        ArrayList<String> db_groupname = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(10), whereClauseColumnName, String.valueOf(final_seasonID)));
        logger.info("Executing for the db_competitionid column");
        AssertHelpers.softAssertTrue(api_competitionid, db_competitionid);
        logger.info("Executing for the db_groupname column");
        AssertHelpers.softAssertTrue(api_groupname, db_groupname);
        logger.info("Executing for the db_seasonid column");
        AssertHelpers.softAssertTrue(api_seasonid, db_seasonid);
        logger.info("Executing for the db_teamid column");
        AssertHelpers.softAssertTrue(api_teamid, db_teamid);
        logger.info("Executing for the db_totaldraws column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_totaldraws), db_totaldraws);
        logger.info("Executing for the db_totalgoalsagainst column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_totalgoalsagainst), db_totalgoalsagainst);
        logger.info("Executing for the db_totalgoalsfor column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_totalgoalsfor), db_totalgoalsfor);
        logger.info("Executing for the db_totallosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_totallosses), db_totallosses);
        logger.info("Executing for the db_totalplayed column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_totalplayed), db_totalplayed);
        logger.info("Executing for the db_totalpoints column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_totalpoints), db_totalpoints);
        logger.info("Executing for the db_totalwins column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_totalwins), db_totalwins);
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify Standings Table Number Of Records")
    @Tag("Standings")
    public void standingsNumberOfRecordsValidation() throws Exception {
        Config.setConfigs();
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.standings_tablename;
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        System.out.println("DB column names: " + columnNames);
        ArrayList<String> db_competitionid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(0));
//        ArrayList<String> db_groupname = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(1));
//        ArrayList<String> db_seasonid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(2));
//        ArrayList<String> db_teamid =  tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(3));
//        ArrayList<String> db_totaldraws = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(4));
//        ArrayList<String> db_totalgoalsagainst = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(5));
//        ArrayList<String> db_totalgoalsfor = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(6));
//        ArrayList<String> db_totallosses = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(7));
//        ArrayList<String> db_totalplayed = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(8));
//        ArrayList<String> db_totalpoints = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(9));
//        ArrayList<String> db_totalwins = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(10));

        // *******************
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        SeasonDataValidation season = new SeasonDataValidation();
        List<String> seasonID = season.getSeasonID();
        logger.info("ALL SEASON ID IS: " + seasonID);
        Response response;
        List<String> api_competitionid = new ArrayList<String>();
        List<String> api_groupname = new ArrayList<String>();
        List<String> api_seasonid = new ArrayList<String>();
        List<String> api_teamid = new ArrayList<String>();
        List<String> api_totaldraws = new ArrayList<String>();
        List<String> api_totalgoalsagainst = new ArrayList<String>();
        List<String> api_totalgoalsfor = new ArrayList<String>();
        List<String> api_totallosses = new ArrayList<String>();
        List<String> api_totalplayed = new ArrayList<String>();
        List<String> api_totalpoints = new ArrayList<String>();
        List<String> api_totalwins = new ArrayList<String>();
        long startTime = System.currentTimeMillis();
        for (int h = 0; h < seasonID.size(); h++) {
            String standing_endPoint = "/v3/seasons/" + seasonID.get(h) + "/standings";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, standing_endPoint);
//            Assertions.assertEquals(200, response.getStatusCode());
            StandingResponse standingResponse = response.as(StandingResponse.class);
            if (standingResponse.getTeams() != null) {
                for (int i = 0; i < standingResponse.getTeams().size(); i++) {
                    api_competitionid.add(String.valueOf(standingResponse.getCompetitionId()));
//                    api_groupname.add(String.valueOf(standingResponse.getTeams().get(i).getGroupName()));
//                    api_seasonid.add(String.valueOf(standingResponse.getSeasonId()));
//                    api_teamid.add(String.valueOf(standingResponse.getTeams().get(i).getTeamId()));
//                    api_totaldraws.add(String.valueOf(standingResponse.getTeams().get(i).getTotalDraws()));
//                    api_totalgoalsagainst.add(String.valueOf(standingResponse.getTeams().get(i).getTotalGoalsAgainst()));
//                    api_totalgoalsfor.add(String.valueOf(standingResponse.getTeams().get(i).getTotalGoalsFor()));
//                    api_totallosses.add(String.valueOf(standingResponse.getTeams().get(i).getTotalLosses()));
//                    api_totalplayed.add(String.valueOf(standingResponse.getTeams().get(i).getTotalPlayed()));
//                    api_totalpoints.add(String.valueOf(standingResponse.getTeams().get(i).getTotalPoints()));
//                    api_totalwins.add(String.valueOf(standingResponse.getTeams().get(i).getTotalWins()));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        AssertHelpers.softAssertTrue(api_competitionid, db_competitionid);
//        AssertHelpers.softAssertTrue(api_groupname, db_groupname);
//        AssertHelpers.softAssertTrue(api_seasonid, db_seasonid);
//        AssertHelpers.softAssertTrue(api_teamid, db_teamid);
//        AssertHelpers.softAssertTrue(api_totaldraws, db_totaldraws);
//        AssertHelpers.softAssertTrue(api_totalgoalsagainst, db_totalgoalsagainst);
//        AssertHelpers.softAssertTrue(api_totalgoalsfor, db_totalgoalsfor);
//        AssertHelpers.softAssertTrue(api_totallosses, db_totallosses);
//        AssertHelpers.softAssertTrue(api_totalplayed, db_totalplayed);
//        AssertHelpers.softAssertTrue(api_totalpoints, db_totalpoints);
//        AssertHelpers.softAssertTrue(api_totalwins, db_totalwins);
//        AssertHelpers.softAssertAll();
    }
}
