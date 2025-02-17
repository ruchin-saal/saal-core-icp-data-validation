package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.playerAdvanceStatsResponse.PlayerAdavanceStatsResponse;
import utilities.*;
import java.util.ArrayList;
import java.util.List;
@Tag("SanityTest")
public class MatchAdvanceStatsDataValidation extends BaseClass {


    private static final Logger logger = LogManager.getLogger(MatchAdvanceStatsDataValidation.class);

    @Test
    @Title("Verify MatchAdvanceStats Table Duplicate Records")
    @Tag("MatchAdvanceStats")
    public void matchAdvanceStatsTableDuplicateRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.match_advancestats_tablename);
        logger.info("Fetching duplicate value for 'match advance' table and column name is '" + columnNames.get(0) + "' and " + columnNames.get(1));
        ArrayList<String> db_teamid = tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.match_advancestats_tablename
                , columnNames.get(0), columnNames.get(1), null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_teamid);
        AssertHelpers.softAssertAll();
    }

    public void matchsAdvanceStatsTableSampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;

        logger.info("Predefined Competition IDs: " + GlbVar.staticAdvanceCompetitionIDList);
        String[] configTeamAdvanceCompetitionCompetitionId = GlbVar.staticAdvanceCompetitionIDList.split(",\\s*");

        /*Fetching team id on the basis of static competition id*/
        int competitionIDSize = GlbVar.competitionIdSize.equals("all") ? configTeamAdvanceCompetitionCompetitionId.length :
                Integer.parseInt(GlbVar.teamAdvanceCompetitionIDSize);
        TeamsDataValidation teamsDataValidation = new TeamsDataValidation();
        List<String> teamID = teamsDataValidation.getTeamIDByStaticCompetitionID(GlbVar.staticAdvanceCompetitionIDList, competitionIDSize);
        /*Fetching match id on the basis of team id which fetched from static competion id*/
        MatchesDataValidation matchesDataValidation=new MatchesDataValidation();
        matchesDataValidation.fetchMatchIdOnTeamIDBasis(teamID);

        SquadDataValidationCopy squadDataValidation = new SquadDataValidationCopy();
        List<String> playerID = squadDataValidation.getPlayerID(teamID, 1);
        logger.info("Fetching data for following competitionID: ");
        PlayerAdavanceStatsResponse playerAdavanceStatsResponse;
        for (int h = 0; h < GlbVar.teamAdvanceCompetitionIDSize.length(); h++) {
            logger.info("Competion ID is:" + configTeamAdvanceCompetitionCompetitionId[h]);
            for (int i = 0; i < playerID.size(); i++) {
                String competition_endPoint = "/v3/players/" + playerID.get(i) + "/advancedstats?compId=" + configTeamAdvanceCompetitionCompetitionId[h];
//                String competition_endPoint = "/v3/players/506638/advancedstats?compId=" + configTeamAdvanceCompetitionCompetitionId[h];
                logger.info("Player Advance Stats End Points: " + competition_endPoint);
                response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
                playerAdavanceStatsResponse = response.as(PlayerAdavanceStatsResponse.class);
                if (response.getStatusCode() == 200 && !(playerAdavanceStatsResponse.getPositions().size() < 1)) {
                    logger.info("Valid Player Advance Stats End Points: " + competition_endPoint);
                    for (int j = 0; j < playerAdavanceStatsResponse.getPositions().size(); j++) {
                        //POJO and API PENDING

                    }
                }
            }
        }
    }
}