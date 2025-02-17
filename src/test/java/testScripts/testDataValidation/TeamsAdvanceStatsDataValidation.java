package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.teamAdvancestatsResponse.TeamAdvanceStatsResponse;
import utilities.*;

import java.util.ArrayList;
import java.util.List;

@Tag("SanityTest")
public class TeamsAdvanceStatsDataValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(TeamsAdvanceStatsDataValidation.class);

    @Test
    @Title("Verify TeamsAdvanceStats Table sample data")
    @Tag("TeamsAdvanceStats")
    public void teamsAdvanceStatsTableSampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API

        //DB to fetch two latest season ID
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tableName = GlbVar.team_advancestats_tablename;
        String whereClauseColumnName = "competitionid";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tableName);

        RestUtils restUtils = new RestUtils();
        Response response;

        logger.info("Predefined Competition IDs: " + GlbVar.staticAdvanceCompetitionIDList);
        String[] configTeamAdvanceCompetitionCompetitionId = GlbVar.staticAdvanceCompetitionIDList.split(",\\s*");
        int competitionIDSize = GlbVar.competitionIdSize.equals("all") ? configTeamAdvanceCompetitionCompetitionId.length :
                Integer.parseInt(GlbVar.teamAdvanceCompetitionIDSize);
        /*Fetching list of TeamID on the basis of competitionID*/
        TeamsDataValidation teamsDataValidation = new TeamsDataValidation();
        List<String> teamID = teamsDataValidation.getTeamIDByStaticCompetitionID(GlbVar.staticAdvanceCompetitionIDList, competitionIDSize);
        /*Fetching list of SeasonID on the basis of competitionID*/
        SeasonDataValidation seasonDataValidation = new SeasonDataValidation();
        List<String> seasonID = seasonDataValidation.getSeasonIDForParticularCompetitionsID(GlbVar.staticAdvanceCompetitionIDList, competitionIDSize);


        logger.info("Fetching data for following competitionID: ");
        TeamAdvanceStatsResponse teamAdvanceStatsResponse;
        String competition_endPoint;


        String competitionList = "";

        String[] competitionArray = GlbVar.staticAdvanceCompetitionIDList.split(",\\s*");
        for (int j = 0; j < competitionIDSize; j++) {
            competitionList += competitionArray[j]; // Append the competition ID
            if (j < competitionIDSize - 1) {
                competitionList += ","; // Add a comma after each ID except the last one
            }
        }

        String column1 = "seasonid";
        String column2 = "competitionid";
        String column3 = "enddate";
        String latestTwoSeason = "Select " + column1 + ", " + column2 + " from "
                + GlbVar.catalog_name + "." + GlbVar.schemas_name + "." + GlbVar.season_tablename
                + " where " + column2 + " in (" + competitionList + ") order by " + column3 + " DESC limit "
                + (competitionIDSize * 2);
        logger.info("SQL QUERY of team table: " + latestTwoSeason);

        ArrayList<String> seasonAndCompetition = tableMetadata.fetchTwoColumnsTableDataByQueryFrom(latestTwoSeason);
        logger.info(seasonAndCompetition);

        /*Outer loop is to fetch list of competition from properties file*/
        for (int k = 0; k < competitionIDSize; k++) {
            competitionArray = GlbVar.staticAdvanceCompetitionIDList.split(",\\s*");
            /*Second Outer loop is to fetch list of teamID from properties file*/
            for (int h = 0; h < teamID.size(); h++) {
                /*Inner loop is to fetch list of seasonID*/
                for (int i = 0; i < seasonID.size(); i++) {
                    if (seasonAndCompetition.contains(GenericFun.fetchTextAfterParticularText(seasonID.get(i), ">>")) & teamID.get(h).contains(competitionArray[k]) && seasonID.get(i).contains(competitionArray[k])) {
                        competition_endPoint = "/v3/teams/" + GenericFun.fetchTextAfterParticularText(teamID.get(h), ">>") +
                                "/advancedstats?compId=" + configTeamAdvanceCompetitionCompetitionId[h] +
                                "&seasonId=" + GenericFun.fetchTextAfterParticularText(seasonID.get(i), ">>");
                        logger.info("TeamID: " + seasonAndCompetition.get(i)+ " SeasonID: "
                                + GenericFun.fetchTextAfterParticularText(seasonID.get(i), ">>") + " CompetitionID: " + configTeamAdvanceCompetitionCompetitionId[h]);
                        response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
                        if (response.getStatusCode() == 200) {
                            teamAdvanceStatsResponse = response.as(TeamAdvanceStatsResponse.class);
                            if (response.getStatusCode() == 200 && !(teamAdvanceStatsResponse.getTotal().getGoals() < 1)) {
                                logger.info("Team Advance End Points: " + competition_endPoint);
                                api_teamid.add(String.valueOf(teamAdvanceStatsResponse.getTeamId()));
                                api_competitionid.add(String.valueOf(teamAdvanceStatsResponse.getCompetitionId()));
                                api_seasonid.add(String.valueOf(teamAdvanceStatsResponse.getSeasonId()));
                                api_total_matches.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getMatches()));
                                api_total_goals.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGoals()));
                                api_total_assists.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getAssists()));
                                api_total_shots.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getShots()));
                                api_total_headshots.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getHeadShots()));
                                api_total_yellowcards.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getYellowCards()));
                                api_total_redcards.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getRedCards()));
                                api_total_directredcards.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDirectRedCards()));
                                api_total_penalties.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getPenalties()));
                                api_total_linkupplays.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getLinkupPlays()));
                                api_total_cleansheets.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getCleanSheets()));
                                api_total_duels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDuels()));
                                api_total_duelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDuelsWon()));
                                api_total_defensiveduels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDefensiveDuels()));
                                api_total_defensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDefensiveDuelsWon()));
                                api_total_offensiveduels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getOffensiveDuels()));
                                api_total_offensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getOffensiveDuelsWon()));
                                api_total_aerialduels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getAerialDuels()));
                                api_total_aerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getAerialDuelsWon()));
                                api_total_fouls.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getFouls()));
                                api_total_offsides.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getOffsides()));
                                api_total_passes.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getPasses()));
                                api_total_successfulpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulPasses()));
                                api_total_smartpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSmartPasses()));
                                api_total_successfulsmartpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulSmartPasses()));
                                api_total_passestofinalthird.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getPassesToFinalThird()));
                                api_total_successfulpassestofinalthird.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulPassesToFinalThird()));
                                api_total_crosses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getCrosses()));
                                api_total_successfulcrosses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulCrosses()));
                                api_total_forwardpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getForwardPasses()));
                                api_total_successfulforwardpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulForwardPasses()));
                                api_total_backpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getBackPasses()));
                                api_total_successfulbackpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulBackPasses()));
                                api_total_throughpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getThroughPasses()));
                                api_total_successfulthroughpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulThroughPasses()));
                                api_total_keypasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getKeyPasses()));
                                api_total_successfulkeypasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulKeyPasses()));
                                api_total_verticalpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getVerticalPasses()));
                                api_total_successfulverticalpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulVerticalPasses()));
                                api_total_longpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getLongPasses()));
                                api_total_successfullongpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulLongPasses()));
                                api_total_dribbles.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDribbles()));
                                api_total_successfuldribbles.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulDribbles()));
                                api_total_interceptions.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getInterceptions()));
                                api_total_defensiveactions.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDefensiveActions()));
                                api_total_successfuldefensiveactions.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulDefensiveActions()));
                                api_total_attackingactions.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getAttackingActions()));
                                api_total_successfulattackingactions.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulAttackingActions()));
                                api_total_freekicks.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getFreeKicks()));
                                api_total_freekicksontarget.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getFreeKicksOnTarget()));
                                api_total_directfreekicks.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDirectFreeKicks()));
                                api_total_directfreekicksontarget.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDirectFreeKicksOnTarget()));
                                api_total_corners.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getCorners()));
                                api_total_successfulpenalties.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulPenalties()));
                                api_total_successfullinkupplays.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulLinkupPlays()));
                                api_total_accelerations.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getAccelerations()));
                                api_total_pressingduels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getPressingDuels()));
                                api_total_pressingduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getPressingDuelsWon()));
                                api_total_looseballduels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getLooseBallDuels()));
                                api_total_looseballduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getLooseBallDuelsWon()));
                                api_total_missedballs.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getMissedBalls()));
                                api_total_shotassists.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getShotAssists()));
                                api_total_shotontargetassists.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getShotOnTargetAssists()));
                                api_total_recoveries.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getRecoveries()));
                                api_total_opponenthalfrecoveries.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getOpponentHalfRecoveries()));
                                api_total_dangerousopponenthalfrecoveries.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDangerousOpponentHalfRecoveries()));
                                api_total_losses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getLosses()));
                                api_total_ownhalflosses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getOwnHalfLosses()));
                                api_total_dangerousownhalflosses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getDangerousOwnHalfLosses()));
                                api_total_fieldaerialduels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getFieldAerialDuels()));
                                api_total_fieldaerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getFieldAerialDuelsWon()));
                                api_total_gkexits.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGkExits()));
                                api_total_gksuccessfulexits.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGkSuccessfulExits()));
                                api_total_gkaerialduels.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGkAerialDuels()));
                                api_total_gkaerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGkAerialDuelsWon()));
                                api_total_gksaves.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGkSaves()));
                                api_total_xgshot.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getXgShot()));
                                api_total_xgshotagainst.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getXgShotAgainst()));
                                api_total_ppda.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getPpda()));
                                api_total_receivedpass.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getReceivedPass()));
                                api_total_touchinbox.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getTouchInBox()));
                                api_total_progressiverun.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getProgressiveRun()));
                                api_total_concededgoals.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getConcededGoals()));
                                api_total_opponentoffsides.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getOpponentOffsides()));
                                api_total_shotsagainst.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getShotsAgainst()));
                                api_total_gkgoalkicks.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGkGoalKicks()));
                                api_total_gkgoalkickssuccess.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getGkGoalKicksSuccess()));
                                api_total_shortgoalkicks.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getShortGoalKicks()));
                                api_total_longgoalkicks.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getLongGoalKicks()));
                                api_total_matchestagged.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getMatchesTagged()));
                                api_total_newduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getNewDuelsWon()));
                                api_total_newdefensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getNewDefensiveDuelsWon()));
                                api_total_newoffensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getNewOffensiveDuelsWon()));
                                api_total_newsuccessfuldribbles.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getNewSuccessfulDribbles()));
                                api_total_lateralpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getLateralPasses()));
                                api_total_successfullateralpasses.add(String.valueOf(teamAdvanceStatsResponse.getTotal().getSuccessfulLateralPasses()));
                                api_percent_duelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getDuelsWon()));
                                api_percent_defensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getDefensiveDuelsWon()));
                                api_percent_offensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getOffensiveDuelsWon()));
                                api_percent_aerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getAerialDuelsWon()));
                                api_percent_successfulpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulPasses()));
                                api_percent_successfulsmartpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulSmartPasses()));
                                api_percent_successfulpassestofinalthird.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulPassesToFinalThird()));
                                api_percent_successfulcrosses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulCrosses()));
                                api_percent_successfuldribbles.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulDribbles()));
                                api_percent_shotsontarget.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getShotsOnTarget()));
                                api_percent_headshotsontarget.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getHeadShotsOnTarget()));
                                api_percent_goalconversion.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getGoalConversion()));
                                api_percent_yellowcardsperfoul.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getYellowCardsPerFoul()));
                                api_percent_directfreekicksontarget.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getDirectFreeKicksOnTarget()));
                                api_percent_penaltiesconversion.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getPenaltiesConversion()));
                                api_percent_win.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getWin()));
                                api_percent_successfulforwardpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulForwardPasses()));
                                api_percent_successfulbackpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulBackPasses()));
                                api_percent_successfulthroughpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulThroughPasses()));
                                api_percent_successfulkeypasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulKeyPasses()));
                                api_percent_successfulverticalpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulVerticalPasses()));
                                api_percent_successfullongpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulLongPasses()));
                                api_percent_successfulshotassists.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulShotAssists()));
                                api_percent_successfullinkupplays.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulLinkupPlays()));
                                api_percent_fieldaerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getFieldAerialDuelsWon()));
                                api_percent_gksaves.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getGkSaves()));
                                api_percent_gksuccessfulexits.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getGkSuccessfulExits()));
                                api_percent_gkaerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getGkAerialDuelsWon()));
                                api_percent_successfultouchinbox.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulTouchInBox()));
                                api_percent_newduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getNewDuelsWon()));
                                api_percent_newdefensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getNewDefensiveDuelsWon()));
                                api_percent_newoffensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getNewOffensiveDuelsWon()));
                                api_percent_newsuccessfuldribbles.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getNewSuccessfulDribbles()));
                                api_percent_successfullateralpasses.add(String.valueOf(teamAdvanceStatsResponse.getPercent().getSuccessfulLateralPasses()));
                                api_average_possessionpercent.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getPossessionPercent()));
                                api_average_duels.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDuels()));
                                api_average_defensiveduels.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDefensiveDuels()));
                                api_average_offensiveduels.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getOffensiveDuels()));
                                api_average_aerialduels.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getAerialDuels()));
                                api_average_fouls.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getFouls()));
                                api_average_goals.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGoals()));
                                api_average_assists.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getAssists()));
                                api_average_passes.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getPasses()));
                                api_average_smartpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSmartPasses()));
                                api_average_passestofinalthird.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getPassesToFinalThird()));
                                api_average_crosses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getCrosses()));
                                api_average_dribbles.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDribbles()));
                                api_average_shots.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getShots()));
                                api_average_headshots.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getHeadShots()));
                                api_average_interceptions.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getInterceptions()));
                                api_average_successfuldefensiveaction.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulDefensiveAction()));
                                api_average_yellowcards.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getYellowCards()));
                                api_average_redcards.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getRedCards()));
                                api_average_directredcards.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDirectRedCards()));
                                api_average_successfulattackingactions.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulAttackingActions()));
                                api_average_freekicks.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getFreeKicks()));
                                api_average_directfreekicks.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDirectFreeKicks()));
                                api_average_corners.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getCorners()));
                                api_average_penalties.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getPenalties()));
                                api_average_passlength.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getPassLength()));
                                api_average_longpasslength.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getLongPassLength()));
                                api_average_dribbledistancefromopponentgoal.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDribbleDistanceFromOpponentGoal()));
                                api_average_accelerations.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getAccelerations()));
                                api_average_looseballduels.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getLooseBallDuels()));
                                api_average_missedballs.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getMissedBalls()));
                                api_average_forwardpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getForwardPasses()));
                                api_average_backpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getBackPasses()));
                                api_average_throughpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getThroughPasses()));
                                api_average_keypasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getKeyPasses()));
                                api_average_verticalpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getVerticalPasses()));
                                api_average_longpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getLongPasses()));
                                api_average_shotassists.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getShotAssists()));
                                api_average_shotontargetassists.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getShotOnTargetAssists()));
                                api_average_linkupplays.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getLinkupPlays()));
                                api_average_ballrecoveries.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getBallRecoveries()));
                                api_average_opponenthalfrecoveries.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getOpponentHalfRecoveries()));
                                api_average_dangerousopponenthalfrecoveries.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDangerousOpponentHalfRecoveries()));
                                api_average_balllosses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getBallLosses()));
                                api_average_ownhalflosses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getOwnHalfLosses()));
                                api_average_dangerousownhalflosses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDangerousOwnHalfLosses()));
                                api_average_fieldaerialduels.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getFieldAerialDuels()));
                                api_average_concededgoals.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getConcededGoals()));
                                api_average_gkexits.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGkExits()));
                                api_average_gkaerialduels.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGkAerialDuels()));
                                api_average_duelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDuelsWon()));
                                api_average_defensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDefensiveDuelsWon()));
                                api_average_offensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getOffensiveDuelsWon()));
                                api_average_successfulpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulPasses()));
                                api_average_successfulsmartpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulSmartPasses()));
                                api_average_successfulpassestofinalthird.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulPassesToFinalThird()));
                                api_average_successfulcrosses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulCrosses()));
                                api_average_successfulforwardpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulForwardPasses()));
                                api_average_successfulbackpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulBackPasses()));
                                api_average_successfulthroughpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulThroughPasses()));
                                api_average_successfulkeypasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulKeyPasses()));
                                api_average_successfulverticalpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulVerticalPasses()));
                                api_average_successfullongpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulLongPasses()));
                                api_average_successfuldribbles.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulDribbles()));
                                api_average_defensiveactions.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDefensiveActions()));
                                api_average_attackingactions.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getAttackingActions()));
                                api_average_freekicksontarget.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getFreeKicksOnTarget()));
                                api_average_directfreekicksontarget.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getDirectFreeKicksOnTarget()));
                                api_average_successfulpenalties.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulPenalties()));
                                api_average_successfullinkupplays.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulLinkupPlays()));
                                api_average_looseballduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getLooseBallDuelsWon()));
                                api_average_fieldaerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getFieldAerialDuelsWon()));
                                api_average_gksuccessfulexits.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGkSuccessfulExits()));
                                api_average_gkaerialduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGkAerialDuelsWon()));
                                api_average_gksaves.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGkSaves()));
                                api_average_xgshot.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getXgShot()));
                                api_average_xgshotagainst.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getXgShotAgainst()));
                                api_average_receivedpass.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getReceivedPass()));
                                api_average_touchinbox.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getTouchInBox()));
                                api_average_progressiverun.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getProgressiveRun()));
                                api_average_offsides.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getOffsides()));
                                api_average_opponentoffsides.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getOpponentOffsides()));
                                api_average_shotsagainst.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getShotsAgainst()));
                                api_average_gkgoalkicks.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGkGoalKicks()));
                                api_average_gkgoalkickssuccess.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getGkGoalKicksSuccess()));
                                api_average_shortgoalkicks.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getShortGoalKicks()));
                                api_average_longgoalkicks.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getLongGoalKicks()));
                                api_average_newduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getNewDuelsWon()));
                                api_average_newdefensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getNewDefensiveDuelsWon()));
                                api_average_newoffensiveduelswon.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getNewOffensiveDuelsWon()));
                                api_average_newsuccessfuldribbles.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getNewSuccessfulDribbles()));
                                api_average_lateralpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getLateralPasses()));
                                api_average_successfullateralpasses.add(String.valueOf(teamAdvanceStatsResponse.getAverage().getSuccessfulLateralPasses()));
                                api_roundid.add(String.valueOf(teamAdvanceStatsResponse.getRoundId()));
                            }
                        }
                    }
                }
            }
        }
//
//        //Issue
//        System.out.println("api_competitionid: " + api_competitionid);
//        System.out.println("api_teamid: " + api_teamid);

        //DB DATA
        logger.info("DB column names: " + columnNames);
        db_teamid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(0), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_competitionid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(1), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_seasonid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tableName, columnNames.get(2), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));

        db_total_matches = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(3), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_goals = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(4), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_assists = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(5), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_shots = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(6), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_headshots = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(7), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_yellowcards = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(8), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_redcards = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(9), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_directredcards = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(10), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_penalties = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(11), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_linkupplays = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(12), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_cleansheets = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(13), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_duels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(14), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_duelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(15), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_defensiveduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(16), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_defensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(17), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_offensiveduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(18), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_offensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(19), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_aerialduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(20), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_aerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(21), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_fouls = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(22), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_offsides = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(23), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_passes = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(24), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(25), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_smartpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(26), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulsmartpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(27), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_passestofinalthird = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(28), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulpassestofinalthird = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(29), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_crosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(30), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulcrosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(31), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_forwardpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(32), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulforwardpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(33), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_backpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(34), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulbackpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(35), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_throughpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(36), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulthroughpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(37), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_keypasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(38), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulkeypasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(39), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_verticalpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(40), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulverticalpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(41), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_longpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(42), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfullongpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(43), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_dribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(44), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfuldribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(45), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_interceptions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(46), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_defensiveactions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(47), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfuldefensiveactions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(48), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_attackingactions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(49), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulattackingactions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(50), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_freekicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(51), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_freekicksontarget = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(52), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_directfreekicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(53), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_directfreekicksontarget = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(54), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_corners = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(55), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfulpenalties = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(56), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfullinkupplays = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(57), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_accelerations = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(58), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_pressingduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(59), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_pressingduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(60), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_looseballduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(61), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_looseballduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(62), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_missedballs = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(63), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_shotassists = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(64), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_shotontargetassists = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(65), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_recoveries = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(66), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_opponenthalfrecoveries = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(67), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_dangerousopponenthalfrecoveries = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(68), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_losses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(69), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_ownhalflosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(70), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_dangerousownhalflosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(71), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_fieldaerialduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(72), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_fieldaerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(73), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_gkexits = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(74), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_gksuccessfulexits = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(75), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_gkaerialduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(76), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_gkaerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(77), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_gksaves = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(78), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_xgshot = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(79), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_xgshotagainst = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(80), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_ppda = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(81), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_receivedpass = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(82), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_touchinbox = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(83), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_progressiverun = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(84), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_concededgoals = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(85), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_opponentoffsides = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(86), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_shotsagainst = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(87), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_gkgoalkicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(88), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_gkgoalkickssuccess = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(89), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_shortgoalkicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(90), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_longgoalkicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(91), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_matchestagged = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(92), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_newduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(93), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_newdefensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(94), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_newoffensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(95), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_newsuccessfuldribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(96), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_lateralpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(97), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_total_successfullateralpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(98), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_duelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(99), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_defensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(100), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_offensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(101), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_aerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(102), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(103), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulsmartpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(104), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulpassestofinalthird = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(105), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulcrosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(106), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfuldribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(107), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_shotsontarget = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(108), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_headshotsontarget = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(109), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_goalconversion = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(110), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_yellowcardsperfoul = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(111), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_directfreekicksontarget = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(112), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_penaltiesconversion = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(113), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_win = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(114), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulforwardpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(115), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulbackpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(116), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulthroughpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(117), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulkeypasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(118), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulverticalpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(119), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfullongpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(120), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfulshotassists = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(121), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfullinkupplays = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(122), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_fieldaerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(123), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_gksaves = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(124), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_gksuccessfulexits = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(125), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_gkaerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(126), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfultouchinbox = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(127), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_newduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(128), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_newdefensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(129), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_newoffensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(130), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_newsuccessfuldribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(131), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_percent_successfullateralpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(132), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_possessionpercent = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(133), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_duels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(134), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_defensiveduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(135), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_offensiveduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(136), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_aerialduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(137), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_fouls = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(138), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_goals = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(139), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_assists = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(140), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_passes = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(141), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_smartpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(142), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_passestofinalthird = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(143), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_crosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(144), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_dribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(145), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_shots = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(146), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_headshots = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(147), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_interceptions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(148), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfuldefensiveaction = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(149), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_yellowcards = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(150), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_redcards = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(151), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_directredcards = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(152), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulattackingactions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(153), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_freekicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(154), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_directfreekicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(155), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_corners = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(156), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_penalties = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(157), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_passlength = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(158), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_longpasslength = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(159), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_dribbledistancefromopponentgoal = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(160), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_accelerations = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(161), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_looseballduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(162), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_missedballs = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(163), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_forwardpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(164), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_backpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(165), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_throughpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(166), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_keypasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(167), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_verticalpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(168), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_longpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(169), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_shotassists = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(170), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_shotontargetassists = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(171), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_linkupplays = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(172), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_ballrecoveries = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(173), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_opponenthalfrecoveries = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(174), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_dangerousopponenthalfrecoveries = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(175), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_balllosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(176), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_ownhalflosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(177), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_dangerousownhalflosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(178), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_fieldaerialduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(179), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_concededgoals = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(180), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_gkexits = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(181), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_gkaerialduels = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(182), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_duelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(183), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_defensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(184), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_offensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(185), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(186), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulsmartpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(187), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulpassestofinalthird = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(188), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulcrosses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(189), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulforwardpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(190), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulbackpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(191), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulthroughpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(192), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulkeypasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(193), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulverticalpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(194), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfullongpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(195), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfuldribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(196), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_defensiveactions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(197), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_attackingactions = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(198), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_freekicksontarget = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(199), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_directfreekicksontarget = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(200), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfulpenalties = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(201), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfullinkupplays = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(202), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_looseballduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(203), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_fieldaerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(204), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_gksuccessfulexits = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(205), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_gkaerialduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(206), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_gksaves = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(207), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_xgshot = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(208), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_xgshotagainst = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(209), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_receivedpass = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(210), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_touchinbox = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(211), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_progressiverun = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(212), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_offsides = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(213), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_opponentoffsides = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(214), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_shotsagainst = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(215), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_gkgoalkicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(216), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_gkgoalkickssuccess = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(217), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_shortgoalkicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(218), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_longgoalkicks = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(219), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_newduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(220), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_newdefensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(221), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_newoffensiveduelswon = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(222), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_newsuccessfuldribbles = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(223), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_lateralpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(224), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_average_successfullateralpasses = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(225), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));
        db_roundid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(226), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid))));


        logger.info("Executing assertion for the  db_teamid column");
        AssertHelpers.softAssertTrue(api_teamid, db_teamid);
        logger.info("Executing assertion for the  db_competitionid column");
        AssertHelpers.softAssertTrue(api_competitionid, db_competitionid);
        logger.info("Executing assertion for the  db_seasonid column");
        AssertHelpers.softAssertTrue(api_seasonid, db_seasonid);
        logger.info("Executing assertion for the  db_total_matches column");


        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_matches), db_total_matches);
        logger.info("Executing assertion for the  db_total_goals column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_goals), db_total_goals);
        logger.info("Executing assertion for the  db_total_assists column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_assists), db_total_assists);
        logger.info("Executing assertion for the  db_total_shots column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_shots), db_total_shots);
        logger.info("Executing assertion for the  db_total_headshots column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_headshots), db_total_headshots);
        logger.info("Executing assertion for the  db_total_yellowcards column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_yellowcards), db_total_yellowcards);
        logger.info("Executing assertion for the  db_total_redcards column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_redcards), db_total_redcards);
        logger.info("Executing assertion for the  db_total_directredcards column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_directredcards), db_total_directredcards);
        logger.info("Executing assertion for the  db_total_penalties column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_penalties), db_total_penalties);
        logger.info("Executing assertion for the  db_total_linkupplays column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_linkupplays), db_total_linkupplays);
        logger.info("Executing assertion for the  db_total_cleansheets column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_cleansheets), db_total_cleansheets);
        logger.info("Executing assertion for the  db_total_duels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_duels), db_total_duels);
        logger.info("Executing assertion for the  db_total_duelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_duelswon), db_total_duelswon);
        logger.info("Executing assertion for the  db_total_defensiveduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_defensiveduels), db_total_defensiveduels);
        logger.info("Executing assertion for the  db_total_defensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_defensiveduelswon), db_total_defensiveduelswon);
        logger.info("Executing assertion for the  db_total_offensiveduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_offensiveduels), db_total_offensiveduels);
        logger.info("Executing assertion for the  db_total_offensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_offensiveduelswon), db_total_offensiveduelswon);
        logger.info("Executing assertion for the  db_total_aerialduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_aerialduels), db_total_aerialduels);
        logger.info("Executing assertion for the  db_total_aerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_aerialduelswon), db_total_aerialduelswon);
        logger.info("Executing assertion for the  db_total_fouls column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_fouls), db_total_fouls);
        logger.info("Executing assertion for the  db_total_offsides column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_offsides), db_total_offsides);
        logger.info("Executing assertion for the  db_total_passes column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_passes), db_total_passes);
        logger.info("Executing assertion for the  db_total_successfulpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulpasses), db_total_successfulpasses);
        logger.info("Executing assertion for the  db_total_smartpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_smartpasses), db_total_smartpasses);
        logger.info("Executing assertion for the  db_total_successfulsmartpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulsmartpasses), db_total_successfulsmartpasses);
        logger.info("Executing assertion for the  db_total_passestofinalthird column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_passestofinalthird), db_total_passestofinalthird);
        logger.info("Executing assertion for the  db_total_successfulpassestofinalthird column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulpassestofinalthird), db_total_successfulpassestofinalthird);
        logger.info("Executing assertion for the  db_total_crosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_crosses), db_total_crosses);
        logger.info("Executing assertion for the  db_total_successfulcrosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulcrosses), db_total_successfulcrosses);
        logger.info("Executing assertion for the  db_total_forwardpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_forwardpasses), db_total_forwardpasses);
        logger.info("Executing assertion for the  db_total_successfulforwardpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulforwardpasses), db_total_successfulforwardpasses);
        logger.info("Executing assertion for the  db_total_backpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_backpasses), db_total_backpasses);
        logger.info("Executing assertion for the  db_total_successfulbackpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulbackpasses), db_total_successfulbackpasses);
        logger.info("Executing assertion for the  db_total_throughpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_throughpasses), db_total_throughpasses);
        logger.info("Executing assertion for the  db_total_successfulthroughpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulthroughpasses), db_total_successfulthroughpasses);
        logger.info("Executing assertion for the  db_total_keypasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_keypasses), db_total_keypasses);
        logger.info("Executing assertion for the  db_total_successfulkeypasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulkeypasses), db_total_successfulkeypasses);
        logger.info("Executing assertion for the  db_total_verticalpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_verticalpasses), db_total_verticalpasses);
        logger.info("Executing assertion for the  db_total_successfulverticalpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulverticalpasses), db_total_successfulverticalpasses);
        logger.info("Executing assertion for the  db_total_longpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_longpasses), db_total_longpasses);
        logger.info("Executing assertion for the  db_total_successfullongpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfullongpasses), db_total_successfullongpasses);
        logger.info("Executing assertion for the  db_total_dribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_dribbles), db_total_dribbles);
        logger.info("Executing assertion for the  db_total_successfuldribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfuldribbles), db_total_successfuldribbles);
        logger.info("Executing assertion for the  db_total_interceptions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_interceptions), db_total_interceptions);
        logger.info("Executing assertion for the  db_total_defensiveactions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_defensiveactions), db_total_defensiveactions);
        logger.info("Executing assertion for the  db_total_successfuldefensiveactions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfuldefensiveactions), db_total_successfuldefensiveactions);
        logger.info("Executing assertion for the  db_total_attackingactions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_attackingactions), db_total_attackingactions);
        logger.info("Executing assertion for the  db_total_successfulattackingactions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulattackingactions), db_total_successfulattackingactions);
        logger.info("Executing assertion for the  db_total_freekicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_freekicks), db_total_freekicks);
        logger.info("Executing assertion for the  db_total_freekicksontarget column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_freekicksontarget), db_total_freekicksontarget);
        logger.info("Executing assertion for the  db_total_directfreekicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_directfreekicks), db_total_directfreekicks);
        logger.info("Executing assertion for the  db_total_directfreekicksontarget column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_directfreekicksontarget), db_total_directfreekicksontarget);
        logger.info("Executing assertion for the  db_total_corners column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_corners), db_total_corners);
        logger.info("Executing assertion for the  db_total_successfulpenalties column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfulpenalties), db_total_successfulpenalties);
        logger.info("Executing assertion for the  db_total_successfullinkupplays column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfullinkupplays), db_total_successfullinkupplays);
        logger.info("Executing assertion for the  db_total_accelerations column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_accelerations), db_total_accelerations);
        logger.info("Executing assertion for the  db_total_pressingduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_pressingduels), db_total_pressingduels);
        logger.info("Executing assertion for the  db_total_pressingduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_pressingduelswon), db_total_pressingduelswon);
        logger.info("Executing assertion for the  db_total_looseballduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_looseballduels), db_total_looseballduels);
        logger.info("Executing assertion for the  db_total_looseballduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_looseballduelswon), db_total_looseballduelswon);
        logger.info("Executing assertion for the  db_total_missedballs column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_missedballs), db_total_missedballs);
        logger.info("Executing assertion for the  db_total_shotassists column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_shotassists), db_total_shotassists);
        logger.info("Executing assertion for the  db_total_shotontargetassists column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_shotontargetassists), db_total_shotontargetassists);
        logger.info("Executing assertion for the  db_total_recoveries column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_recoveries), db_total_recoveries);
        logger.info("Executing assertion for the  db_total_opponenthalfrecoveries column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_opponenthalfrecoveries), db_total_opponenthalfrecoveries);
        logger.info("Executing assertion for the  db_total_dangerousopponenthalfrecoveries column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_dangerousopponenthalfrecoveries), db_total_dangerousopponenthalfrecoveries);
        logger.info("Executing assertion for the  db_total_losses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_losses), db_total_losses);
        logger.info("Executing assertion for the  db_total_ownhalflosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_ownhalflosses), db_total_ownhalflosses);
        logger.info("Executing assertion for the  db_total_dangerousownhalflosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_dangerousownhalflosses), db_total_dangerousownhalflosses);
        logger.info("Executing assertion for the  db_total_fieldaerialduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_fieldaerialduels), db_total_fieldaerialduels);
        logger.info("Executing assertion for the  db_total_fieldaerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_fieldaerialduelswon), db_total_fieldaerialduelswon);
        logger.info("Executing assertion for the  db_total_gkexits column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_gkexits), db_total_gkexits);
        logger.info("Executing assertion for the  db_total_gksuccessfulexits column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_gksuccessfulexits), db_total_gksuccessfulexits);
        logger.info("Executing assertion for the  db_total_gkaerialduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_gkaerialduels), db_total_gkaerialduels);
        logger.info("Executing assertion for the  db_total_gkaerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_gkaerialduelswon), db_total_gkaerialduelswon);
        logger.info("Executing assertion for the  db_total_gksaves column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_gksaves), db_total_gksaves);
        logger.info("Executing assertion for the  db_total_xgshot column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_xgshot), db_total_xgshot);
        logger.info("Executing assertion for the  db_total_xgshotagainst column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_xgshotagainst), db_total_xgshotagainst);
        logger.info("Executing assertion for the  db_total_ppda column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_ppda), db_total_ppda);
        logger.info("Executing assertion for the  db_total_receivedpass column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_receivedpass), db_total_receivedpass);
        logger.info("Executing assertion for the  db_total_touchinbox column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_touchinbox), db_total_touchinbox);
        logger.info("Executing assertion for the  db_total_progressiverun column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_progressiverun), db_total_progressiverun);
        logger.info("Executing assertion for the  db_total_concededgoals column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_concededgoals), db_total_concededgoals);
        logger.info("Executing assertion for the  db_total_opponentoffsides column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_opponentoffsides), db_total_opponentoffsides);
        logger.info("Executing assertion for the  db_total_shotsagainst column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_shotsagainst), db_total_shotsagainst);
        logger.info("Executing assertion for the  db_total_gkgoalkicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_gkgoalkicks), db_total_gkgoalkicks);
        logger.info("Executing assertion for the  db_total_gkgoalkickssuccess column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_gkgoalkickssuccess), db_total_gkgoalkickssuccess);
        logger.info("Executing assertion for the  db_total_shortgoalkicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_shortgoalkicks), db_total_shortgoalkicks);
        logger.info("Executing assertion for the  db_total_longgoalkicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_longgoalkicks), db_total_longgoalkicks);
        logger.info("Executing assertion for the  db_total_matchestagged column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_matchestagged), db_total_matchestagged);
        logger.info("Executing assertion for the  db_total_newduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_newduelswon), db_total_newduelswon);
        logger.info("Executing assertion for the  db_total_newdefensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_newdefensiveduelswon), db_total_newdefensiveduelswon);
        logger.info("Executing assertion for the  db_total_newoffensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_newoffensiveduelswon), db_total_newoffensiveduelswon);
        logger.info("Executing assertion for the  db_total_newsuccessfuldribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_newsuccessfuldribbles), db_total_newsuccessfuldribbles);
        logger.info("Executing assertion for the  db_total_lateralpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_lateralpasses), db_total_lateralpasses);
        logger.info("Executing assertion for the  db_total_successfullateralpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_total_successfullateralpasses), db_total_successfullateralpasses);
        logger.info("Executing assertion for the  db_percent_duelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_duelswon), db_percent_duelswon);
        logger.info("Executing assertion for the  db_percent_defensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_defensiveduelswon), db_percent_defensiveduelswon);
        logger.info("Executing assertion for the  db_percent_offensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_offensiveduelswon), db_percent_offensiveduelswon);
        logger.info("Executing assertion for the  db_percent_aerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_aerialduelswon), db_percent_aerialduelswon);
        logger.info("Executing assertion for the  db_percent_successfulpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulpasses), db_percent_successfulpasses);
        logger.info("Executing assertion for the  db_percent_successfulsmartpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulsmartpasses), db_percent_successfulsmartpasses);
        logger.info("Executing assertion for the  db_percent_successfulpassestofinalthird column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulpassestofinalthird), db_percent_successfulpassestofinalthird);
        logger.info("Executing assertion for the  db_percent_successfulcrosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulcrosses), db_percent_successfulcrosses);
        logger.info("Executing assertion for the  db_percent_successfuldribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfuldribbles), db_percent_successfuldribbles);
        logger.info("Executing assertion for the  db_percent_shotsontarget column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_shotsontarget), db_percent_shotsontarget);
        logger.info("Executing assertion for the  db_percent_headshotsontarget column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_headshotsontarget), db_percent_headshotsontarget);
        logger.info("Executing assertion for the  db_percent_goalconversion column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_goalconversion), db_percent_goalconversion);
        logger.info("Executing assertion for the  db_percent_yellowcardsperfoul column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_yellowcardsperfoul), db_percent_yellowcardsperfoul);
        logger.info("Executing assertion for the  db_percent_directfreekicksontarget column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_directfreekicksontarget), db_percent_directfreekicksontarget);
        logger.info("Executing assertion for the  db_percent_penaltiesconversion column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_penaltiesconversion), db_percent_penaltiesconversion);
        logger.info("Executing assertion for the  db_percent_win column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_win), db_percent_win);
        logger.info("Executing assertion for the  db_percent_successfulforwardpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulforwardpasses), db_percent_successfulforwardpasses);
        logger.info("Executing assertion for the  db_percent_successfulbackpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulbackpasses), db_percent_successfulbackpasses);
        logger.info("Executing assertion for the  db_percent_successfulthroughpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulthroughpasses), db_percent_successfulthroughpasses);
        logger.info("Executing assertion for the  db_percent_successfulkeypasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulkeypasses), db_percent_successfulkeypasses);
        logger.info("Executing assertion for the  db_percent_successfulverticalpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulverticalpasses), db_percent_successfulverticalpasses);
        logger.info("Executing assertion for the  db_percent_successfullongpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfullongpasses), db_percent_successfullongpasses);
        logger.info("Executing assertion for the  db_percent_successfulshotassists column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfulshotassists), db_percent_successfulshotassists);
        logger.info("Executing assertion for the  db_percent_successfullinkupplays column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfullinkupplays), db_percent_successfullinkupplays);
        logger.info("Executing assertion for the  db_percent_fieldaerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_fieldaerialduelswon), db_percent_fieldaerialduelswon);
        logger.info("Executing assertion for the  db_percent_gksaves column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_gksaves), db_percent_gksaves);
        logger.info("Executing assertion for the  db_percent_gksuccessfulexits column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_gksuccessfulexits), db_percent_gksuccessfulexits);
        logger.info("Executing assertion for the  db_percent_gkaerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_gkaerialduelswon), db_percent_gkaerialduelswon);
        logger.info("Executing assertion for the  db_percent_successfultouchinbox column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfultouchinbox), db_percent_successfultouchinbox);
        logger.info("Executing assertion for the  db_percent_newduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_newduelswon), db_percent_newduelswon);
        logger.info("Executing assertion for the  db_percent_newdefensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_newdefensiveduelswon), db_percent_newdefensiveduelswon);
        logger.info("Executing assertion for the  db_percent_newoffensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_newoffensiveduelswon), db_percent_newoffensiveduelswon);
        logger.info("Executing assertion for the  db_percent_newsuccessfuldribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_newsuccessfuldribbles), db_percent_newsuccessfuldribbles);
        logger.info("Executing assertion for the  db_percent_successfullateralpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_percent_successfullateralpasses), db_percent_successfullateralpasses);
        logger.info("Executing assertion for the  db_average_possessionpercent column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_possessionpercent), db_average_possessionpercent);
        logger.info("Executing assertion for the  db_average_duels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_duels), db_average_duels);
        logger.info("Executing assertion for the  db_average_defensiveduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_defensiveduels), db_average_defensiveduels);
        logger.info("Executing assertion for the  db_average_offensiveduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_offensiveduels), db_average_offensiveduels);
        logger.info("Executing assertion for the  db_average_aerialduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_aerialduels), db_average_aerialduels);
        logger.info("Executing assertion for the  db_average_fouls column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_fouls), db_average_fouls);
        logger.info("Executing assertion for the  db_average_goals column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_goals), db_average_goals);
        logger.info("Executing assertion for the  db_average_assists column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_assists), db_average_assists);
        logger.info("Executing assertion for the  db_average_passes column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_passes), db_average_passes);
        logger.info("Executing assertion for the  db_average_smartpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_smartpasses), db_average_smartpasses);
        logger.info("Executing assertion for the  db_average_passestofinalthird column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_passestofinalthird), db_average_passestofinalthird);
        logger.info("Executing assertion for the  db_average_crosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_crosses), db_average_crosses);
        logger.info("Executing assertion for the  db_average_dribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_dribbles), db_average_dribbles);
        logger.info("Executing assertion for the  db_average_shots column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_shots), db_average_shots);
        logger.info("Executing assertion for the  db_average_headshots column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_headshots), db_average_headshots);
        logger.info("Executing assertion for the  db_average_interceptions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_interceptions), db_average_interceptions);
        logger.info("Executing assertion for the  db_average_successfuldefensiveaction column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfuldefensiveaction), db_average_successfuldefensiveaction);
        logger.info("Executing assertion for the  db_average_yellowcards column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_yellowcards), db_average_yellowcards);
        logger.info("Executing assertion for the  db_average_redcards column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_redcards), db_average_redcards);
        logger.info("Executing assertion for the  db_average_directredcards column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_directredcards), db_average_directredcards);
        logger.info("Executing assertion for the  db_average_successfulattackingactions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulattackingactions), db_average_successfulattackingactions);
        logger.info("Executing assertion for the  db_average_freekicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_freekicks), db_average_freekicks);
        logger.info("Executing assertion for the  db_average_directfreekicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_directfreekicks), db_average_directfreekicks);
        logger.info("Executing assertion for the  db_average_corners column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_corners), db_average_corners);
        logger.info("Executing assertion for the  db_average_penalties column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_penalties), db_average_penalties);
        logger.info("Executing assertion for the  db_average_passlength column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_passlength), db_average_passlength);
        logger.info("Executing assertion for the  db_average_longpasslength column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_longpasslength), db_average_longpasslength);
        logger.info("Executing assertion for the  db_average_dribbledistancefromopponentgoal column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_dribbledistancefromopponentgoal), db_average_dribbledistancefromopponentgoal);
        logger.info("Executing assertion for the  db_average_accelerations column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_accelerations), db_average_accelerations);
        logger.info("Executing assertion for the  db_average_looseballduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_looseballduels), db_average_looseballduels);
        logger.info("Executing assertion for the  db_average_missedballs column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_missedballs), db_average_missedballs);
        logger.info("Executing assertion for the  db_average_forwardpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_forwardpasses), db_average_forwardpasses);
        logger.info("Executing assertion for the  db_average_backpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_backpasses), db_average_backpasses);
        logger.info("Executing assertion for the  db_average_throughpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_throughpasses), db_average_throughpasses);
        logger.info("Executing assertion for the  db_average_keypasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_keypasses), db_average_keypasses);
        logger.info("Executing assertion for the  db_average_verticalpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_verticalpasses), db_average_verticalpasses);
        logger.info("Executing assertion for the  db_average_longpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_longpasses), db_average_longpasses);
        logger.info("Executing assertion for the  db_average_shotassists column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_shotassists), db_average_shotassists);
        logger.info("Executing assertion for the  db_average_shotontargetassists column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_shotontargetassists), db_average_shotontargetassists);
        logger.info("Executing assertion for the  db_average_linkupplays column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_linkupplays), db_average_linkupplays);
        logger.info("Executing assertion for the  db_average_ballrecoveries column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_ballrecoveries), db_average_ballrecoveries);
        logger.info("Executing assertion for the  db_average_opponenthalfrecoveries column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_opponenthalfrecoveries), db_average_opponenthalfrecoveries);
        logger.info("Executing assertion for the  db_average_dangerousopponenthalfrecoveries column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_dangerousopponenthalfrecoveries), db_average_dangerousopponenthalfrecoveries);
        logger.info("Executing assertion for the  db_average_balllosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_balllosses), db_average_balllosses);
        logger.info("Executing assertion for the  db_average_ownhalflosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_ownhalflosses), db_average_ownhalflosses);
        logger.info("Executing assertion for the  db_average_dangerousownhalflosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_dangerousownhalflosses), db_average_dangerousownhalflosses);
        logger.info("Executing assertion for the  db_average_fieldaerialduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_fieldaerialduels), db_average_fieldaerialduels);
        logger.info("Executing assertion for the  db_average_concededgoals column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_concededgoals), db_average_concededgoals);
        logger.info("Executing assertion for the  db_average_gkexits column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_gkexits), db_average_gkexits);
        logger.info("Executing assertion for the  db_average_gkaerialduels column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_gkaerialduels), db_average_gkaerialduels);
        logger.info("Executing assertion for the  db_average_duelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_duelswon), db_average_duelswon);
        logger.info("Executing assertion for the  db_average_defensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_defensiveduelswon), db_average_defensiveduelswon);
        logger.info("Executing assertion for the  db_average_offensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_offensiveduelswon), db_average_offensiveduelswon);
        logger.info("Executing assertion for the  db_average_successfulpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulpasses), db_average_successfulpasses);
        logger.info("Executing assertion for the  db_average_successfulsmartpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulsmartpasses), db_average_successfulsmartpasses);
        logger.info("Executing assertion for the  db_average_successfulpassestofinalthird column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulpassestofinalthird), db_average_successfulpassestofinalthird);
        logger.info("Executing assertion for the  db_average_successfulcrosses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulcrosses), db_average_successfulcrosses);
        logger.info("Executing assertion for the  db_average_successfulforwardpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulforwardpasses), db_average_successfulforwardpasses);
        logger.info("Executing assertion for the  db_average_successfulbackpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulbackpasses), db_average_successfulbackpasses);
        logger.info("Executing assertion for the  db_average_successfulthroughpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulthroughpasses), db_average_successfulthroughpasses);
        logger.info("Executing assertion for the  db_average_successfulkeypasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulkeypasses), db_average_successfulkeypasses);
        logger.info("Executing assertion for the  db_average_successfulverticalpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulverticalpasses), db_average_successfulverticalpasses);
        logger.info("Executing assertion for the  db_average_successfullongpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfullongpasses), db_average_successfullongpasses);
        logger.info("Executing assertion for the  db_average_successfuldribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfuldribbles), db_average_successfuldribbles);
        logger.info("Executing assertion for the  db_average_defensiveactions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_defensiveactions), db_average_defensiveactions);
        logger.info("Executing assertion for the  db_average_attackingactions column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_attackingactions), db_average_attackingactions);
        logger.info("Executing assertion for the  db_average_freekicksontarget column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_freekicksontarget), db_average_freekicksontarget);
        logger.info("Executing assertion for the  db_average_directfreekicksontarget column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_directfreekicksontarget), db_average_directfreekicksontarget);
        logger.info("Executing assertion for the  db_average_successfulpenalties column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfulpenalties), db_average_successfulpenalties);
        logger.info("Executing assertion for the  db_average_successfullinkupplays column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfullinkupplays), db_average_successfullinkupplays);
        logger.info("Executing assertion for the  db_average_looseballduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_looseballduelswon), db_average_looseballduelswon);
        logger.info("Executing assertion for the  db_average_fieldaerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_fieldaerialduelswon), db_average_fieldaerialduelswon);
        logger.info("Executing assertion for the  db_average_gksuccessfulexits column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_gksuccessfulexits), db_average_gksuccessfulexits);
        logger.info("Executing assertion for the  db_average_gkaerialduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_gkaerialduelswon), db_average_gkaerialduelswon);
        logger.info("Executing assertion for the  db_average_gksaves column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_gksaves), db_average_gksaves);
        logger.info("Executing assertion for the  db_average_xgshot column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_xgshot), db_average_xgshot);
        logger.info("Executing assertion for the  db_average_xgshotagainst column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_xgshotagainst), db_average_xgshotagainst);
        logger.info("Executing assertion for the  db_average_receivedpass column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_receivedpass), db_average_receivedpass);
        logger.info("Executing assertion for the  db_average_touchinbox column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_touchinbox), db_average_touchinbox);
        logger.info("Executing assertion for the  db_average_progressiverun column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_progressiverun), db_average_progressiverun);
        logger.info("Executing assertion for the  db_average_offsides column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_offsides), db_average_offsides);
        logger.info("Executing assertion for the  db_average_opponentoffsides column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_opponentoffsides), db_average_opponentoffsides);
        logger.info("Executing assertion for the  db_average_shotsagainst column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_shotsagainst), db_average_shotsagainst);
        logger.info("Executing assertion for the  db_average_gkgoalkicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_gkgoalkicks), db_average_gkgoalkicks);
        logger.info("Executing assertion for the  db_average_gkgoalkickssuccess column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_gkgoalkickssuccess), db_average_gkgoalkickssuccess);
        logger.info("Executing assertion for the  db_average_shortgoalkicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_shortgoalkicks), db_average_shortgoalkicks);
        logger.info("Executing assertion for the  db_average_longgoalkicks column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_longgoalkicks), db_average_longgoalkicks);
        logger.info("Executing assertion for the  db_average_newduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_newduelswon), db_average_newduelswon);
        logger.info("Executing assertion for the  db_average_newdefensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_newdefensiveduelswon), db_average_newdefensiveduelswon);
        logger.info("Executing assertion for the  db_average_newoffensiveduelswon column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_newoffensiveduelswon), db_average_newoffensiveduelswon);
        logger.info("Executing assertion for the  db_average_newsuccessfuldribbles column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_newsuccessfuldribbles), db_average_newsuccessfuldribbles);
        logger.info("Executing assertion for the  db_average_lateralpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_lateralpasses), db_average_lateralpasses);
        logger.info("Executing assertion for the  db_average_successfullateralpasses column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_average_successfullateralpasses), db_average_successfullateralpasses);
        logger.info("Executing assertion for the  db_roundid column");
        AssertHelpers.softAssertTrue(api_roundid, db_roundid);
        AssertHelpers.softAssertAll();
    }


    @Test
    @Title("Verify TeamsAdvanceStats Table Number Of Records")
    @Tag("TeamsAdvanceStats")
    public void teamsAdvanceStatsValidateNumberORecordCount() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        //Fetching record of Team Advance table
        String columnName1 = "teamid";
        String columnName2 = "competitionid";
        String teamAdvanceSQLQuery = "Select " + columnName1 + ", " + columnName2 + " from " + GlbVar.catalog_name + "."
                + GlbVar.schemas_name + "." + GlbVar.team_advancestats_tablename + " where " + columnName2 + " IN(" + GlbVar.staticAdvanceCompetitionIDList + ") ORDER BY " + columnName1;
        logger.info("SQL QUERY of TeamAdvance table: " + teamAdvanceSQLQuery);
        ArrayList<String> teamAdvanceResult = tableMetadata.fetchTableDataByQuery(teamAdvanceSQLQuery);
        //Fetching record of Team table
        String teamColumnName1 = "teamid";
        String teamColumnName2 = "competitionid";
        String teamSQLQuery = "Select " + teamColumnName1 + ", " + teamColumnName2 + " from " + GlbVar.catalog_name + "."
                + GlbVar.schemas_name + "." + GlbVar.teams_tablename + " where " + teamColumnName2 + " IN(" + GlbVar.staticAdvanceCompetitionIDList + ") ORDER BY " + teamColumnName1;
        logger.info("SQL QUERY of team table: " + teamAdvanceSQLQuery);
        ArrayList<String> teamTableResult = tableMetadata.fetchTableDataByQuery(teamSQLQuery);
        AssertHelpers.softAssertMatchTwoList(teamTableResult, teamAdvanceResult);
        logger.info("Missing data in Team Advance Table: " + GenericFun.missingDataInSecondList(teamTableResult, teamAdvanceResult));
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify TeamsAdvanceStats Table Duplicate Records")
    @Tag("TeamsAdvanceStats")
    public void teamsAdvanceStatsTableDuplicateRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.team_advancestats_tablename);
        logger.info("Fetching duplicate value for 'teams' table and column name is '" + columnNames.get(0) + "' and " + columnNames.get(9));
        ArrayList<String> db_teamid = tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.team_advancestats_tablename
                , columnNames.get(0), columnNames.get(2), null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_teamid);
        AssertHelpers.softAssertAll();
    }


    //DB AND API VARIABLES
    ArrayList<String> db_teamid = new ArrayList<String>();
    ArrayList<String> db_competitionid = new ArrayList<String>();
    ArrayList<String> db_seasonid = new ArrayList<String>();
    ArrayList<String> db_total_matches = new ArrayList<String>();
    ArrayList<String> db_total_goals = new ArrayList<String>();
    ArrayList<String> db_total_assists = new ArrayList<String>();
    ArrayList<String> db_total_shots = new ArrayList<String>();
    ArrayList<String> db_total_headshots = new ArrayList<String>();
    ArrayList<String> db_total_yellowcards = new ArrayList<String>();
    ArrayList<String> db_total_redcards = new ArrayList<String>();
    ArrayList<String> db_total_directredcards = new ArrayList<String>();
    ArrayList<String> db_total_penalties = new ArrayList<String>();
    ArrayList<String> db_total_linkupplays = new ArrayList<String>();
    ArrayList<String> db_total_cleansheets = new ArrayList<String>();
    ArrayList<String> db_total_duels = new ArrayList<String>();
    ArrayList<String> db_total_duelswon = new ArrayList<String>();
    ArrayList<String> db_total_defensiveduels = new ArrayList<String>();
    ArrayList<String> db_total_defensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_total_offensiveduels = new ArrayList<String>();
    ArrayList<String> db_total_offensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_total_aerialduels = new ArrayList<String>();
    ArrayList<String> db_total_aerialduelswon = new ArrayList<String>();
    ArrayList<String> db_total_fouls = new ArrayList<String>();
    ArrayList<String> db_total_offsides = new ArrayList<String>();
    ArrayList<String> db_total_passes = new ArrayList<String>();
    ArrayList<String> db_total_successfulpasses = new ArrayList<String>();
    ArrayList<String> db_total_smartpasses = new ArrayList<String>();
    ArrayList<String> db_total_successfulsmartpasses = new ArrayList<String>();
    ArrayList<String> db_total_passestofinalthird = new ArrayList<String>();
    ArrayList<String> db_total_successfulpassestofinalthird = new ArrayList<String>();
    ArrayList<String> db_total_crosses = new ArrayList<String>();
    ArrayList<String> db_total_successfulcrosses = new ArrayList<String>();
    ArrayList<String> db_total_forwardpasses = new ArrayList<String>();
    ArrayList<String> db_total_successfulforwardpasses = new ArrayList<String>();
    ArrayList<String> db_total_backpasses = new ArrayList<String>();
    ArrayList<String> db_total_successfulbackpasses = new ArrayList<String>();
    ArrayList<String> db_total_throughpasses = new ArrayList<String>();
    ArrayList<String> db_total_successfulthroughpasses = new ArrayList<String>();
    ArrayList<String> db_total_keypasses = new ArrayList<String>();
    ArrayList<String> db_total_successfulkeypasses = new ArrayList<String>();
    ArrayList<String> db_total_verticalpasses = new ArrayList<String>();
    ArrayList<String> db_total_successfulverticalpasses = new ArrayList<String>();
    ArrayList<String> db_total_longpasses = new ArrayList<String>();
    ArrayList<String> db_total_successfullongpasses = new ArrayList<String>();
    ArrayList<String> db_total_dribbles = new ArrayList<String>();
    ArrayList<String> db_total_successfuldribbles = new ArrayList<String>();
    ArrayList<String> db_total_interceptions = new ArrayList<String>();
    ArrayList<String> db_total_defensiveactions = new ArrayList<String>();
    ArrayList<String> db_total_successfuldefensiveactions = new ArrayList<String>();
    ArrayList<String> db_total_attackingactions = new ArrayList<String>();
    ArrayList<String> db_total_successfulattackingactions = new ArrayList<String>();
    ArrayList<String> db_total_freekicks = new ArrayList<String>();
    ArrayList<String> db_total_freekicksontarget = new ArrayList<String>();
    ArrayList<String> db_total_directfreekicks = new ArrayList<String>();
    ArrayList<String> db_total_directfreekicksontarget = new ArrayList<String>();
    ArrayList<String> db_total_corners = new ArrayList<String>();
    ArrayList<String> db_total_successfulpenalties = new ArrayList<String>();
    ArrayList<String> db_total_successfullinkupplays = new ArrayList<String>();
    ArrayList<String> db_total_accelerations = new ArrayList<String>();
    ArrayList<String> db_total_pressingduels = new ArrayList<String>();
    ArrayList<String> db_total_pressingduelswon = new ArrayList<String>();
    ArrayList<String> db_total_looseballduels = new ArrayList<String>();
    ArrayList<String> db_total_looseballduelswon = new ArrayList<String>();
    ArrayList<String> db_total_missedballs = new ArrayList<String>();
    ArrayList<String> db_total_shotassists = new ArrayList<String>();
    ArrayList<String> db_total_shotontargetassists = new ArrayList<String>();
    ArrayList<String> db_total_recoveries = new ArrayList<String>();
    ArrayList<String> db_total_opponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> db_total_dangerousopponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> db_total_losses = new ArrayList<String>();
    ArrayList<String> db_total_ownhalflosses = new ArrayList<String>();
    ArrayList<String> db_total_dangerousownhalflosses = new ArrayList<String>();
    ArrayList<String> db_total_fieldaerialduels = new ArrayList<String>();
    ArrayList<String> db_total_fieldaerialduelswon = new ArrayList<String>();
    ArrayList<String> db_total_gkexits = new ArrayList<String>();
    ArrayList<String> db_total_gksuccessfulexits = new ArrayList<String>();
    ArrayList<String> db_total_gkaerialduels = new ArrayList<String>();
    ArrayList<String> db_total_gkaerialduelswon = new ArrayList<String>();
    ArrayList<String> db_total_gksaves = new ArrayList<String>();
    ArrayList<String> db_total_xgshot = new ArrayList<String>();
    ArrayList<String> db_total_xgshotagainst = new ArrayList<String>();
    ArrayList<String> db_total_ppda = new ArrayList<String>();
    ArrayList<String> db_total_receivedpass = new ArrayList<String>();
    ArrayList<String> db_total_touchinbox = new ArrayList<String>();
    ArrayList<String> db_total_progressiverun = new ArrayList<String>();
    ArrayList<String> db_total_concededgoals = new ArrayList<String>();
    ArrayList<String> db_total_opponentoffsides = new ArrayList<String>();
    ArrayList<String> db_total_shotsagainst = new ArrayList<String>();
    ArrayList<String> db_total_gkgoalkicks = new ArrayList<String>();
    ArrayList<String> db_total_gkgoalkickssuccess = new ArrayList<String>();
    ArrayList<String> db_total_shortgoalkicks = new ArrayList<String>();
    ArrayList<String> db_total_longgoalkicks = new ArrayList<String>();
    ArrayList<String> db_total_matchestagged = new ArrayList<String>();
    ArrayList<String> db_total_newduelswon = new ArrayList<String>();
    ArrayList<String> db_total_newdefensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_total_newoffensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_total_newsuccessfuldribbles = new ArrayList<String>();
    ArrayList<String> db_total_lateralpasses = new ArrayList<String>();
    ArrayList<String> db_total_successfullateralpasses = new ArrayList<String>();
    ArrayList<String> db_percent_duelswon = new ArrayList<String>();
    ArrayList<String> db_percent_defensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_offensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_aerialduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_successfulpasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfulsmartpasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfulpassestofinalthird = new ArrayList<String>();
    ArrayList<String> db_percent_successfulcrosses = new ArrayList<String>();
    ArrayList<String> db_percent_successfuldribbles = new ArrayList<String>();
    ArrayList<String> db_percent_shotsontarget = new ArrayList<String>();
    ArrayList<String> db_percent_headshotsontarget = new ArrayList<String>();
    ArrayList<String> db_percent_goalconversion = new ArrayList<String>();
    ArrayList<String> db_percent_yellowcardsperfoul = new ArrayList<String>();
    ArrayList<String> db_percent_directfreekicksontarget = new ArrayList<String>();
    ArrayList<String> db_percent_penaltiesconversion = new ArrayList<String>();
    ArrayList<String> db_percent_win = new ArrayList<String>();
    ArrayList<String> db_percent_successfulforwardpasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfulbackpasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfulthroughpasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfulkeypasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfulverticalpasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfullongpasses = new ArrayList<String>();
    ArrayList<String> db_percent_successfulshotassists = new ArrayList<String>();
    ArrayList<String> db_percent_successfullinkupplays = new ArrayList<String>();
    ArrayList<String> db_percent_fieldaerialduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_gksaves = new ArrayList<String>();
    ArrayList<String> db_percent_gksuccessfulexits = new ArrayList<String>();
    ArrayList<String> db_percent_gkaerialduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_successfultouchinbox = new ArrayList<String>();
    ArrayList<String> db_percent_newduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_newdefensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_newoffensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_percent_newsuccessfuldribbles = new ArrayList<String>();
    ArrayList<String> db_percent_successfullateralpasses = new ArrayList<String>();
    ArrayList<String> db_average_possessionpercent = new ArrayList<String>();
    ArrayList<String> db_average_duels = new ArrayList<String>();
    ArrayList<String> db_average_defensiveduels = new ArrayList<String>();
    ArrayList<String> db_average_offensiveduels = new ArrayList<String>();
    ArrayList<String> db_average_aerialduels = new ArrayList<String>();
    ArrayList<String> db_average_fouls = new ArrayList<String>();
    ArrayList<String> db_average_goals = new ArrayList<String>();
    ArrayList<String> db_average_assists = new ArrayList<String>();
    ArrayList<String> db_average_passes = new ArrayList<String>();
    ArrayList<String> db_average_smartpasses = new ArrayList<String>();
    ArrayList<String> db_average_passestofinalthird = new ArrayList<String>();
    ArrayList<String> db_average_crosses = new ArrayList<String>();
    ArrayList<String> db_average_dribbles = new ArrayList<String>();
    ArrayList<String> db_average_shots = new ArrayList<String>();
    ArrayList<String> db_average_headshots = new ArrayList<String>();
    ArrayList<String> db_average_interceptions = new ArrayList<String>();
    ArrayList<String> db_average_successfuldefensiveaction = new ArrayList<String>();
    ArrayList<String> db_average_yellowcards = new ArrayList<String>();
    ArrayList<String> db_average_redcards = new ArrayList<String>();
    ArrayList<String> db_average_directredcards = new ArrayList<String>();
    ArrayList<String> db_average_successfulattackingactions = new ArrayList<String>();
    ArrayList<String> db_average_freekicks = new ArrayList<String>();
    ArrayList<String> db_average_directfreekicks = new ArrayList<String>();
    ArrayList<String> db_average_corners = new ArrayList<String>();
    ArrayList<String> db_average_penalties = new ArrayList<String>();
    ArrayList<String> db_average_passlength = new ArrayList<String>();
    ArrayList<String> db_average_longpasslength = new ArrayList<String>();
    ArrayList<String> db_average_dribbledistancefromopponentgoal = new ArrayList<String>();
    ArrayList<String> db_average_accelerations = new ArrayList<String>();
    ArrayList<String> db_average_looseballduels = new ArrayList<String>();
    ArrayList<String> db_average_missedballs = new ArrayList<String>();
    ArrayList<String> db_average_forwardpasses = new ArrayList<String>();
    ArrayList<String> db_average_backpasses = new ArrayList<String>();
    ArrayList<String> db_average_throughpasses = new ArrayList<String>();
    ArrayList<String> db_average_keypasses = new ArrayList<String>();
    ArrayList<String> db_average_verticalpasses = new ArrayList<String>();
    ArrayList<String> db_average_longpasses = new ArrayList<String>();
    ArrayList<String> db_average_shotassists = new ArrayList<String>();
    ArrayList<String> db_average_shotontargetassists = new ArrayList<String>();
    ArrayList<String> db_average_linkupplays = new ArrayList<String>();
    ArrayList<String> db_average_ballrecoveries = new ArrayList<String>();
    ArrayList<String> db_average_opponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> db_average_dangerousopponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> db_average_balllosses = new ArrayList<String>();
    ArrayList<String> db_average_ownhalflosses = new ArrayList<String>();
    ArrayList<String> db_average_dangerousownhalflosses = new ArrayList<String>();
    ArrayList<String> db_average_fieldaerialduels = new ArrayList<String>();
    ArrayList<String> db_average_concededgoals = new ArrayList<String>();
    ArrayList<String> db_average_gkexits = new ArrayList<String>();
    ArrayList<String> db_average_gkaerialduels = new ArrayList<String>();
    ArrayList<String> db_average_duelswon = new ArrayList<String>();
    ArrayList<String> db_average_defensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_average_offensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_average_successfulpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfulsmartpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfulpassestofinalthird = new ArrayList<String>();
    ArrayList<String> db_average_successfulcrosses = new ArrayList<String>();
    ArrayList<String> db_average_successfulforwardpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfulbackpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfulthroughpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfulkeypasses = new ArrayList<String>();
    ArrayList<String> db_average_successfulverticalpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfullongpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfuldribbles = new ArrayList<String>();
    ArrayList<String> db_average_defensiveactions = new ArrayList<String>();
    ArrayList<String> db_average_attackingactions = new ArrayList<String>();
    ArrayList<String> db_average_freekicksontarget = new ArrayList<String>();
    ArrayList<String> db_average_directfreekicksontarget = new ArrayList<String>();
    ArrayList<String> db_average_successfulpenalties = new ArrayList<String>();
    ArrayList<String> db_average_successfullinkupplays = new ArrayList<String>();
    ArrayList<String> db_average_looseballduelswon = new ArrayList<String>();
    ArrayList<String> db_average_fieldaerialduelswon = new ArrayList<String>();
    ArrayList<String> db_average_gksuccessfulexits = new ArrayList<String>();
    ArrayList<String> db_average_gkaerialduelswon = new ArrayList<String>();
    ArrayList<String> db_average_gksaves = new ArrayList<String>();
    ArrayList<String> db_average_xgshot = new ArrayList<String>();
    ArrayList<String> db_average_xgshotagainst = new ArrayList<String>();
    ArrayList<String> db_average_receivedpass = new ArrayList<String>();
    ArrayList<String> db_average_touchinbox = new ArrayList<String>();
    ArrayList<String> db_average_progressiverun = new ArrayList<String>();
    ArrayList<String> db_average_offsides = new ArrayList<String>();
    ArrayList<String> db_average_opponentoffsides = new ArrayList<String>();
    ArrayList<String> db_average_shotsagainst = new ArrayList<String>();
    ArrayList<String> db_average_gkgoalkicks = new ArrayList<String>();
    ArrayList<String> db_average_gkgoalkickssuccess = new ArrayList<String>();
    ArrayList<String> db_average_shortgoalkicks = new ArrayList<String>();
    ArrayList<String> db_average_longgoalkicks = new ArrayList<String>();
    ArrayList<String> db_average_newduelswon = new ArrayList<String>();
    ArrayList<String> db_average_newdefensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_average_newoffensiveduelswon = new ArrayList<String>();
    ArrayList<String> db_average_newsuccessfuldribbles = new ArrayList<String>();
    ArrayList<String> db_average_lateralpasses = new ArrayList<String>();
    ArrayList<String> db_average_successfullateralpasses = new ArrayList<String>();
    ArrayList<String> db_roundid = new ArrayList<String>();

    ArrayList<String> api_teamid = new ArrayList<String>();
    ArrayList<String> api_competitionid = new ArrayList<String>();
    ArrayList<String> api_seasonid = new ArrayList<String>();
    ArrayList<String> api_total_matches = new ArrayList<String>();
    ArrayList<String> api_total_goals = new ArrayList<String>();
    ArrayList<String> api_total_assists = new ArrayList<String>();
    ArrayList<String> api_total_shots = new ArrayList<String>();
    ArrayList<String> api_total_headshots = new ArrayList<String>();
    ArrayList<String> api_total_yellowcards = new ArrayList<String>();
    ArrayList<String> api_total_redcards = new ArrayList<String>();
    ArrayList<String> api_total_directredcards = new ArrayList<String>();
    ArrayList<String> api_total_penalties = new ArrayList<String>();
    ArrayList<String> api_total_linkupplays = new ArrayList<String>();
    ArrayList<String> api_total_cleansheets = new ArrayList<String>();
    ArrayList<String> api_total_duels = new ArrayList<String>();
    ArrayList<String> api_total_duelswon = new ArrayList<String>();
    ArrayList<String> api_total_defensiveduels = new ArrayList<String>();
    ArrayList<String> api_total_defensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_total_offensiveduels = new ArrayList<String>();
    ArrayList<String> api_total_offensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_total_aerialduels = new ArrayList<String>();
    ArrayList<String> api_total_aerialduelswon = new ArrayList<String>();
    ArrayList<String> api_total_fouls = new ArrayList<String>();
    ArrayList<String> api_total_offsides = new ArrayList<String>();
    ArrayList<String> api_total_passes = new ArrayList<String>();
    ArrayList<String> api_total_successfulpasses = new ArrayList<String>();
    ArrayList<String> api_total_smartpasses = new ArrayList<String>();
    ArrayList<String> api_total_successfulsmartpasses = new ArrayList<String>();
    ArrayList<String> api_total_passestofinalthird = new ArrayList<String>();
    ArrayList<String> api_total_successfulpassestofinalthird = new ArrayList<String>();
    ArrayList<String> api_total_crosses = new ArrayList<String>();
    ArrayList<String> api_total_successfulcrosses = new ArrayList<String>();
    ArrayList<String> api_total_forwardpasses = new ArrayList<String>();
    ArrayList<String> api_total_successfulforwardpasses = new ArrayList<String>();
    ArrayList<String> api_total_backpasses = new ArrayList<String>();
    ArrayList<String> api_total_successfulbackpasses = new ArrayList<String>();
    ArrayList<String> api_total_throughpasses = new ArrayList<String>();
    ArrayList<String> api_total_successfulthroughpasses = new ArrayList<String>();
    ArrayList<String> api_total_keypasses = new ArrayList<String>();
    ArrayList<String> api_total_successfulkeypasses = new ArrayList<String>();
    ArrayList<String> api_total_verticalpasses = new ArrayList<String>();
    ArrayList<String> api_total_successfulverticalpasses = new ArrayList<String>();
    ArrayList<String> api_total_longpasses = new ArrayList<String>();
    ArrayList<String> api_total_successfullongpasses = new ArrayList<String>();
    ArrayList<String> api_total_dribbles = new ArrayList<String>();
    ArrayList<String> api_total_successfuldribbles = new ArrayList<String>();
    ArrayList<String> api_total_interceptions = new ArrayList<String>();
    ArrayList<String> api_total_defensiveactions = new ArrayList<String>();
    ArrayList<String> api_total_successfuldefensiveactions = new ArrayList<String>();
    ArrayList<String> api_total_attackingactions = new ArrayList<String>();
    ArrayList<String> api_total_successfulattackingactions = new ArrayList<String>();
    ArrayList<String> api_total_freekicks = new ArrayList<String>();
    ArrayList<String> api_total_freekicksontarget = new ArrayList<String>();
    ArrayList<String> api_total_directfreekicks = new ArrayList<String>();
    ArrayList<String> api_total_directfreekicksontarget = new ArrayList<String>();
    ArrayList<String> api_total_corners = new ArrayList<String>();
    ArrayList<String> api_total_successfulpenalties = new ArrayList<String>();
    ArrayList<String> api_total_successfullinkupplays = new ArrayList<String>();
    ArrayList<String> api_total_accelerations = new ArrayList<String>();
    ArrayList<String> api_total_pressingduels = new ArrayList<String>();
    ArrayList<String> api_total_pressingduelswon = new ArrayList<String>();
    ArrayList<String> api_total_looseballduels = new ArrayList<String>();
    ArrayList<String> api_total_looseballduelswon = new ArrayList<String>();
    ArrayList<String> api_total_missedballs = new ArrayList<String>();
    ArrayList<String> api_total_shotassists = new ArrayList<String>();
    ArrayList<String> api_total_shotontargetassists = new ArrayList<String>();
    ArrayList<String> api_total_recoveries = new ArrayList<String>();
    ArrayList<String> api_total_opponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> api_total_dangerousopponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> api_total_losses = new ArrayList<String>();
    ArrayList<String> api_total_ownhalflosses = new ArrayList<String>();
    ArrayList<String> api_total_dangerousownhalflosses = new ArrayList<String>();
    ArrayList<String> api_total_fieldaerialduels = new ArrayList<String>();
    ArrayList<String> api_total_fieldaerialduelswon = new ArrayList<String>();
    ArrayList<String> api_total_gkexits = new ArrayList<String>();
    ArrayList<String> api_total_gksuccessfulexits = new ArrayList<String>();
    ArrayList<String> api_total_gkaerialduels = new ArrayList<String>();
    ArrayList<String> api_total_gkaerialduelswon = new ArrayList<String>();
    ArrayList<String> api_total_gksaves = new ArrayList<String>();
    ArrayList<String> api_total_xgshot = new ArrayList<String>();
    ArrayList<String> api_total_xgshotagainst = new ArrayList<String>();
    ArrayList<String> api_total_ppda = new ArrayList<String>();
    ArrayList<String> api_total_receivedpass = new ArrayList<String>();
    ArrayList<String> api_total_touchinbox = new ArrayList<String>();
    ArrayList<String> api_total_progressiverun = new ArrayList<String>();
    ArrayList<String> api_total_concededgoals = new ArrayList<String>();
    ArrayList<String> api_total_opponentoffsides = new ArrayList<String>();
    ArrayList<String> api_total_shotsagainst = new ArrayList<String>();
    ArrayList<String> api_total_gkgoalkicks = new ArrayList<String>();
    ArrayList<String> api_total_gkgoalkickssuccess = new ArrayList<String>();
    ArrayList<String> api_total_shortgoalkicks = new ArrayList<String>();
    ArrayList<String> api_total_longgoalkicks = new ArrayList<String>();
    ArrayList<String> api_total_matchestagged = new ArrayList<String>();
    ArrayList<String> api_total_newduelswon = new ArrayList<String>();
    ArrayList<String> api_total_newdefensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_total_newoffensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_total_newsuccessfuldribbles = new ArrayList<String>();
    ArrayList<String> api_total_lateralpasses = new ArrayList<String>();
    ArrayList<String> api_total_successfullateralpasses = new ArrayList<String>();
    ArrayList<String> api_percent_duelswon = new ArrayList<String>();
    ArrayList<String> api_percent_defensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_offensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_aerialduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_successfulpasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfulsmartpasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfulpassestofinalthird = new ArrayList<String>();
    ArrayList<String> api_percent_successfulcrosses = new ArrayList<String>();
    ArrayList<String> api_percent_successfuldribbles = new ArrayList<String>();
    ArrayList<String> api_percent_shotsontarget = new ArrayList<String>();
    ArrayList<String> api_percent_headshotsontarget = new ArrayList<String>();
    ArrayList<String> api_percent_goalconversion = new ArrayList<String>();
    ArrayList<String> api_percent_yellowcardsperfoul = new ArrayList<String>();
    ArrayList<String> api_percent_directfreekicksontarget = new ArrayList<String>();
    ArrayList<String> api_percent_penaltiesconversion = new ArrayList<String>();
    ArrayList<String> api_percent_win = new ArrayList<String>();
    ArrayList<String> api_percent_successfulforwardpasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfulbackpasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfulthroughpasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfulkeypasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfulverticalpasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfullongpasses = new ArrayList<String>();
    ArrayList<String> api_percent_successfulshotassists = new ArrayList<String>();
    ArrayList<String> api_percent_successfullinkupplays = new ArrayList<String>();
    ArrayList<String> api_percent_fieldaerialduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_gksaves = new ArrayList<String>();
    ArrayList<String> api_percent_gksuccessfulexits = new ArrayList<String>();
    ArrayList<String> api_percent_gkaerialduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_successfultouchinbox = new ArrayList<String>();
    ArrayList<String> api_percent_newduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_newdefensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_newoffensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_percent_newsuccessfuldribbles = new ArrayList<String>();
    ArrayList<String> api_percent_successfullateralpasses = new ArrayList<String>();
    ArrayList<String> api_average_possessionpercent = new ArrayList<String>();
    ArrayList<String> api_average_duels = new ArrayList<String>();
    ArrayList<String> api_average_defensiveduels = new ArrayList<String>();
    ArrayList<String> api_average_offensiveduels = new ArrayList<String>();
    ArrayList<String> api_average_aerialduels = new ArrayList<String>();
    ArrayList<String> api_average_fouls = new ArrayList<String>();
    ArrayList<String> api_average_goals = new ArrayList<String>();
    ArrayList<String> api_average_assists = new ArrayList<String>();
    ArrayList<String> api_average_passes = new ArrayList<String>();
    ArrayList<String> api_average_smartpasses = new ArrayList<String>();
    ArrayList<String> api_average_passestofinalthird = new ArrayList<String>();
    ArrayList<String> api_average_crosses = new ArrayList<String>();
    ArrayList<String> api_average_dribbles = new ArrayList<String>();
    ArrayList<String> api_average_shots = new ArrayList<String>();
    ArrayList<String> api_average_headshots = new ArrayList<String>();
    ArrayList<String> api_average_interceptions = new ArrayList<String>();
    ArrayList<String> api_average_successfuldefensiveaction = new ArrayList<String>();
    ArrayList<String> api_average_yellowcards = new ArrayList<String>();
    ArrayList<String> api_average_redcards = new ArrayList<String>();
    ArrayList<String> api_average_directredcards = new ArrayList<String>();
    ArrayList<String> api_average_successfulattackingactions = new ArrayList<String>();
    ArrayList<String> api_average_freekicks = new ArrayList<String>();
    ArrayList<String> api_average_directfreekicks = new ArrayList<String>();
    ArrayList<String> api_average_corners = new ArrayList<String>();
    ArrayList<String> api_average_penalties = new ArrayList<String>();
    ArrayList<String> api_average_passlength = new ArrayList<String>();
    ArrayList<String> api_average_longpasslength = new ArrayList<String>();
    ArrayList<String> api_average_dribbledistancefromopponentgoal = new ArrayList<String>();
    ArrayList<String> api_average_accelerations = new ArrayList<String>();
    ArrayList<String> api_average_looseballduels = new ArrayList<String>();
    ArrayList<String> api_average_missedballs = new ArrayList<String>();
    ArrayList<String> api_average_forwardpasses = new ArrayList<String>();
    ArrayList<String> api_average_backpasses = new ArrayList<String>();
    ArrayList<String> api_average_throughpasses = new ArrayList<String>();
    ArrayList<String> api_average_keypasses = new ArrayList<String>();
    ArrayList<String> api_average_verticalpasses = new ArrayList<String>();
    ArrayList<String> api_average_longpasses = new ArrayList<String>();
    ArrayList<String> api_average_shotassists = new ArrayList<String>();
    ArrayList<String> api_average_shotontargetassists = new ArrayList<String>();
    ArrayList<String> api_average_linkupplays = new ArrayList<String>();
    ArrayList<String> api_average_ballrecoveries = new ArrayList<String>();
    ArrayList<String> api_average_opponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> api_average_dangerousopponenthalfrecoveries = new ArrayList<String>();
    ArrayList<String> api_average_balllosses = new ArrayList<String>();
    ArrayList<String> api_average_ownhalflosses = new ArrayList<String>();
    ArrayList<String> api_average_dangerousownhalflosses = new ArrayList<String>();
    ArrayList<String> api_average_fieldaerialduels = new ArrayList<String>();
    ArrayList<String> api_average_concededgoals = new ArrayList<String>();
    ArrayList<String> api_average_gkexits = new ArrayList<String>();
    ArrayList<String> api_average_gkaerialduels = new ArrayList<String>();
    ArrayList<String> api_average_duelswon = new ArrayList<String>();
    ArrayList<String> api_average_defensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_average_offensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_average_successfulpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfulsmartpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfulpassestofinalthird = new ArrayList<String>();
    ArrayList<String> api_average_successfulcrosses = new ArrayList<String>();
    ArrayList<String> api_average_successfulforwardpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfulbackpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfulthroughpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfulkeypasses = new ArrayList<String>();
    ArrayList<String> api_average_successfulverticalpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfullongpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfuldribbles = new ArrayList<String>();
    ArrayList<String> api_average_defensiveactions = new ArrayList<String>();
    ArrayList<String> api_average_attackingactions = new ArrayList<String>();
    ArrayList<String> api_average_freekicksontarget = new ArrayList<String>();
    ArrayList<String> api_average_directfreekicksontarget = new ArrayList<String>();
    ArrayList<String> api_average_successfulpenalties = new ArrayList<String>();
    ArrayList<String> api_average_successfullinkupplays = new ArrayList<String>();
    ArrayList<String> api_average_looseballduelswon = new ArrayList<String>();
    ArrayList<String> api_average_fieldaerialduelswon = new ArrayList<String>();
    ArrayList<String> api_average_gksuccessfulexits = new ArrayList<String>();
    ArrayList<String> api_average_gkaerialduelswon = new ArrayList<String>();
    ArrayList<String> api_average_gksaves = new ArrayList<String>();
    ArrayList<String> api_average_xgshot = new ArrayList<String>();
    ArrayList<String> api_average_xgshotagainst = new ArrayList<String>();
    ArrayList<String> api_average_receivedpass = new ArrayList<String>();
    ArrayList<String> api_average_touchinbox = new ArrayList<String>();
    ArrayList<String> api_average_progressiverun = new ArrayList<String>();
    ArrayList<String> api_average_offsides = new ArrayList<String>();
    ArrayList<String> api_average_opponentoffsides = new ArrayList<String>();
    ArrayList<String> api_average_shotsagainst = new ArrayList<String>();
    ArrayList<String> api_average_gkgoalkicks = new ArrayList<String>();
    ArrayList<String> api_average_gkgoalkickssuccess = new ArrayList<String>();
    ArrayList<String> api_average_shortgoalkicks = new ArrayList<String>();
    ArrayList<String> api_average_longgoalkicks = new ArrayList<String>();
    ArrayList<String> api_average_newduelswon = new ArrayList<String>();
    ArrayList<String> api_average_newdefensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_average_newoffensiveduelswon = new ArrayList<String>();
    ArrayList<String> api_average_newsuccessfuldribbles = new ArrayList<String>();
    ArrayList<String> api_average_lateralpasses = new ArrayList<String>();
    ArrayList<String> api_average_successfullateralpasses = new ArrayList<String>();
    ArrayList<String> api_roundid = new ArrayList<String>();
}