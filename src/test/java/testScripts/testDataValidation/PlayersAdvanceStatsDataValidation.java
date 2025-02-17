package testScripts.testDataValidation;


import baseConfig.BaseClass;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.playerAdvanceStatsResponse.PlayerAdavanceStatsResponse;
import utilities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Tag("SanityTest")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayersAdvanceStatsDataValidation extends BaseClass {


    private static final Logger logger = LogManager.getLogger(PlayersAdvanceStatsDataValidation.class);

    @Test
    @Title("Verify Players Advance Stats Table Sample Data")
    @Tag("PlayersAdvanceStats")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public void playersAdvanceStatsTableSampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;

        logger.info("Predefined Competition IDs: " + GlbVar.staticAdvanceCompetitionIDList);
        String[] configTeamAdvanceCompetitionCompetitionId = GlbVar.staticAdvanceCompetitionIDList.split(",\\s*");
        int competitionIDSize = GlbVar.competitionIdSize.equals("all") ? configTeamAdvanceCompetitionCompetitionId.length :
                Integer.parseInt(GlbVar.teamAdvanceCompetitionIDSize);
        TeamsDataValidation teamsDataValidation = new TeamsDataValidation();
        List<String> teamID = teamsDataValidation.getTeamIDByStaticCompetitionID(GlbVar.staticAdvanceCompetitionIDList, competitionIDSize);
        SquadDataValidationCopy squadDataValidation=new SquadDataValidationCopy();
        List<String> playerID=squadDataValidation.getPlayerID(teamID,1);
        logger.info("Fetching data for following competitionID: ");
        PlayerAdavanceStatsResponse playerAdavanceStatsResponse;
        for (int h = 0; h < GlbVar.teamAdvanceCompetitionIDSize.length(); h++) {
            logger.info("Competion ID is:"+ configTeamAdvanceCompetitionCompetitionId[h]);
            for (int i = 0; i < playerID.size(); i++) {
                String competition_endPoint = "/v3/players/" + playerID.get(i) + "/advancedstats?compId=" + configTeamAdvanceCompetitionCompetitionId[h];
//                String competition_endPoint = "/v3/players/506638/advancedstats?compId=" + configTeamAdvanceCompetitionCompetitionId[h];
                logger.info("Player Advance Stats End Points: "+competition_endPoint);
                response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
                Thread.sleep(2000);
                playerAdavanceStatsResponse = response.as(PlayerAdavanceStatsResponse.class);
                if (response.getStatusCode() == 200 && !(playerAdavanceStatsResponse.getPositions().size()<1)) {
                    logger.info("Valid Player Advance Stats End Points: "+competition_endPoint);
                    for(int j=0;j<playerAdavanceStatsResponse.getPositions().size();j++) {
                        api_playerid.add(String.valueOf(playerAdavanceStatsResponse.getPlayerId()));
                        api_competitionid.add(String.valueOf(playerAdavanceStatsResponse.getCompetitionId()));
                        api_seasonid.add(String.valueOf(playerAdavanceStatsResponse.getSeasonId()));
                        api_position_name.add(String.valueOf(playerAdavanceStatsResponse.getPositions().get(j).getPosition().getName()));
                        api_position_code.add(String.valueOf(playerAdavanceStatsResponse.getPositions().get(j).getPosition().getCode()));
                        api_position_percent.add(String.valueOf(playerAdavanceStatsResponse.getPositions().get(j).getPercent()));

                        api_total_matches.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getMatches()));
                        api_total_matches_in_start.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getMatchesInStart()));
                        api_total_matches_substituted.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getMatchesSubstituted()));
                        api_total_matches_coming_off.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getMatchesComingOff()));
                        api_total_minutes_on_field.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getMinutesOnField()));
                        api_total_minutes_tagged.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getMinutesTagged()));
                        api_total_goals.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGoals()));
                        api_total_assists.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getAssists()));
                        api_total_shots.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getShots()));
                        api_total_head_shots.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getHeadShots()));
                        api_total_yellow_cards.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getYellowCards()));
                        api_total_red_cards.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getRedCards()));
                        api_total_direct_red_cards.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDirectRedCards()));
                        api_total_penalties.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getPenalties()));
                        api_total_linkup_plays.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getLinkupPlays()));
                        api_total_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDuels()));
                        api_total_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDuelsWon()));
                        api_total_defensive_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDefensiveDuels()));
                        api_total_defensive_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDefensiveDuelsWon()));
                        api_total_offensive_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getOffensiveDuels()));
                        api_total_offensive_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getOffensiveDuelsWon()));
                        api_total_aerial_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getAerialDuels()));
                        api_total_aerial_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getAerialDuelsWon()));
                        api_total_fouls.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getFouls()));
                        api_total_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getPasses()));
                        api_total_successful_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulPasses()));
                        api_total_smart_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSmartPasses()));
                        api_total_successful_smart_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulSmartPasses()));
                        api_total_passes_to_final_third.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getPassesToFinalThird()));
                        api_total_successful_passes_to_final_third.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulPassesToFinalThird()));
                        api_total_crosses.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getCrosses()));
                        api_total_successful_crosses.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulCrosses()));
                        api_total_forward_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getForwardPasses()));
                        api_total_successful_forward_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulForwardPasses()));
                        api_total_back_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getBackPasses()));
                        api_total_successful_back_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulBackPasses()));
                        api_total_through_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getThroughPasses()));
                        api_total_successful_through_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulThroughPasses()));
                        api_total_key_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getKeyPasses()));
                        api_total_successful_key_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulKeyPasses()));
                        api_total_vertical_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getVerticalPasses()));
                        api_total_successful_vertical_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulVerticalPasses()));
                        api_total_long_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getLongPasses()));
                        api_total_successful_long_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulLongPasses()));
                        api_total_dribbles.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDribbles()));
                        api_total_successful_dribbles.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulDribbles()));
                        api_total_interceptions.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getInterceptions()));
                        api_total_defensive_actions.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDefensiveActions()));
                        api_total_successful_defensive_action.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulDefensiveAction()));
                        api_total_attacking_actions.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getAttackingActions()));
                        api_total_successful_attacking_actions.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulAttackingActions()));
                        api_total_free_kicks.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getFreeKicks()));
                        api_total_free_kicks_on_target.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getFreeKicksOnTarget()));
                        api_total_direct_free_kicks.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDirectFreeKicks()));
                        api_total_direct_free_kicks_on_target.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDirectFreeKicksOnTarget()));
                        api_total_corners.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getCorners()));
                        api_total_successful_penalties.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulPenalties()));
                        api_total_successful_linkup_plays.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulLinkupPlays()));
                        api_total_accelerations.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getAccelerations()));
                        api_total_pressing_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getPressingDuels()));
                        api_total_pressing_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getPressingDuelsWon()));
                        api_total_loose_ball_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getLooseBallDuels()));
                        api_total_loose_ball_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getLooseBallDuelsWon()));
                        api_total_missed_balls.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getMissedBalls()));
                        api_total_shot_assists.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getShotAssists()));
                        api_total_shot_on_target_assists.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getShotOnTargetAssists()));
                        api_total_recoveries.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getRecoveries()));
                        api_total_opponent_half_recoveries.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getOpponentHalfRecoveries()));
                        api_total_dangerous_opponent_half_recoveries.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDangerousOpponentHalfRecoveries()));
                        api_total_losses.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getLosses()));
                        api_total_own_half_losses.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getOwnHalfLosses()));
                        api_total_dangerous_own_half_losses.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDangerousOwnHalfLosses()));
                        api_total_xg_shot.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getXgShot()));
                        api_total_xg_assist.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getXgAssist()));
                        api_total_xg_save.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getXgSave()));
                        api_total_received_pass.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getReceivedPass()));
                        api_total_touch_in_box.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getTouchInBox()));
                        api_total_progressive_run.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getProgressiveRun()));
                        api_total_offsides.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getOffsides()));
                        api_total_clearances.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getClearances()));
                        api_total_second_assists.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSecondAssists()));
                        api_total_third_assists.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getThirdAssists()));
                        api_total_shots_blocked.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getShotsBlocked()));
                        api_total_fouls_suffered.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getFoulsSuffered()));
                        api_total_progressive_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getProgressivePasses()));
                        api_total_counterpressing_recoveries.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getCounterpressingRecoveries()));
                        api_total_sliding_tackles.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSlidingTackles()));
                        api_total_goal_kicks.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGoalKicks()));
                        api_total_dribbles_against.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDribblesAgainst()));
                        api_total_dribbles_against_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getDribblesAgainstWon()));
                        api_total_goal_kicks_short.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGoalKicksShort()));
                        api_total_goal_kicks_long.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGoalKicksLong()));
                        api_total_shots_on_target.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getShotsOnTarget()));
                        api_total_successful_progressive_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulProgressivePasses()));
                        api_total_successful_sliding_tackles.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulSlidingTackles()));
                        api_total_successful_goal_kicks.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulGoalKicks()));
                        api_total_field_aerial_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getFieldAerialDuels()));
                        api_total_field_aerial_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getFieldAerialDuelsWon()));
                        api_total_gk_clean_sheets.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkCleanSheets()));
                        api_total_gk_conceded_goals.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkConcededGoals()));
                        api_total_gk_shots_against.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkShotsAgainst()));
                        api_total_gk_exits.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkExits()));
                        api_total_gk_successful_exits.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkSuccessfulExits()));
                        api_total_gk_aerial_duels.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkAerialDuels()));
                        api_total_gk_aerial_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkAerialDuelsWon()));
                        api_total_gk_saves.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getGkSaves()));
                        api_total_new_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getNewDuelsWon()));
                        api_total_new_defensive_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getNewDefensiveDuelsWon()));
                        api_total_new_offensive_duels_won.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getNewOffensiveDuelsWon()));
                        api_total_new_successful_dribbles.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getNewSuccessfulDribbles()));
                        api_total_lateral_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getLateralPasses()));
                        api_total_successful_lateral_passes.add(String.valueOf(playerAdavanceStatsResponse.getTotal().getSuccessfulLateralPasses()));

                        api_average_passlength.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getPassLength()));
                        api_average_longpasslength.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getLongPassLength()));
                        api_average_dribbledistancefromopponentgoal.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDribbleDistanceFromOpponentGoal()));
                        api_average_ballrecoveries.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getBallRecoveries()));
                        api_average_duels.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDuels()));
                        api_average_defensiveduels.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDefensiveDuels()));
                        api_average_offensiveduels.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getOffensiveDuels()));
                        api_average_aerialduels.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getAerialDuels()));
                        api_average_fouls.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getFouls()));
                        api_average_goals.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGoals()));
                        api_average_assists.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getAssists()));
                        api_average_passes.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getPasses()));
                        api_average_smartpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSmartPasses()));
                        api_average_passestofinalthird.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getPassesToFinalThird()));
                        api_average_crosses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getCrosses()));
                        api_average_dribbles.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDribbles()));
                        api_average_shots.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getShots()));
                        api_average_headshots.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getHeadShots()));
                        api_average_interceptions.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getInterceptions()));
                        api_average_successfuldefensiveaction.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulDefensiveAction()));
                        api_average_yellowcards.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getYellowCards()));
                        api_average_redcards.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getRedCards()));
                        api_average_directredcards.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDirectRedCards()));
                        api_average_successfulattackingactions.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulAttackingActions()));
                        api_average_freekicks.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getFreeKicks()));
                        api_average_directfreekicks.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDirectFreeKicks()));
                        api_average_corners.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getCorners()));
                        api_average_penalties.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getPenalties()));
                        api_average_accelerations.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getAccelerations()));
                        api_average_looseballduels.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getLooseBallDuels()));
                        api_average_missedballs.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getMissedBalls()));
                        api_average_forwardpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getForwardPasses()));
                        api_average_backpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getBackPasses()));
                        api_average_throughpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getThroughPasses()));
                        api_average_keypasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getKeyPasses()));
                        api_average_verticalpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getVerticalPasses()));
                        api_average_longpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getLongPasses()));
                        api_average_shotassists.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getShotAssists()));
                        api_average_shotontargetassists.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getShotOnTargetAssists()));
                        api_average_linkupplays.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getLinkupPlays()));
                        api_average_opponenthalfrecoveries.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getOpponentHalfRecoveries()));
                        api_average_dangerousopponenthalfrecoveries.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDangerousOpponentHalfRecoveries()));






                        api_average_balllosses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getBallLosses()));
                        api_average_losses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getLosses()));
                        api_average_ownhalflosses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getOwnHalfLosses()));
                        api_average_dangerousownhalflosses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDangerousOwnHalfLosses()));
                        api_average_duelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDuelsWon()));
                        api_average_defensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDefensiveDuelsWon()));
                        api_average_offensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getOffensiveDuelsWon()));
                        api_average_successfulpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulPasses()));
                        api_average_successfulsmartpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulSmartPasses()));
                        api_average_successfulcrosses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulCrosses()));
                        api_average_successfulforwardpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulForwardPasses()));
                        api_average_successfulbackpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulBackPasses()));
                        api_average_successfulthroughpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulThroughPasses()));
                        api_average_successfulkeypasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulKeyPasses()));
                        api_average_successfulverticalpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulVerticalPasses()));
                        api_average_successfullongpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulLongPasses()));
                        api_average_successfuldribbles.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulDribbles()));
                        api_average_defensiveactions.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDefensiveActions()));
                        api_average_attackingactions.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getAttackingActions()));
                        api_average_freekicksontarget.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getFreeKicksOnTarget()));
                        api_average_directfreekicksontarget.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDirectFreeKicksOnTarget()));
                        api_average_successfulpenalties.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulPenalties()));
                        api_average_successfullinkupplays.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulLinkupPlays()));
                        api_average_looseballduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getLooseBallDuelsWon()));
                        api_average_successfulpassestofinalthird.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulPassesToFinalThird()));
                        api_average_xgshot.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getXgShot()));
                        api_average_xgassist.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getXgAssist()));
                        api_average_xgsave.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getXgSave()));
                        api_average_receivedpass.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getReceivedPass()));
                        api_average_touchinbox.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getTouchInBox()));
                        api_average_progressiverun.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getProgressiveRun()));
                        api_average_offsides.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getOffsides()));
                        api_average_clearances.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getClearances()));
                        api_average_secondassists.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSecondAssists()));
                        api_average_thirdassists.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getThirdAssists()));
                        api_average_foulssuffered.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getFoulsSuffered()));
                        api_average_progressivepasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getProgressivePasses()));
                        api_average_counterpressingrecoveries.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getCounterpressingRecoveries()));
                        api_average_slidingtackles.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSlidingTackles()));
                        api_average_goalkicks.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGoalKicks()));
                        api_average_shotsblocked.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getShotsBlocked()));
                        api_average_shotsontarget.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getShotsOnTarget()));
                        api_average_successfulprogressivepasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulProgressivePasses()));
                        api_average_successfulslidingtackles.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulSlidingTackles()));
                        api_average_successfulgoalkicks.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulGoalKicks()));
                        api_average_dribblesagainst.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDribblesAgainst()));
                        api_average_dribblesagainstwon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getDribblesAgainstWon()));
                        api_average_goalkicksshort.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGoalKicksShort()));
                        api_average_goalkickslong.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGoalKicksLong()));
                        api_average_fieldaerialduels.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getFieldAerialDuels()));
                        api_average_fieldaerialduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getFieldAerialDuelsWon()));
                        api_average_gkconcededgoals.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGkConcededGoals()));
                        api_average_gkshotsagainst.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGkShotsAgainst()));
                        api_average_gkexits.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGkExits()));
                        api_average_gkaerialduels.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGkAerialDuels()));
                        api_average_gksaves.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGkSaves()));
                        api_average_gksuccessfulexits.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGkSuccessfulExits()));
                        api_average_gkaerialduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getGkAerialDuelsWon()));
                        api_average_newduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getNewDuelsWon()));
                        api_average_newdefensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getNewDefensiveDuelsWon()));
                        api_average_newoffensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getNewOffensiveDuelsWon()));
                        api_average_newsuccessfuldribbles.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getNewSuccessfulDribbles()));
                        api_average_lateralpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getLateralPasses()));
                        api_average_successfullateralpasses.add(String.valueOf(playerAdavanceStatsResponse.getAverage().getSuccessfulLateralPasses()));






                        api_percent_duelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getDuelsWon()));
                        api_percent_defensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getDefensiveDuelsWon()));
                        api_percent_offensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getOffensiveDuelsWon()));
                        api_percent_aerialduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getAerialDuelsWon()));
                        api_percent_successfulpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulPasses()));
                        api_percent_successfulsmartpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulSmartPasses()));
                        api_percent_successfulpassestofinalthird.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulPassesToFinalThird()));
                        api_percent_successfulcrosses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulCrosses()));
                        api_percent_successfuldribbles.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulDribbles()));
                        api_percent_shotsontarget.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getShotsOnTarget()));
                        api_percent_headshotsontarget.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getHeadShotsOnTarget()));
                        api_percent_goalconversion.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getGoalConversion()));
                        api_percent_directfreekicksontarget.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getDirectFreeKicksOnTarget()));
                        api_percent_penaltiesconversion.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getPenaltiesConversion()));
                        api_percent_win.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getWin()));
                        api_percent_successfulforwardpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulForwardPasses()));
                        api_percent_successfulbackpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulBackPasses()));
                        api_percent_successfulthroughpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulThroughPasses()));
                        api_percent_successfulkeypasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulKeyPasses()));
                        api_percent_successfulverticalpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulVerticalPasses()));
                        api_percent_successfullongpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulLongPasses()));
                        api_percent_successfulshotassists.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulShotAssists()));
                        api_percent_successfullinkupplays.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulLinkupPlays()));
                        api_percent_yellowcardsperfoul.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getYellowCardsPerFoul()));
                        api_percent_successfulprogressivepasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulProgressivePasses()));
                        api_percent_successfulslidingtackles.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulSlidingTackles()));
                        api_percent_successfulgoalkicks.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulGoalKicks()));
                        api_percent_dribblesagainstwon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getDribblesAgainstWon()));
                        api_percent_fieldaerialduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getFieldAerialDuelsWon()));
                        api_percent_gksaves.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getGkSaves()));
                        api_percent_gksuccessfulexits.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getGkSuccessfulExits()));
                        api_percent_gkaerialduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getGkAerialDuelsWon()));
                        api_percent_newduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getNewDuelsWon()));
                        api_percent_newdefensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getNewDefensiveDuelsWon()));
                        api_percent_newoffensiveduelswon.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getNewOffensiveDuelsWon()));
                        api_percent_newsuccessfuldribbles.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getNewSuccessfulDribbles()));
                        api_percent_successfullateralpasses.add(String.valueOf(playerAdavanceStatsResponse.getPercent().getSuccessfulLateralPasses()));
                        api_roundid.add(String.valueOf(playerAdavanceStatsResponse.getRoundId()));
                    }
                }
            }
        }

        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tableName = GlbVar.player_advancestats_tablename;
        String whereClauseColumnName1="competitionid";
        String whereClauseColumnName2="playerid";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tableName);
        logger.info("DB column names: " + columnNames);
        db_playerid=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClause(catalogName, schemaName, tableName, columnNames.get(0),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_competitionid=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClause(catalogName, schemaName, tableName, columnNames.get(1),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_seasonid=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClause(catalogName, schemaName, tableName, columnNames.get(2),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_position_name=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClause(catalogName, schemaName, tableName, columnNames.get(3),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_position_code=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClause(catalogName, schemaName, tableName, columnNames.get(4),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));

        db_position_percent=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(5),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_matches=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(6),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_matches_in_start=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(7),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_matches_substituted=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(8),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_matches_coming_off=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(9),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_minutes_on_field=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(10),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_minutes_tagged=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(11),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_goals=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(12),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_assists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(13),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_shots=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(14),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_head_shots=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(15),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_yellow_cards=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(16),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_red_cards=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(17),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_direct_red_cards=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(18),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_penalties=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(19),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_linkup_plays=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(20),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(21),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(22),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_defensive_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(23),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_defensive_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(24),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_offensive_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(25),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_offensive_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(26),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_aerial_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(27),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_aerial_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(28),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_fouls=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(29),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(30),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(31),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_smart_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(32),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_smart_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(33),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_passes_to_final_third=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(34),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_passes_to_final_third=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(35),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_crosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(36),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_crosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(37),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_forward_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(38),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_forward_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(39),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_back_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(40),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_back_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(41),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_through_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(42),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_through_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(43),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_key_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(44),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_key_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(45),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_vertical_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(46),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_vertical_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(47),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_long_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(48),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_long_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(49),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_dribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(50),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_dribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(51),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_interceptions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(52),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_defensive_actions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(53),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_defensive_action=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(54),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_attacking_actions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(55),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_attacking_actions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(56),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_free_kicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(57),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_free_kicks_on_target=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(58),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_direct_free_kicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(59),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_direct_free_kicks_on_target=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(60),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_corners=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(61),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_penalties=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(62),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_linkup_plays=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(63),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_accelerations=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(64),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_pressing_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(65),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_pressing_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(66),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_loose_ball_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(67),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_loose_ball_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(68),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_missed_balls=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(69),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_shot_assists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(70),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_shot_on_target_assists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(71),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_recoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(72),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_opponent_half_recoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(73),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_dangerous_opponent_half_recoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(74),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_losses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(75),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_own_half_losses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(76),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_dangerous_own_half_losses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(77),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_xg_shot=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(78),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_xg_assist=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(79),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_xg_save=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(80),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_received_pass=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(81),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_touch_in_box=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(82),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_progressive_run=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(83),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_offsides=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(84),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_clearances=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(85),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_second_assists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(86),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_third_assists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(87),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_shots_blocked=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(88),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_fouls_suffered=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(89),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_progressive_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(90),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_counterpressing_recoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(91),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_sliding_tackles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(92),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_goal_kicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(93),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_dribbles_against=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(94),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_dribbles_against_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(95),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_goal_kicks_short=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(96),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_goal_kicks_long=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(97),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_shots_on_target=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(98),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_progressive_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(99),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_sliding_tackles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(100),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_goal_kicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(101),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_field_aerial_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(102),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_field_aerial_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(103),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_clean_sheets=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(104),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_conceded_goals=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(105),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_shots_against=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(106),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_exits=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(107),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_successful_exits=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(108),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_aerial_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(109),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_aerial_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(110),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_gk_saves=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(111),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_new_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(112),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_new_defensive_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(113),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_new_offensive_duels_won=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(114),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_new_successful_dribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(115),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_lateral_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(116),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_total_successful_lateral_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(117),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_passlength=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(118),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_longpasslength=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(119),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_dribbledistancefromopponentgoal=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(120),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_ballrecoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(121),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_duels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(122),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_defensiveduels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(123),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_offensiveduels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(124),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_aerialduels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(125),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_fouls=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(126),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_goals=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(127),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_assists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(128),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_passes=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(129),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_smartpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(130),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_passestofinalthird=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(131),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_crosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(132),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_dribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(133),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_shots=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(134),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_headshots=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(135),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_interceptions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(136),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfuldefensiveaction=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(137),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_yellowcards=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(138),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_redcards=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(139),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_directredcards=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(140),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulattackingactions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(141),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_freekicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(142),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_directfreekicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(143),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_corners=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(144),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_penalties=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(145),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_accelerations=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(146),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_looseballduels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(147),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_missedballs=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(148),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_forwardpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(149),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_backpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(150),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_throughpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(151),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_keypasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(152),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_verticalpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(153),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_longpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(154),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_shotassists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(155),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_shotontargetassists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(156),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_linkupplays=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(157),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_opponenthalfrecoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(158),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_dangerousopponenthalfrecoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(159),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_balllosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(160),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_losses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(161),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_ownhalflosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(162),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_dangerousownhalflosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(163),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_duelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(164),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_defensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(165),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_offensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(166),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(167),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulsmartpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(168),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulcrosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(169),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulforwardpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(170),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulbackpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(171),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulthroughpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(172),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulkeypasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(173),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulverticalpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(174),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfullongpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(175),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfuldribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(176),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_defensiveactions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(177),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_attackingactions=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(178),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_freekicksontarget=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(179),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_directfreekicksontarget=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(180),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulpenalties=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(181),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfullinkupplays=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(182),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_looseballduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(183),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulpassestofinalthird=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(184),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_xgshot=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(185),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_xgassist=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(186),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_xgsave=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(187),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_receivedpass=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(188),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_touchinbox=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(189),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_progressiverun=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(190),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_offsides=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(191),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_clearances=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(192),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_secondassists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(193),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_thirdassists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(194),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_foulssuffered=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(195),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_progressivepasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(196),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_counterpressingrecoveries=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(197),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_slidingtackles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(198),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_goalkicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(199),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_shotsblocked=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(200),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_shotsontarget=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(201),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulprogressivepasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(202),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulslidingtackles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(203),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfulgoalkicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(204),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_dribblesagainst=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(205),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_dribblesagainstwon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(206),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_goalkicksshort=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(207),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_goalkickslong=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(208),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_fieldaerialduels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(209),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_fieldaerialduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(210),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_gkconcededgoals=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(211),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_gkshotsagainst=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(212),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_gkexits=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(213),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_gkaerialduels=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(214),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_gksaves=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(215),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_gksuccessfulexits=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(216),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_gkaerialduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(217),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_newduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(218),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_newdefensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(219),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_newoffensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(220),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_newsuccessfuldribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(221),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_lateralpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(222),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_average_successfullateralpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(223),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_duelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(224),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_defensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(225),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_offensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(226),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_aerialduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(227),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(228),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulsmartpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(229),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulpassestofinalthird=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(230),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulcrosses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(231),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfuldribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(232),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_shotsontarget=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(233),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_headshotsontarget=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(234),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_goalconversion=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(235),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_directfreekicksontarget=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(236),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_penaltiesconversion=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(237),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_win=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(238),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulforwardpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(239),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulbackpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(240),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulthroughpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(241),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulkeypasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(242),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulverticalpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(243),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfullongpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(244),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulshotassists=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(245),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfullinkupplays=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(246),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_yellowcardsperfoul=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(247),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulprogressivepasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(248),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulslidingtackles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(249),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfulgoalkicks=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(250),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_dribblesagainstwon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(251),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_fieldaerialduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(252),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_gksaves=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(253),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_gksuccessfulexits=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(254),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_gkaerialduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(255),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_newduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(256),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_newdefensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(257),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_newoffensiveduelswon=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(258),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_newsuccessfuldribbles=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(259),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_percent_successfullateralpasses=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(260),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        db_roundid=tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQueryWithTwoClauseAndWithDecimalCasting(catalogName, schemaName, tableName, columnNames.get(261),
                        whereClauseColumnName1, String.valueOf(GenericFun.removeDuplicateDataFromList(api_competitionid)), whereClauseColumnName2, String.valueOf(GenericFun.removeDuplicateDataFromList(api_playerid))));
        //Assertions
        logger.info("Executing assertion for the  db_playerid column");
        AssertHelpers.softAssertTrue( api_playerid,  db_playerid);
        logger.info("Executing assertion for the  db_competitionid column");
        AssertHelpers.softAssertTrue( api_competitionid,  db_competitionid);
        logger.info("Executing assertion for the  db_seasonid column");
        AssertHelpers.softAssertTrue( api_seasonid,  db_seasonid);
        logger.info("Executing assertion for the  db_position_name column");
        AssertHelpers.softAssertTrue( api_position_name,  db_position_name);
        logger.info("Executing assertion for the  db_position_code column");
        AssertHelpers.softAssertTrue( api_position_code,  db_position_code);
        logger.info("Executing assertion for the  db_position_percent column");
        AssertHelpers.softAssertTrue( api_position_percent,  db_position_percent);
        logger.info("Executing assertion for the  db_total_matches column");
        AssertHelpers.softAssertTrue( api_total_matches,  db_total_matches);
        logger.info("Executing assertion for the  db_total_matches_in_start column");
        AssertHelpers.softAssertTrue( api_total_matches_in_start,  db_total_matches_in_start);
        logger.info("Executing assertion for the  db_total_matches_substituted column");
        AssertHelpers.softAssertTrue( api_total_matches_substituted,  db_total_matches_substituted);
        logger.info("Executing assertion for the  db_total_matches_coming_off column");
        AssertHelpers.softAssertTrue( api_total_matches_coming_off,  db_total_matches_coming_off);
        logger.info("Executing assertion for the  db_total_minutes_on_field column");
        AssertHelpers.softAssertTrue( api_total_minutes_on_field,  db_total_minutes_on_field);
        logger.info("Executing assertion for the  db_total_minutes_tagged column");
        AssertHelpers.softAssertTrue( api_total_minutes_tagged,  db_total_minutes_tagged);
        logger.info("Executing assertion for the  db_total_goals column");
        AssertHelpers.softAssertTrue( api_total_goals,  db_total_goals);
        logger.info("Executing assertion for the  db_total_assists column");
        AssertHelpers.softAssertTrue( api_total_assists,  db_total_assists);
        logger.info("Executing assertion for the  db_total_shots column");
        AssertHelpers.softAssertTrue( api_total_shots,  db_total_shots);
        logger.info("Executing assertion for the  db_total_head_shots column");
        AssertHelpers.softAssertTrue( api_total_head_shots,  db_total_head_shots);
        logger.info("Executing assertion for the  db_total_yellow_cards column");
        AssertHelpers.softAssertTrue( api_total_yellow_cards,  db_total_yellow_cards);
        logger.info("Executing assertion for the  db_total_red_cards column");
        AssertHelpers.softAssertTrue( api_total_red_cards,  db_total_red_cards);
        logger.info("Executing assertion for the  db_total_direct_red_cards column");
        AssertHelpers.softAssertTrue( api_total_direct_red_cards,  db_total_direct_red_cards);
        logger.info("Executing assertion for the  db_total_penalties column");
        AssertHelpers.softAssertTrue( api_total_penalties,  db_total_penalties);
        logger.info("Executing assertion for the  db_total_linkup_plays column");
        AssertHelpers.softAssertTrue( api_total_linkup_plays,  db_total_linkup_plays);
        logger.info("Executing assertion for the  db_total_duels column");
        AssertHelpers.softAssertTrue( api_total_duels,  db_total_duels);
        logger.info("Executing assertion for the  db_total_duels_won column");
        AssertHelpers.softAssertTrue( api_total_duels_won,  db_total_duels_won);
        logger.info("Executing assertion for the  db_total_defensive_duels column");
        AssertHelpers.softAssertTrue( api_total_defensive_duels,  db_total_defensive_duels);
        logger.info("Executing assertion for the  db_total_defensive_duels_won column");
        AssertHelpers.softAssertTrue( api_total_defensive_duels_won,  db_total_defensive_duels_won);
        logger.info("Executing assertion for the  db_total_offensive_duels column");
        AssertHelpers.softAssertTrue( api_total_offensive_duels,  db_total_offensive_duels);
        logger.info("Executing assertion for the  db_total_offensive_duels_won column");
        AssertHelpers.softAssertTrue( api_total_offensive_duels_won,  db_total_offensive_duels_won);
        logger.info("Executing assertion for the  db_total_aerial_duels column");
        AssertHelpers.softAssertTrue( api_total_aerial_duels,  db_total_aerial_duels);
        logger.info("Executing assertion for the  db_total_aerial_duels_won column");
        AssertHelpers.softAssertTrue( api_total_aerial_duels_won,  db_total_aerial_duels_won);
        logger.info("Executing assertion for the  db_total_fouls column");
        AssertHelpers.softAssertTrue( api_total_fouls,  db_total_fouls);
        logger.info("Executing assertion for the  db_total_passes column");
        AssertHelpers.softAssertTrue( api_total_passes,  db_total_passes);
        logger.info("Executing assertion for the  db_total_successful_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_passes,  db_total_successful_passes);
        logger.info("Executing assertion for the  db_total_smart_passes column");
        AssertHelpers.softAssertTrue( api_total_smart_passes,  db_total_smart_passes);
        logger.info("Executing assertion for the  db_total_successful_smart_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_smart_passes,  db_total_successful_smart_passes);
        logger.info("Executing assertion for the  db_total_passes_to_final_third column");
        AssertHelpers.softAssertTrue( api_total_passes_to_final_third,  db_total_passes_to_final_third);
        logger.info("Executing assertion for the  db_total_successful_passes_to_final_third column");
        AssertHelpers.softAssertTrue( api_total_successful_passes_to_final_third,  db_total_successful_passes_to_final_third);
        logger.info("Executing assertion for the  db_total_crosses column");
        AssertHelpers.softAssertTrue( api_total_crosses,  db_total_crosses);
        logger.info("Executing assertion for the  db_total_successful_crosses column");
        AssertHelpers.softAssertTrue( api_total_successful_crosses,  db_total_successful_crosses);
        logger.info("Executing assertion for the  db_total_forward_passes column");
        AssertHelpers.softAssertTrue( api_total_forward_passes,  db_total_forward_passes);
        logger.info("Executing assertion for the  db_total_successful_forward_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_forward_passes,  db_total_successful_forward_passes);
        logger.info("Executing assertion for the  db_total_back_passes column");
        AssertHelpers.softAssertTrue( api_total_back_passes,  db_total_back_passes);
        logger.info("Executing assertion for the  db_total_successful_back_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_back_passes,  db_total_successful_back_passes);
        logger.info("Executing assertion for the  db_total_through_passes column");
        AssertHelpers.softAssertTrue( api_total_through_passes,  db_total_through_passes);
        logger.info("Executing assertion for the  db_total_successful_through_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_through_passes,  db_total_successful_through_passes);
        logger.info("Executing assertion for the  db_total_key_passes column");
        AssertHelpers.softAssertTrue( api_total_key_passes,  db_total_key_passes);
        logger.info("Executing assertion for the  db_total_successful_key_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_key_passes,  db_total_successful_key_passes);
        logger.info("Executing assertion for the  db_total_vertical_passes column");
        AssertHelpers.softAssertTrue( api_total_vertical_passes,  db_total_vertical_passes);
        logger.info("Executing assertion for the  db_total_successful_vertical_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_vertical_passes,  db_total_successful_vertical_passes);
        logger.info("Executing assertion for the  db_total_long_passes column");
        AssertHelpers.softAssertTrue( api_total_long_passes,  db_total_long_passes);
        logger.info("Executing assertion for the  db_total_successful_long_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_long_passes,  db_total_successful_long_passes);
        logger.info("Executing assertion for the  db_total_dribbles column");
        AssertHelpers.softAssertTrue( api_total_dribbles,  db_total_dribbles);
        logger.info("Executing assertion for the  db_total_successful_dribbles column");
        AssertHelpers.softAssertTrue( api_total_successful_dribbles,  db_total_successful_dribbles);
        logger.info("Executing assertion for the  db_total_interceptions column");
        AssertHelpers.softAssertTrue( api_total_interceptions,  db_total_interceptions);
        logger.info("Executing assertion for the  db_total_defensive_actions column");
        AssertHelpers.softAssertTrue( api_total_defensive_actions,  db_total_defensive_actions);
        logger.info("Executing assertion for the  db_total_successful_defensive_action column");
        AssertHelpers.softAssertTrue( api_total_successful_defensive_action,  db_total_successful_defensive_action);
        logger.info("Executing assertion for the  db_total_attacking_actions column");
        AssertHelpers.softAssertTrue( api_total_attacking_actions,  db_total_attacking_actions);
        logger.info("Executing assertion for the  db_total_successful_attacking_actions column");
        AssertHelpers.softAssertTrue( api_total_successful_attacking_actions,  db_total_successful_attacking_actions);
        logger.info("Executing assertion for the  db_total_free_kicks column");
        AssertHelpers.softAssertTrue( api_total_free_kicks,  db_total_free_kicks);
        logger.info("Executing assertion for the  db_total_free_kicks_on_target column");
        AssertHelpers.softAssertTrue( api_total_free_kicks_on_target,  db_total_free_kicks_on_target);
        logger.info("Executing assertion for the  db_total_direct_free_kicks column");
        AssertHelpers.softAssertTrue( api_total_direct_free_kicks,  db_total_direct_free_kicks);
        logger.info("Executing assertion for the  db_total_direct_free_kicks_on_target column");
        AssertHelpers.softAssertTrue( api_total_direct_free_kicks_on_target,  db_total_direct_free_kicks_on_target);
        logger.info("Executing assertion for the  db_total_corners column");
        AssertHelpers.softAssertTrue( api_total_corners,  db_total_corners);
        logger.info("Executing assertion for the  db_total_successful_penalties column");
        AssertHelpers.softAssertTrue( api_total_successful_penalties,  db_total_successful_penalties);
        logger.info("Executing assertion for the  db_total_successful_linkup_plays column");
        AssertHelpers.softAssertTrue( api_total_successful_linkup_plays,  db_total_successful_linkup_plays);
        logger.info("Executing assertion for the  db_total_accelerations column");
        AssertHelpers.softAssertTrue( api_total_accelerations,  db_total_accelerations);
        logger.info("Executing assertion for the  db_total_pressing_duels column");
        AssertHelpers.softAssertTrue( api_total_pressing_duels,  db_total_pressing_duels);
        logger.info("Executing assertion for the  db_total_pressing_duels_won column");
        AssertHelpers.softAssertTrue( api_total_pressing_duels_won,  db_total_pressing_duels_won);
        logger.info("Executing assertion for the  db_total_loose_ball_duels column");
        AssertHelpers.softAssertTrue( api_total_loose_ball_duels,  db_total_loose_ball_duels);
        logger.info("Executing assertion for the  db_total_loose_ball_duels_won column");
        AssertHelpers.softAssertTrue( api_total_loose_ball_duels_won,  db_total_loose_ball_duels_won);
        logger.info("Executing assertion for the  db_total_missed_balls column");
        AssertHelpers.softAssertTrue( api_total_missed_balls,  db_total_missed_balls);
        logger.info("Executing assertion for the  db_total_shot_assists column");
        AssertHelpers.softAssertTrue( api_total_shot_assists,  db_total_shot_assists);
        logger.info("Executing assertion for the  db_total_shot_on_target_assists column");
        AssertHelpers.softAssertTrue( api_total_shot_on_target_assists,  db_total_shot_on_target_assists);
        logger.info("Executing assertion for the  db_total_recoveries column");
        AssertHelpers.softAssertTrue( api_total_recoveries,  db_total_recoveries);
        logger.info("Executing assertion for the  db_total_opponent_half_recoveries column");
        AssertHelpers.softAssertTrue( api_total_opponent_half_recoveries,  db_total_opponent_half_recoveries);
        logger.info("Executing assertion for the  db_total_dangerous_opponent_half_recoveries column");
        AssertHelpers.softAssertTrue( api_total_dangerous_opponent_half_recoveries,  db_total_dangerous_opponent_half_recoveries);
        logger.info("Executing assertion for the  db_total_losses column");
        AssertHelpers.softAssertTrue( api_total_losses,  db_total_losses);
        logger.info("Executing assertion for the  db_total_own_half_losses column");
        AssertHelpers.softAssertTrue( api_total_own_half_losses,  db_total_own_half_losses);
        logger.info("Executing assertion for the  db_total_dangerous_own_half_losses column");
        AssertHelpers.softAssertTrue( api_total_dangerous_own_half_losses,  db_total_dangerous_own_half_losses);
        logger.info("Executing assertion for the  db_total_xg_shot column");
        AssertHelpers.softAssertTrue( api_total_xg_shot,  db_total_xg_shot);
        logger.info("Executing assertion for the  db_total_xg_assist column");
        AssertHelpers.softAssertTrue( api_total_xg_assist,  db_total_xg_assist);
        logger.info("Executing assertion for the  db_total_xg_save column");
        AssertHelpers.softAssertTrue( api_total_xg_save,  db_total_xg_save);
        logger.info("Executing assertion for the  db_total_received_pass column");
        AssertHelpers.softAssertTrue( api_total_received_pass,  db_total_received_pass);
        logger.info("Executing assertion for the  db_total_touch_in_box column");
        AssertHelpers.softAssertTrue( api_total_touch_in_box,  db_total_touch_in_box);
        logger.info("Executing assertion for the  db_total_progressive_run column");
        AssertHelpers.softAssertTrue( api_total_progressive_run,  db_total_progressive_run);
        logger.info("Executing assertion for the  db_total_offsides column");
        AssertHelpers.softAssertTrue( api_total_offsides,  db_total_offsides);
        logger.info("Executing assertion for the  db_total_clearances column");
        AssertHelpers.softAssertTrue( api_total_clearances,  db_total_clearances);
        logger.info("Executing assertion for the  db_total_second_assists column");
        AssertHelpers.softAssertTrue( api_total_second_assists,  db_total_second_assists);
        logger.info("Executing assertion for the  db_total_third_assists column");
        AssertHelpers.softAssertTrue( api_total_third_assists,  db_total_third_assists);
        logger.info("Executing assertion for the  db_total_shots_blocked column");
        AssertHelpers.softAssertTrue( api_total_shots_blocked,  db_total_shots_blocked);
        logger.info("Executing assertion for the  db_total_fouls_suffered column");
        AssertHelpers.softAssertTrue( api_total_fouls_suffered,  db_total_fouls_suffered);
        logger.info("Executing assertion for the  db_total_progressive_passes column");
        AssertHelpers.softAssertTrue( api_total_progressive_passes,  db_total_progressive_passes);
        logger.info("Executing assertion for the  db_total_counterpressing_recoveries column");
        AssertHelpers.softAssertTrue( api_total_counterpressing_recoveries,  db_total_counterpressing_recoveries);
        logger.info("Executing assertion for the  db_total_sliding_tackles column");
        AssertHelpers.softAssertTrue( api_total_sliding_tackles,  db_total_sliding_tackles);
        logger.info("Executing assertion for the  db_total_goal_kicks column");
        AssertHelpers.softAssertTrue( api_total_goal_kicks,  db_total_goal_kicks);
        logger.info("Executing assertion for the  db_total_dribbles_against column");
        AssertHelpers.softAssertTrue( api_total_dribbles_against,  db_total_dribbles_against);
        logger.info("Executing assertion for the  db_total_dribbles_against_won column");
        AssertHelpers.softAssertTrue( api_total_dribbles_against_won,  db_total_dribbles_against_won);
        logger.info("Executing assertion for the  db_total_goal_kicks_short column");
        AssertHelpers.softAssertTrue( api_total_goal_kicks_short,  db_total_goal_kicks_short);
        logger.info("Executing assertion for the  db_total_goal_kicks_long column");
        AssertHelpers.softAssertTrue( api_total_goal_kicks_long,  db_total_goal_kicks_long);
        logger.info("Executing assertion for the  db_total_shots_on_target column");
        AssertHelpers.softAssertTrue( api_total_shots_on_target,  db_total_shots_on_target);
        logger.info("Executing assertion for the  db_total_successful_progressive_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_progressive_passes,  db_total_successful_progressive_passes);
        logger.info("Executing assertion for the  db_total_successful_sliding_tackles column");
        AssertHelpers.softAssertTrue( api_total_successful_sliding_tackles,  db_total_successful_sliding_tackles);
        logger.info("Executing assertion for the  db_total_successful_goal_kicks column");
        AssertHelpers.softAssertTrue( api_total_successful_goal_kicks,  db_total_successful_goal_kicks);
        logger.info("Executing assertion for the  db_total_field_aerial_duels column");
        AssertHelpers.softAssertTrue( api_total_field_aerial_duels,  db_total_field_aerial_duels);
        logger.info("Executing assertion for the  db_total_field_aerial_duels_won column");
        AssertHelpers.softAssertTrue( api_total_field_aerial_duels_won,  db_total_field_aerial_duels_won);
        logger.info("Executing assertion for the  db_total_gk_clean_sheets column");
        AssertHelpers.softAssertTrue( api_total_gk_clean_sheets,  db_total_gk_clean_sheets);
        logger.info("Executing assertion for the  db_total_gk_conceded_goals column");
        AssertHelpers.softAssertTrue( api_total_gk_conceded_goals,  db_total_gk_conceded_goals);
        logger.info("Executing assertion for the  db_total_gk_shots_against column");
        AssertHelpers.softAssertTrue( api_total_gk_shots_against,  db_total_gk_shots_against);
        logger.info("Executing assertion for the  db_total_gk_exits column");
        AssertHelpers.softAssertTrue( api_total_gk_exits,  db_total_gk_exits);
        logger.info("Executing assertion for the  db_total_gk_successful_exits column");
        AssertHelpers.softAssertTrue( api_total_gk_successful_exits,  db_total_gk_successful_exits);
        logger.info("Executing assertion for the  db_total_gk_aerial_duels column");
        AssertHelpers.softAssertTrue( api_total_gk_aerial_duels,  db_total_gk_aerial_duels);
        logger.info("Executing assertion for the  db_total_gk_aerial_duels_won column");
        AssertHelpers.softAssertTrue( api_total_gk_aerial_duels_won,  db_total_gk_aerial_duels_won);
        logger.info("Executing assertion for the  db_total_gk_saves column");
        AssertHelpers.softAssertTrue( api_total_gk_saves,  db_total_gk_saves);
        logger.info("Executing assertion for the  db_total_new_duels_won column");
        AssertHelpers.softAssertTrue( api_total_new_duels_won,  db_total_new_duels_won);
        logger.info("Executing assertion for the  db_total_new_defensive_duels_won column");
        AssertHelpers.softAssertTrue( api_total_new_defensive_duels_won,  db_total_new_defensive_duels_won);
        logger.info("Executing assertion for the  db_total_new_offensive_duels_won column");
        AssertHelpers.softAssertTrue( api_total_new_offensive_duels_won,  db_total_new_offensive_duels_won);
        logger.info("Executing assertion for the  db_total_new_successful_dribbles column");
        AssertHelpers.softAssertTrue( api_total_new_successful_dribbles,  db_total_new_successful_dribbles);
        logger.info("Executing assertion for the  db_total_lateral_passes column");
        AssertHelpers.softAssertTrue( api_total_lateral_passes,  db_total_lateral_passes);
        logger.info("Executing assertion for the  db_total_successful_lateral_passes column");
        AssertHelpers.softAssertTrue( api_total_successful_lateral_passes,  db_total_successful_lateral_passes);
        logger.info("Executing assertion for the  db_average_passlength column");
        AssertHelpers.softAssertTrue( api_average_passlength,  db_average_passlength);
        logger.info("Executing assertion for the  db_average_longpasslength column");
        AssertHelpers.softAssertTrue( api_average_longpasslength,  db_average_longpasslength);
        logger.info("Executing assertion for the  db_average_dribbledistancefromopponentgoal column");
        AssertHelpers.softAssertTrue( api_average_dribbledistancefromopponentgoal,  db_average_dribbledistancefromopponentgoal);
        logger.info("Executing assertion for the  db_average_ballrecoveries column");
        AssertHelpers.softAssertTrue( api_average_ballrecoveries,  db_average_ballrecoveries);
        logger.info("Executing assertion for the  db_average_duels column");
        AssertHelpers.softAssertTrue( api_average_duels,  db_average_duels);
        logger.info("Executing assertion for the  db_average_defensiveduels column");
        AssertHelpers.softAssertTrue( api_average_defensiveduels,  db_average_defensiveduels);
        logger.info("Executing assertion for the  db_average_offensiveduels column");
        AssertHelpers.softAssertTrue( api_average_offensiveduels,  db_average_offensiveduels);
        logger.info("Executing assertion for the  db_average_aerialduels column");
        AssertHelpers.softAssertTrue( api_average_aerialduels,  db_average_aerialduels);
        logger.info("Executing assertion for the  db_average_fouls column");
        AssertHelpers.softAssertTrue( api_average_fouls,  db_average_fouls);
        logger.info("Executing assertion for the  db_average_goals column");
        AssertHelpers.softAssertTrue( api_average_goals,  db_average_goals);
        logger.info("Executing assertion for the  db_average_assists column");
        AssertHelpers.softAssertTrue( api_average_assists,  db_average_assists);
        logger.info("Executing assertion for the  db_average_passes column");
        AssertHelpers.softAssertTrue( api_average_passes,  db_average_passes);
        logger.info("Executing assertion for the  db_average_smartpasses column");
        AssertHelpers.softAssertTrue( api_average_smartpasses,  db_average_smartpasses);
        logger.info("Executing assertion for the  db_average_passestofinalthird column");
        AssertHelpers.softAssertTrue( api_average_passestofinalthird,  db_average_passestofinalthird);
        logger.info("Executing assertion for the  db_average_crosses column");
        AssertHelpers.softAssertTrue( api_average_crosses,  db_average_crosses);
        logger.info("Executing assertion for the  db_average_dribbles column");
        AssertHelpers.softAssertTrue( api_average_dribbles,  db_average_dribbles);
        logger.info("Executing assertion for the  db_average_shots column");
        AssertHelpers.softAssertTrue( api_average_shots,  db_average_shots);
        logger.info("Executing assertion for the  db_average_headshots column");
        AssertHelpers.softAssertTrue( api_average_headshots,  db_average_headshots);
        logger.info("Executing assertion for the  db_average_interceptions column");
        AssertHelpers.softAssertTrue( api_average_interceptions,  db_average_interceptions);
        logger.info("Executing assertion for the  db_average_successfuldefensiveaction column");
        AssertHelpers.softAssertTrue( api_average_successfuldefensiveaction,  db_average_successfuldefensiveaction);
        logger.info("Executing assertion for the  db_average_yellowcards column");
        AssertHelpers.softAssertTrue( api_average_yellowcards,  db_average_yellowcards);
        logger.info("Executing assertion for the  db_average_redcards column");
        AssertHelpers.softAssertTrue( api_average_redcards,  db_average_redcards);
        logger.info("Executing assertion for the  db_average_directredcards column");
        AssertHelpers.softAssertTrue( api_average_directredcards,  db_average_directredcards);
        logger.info("Executing assertion for the  db_average_successfulattackingactions column");
        AssertHelpers.softAssertTrue( api_average_successfulattackingactions,  db_average_successfulattackingactions);
        logger.info("Executing assertion for the  db_average_freekicks column");
        AssertHelpers.softAssertTrue( api_average_freekicks,  db_average_freekicks);
        logger.info("Executing assertion for the  db_average_directfreekicks column");
        AssertHelpers.softAssertTrue( api_average_directfreekicks,  db_average_directfreekicks);
        logger.info("Executing assertion for the  db_average_corners column");
        AssertHelpers.softAssertTrue( api_average_corners,  db_average_corners);
        logger.info("Executing assertion for the  db_average_penalties column");
        AssertHelpers.softAssertTrue( api_average_penalties,  db_average_penalties);
        logger.info("Executing assertion for the  db_average_accelerations column");
        AssertHelpers.softAssertTrue( api_average_accelerations,  db_average_accelerations);
        logger.info("Executing assertion for the  db_average_looseballduels column");
        AssertHelpers.softAssertTrue( api_average_looseballduels,  db_average_looseballduels);
        logger.info("Executing assertion for the  db_average_missedballs column");
        AssertHelpers.softAssertTrue( api_average_missedballs,  db_average_missedballs);
        logger.info("Executing assertion for the  db_average_forwardpasses column");
        AssertHelpers.softAssertTrue( api_average_forwardpasses,  db_average_forwardpasses);
        logger.info("Executing assertion for the  db_average_backpasses column");
        AssertHelpers.softAssertTrue( api_average_backpasses,  db_average_backpasses);
        logger.info("Executing assertion for the  db_average_throughpasses column");
        AssertHelpers.softAssertTrue( api_average_throughpasses,  db_average_throughpasses);
        logger.info("Executing assertion for the  db_average_keypasses column");
        AssertHelpers.softAssertTrue( api_average_keypasses,  db_average_keypasses);
        logger.info("Executing assertion for the  db_average_verticalpasses column");
        AssertHelpers.softAssertTrue( api_average_verticalpasses,  db_average_verticalpasses);
        logger.info("Executing assertion for the  db_average_longpasses column");
        AssertHelpers.softAssertTrue( api_average_longpasses,  db_average_longpasses);
        logger.info("Executing assertion for the  db_average_shotassists column");
        AssertHelpers.softAssertTrue( api_average_shotassists,  db_average_shotassists);
        logger.info("Executing assertion for the  db_average_shotontargetassists column");
        AssertHelpers.softAssertTrue( api_average_shotontargetassists,  db_average_shotontargetassists);
        logger.info("Executing assertion for the  db_average_linkupplays column");
        AssertHelpers.softAssertTrue( api_average_linkupplays,  db_average_linkupplays);
        logger.info("Executing assertion for the  db_average_opponenthalfrecoveries column");
        AssertHelpers.softAssertTrue( api_average_opponenthalfrecoveries,  db_average_opponenthalfrecoveries);
        logger.info("Executing assertion for the  db_average_dangerousopponenthalfrecoveries column");
        AssertHelpers.softAssertTrue( api_average_dangerousopponenthalfrecoveries,  db_average_dangerousopponenthalfrecoveries);
        logger.info("Executing assertion for the  db_average_balllosses column");
        AssertHelpers.softAssertTrue( api_average_balllosses,  db_average_balllosses);
        logger.info("Executing assertion for the  db_average_losses column");
        AssertHelpers.softAssertTrue( api_average_losses,  db_average_losses);
        logger.info("Executing assertion for the  db_average_ownhalflosses column");
        AssertHelpers.softAssertTrue( api_average_ownhalflosses,  db_average_ownhalflosses);
        logger.info("Executing assertion for the  db_average_dangerousownhalflosses column");
        AssertHelpers.softAssertTrue( api_average_dangerousownhalflosses,  db_average_dangerousownhalflosses);
        logger.info("Executing assertion for the  db_average_duelswon column");
        AssertHelpers.softAssertTrue( api_average_duelswon,  db_average_duelswon);
        logger.info("Executing assertion for the  db_average_defensiveduelswon column");
        AssertHelpers.softAssertTrue( api_average_defensiveduelswon,  db_average_defensiveduelswon);
        logger.info("Executing assertion for the  db_average_offensiveduelswon column");
        AssertHelpers.softAssertTrue( api_average_offensiveduelswon,  db_average_offensiveduelswon);
        logger.info("Executing assertion for the  db_average_successfulpasses column");
        AssertHelpers.softAssertTrue( api_average_successfulpasses,  db_average_successfulpasses);
        logger.info("Executing assertion for the  db_average_successfulsmartpasses column");
        AssertHelpers.softAssertTrue( api_average_successfulsmartpasses,  db_average_successfulsmartpasses);
        logger.info("Executing assertion for the  db_average_successfulcrosses column");
        AssertHelpers.softAssertTrue( api_average_successfulcrosses,  db_average_successfulcrosses);
        logger.info("Executing assertion for the  db_average_successfulforwardpasses column");
        AssertHelpers.softAssertTrue( api_average_successfulforwardpasses,  db_average_successfulforwardpasses);
        logger.info("Executing assertion for the  db_average_successfulbackpasses column");
        AssertHelpers.softAssertTrue( api_average_successfulbackpasses,  db_average_successfulbackpasses);
        logger.info("Executing assertion for the  db_average_successfulthroughpasses column");
        AssertHelpers.softAssertTrue( api_average_successfulthroughpasses,  db_average_successfulthroughpasses);
        logger.info("Executing assertion for the  db_average_successfulkeypasses column");
        AssertHelpers.softAssertTrue( api_average_successfulkeypasses,  db_average_successfulkeypasses);
        logger.info("Executing assertion for the  db_average_successfulverticalpasses column");
        AssertHelpers.softAssertTrue( api_average_successfulverticalpasses,  db_average_successfulverticalpasses);
        logger.info("Executing assertion for the  db_average_successfullongpasses column");
        AssertHelpers.softAssertTrue( api_average_successfullongpasses,  db_average_successfullongpasses);
        logger.info("Executing assertion for the  db_average_successfuldribbles column");
        AssertHelpers.softAssertTrue( api_average_successfuldribbles,  db_average_successfuldribbles);
        logger.info("Executing assertion for the  db_average_defensiveactions column");
        AssertHelpers.softAssertTrue( api_average_defensiveactions,  db_average_defensiveactions);
        logger.info("Executing assertion for the  db_average_attackingactions column");
        AssertHelpers.softAssertTrue( api_average_attackingactions,  db_average_attackingactions);
        logger.info("Executing assertion for the  db_average_freekicksontarget column");
        AssertHelpers.softAssertTrue( api_average_freekicksontarget,  db_average_freekicksontarget);
        logger.info("Executing assertion for the  db_average_directfreekicksontarget column");
        AssertHelpers.softAssertTrue( api_average_directfreekicksontarget,  db_average_directfreekicksontarget);
        logger.info("Executing assertion for the  db_average_successfulpenalties column");
        AssertHelpers.softAssertTrue( api_average_successfulpenalties,  db_average_successfulpenalties);
        logger.info("Executing assertion for the  db_average_successfullinkupplays column");
        AssertHelpers.softAssertTrue( api_average_successfullinkupplays,  db_average_successfullinkupplays);
        logger.info("Executing assertion for the  db_average_looseballduelswon column");
        AssertHelpers.softAssertTrue( api_average_looseballduelswon,  db_average_looseballduelswon);
        logger.info("Executing assertion for the  db_average_successfulpassestofinalthird column");
        AssertHelpers.softAssertTrue( api_average_successfulpassestofinalthird,  db_average_successfulpassestofinalthird);
        logger.info("Executing assertion for the  db_average_xgshot column");
        AssertHelpers.softAssertTrue( api_average_xgshot,  db_average_xgshot);
        logger.info("Executing assertion for the  db_average_xgassist column");
        AssertHelpers.softAssertTrue( api_average_xgassist,  db_average_xgassist);
        logger.info("Executing assertion for the  db_average_xgsave column");
        AssertHelpers.softAssertTrue( api_average_xgsave,  db_average_xgsave);
        logger.info("Executing assertion for the  db_average_receivedpass column");
        AssertHelpers.softAssertTrue( api_average_receivedpass,  db_average_receivedpass);
        logger.info("Executing assertion for the  db_average_touchinbox column");
        AssertHelpers.softAssertTrue( api_average_touchinbox,  db_average_touchinbox);
        logger.info("Executing assertion for the  db_average_progressiverun column");
        AssertHelpers.softAssertTrue( api_average_progressiverun,  db_average_progressiverun);
        logger.info("Executing assertion for the  db_average_offsides column");
        AssertHelpers.softAssertTrue( api_average_offsides,  db_average_offsides);
        logger.info("Executing assertion for the  db_average_clearances column");
        AssertHelpers.softAssertTrue( api_average_clearances,  db_average_clearances);
        logger.info("Executing assertion for the  db_average_secondassists column");
        AssertHelpers.softAssertTrue( api_average_secondassists,  db_average_secondassists);
        logger.info("Executing assertion for the  db_average_thirdassists column");
        AssertHelpers.softAssertTrue( api_average_thirdassists,  db_average_thirdassists);
        logger.info("Executing assertion for the  db_average_foulssuffered column");
        AssertHelpers.softAssertTrue( api_average_foulssuffered,  db_average_foulssuffered);
        logger.info("Executing assertion for the  db_average_progressivepasses column");
        AssertHelpers.softAssertTrue( api_average_progressivepasses,  db_average_progressivepasses);
        logger.info("Executing assertion for the  db_average_counterpressingrecoveries column");
        AssertHelpers.softAssertTrue( api_average_counterpressingrecoveries,  db_average_counterpressingrecoveries);
        logger.info("Executing assertion for the  db_average_slidingtackles column");
        AssertHelpers.softAssertTrue( api_average_slidingtackles,  db_average_slidingtackles);
        logger.info("Executing assertion for the  db_average_goalkicks column");
        AssertHelpers.softAssertTrue( api_average_goalkicks,  db_average_goalkicks);
        logger.info("Executing assertion for the  db_average_shotsblocked column");
        AssertHelpers.softAssertTrue( api_average_shotsblocked,  db_average_shotsblocked);
        logger.info("Executing assertion for the  db_average_shotsontarget column");
        AssertHelpers.softAssertTrue( api_average_shotsontarget,  db_average_shotsontarget);
        logger.info("Executing assertion for the  db_average_successfulprogressivepasses column");
        AssertHelpers.softAssertTrue( api_average_successfulprogressivepasses,  db_average_successfulprogressivepasses);
        logger.info("Executing assertion for the  db_average_successfulslidingtackles column");
        AssertHelpers.softAssertTrue( api_average_successfulslidingtackles,  db_average_successfulslidingtackles);
        logger.info("Executing assertion for the  db_average_successfulgoalkicks column");
        AssertHelpers.softAssertTrue( api_average_successfulgoalkicks,  db_average_successfulgoalkicks);
        logger.info("Executing assertion for the  db_average_dribblesagainst column");
        AssertHelpers.softAssertTrue( api_average_dribblesagainst,  db_average_dribblesagainst);
        logger.info("Executing assertion for the  db_average_dribblesagainstwon column");
        AssertHelpers.softAssertTrue( api_average_dribblesagainstwon,  db_average_dribblesagainstwon);
        logger.info("Executing assertion for the  db_average_goalkicksshort column");
        AssertHelpers.softAssertTrue( api_average_goalkicksshort,  db_average_goalkicksshort);
        logger.info("Executing assertion for the  db_average_goalkickslong column");
        AssertHelpers.softAssertTrue( api_average_goalkickslong,  db_average_goalkickslong);
        logger.info("Executing assertion for the  db_average_fieldaerialduels column");
        AssertHelpers.softAssertTrue( api_average_fieldaerialduels,  db_average_fieldaerialduels);
        logger.info("Executing assertion for the  db_average_fieldaerialduelswon column");
        AssertHelpers.softAssertTrue( api_average_fieldaerialduelswon,  db_average_fieldaerialduelswon);
        logger.info("Executing assertion for the  db_average_gkconcededgoals column");
        AssertHelpers.softAssertTrue( api_average_gkconcededgoals,  db_average_gkconcededgoals);
        logger.info("Executing assertion for the  db_average_gkshotsagainst column");
        AssertHelpers.softAssertTrue( api_average_gkshotsagainst,  db_average_gkshotsagainst);
        logger.info("Executing assertion for the  db_average_gkexits column");
        AssertHelpers.softAssertTrue( api_average_gkexits,  db_average_gkexits);
        logger.info("Executing assertion for the  db_average_gkaerialduels column");
        AssertHelpers.softAssertTrue( api_average_gkaerialduels,  db_average_gkaerialduels);
        logger.info("Executing assertion for the  db_average_gksaves column");
        AssertHelpers.softAssertTrue( api_average_gksaves,  db_average_gksaves);
        logger.info("Executing assertion for the  db_average_gksuccessfulexits column");
        AssertHelpers.softAssertTrue( api_average_gksuccessfulexits,  db_average_gksuccessfulexits);
        logger.info("Executing assertion for the  db_average_gkaerialduelswon column");
        AssertHelpers.softAssertTrue( api_average_gkaerialduelswon,  db_average_gkaerialduelswon);
        logger.info("Executing assertion for the  db_average_newduelswon column");
        AssertHelpers.softAssertTrue( api_average_newduelswon,  db_average_newduelswon);
        logger.info("Executing assertion for the  db_average_newdefensiveduelswon column");
        AssertHelpers.softAssertTrue( api_average_newdefensiveduelswon,  db_average_newdefensiveduelswon);
        logger.info("Executing assertion for the  db_average_newoffensiveduelswon column");
        AssertHelpers.softAssertTrue( api_average_newoffensiveduelswon,  db_average_newoffensiveduelswon);
        logger.info("Executing assertion for the  db_average_newsuccessfuldribbles column");
        AssertHelpers.softAssertTrue( api_average_newsuccessfuldribbles,  db_average_newsuccessfuldribbles);
        logger.info("Executing assertion for the  db_average_lateralpasses column");
        AssertHelpers.softAssertTrue( api_average_lateralpasses,  db_average_lateralpasses);
        logger.info("Executing assertion for the  db_average_successfullateralpasses column");
        AssertHelpers.softAssertTrue( api_average_successfullateralpasses,  db_average_successfullateralpasses);
        logger.info("Executing assertion for the  db_percent_duelswon column");
        AssertHelpers.softAssertTrue( api_percent_duelswon,  db_percent_duelswon);
        logger.info("Executing assertion for the  db_percent_defensiveduelswon column");
        AssertHelpers.softAssertTrue( api_percent_defensiveduelswon,  db_percent_defensiveduelswon);
        logger.info("Executing assertion for the  db_percent_offensiveduelswon column");
        AssertHelpers.softAssertTrue( api_percent_offensiveduelswon,  db_percent_offensiveduelswon);
        logger.info("Executing assertion for the  db_percent_aerialduelswon column");
        AssertHelpers.softAssertTrue( api_percent_aerialduelswon,  db_percent_aerialduelswon);
        logger.info("Executing assertion for the  db_percent_successfulpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulpasses,  db_percent_successfulpasses);
        logger.info("Executing assertion for the  db_percent_successfulsmartpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulsmartpasses,  db_percent_successfulsmartpasses);
        logger.info("Executing assertion for the  db_percent_successfulpassestofinalthird column");
        AssertHelpers.softAssertTrue( api_percent_successfulpassestofinalthird,  db_percent_successfulpassestofinalthird);
        logger.info("Executing assertion for the  db_percent_successfulcrosses column");
        AssertHelpers.softAssertTrue( api_percent_successfulcrosses,  db_percent_successfulcrosses);
        logger.info("Executing assertion for the  db_percent_successfuldribbles column");
        AssertHelpers.softAssertTrue( api_percent_successfuldribbles,  db_percent_successfuldribbles);
        logger.info("Executing assertion for the  db_percent_shotsontarget column");
        AssertHelpers.softAssertTrue( api_percent_shotsontarget,  db_percent_shotsontarget);
        logger.info("Executing assertion for the  db_percent_headshotsontarget column");
        AssertHelpers.softAssertTrue( api_percent_headshotsontarget,  db_percent_headshotsontarget);
        logger.info("Executing assertion for the  db_percent_goalconversion column");
        AssertHelpers.softAssertTrue( api_percent_goalconversion,  db_percent_goalconversion);
        logger.info("Executing assertion for the  db_percent_directfreekicksontarget column");
        AssertHelpers.softAssertTrue( api_percent_directfreekicksontarget,  db_percent_directfreekicksontarget);
        logger.info("Executing assertion for the  db_percent_penaltiesconversion column");
        AssertHelpers.softAssertTrue( api_percent_penaltiesconversion,  db_percent_penaltiesconversion);
        logger.info("Executing assertion for the  db_percent_win column");
        AssertHelpers.softAssertTrue( api_percent_win,  db_percent_win);
        logger.info("Executing assertion for the  db_percent_successfulforwardpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulforwardpasses,  db_percent_successfulforwardpasses);
        logger.info("Executing assertion for the  db_percent_successfulbackpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulbackpasses,  db_percent_successfulbackpasses);
        logger.info("Executing assertion for the  db_percent_successfulthroughpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulthroughpasses,  db_percent_successfulthroughpasses);
        logger.info("Executing assertion for the  db_percent_successfulkeypasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulkeypasses,  db_percent_successfulkeypasses);
        logger.info("Executing assertion for the  db_percent_successfulverticalpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulverticalpasses,  db_percent_successfulverticalpasses);
        logger.info("Executing assertion for the  db_percent_successfullongpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfullongpasses,  db_percent_successfullongpasses);
        logger.info("Executing assertion for the  db_percent_successfulshotassists column");
        AssertHelpers.softAssertTrue( api_percent_successfulshotassists,  db_percent_successfulshotassists);
        logger.info("Executing assertion for the  db_percent_successfullinkupplays column");
        AssertHelpers.softAssertTrue( api_percent_successfullinkupplays,  db_percent_successfullinkupplays);
        logger.info("Executing assertion for the  db_percent_yellowcardsperfoul column");
        AssertHelpers.softAssertTrue( api_percent_yellowcardsperfoul,  db_percent_yellowcardsperfoul);
        logger.info("Executing assertion for the  db_percent_successfulprogressivepasses column");
        AssertHelpers.softAssertTrue( api_percent_successfulprogressivepasses,  db_percent_successfulprogressivepasses);
        logger.info("Executing assertion for the  db_percent_successfulslidingtackles column");
        AssertHelpers.softAssertTrue( api_percent_successfulslidingtackles,  db_percent_successfulslidingtackles);
        logger.info("Executing assertion for the  db_percent_successfulgoalkicks column");
        AssertHelpers.softAssertTrue( api_percent_successfulgoalkicks,  db_percent_successfulgoalkicks);
        logger.info("Executing assertion for the  db_percent_dribblesagainstwon column");
        AssertHelpers.softAssertTrue( api_percent_dribblesagainstwon,  db_percent_dribblesagainstwon);
        logger.info("Executing assertion for the  db_percent_fieldaerialduelswon column");
        AssertHelpers.softAssertTrue( api_percent_fieldaerialduelswon,  db_percent_fieldaerialduelswon);
        logger.info("Executing assertion for the  db_percent_gksaves column");
        AssertHelpers.softAssertTrue( api_percent_gksaves,  db_percent_gksaves);
        logger.info("Executing assertion for the  db_percent_gksuccessfulexits column");
        AssertHelpers.softAssertTrue( api_percent_gksuccessfulexits,  db_percent_gksuccessfulexits);
        logger.info("Executing assertion for the  db_percent_gkaerialduelswon column");
        AssertHelpers.softAssertTrue( api_percent_gkaerialduelswon,  db_percent_gkaerialduelswon);
        logger.info("Executing assertion for the  db_percent_newduelswon column");
        AssertHelpers.softAssertTrue( api_percent_newduelswon,  db_percent_newduelswon);
        logger.info("Executing assertion for the  db_percent_newdefensiveduelswon column");
        AssertHelpers.softAssertTrue( api_percent_newdefensiveduelswon,  db_percent_newdefensiveduelswon);
        logger.info("Executing assertion for the  db_percent_newoffensiveduelswon column");
        AssertHelpers.softAssertTrue( api_percent_newoffensiveduelswon,  db_percent_newoffensiveduelswon);
        logger.info("Executing assertion for the  db_percent_newsuccessfuldribbles column");
        AssertHelpers.softAssertTrue( api_percent_newsuccessfuldribbles,  db_percent_newsuccessfuldribbles);
        logger.info("Executing assertion for the  db_percent_successfullateralpasses column");
        AssertHelpers.softAssertTrue( api_percent_successfullateralpasses,  db_percent_successfullateralpasses);
        logger.info("Executing assertion for the  db_roundid column");
        AssertHelpers.softAssertTrue( api_roundid,  db_roundid);
        AssertHelpers.softAssertAll();
    }


    @Test
    @Title("Verify Players Advance Stats Table CompetitionID Records")
    @Tag("PlayersAdvanceStats")
    public void playersAdvanceStatsValidateCompetitionIDRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.team_advancestats_tablename);
        logger.info("Fetching duplicate value for 'teams' table and column name is '" + columnNames.get(0) + "' and " + columnNames.get(9));
        String columnName = "competitionid";
        String sqlQuery = "Select DISTINCT " + columnName + " from " + GlbVar.catalog_name + "."
                + GlbVar.schemas_name + "." + GlbVar.player_advancestats_tablename + " ORDER BY competitionid";
        logger.info("SQL QUERY: " + sqlQuery);
        ArrayList<String> actualCompetitionId = tableMetadata.fetchTableDataByQuery(sqlQuery);
        String[] configCompetitionId = GlbVar.staticAdvanceCompetitionIDList.split(",\\s*");
        ArrayList<String> expectedCompetitionId = new ArrayList<>(Arrays.asList(configCompetitionId));
        logger.info("actualCompetitionId: " + actualCompetitionId + " expectedCompetitionId: " + expectedCompetitionId);
        AssertHelpers.softAssertMatchTwoList(actualCompetitionId, expectedCompetitionId);
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify Players Advance Stats Table number of records count")
    @Tag("PlayersAdvanceStats")
    public void playersAdvanceStatsValidateNumberORecordCount() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        //Fetching the record of Player from squad table
        String[] configTeamAdvanceCompetitionCompetitionId = GlbVar.staticAdvanceCompetitionIDList.split(",\\s*");
        int competitionIDSize=configTeamAdvanceCompetitionCompetitionId.length;
        TeamsDataValidation teamsDataValidation = new TeamsDataValidation();
        List<String> teamID = teamsDataValidation.getTeamIDByStaticCompetitionID(GlbVar.staticAdvanceCompetitionIDList, competitionIDSize);
        SquadDataValidationCopy squadDataValidation=new SquadDataValidationCopy();
        List<String> playerID=squadDataValidation.getPlayerID(teamID,teamID.size());
        String squadColumnName1 = "playerid";
        String squadSQLQuery = "Select " + squadColumnName1 + " from " + GlbVar.catalog_name + "."
                + GlbVar.schemas_name + "." + GlbVar.player_advancestats_tablename + " where "+squadColumnName1+" IN("+playerID+") ORDER BY "+squadColumnName1;
        squadSQLQuery=GenericFun.replaceAnyValue(squadSQLQuery,"[","");
        squadSQLQuery=GenericFun.replaceAnyValue(squadSQLQuery,"]","");
        logger.info("SQL QUERY of squad table: " + squadSQLQuery);
        ArrayList<String> squadResult = tableMetadata.fetchTableDataByQuery(squadSQLQuery);


        //Fetching record of player advance table
        String playerAdvanceColumnName1 = "playerid";
        String playerAdvanceColumnName2 = "competitionid";
        String playerAdvanceSQLQuery = "Select " + playerAdvanceColumnName1 +", "+playerAdvanceColumnName2+ " from " + GlbVar.catalog_name + "."
                + GlbVar.schemas_name + "." + GlbVar.player_advancestats_tablename + " where "+playerAdvanceColumnName2+" IN("+GlbVar.staticAdvanceCompetitionIDList+") ORDER BY "+playerAdvanceColumnName1;
        logger.info("SQL QUERY of team table: " + playerAdvanceSQLQuery);
        ArrayList<String> playerAdvanceResult = tableMetadata.fetchTableDataByQuery(playerAdvanceSQLQuery);
        logger.info("Missing player id in PlayerAdvance Table: "+GenericFun.missingDataInSecondList(squadResult,playerAdvanceResult));
        AssertHelpers.softAssertMatchTwoList(squadResult, playerAdvanceResult);
        AssertHelpers.softAssertAll();
    }


    @Test
    @Title("Verify Players Advance Stats Table Duplicate Records")
    @Tag("PlayersAdvanceStats")
    public void playersAdvanceStatsTableDuplicateRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.player_advancestats_tablename);
        String playerAdvanceDuplicateSQLQuery="SELECT playerid, competitionid,seasonid, position_code, COUNT(*) FROM raw.alain_wyscout.player_advancestats Group by playerid, competitionid,seasonid, position_code having COUNT(*) >1";
        logger.info("SQL QUERY of Player Advance table: " + playerAdvanceDuplicateSQLQuery);
        ArrayList<String> playerAdvanceResult = tableMetadata.fetchTableDataByQuery(playerAdvanceDuplicateSQLQuery);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(playerAdvanceResult);
        AssertHelpers.softAssertAll();
    }


    //DB AND API VARIABLES
    ArrayList<String> db_playerid  = new ArrayList<String>();
    ArrayList<String> db_competitionid  = new ArrayList<String>();
    ArrayList<String> db_seasonid  = new ArrayList<String>();
    ArrayList<String> db_position_name  = new ArrayList<String>();
    ArrayList<String> db_position_code  = new ArrayList<String>();
    ArrayList<String> db_position_percent  = new ArrayList<String>();
    ArrayList<String> db_total_matches  = new ArrayList<String>();
    ArrayList<String> db_total_matches_in_start  = new ArrayList<String>();
    ArrayList<String> db_total_matches_substituted  = new ArrayList<String>();
    ArrayList<String> db_total_matches_coming_off  = new ArrayList<String>();
    ArrayList<String> db_total_minutes_on_field  = new ArrayList<String>();
    ArrayList<String> db_total_minutes_tagged  = new ArrayList<String>();
    ArrayList<String> db_total_goals  = new ArrayList<String>();
    ArrayList<String> db_total_assists  = new ArrayList<String>();
    ArrayList<String> db_total_shots  = new ArrayList<String>();
    ArrayList<String> db_total_head_shots  = new ArrayList<String>();
    ArrayList<String> db_total_yellow_cards  = new ArrayList<String>();
    ArrayList<String> db_total_red_cards  = new ArrayList<String>();
    ArrayList<String> db_total_direct_red_cards  = new ArrayList<String>();
    ArrayList<String> db_total_penalties  = new ArrayList<String>();
    ArrayList<String> db_total_linkup_plays  = new ArrayList<String>();
    ArrayList<String> db_total_duels  = new ArrayList<String>();
    ArrayList<String> db_total_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_defensive_duels  = new ArrayList<String>();
    ArrayList<String> db_total_defensive_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_offensive_duels  = new ArrayList<String>();
    ArrayList<String> db_total_offensive_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_aerial_duels  = new ArrayList<String>();
    ArrayList<String> db_total_aerial_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_fouls  = new ArrayList<String>();
    ArrayList<String> db_total_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_passes  = new ArrayList<String>();
    ArrayList<String> db_total_smart_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_smart_passes  = new ArrayList<String>();
    ArrayList<String> db_total_passes_to_final_third  = new ArrayList<String>();
    ArrayList<String> db_total_successful_passes_to_final_third  = new ArrayList<String>();
    ArrayList<String> db_total_crosses  = new ArrayList<String>();
    ArrayList<String> db_total_successful_crosses  = new ArrayList<String>();
    ArrayList<String> db_total_forward_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_forward_passes  = new ArrayList<String>();
    ArrayList<String> db_total_back_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_back_passes  = new ArrayList<String>();
    ArrayList<String> db_total_through_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_through_passes  = new ArrayList<String>();
    ArrayList<String> db_total_key_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_key_passes  = new ArrayList<String>();
    ArrayList<String> db_total_vertical_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_vertical_passes  = new ArrayList<String>();
    ArrayList<String> db_total_long_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_long_passes  = new ArrayList<String>();
    ArrayList<String> db_total_dribbles  = new ArrayList<String>();
    ArrayList<String> db_total_successful_dribbles  = new ArrayList<String>();
    ArrayList<String> db_total_interceptions  = new ArrayList<String>();
    ArrayList<String> db_total_defensive_actions  = new ArrayList<String>();
    ArrayList<String> db_total_successful_defensive_action  = new ArrayList<String>();
    ArrayList<String> db_total_attacking_actions  = new ArrayList<String>();
    ArrayList<String> db_total_successful_attacking_actions  = new ArrayList<String>();
    ArrayList<String> db_total_free_kicks  = new ArrayList<String>();
    ArrayList<String> db_total_free_kicks_on_target  = new ArrayList<String>();
    ArrayList<String> db_total_direct_free_kicks  = new ArrayList<String>();
    ArrayList<String> db_total_direct_free_kicks_on_target  = new ArrayList<String>();
    ArrayList<String> db_total_corners  = new ArrayList<String>();
    ArrayList<String> db_total_successful_penalties  = new ArrayList<String>();
    ArrayList<String> db_total_successful_linkup_plays  = new ArrayList<String>();
    ArrayList<String> db_total_accelerations  = new ArrayList<String>();
    ArrayList<String> db_total_pressing_duels  = new ArrayList<String>();
    ArrayList<String> db_total_pressing_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_loose_ball_duels  = new ArrayList<String>();
    ArrayList<String> db_total_loose_ball_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_missed_balls  = new ArrayList<String>();
    ArrayList<String> db_total_shot_assists  = new ArrayList<String>();
    ArrayList<String> db_total_shot_on_target_assists  = new ArrayList<String>();
    ArrayList<String> db_total_recoveries  = new ArrayList<String>();
    ArrayList<String> db_total_opponent_half_recoveries  = new ArrayList<String>();
    ArrayList<String> db_total_dangerous_opponent_half_recoveries  = new ArrayList<String>();
    ArrayList<String> db_total_losses  = new ArrayList<String>();
    ArrayList<String> db_total_own_half_losses  = new ArrayList<String>();
    ArrayList<String> db_total_dangerous_own_half_losses  = new ArrayList<String>();
    ArrayList<String> db_total_xg_shot  = new ArrayList<String>();
    ArrayList<String> db_total_xg_assist  = new ArrayList<String>();
    ArrayList<String> db_total_xg_save  = new ArrayList<String>();
    ArrayList<String> db_total_received_pass  = new ArrayList<String>();
    ArrayList<String> db_total_touch_in_box  = new ArrayList<String>();
    ArrayList<String> db_total_progressive_run  = new ArrayList<String>();
    ArrayList<String> db_total_offsides  = new ArrayList<String>();
    ArrayList<String> db_total_clearances  = new ArrayList<String>();
    ArrayList<String> db_total_second_assists  = new ArrayList<String>();
    ArrayList<String> db_total_third_assists  = new ArrayList<String>();
    ArrayList<String> db_total_shots_blocked  = new ArrayList<String>();
    ArrayList<String> db_total_fouls_suffered  = new ArrayList<String>();
    ArrayList<String> db_total_progressive_passes  = new ArrayList<String>();
    ArrayList<String> db_total_counterpressing_recoveries  = new ArrayList<String>();
    ArrayList<String> db_total_sliding_tackles  = new ArrayList<String>();
    ArrayList<String> db_total_goal_kicks  = new ArrayList<String>();
    ArrayList<String> db_total_dribbles_against  = new ArrayList<String>();
    ArrayList<String> db_total_dribbles_against_won  = new ArrayList<String>();
    ArrayList<String> db_total_goal_kicks_short  = new ArrayList<String>();
    ArrayList<String> db_total_goal_kicks_long  = new ArrayList<String>();
    ArrayList<String> db_total_shots_on_target  = new ArrayList<String>();
    ArrayList<String> db_total_successful_progressive_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_sliding_tackles  = new ArrayList<String>();
    ArrayList<String> db_total_successful_goal_kicks  = new ArrayList<String>();
    ArrayList<String> db_total_field_aerial_duels  = new ArrayList<String>();
    ArrayList<String> db_total_field_aerial_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_gk_clean_sheets  = new ArrayList<String>();
    ArrayList<String> db_total_gk_conceded_goals  = new ArrayList<String>();
    ArrayList<String> db_total_gk_shots_against  = new ArrayList<String>();
    ArrayList<String> db_total_gk_exits  = new ArrayList<String>();
    ArrayList<String> db_total_gk_successful_exits  = new ArrayList<String>();
    ArrayList<String> db_total_gk_aerial_duels  = new ArrayList<String>();
    ArrayList<String> db_total_gk_aerial_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_gk_saves  = new ArrayList<String>();
    ArrayList<String> db_total_new_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_new_defensive_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_new_offensive_duels_won  = new ArrayList<String>();
    ArrayList<String> db_total_new_successful_dribbles  = new ArrayList<String>();
    ArrayList<String> db_total_lateral_passes  = new ArrayList<String>();
    ArrayList<String> db_total_successful_lateral_passes  = new ArrayList<String>();
    ArrayList<String> db_average_passlength  = new ArrayList<String>();
    ArrayList<String> db_average_longpasslength  = new ArrayList<String>();
    ArrayList<String> db_average_dribbledistancefromopponentgoal  = new ArrayList<String>();
    ArrayList<String> db_average_ballrecoveries  = new ArrayList<String>();
    ArrayList<String> db_average_duels  = new ArrayList<String>();
    ArrayList<String> db_average_defensiveduels  = new ArrayList<String>();
    ArrayList<String> db_average_offensiveduels  = new ArrayList<String>();
    ArrayList<String> db_average_aerialduels  = new ArrayList<String>();
    ArrayList<String> db_average_fouls  = new ArrayList<String>();
    ArrayList<String> db_average_goals  = new ArrayList<String>();
    ArrayList<String> db_average_assists  = new ArrayList<String>();
    ArrayList<String> db_average_passes  = new ArrayList<String>();
    ArrayList<String> db_average_smartpasses  = new ArrayList<String>();
    ArrayList<String> db_average_passestofinalthird  = new ArrayList<String>();
    ArrayList<String> db_average_crosses  = new ArrayList<String>();
    ArrayList<String> db_average_dribbles  = new ArrayList<String>();
    ArrayList<String> db_average_shots  = new ArrayList<String>();
    ArrayList<String> db_average_headshots  = new ArrayList<String>();
    ArrayList<String> db_average_interceptions  = new ArrayList<String>();
    ArrayList<String> db_average_successfuldefensiveaction  = new ArrayList<String>();
    ArrayList<String> db_average_yellowcards  = new ArrayList<String>();
    ArrayList<String> db_average_redcards  = new ArrayList<String>();
    ArrayList<String> db_average_directredcards  = new ArrayList<String>();
    ArrayList<String> db_average_successfulattackingactions  = new ArrayList<String>();
    ArrayList<String> db_average_freekicks  = new ArrayList<String>();
    ArrayList<String> db_average_directfreekicks  = new ArrayList<String>();
    ArrayList<String> db_average_corners  = new ArrayList<String>();
    ArrayList<String> db_average_penalties  = new ArrayList<String>();
    ArrayList<String> db_average_accelerations  = new ArrayList<String>();
    ArrayList<String> db_average_looseballduels  = new ArrayList<String>();
    ArrayList<String> db_average_missedballs  = new ArrayList<String>();
    ArrayList<String> db_average_forwardpasses  = new ArrayList<String>();
    ArrayList<String> db_average_backpasses  = new ArrayList<String>();
    ArrayList<String> db_average_throughpasses  = new ArrayList<String>();
    ArrayList<String> db_average_keypasses  = new ArrayList<String>();
    ArrayList<String> db_average_verticalpasses  = new ArrayList<String>();
    ArrayList<String> db_average_longpasses  = new ArrayList<String>();
    ArrayList<String> db_average_shotassists  = new ArrayList<String>();
    ArrayList<String> db_average_shotontargetassists  = new ArrayList<String>();
    ArrayList<String> db_average_linkupplays  = new ArrayList<String>();
    ArrayList<String> db_average_opponenthalfrecoveries  = new ArrayList<String>();
    ArrayList<String> db_average_dangerousopponenthalfrecoveries  = new ArrayList<String>();
    ArrayList<String> db_average_balllosses  = new ArrayList<String>();
    ArrayList<String> db_average_losses  = new ArrayList<String>();
    ArrayList<String> db_average_ownhalflosses  = new ArrayList<String>();
    ArrayList<String> db_average_dangerousownhalflosses  = new ArrayList<String>();
    ArrayList<String> db_average_duelswon  = new ArrayList<String>();
    ArrayList<String> db_average_defensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_offensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_successfulpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulsmartpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulcrosses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulforwardpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulbackpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulthroughpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulkeypasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulverticalpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfullongpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfuldribbles  = new ArrayList<String>();
    ArrayList<String> db_average_defensiveactions  = new ArrayList<String>();
    ArrayList<String> db_average_attackingactions  = new ArrayList<String>();
    ArrayList<String> db_average_freekicksontarget  = new ArrayList<String>();
    ArrayList<String> db_average_directfreekicksontarget  = new ArrayList<String>();
    ArrayList<String> db_average_successfulpenalties  = new ArrayList<String>();
    ArrayList<String> db_average_successfullinkupplays  = new ArrayList<String>();
    ArrayList<String> db_average_looseballduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_successfulpassestofinalthird  = new ArrayList<String>();
    ArrayList<String> db_average_xgshot  = new ArrayList<String>();
    ArrayList<String> db_average_xgassist  = new ArrayList<String>();
    ArrayList<String> db_average_xgsave  = new ArrayList<String>();
    ArrayList<String> db_average_receivedpass  = new ArrayList<String>();
    ArrayList<String> db_average_touchinbox  = new ArrayList<String>();
    ArrayList<String> db_average_progressiverun  = new ArrayList<String>();
    ArrayList<String> db_average_offsides  = new ArrayList<String>();
    ArrayList<String> db_average_clearances  = new ArrayList<String>();
    ArrayList<String> db_average_secondassists  = new ArrayList<String>();
    ArrayList<String> db_average_thirdassists  = new ArrayList<String>();
    ArrayList<String> db_average_foulssuffered  = new ArrayList<String>();
    ArrayList<String> db_average_progressivepasses  = new ArrayList<String>();
    ArrayList<String> db_average_counterpressingrecoveries  = new ArrayList<String>();
    ArrayList<String> db_average_slidingtackles  = new ArrayList<String>();
    ArrayList<String> db_average_goalkicks  = new ArrayList<String>();
    ArrayList<String> db_average_shotsblocked  = new ArrayList<String>();
    ArrayList<String> db_average_shotsontarget  = new ArrayList<String>();
    ArrayList<String> db_average_successfulprogressivepasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfulslidingtackles  = new ArrayList<String>();
    ArrayList<String> db_average_successfulgoalkicks  = new ArrayList<String>();
    ArrayList<String> db_average_dribblesagainst  = new ArrayList<String>();
    ArrayList<String> db_average_dribblesagainstwon  = new ArrayList<String>();
    ArrayList<String> db_average_goalkicksshort  = new ArrayList<String>();
    ArrayList<String> db_average_goalkickslong  = new ArrayList<String>();
    ArrayList<String> db_average_fieldaerialduels  = new ArrayList<String>();
    ArrayList<String> db_average_fieldaerialduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_gkconcededgoals  = new ArrayList<String>();
    ArrayList<String> db_average_gkshotsagainst  = new ArrayList<String>();
    ArrayList<String> db_average_gkexits  = new ArrayList<String>();
    ArrayList<String> db_average_gkaerialduels  = new ArrayList<String>();
    ArrayList<String> db_average_gksaves  = new ArrayList<String>();
    ArrayList<String> db_average_gksuccessfulexits  = new ArrayList<String>();
    ArrayList<String> db_average_gkaerialduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_newduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_newdefensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_newoffensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_average_newsuccessfuldribbles  = new ArrayList<String>();
    ArrayList<String> db_average_lateralpasses  = new ArrayList<String>();
    ArrayList<String> db_average_successfullateralpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_duelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_defensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_offensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_aerialduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulsmartpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulpassestofinalthird  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulcrosses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfuldribbles  = new ArrayList<String>();
    ArrayList<String> db_percent_shotsontarget  = new ArrayList<String>();
    ArrayList<String> db_percent_headshotsontarget  = new ArrayList<String>();
    ArrayList<String> db_percent_goalconversion  = new ArrayList<String>();
    ArrayList<String> db_percent_directfreekicksontarget  = new ArrayList<String>();
    ArrayList<String> db_percent_penaltiesconversion  = new ArrayList<String>();
    ArrayList<String> db_percent_win  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulforwardpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulbackpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulthroughpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulkeypasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulverticalpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfullongpasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulshotassists  = new ArrayList<String>();
    ArrayList<String> db_percent_successfullinkupplays  = new ArrayList<String>();
    ArrayList<String> db_percent_yellowcardsperfoul  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulprogressivepasses  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulslidingtackles  = new ArrayList<String>();
    ArrayList<String> db_percent_successfulgoalkicks  = new ArrayList<String>();
    ArrayList<String> db_percent_dribblesagainstwon  = new ArrayList<String>();
    ArrayList<String> db_percent_fieldaerialduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_gksaves  = new ArrayList<String>();
    ArrayList<String> db_percent_gksuccessfulexits  = new ArrayList<String>();
    ArrayList<String> db_percent_gkaerialduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_newduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_newdefensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_newoffensiveduelswon  = new ArrayList<String>();
    ArrayList<String> db_percent_newsuccessfuldribbles  = new ArrayList<String>();
    ArrayList<String> db_percent_successfullateralpasses  = new ArrayList<String>();
    ArrayList<String> db_roundid  = new ArrayList<String>();
    ArrayList<String> api_playerid  = new ArrayList<String>();
    ArrayList<String> api_competitionid  = new ArrayList<String>();
    ArrayList<String> api_seasonid  = new ArrayList<String>();
    ArrayList<String> api_position_name  = new ArrayList<String>();
    ArrayList<String> api_position_code  = new ArrayList<String>();
    ArrayList<String> api_position_percent  = new ArrayList<String>();
    ArrayList<String> api_total_matches  = new ArrayList<String>();
    ArrayList<String> api_total_matches_in_start  = new ArrayList<String>();
    ArrayList<String> api_total_matches_substituted  = new ArrayList<String>();
    ArrayList<String> api_total_matches_coming_off  = new ArrayList<String>();
    ArrayList<String> api_total_minutes_on_field  = new ArrayList<String>();
    ArrayList<String> api_total_minutes_tagged  = new ArrayList<String>();
    ArrayList<String> api_total_goals  = new ArrayList<String>();
    ArrayList<String> api_total_assists  = new ArrayList<String>();
    ArrayList<String> api_total_shots  = new ArrayList<String>();
    ArrayList<String> api_total_head_shots  = new ArrayList<String>();
    ArrayList<String> api_total_yellow_cards  = new ArrayList<String>();
    ArrayList<String> api_total_red_cards  = new ArrayList<String>();
    ArrayList<String> api_total_direct_red_cards  = new ArrayList<String>();
    ArrayList<String> api_total_penalties  = new ArrayList<String>();
    ArrayList<String> api_total_linkup_plays  = new ArrayList<String>();
    ArrayList<String> api_total_duels  = new ArrayList<String>();
    ArrayList<String> api_total_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_defensive_duels  = new ArrayList<String>();
    ArrayList<String> api_total_defensive_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_offensive_duels  = new ArrayList<String>();
    ArrayList<String> api_total_offensive_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_aerial_duels  = new ArrayList<String>();
    ArrayList<String> api_total_aerial_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_fouls  = new ArrayList<String>();
    ArrayList<String> api_total_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_passes  = new ArrayList<String>();
    ArrayList<String> api_total_smart_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_smart_passes  = new ArrayList<String>();
    ArrayList<String> api_total_passes_to_final_third  = new ArrayList<String>();
    ArrayList<String> api_total_successful_passes_to_final_third  = new ArrayList<String>();
    ArrayList<String> api_total_crosses  = new ArrayList<String>();
    ArrayList<String> api_total_successful_crosses  = new ArrayList<String>();
    ArrayList<String> api_total_forward_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_forward_passes  = new ArrayList<String>();
    ArrayList<String> api_total_back_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_back_passes  = new ArrayList<String>();
    ArrayList<String> api_total_through_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_through_passes  = new ArrayList<String>();
    ArrayList<String> api_total_key_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_key_passes  = new ArrayList<String>();
    ArrayList<String> api_total_vertical_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_vertical_passes  = new ArrayList<String>();
    ArrayList<String> api_total_long_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_long_passes  = new ArrayList<String>();
    ArrayList<String> api_total_dribbles  = new ArrayList<String>();
    ArrayList<String> api_total_successful_dribbles  = new ArrayList<String>();
    ArrayList<String> api_total_interceptions  = new ArrayList<String>();
    ArrayList<String> api_total_defensive_actions  = new ArrayList<String>();
    ArrayList<String> api_total_successful_defensive_action  = new ArrayList<String>();
    ArrayList<String> api_total_attacking_actions  = new ArrayList<String>();
    ArrayList<String> api_total_successful_attacking_actions  = new ArrayList<String>();
    ArrayList<String> api_total_free_kicks  = new ArrayList<String>();
    ArrayList<String> api_total_free_kicks_on_target  = new ArrayList<String>();
    ArrayList<String> api_total_direct_free_kicks  = new ArrayList<String>();
    ArrayList<String> api_total_direct_free_kicks_on_target  = new ArrayList<String>();
    ArrayList<String> api_total_corners  = new ArrayList<String>();
    ArrayList<String> api_total_successful_penalties  = new ArrayList<String>();
    ArrayList<String> api_total_successful_linkup_plays  = new ArrayList<String>();
    ArrayList<String> api_total_accelerations  = new ArrayList<String>();
    ArrayList<String> api_total_pressing_duels  = new ArrayList<String>();
    ArrayList<String> api_total_pressing_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_loose_ball_duels  = new ArrayList<String>();
    ArrayList<String> api_total_loose_ball_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_missed_balls  = new ArrayList<String>();
    ArrayList<String> api_total_shot_assists  = new ArrayList<String>();
    ArrayList<String> api_total_shot_on_target_assists  = new ArrayList<String>();
    ArrayList<String> api_total_recoveries  = new ArrayList<String>();
    ArrayList<String> api_total_opponent_half_recoveries  = new ArrayList<String>();
    ArrayList<String> api_total_dangerous_opponent_half_recoveries  = new ArrayList<String>();
    ArrayList<String> api_total_losses  = new ArrayList<String>();
    ArrayList<String> api_total_own_half_losses  = new ArrayList<String>();
    ArrayList<String> api_total_dangerous_own_half_losses  = new ArrayList<String>();
    ArrayList<String> api_total_xg_shot  = new ArrayList<String>();
    ArrayList<String> api_total_xg_assist  = new ArrayList<String>();
    ArrayList<String> api_total_xg_save  = new ArrayList<String>();
    ArrayList<String> api_total_received_pass  = new ArrayList<String>();
    ArrayList<String> api_total_touch_in_box  = new ArrayList<String>();
    ArrayList<String> api_total_progressive_run  = new ArrayList<String>();
    ArrayList<String> api_total_offsides  = new ArrayList<String>();
    ArrayList<String> api_total_clearances  = new ArrayList<String>();
    ArrayList<String> api_total_second_assists  = new ArrayList<String>();
    ArrayList<String> api_total_third_assists  = new ArrayList<String>();
    ArrayList<String> api_total_shots_blocked  = new ArrayList<String>();
    ArrayList<String> api_total_fouls_suffered  = new ArrayList<String>();
    ArrayList<String> api_total_progressive_passes  = new ArrayList<String>();
    ArrayList<String> api_total_counterpressing_recoveries  = new ArrayList<String>();
    ArrayList<String> api_total_sliding_tackles  = new ArrayList<String>();
    ArrayList<String> api_total_goal_kicks  = new ArrayList<String>();
    ArrayList<String> api_total_dribbles_against  = new ArrayList<String>();
    ArrayList<String> api_total_dribbles_against_won  = new ArrayList<String>();
    ArrayList<String> api_total_goal_kicks_short  = new ArrayList<String>();
    ArrayList<String> api_total_goal_kicks_long  = new ArrayList<String>();
    ArrayList<String> api_total_shots_on_target  = new ArrayList<String>();
    ArrayList<String> api_total_successful_progressive_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_sliding_tackles  = new ArrayList<String>();
    ArrayList<String> api_total_successful_goal_kicks  = new ArrayList<String>();
    ArrayList<String> api_total_field_aerial_duels  = new ArrayList<String>();
    ArrayList<String> api_total_field_aerial_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_gk_clean_sheets  = new ArrayList<String>();
    ArrayList<String> api_total_gk_conceded_goals  = new ArrayList<String>();
    ArrayList<String> api_total_gk_shots_against  = new ArrayList<String>();
    ArrayList<String> api_total_gk_exits  = new ArrayList<String>();
    ArrayList<String> api_total_gk_successful_exits  = new ArrayList<String>();
    ArrayList<String> api_total_gk_aerial_duels  = new ArrayList<String>();
    ArrayList<String> api_total_gk_aerial_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_gk_saves  = new ArrayList<String>();
    ArrayList<String> api_total_new_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_new_defensive_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_new_offensive_duels_won  = new ArrayList<String>();
    ArrayList<String> api_total_new_successful_dribbles  = new ArrayList<String>();
    ArrayList<String> api_total_lateral_passes  = new ArrayList<String>();
    ArrayList<String> api_total_successful_lateral_passes  = new ArrayList<String>();
    ArrayList<String> api_average_passlength  = new ArrayList<String>();
    ArrayList<String> api_average_longpasslength  = new ArrayList<String>();
    ArrayList<String> api_average_dribbledistancefromopponentgoal  = new ArrayList<String>();
    ArrayList<String> api_average_ballrecoveries  = new ArrayList<String>();
    ArrayList<String> api_average_duels  = new ArrayList<String>();
    ArrayList<String> api_average_defensiveduels  = new ArrayList<String>();
    ArrayList<String> api_average_offensiveduels  = new ArrayList<String>();
    ArrayList<String> api_average_aerialduels  = new ArrayList<String>();
    ArrayList<String> api_average_fouls  = new ArrayList<String>();
    ArrayList<String> api_average_goals  = new ArrayList<String>();
    ArrayList<String> api_average_assists  = new ArrayList<String>();
    ArrayList<String> api_average_passes  = new ArrayList<String>();
    ArrayList<String> api_average_smartpasses  = new ArrayList<String>();
    ArrayList<String> api_average_passestofinalthird  = new ArrayList<String>();
    ArrayList<String> api_average_crosses  = new ArrayList<String>();
    ArrayList<String> api_average_dribbles  = new ArrayList<String>();
    ArrayList<String> api_average_shots  = new ArrayList<String>();
    ArrayList<String> api_average_headshots  = new ArrayList<String>();
    ArrayList<String> api_average_interceptions  = new ArrayList<String>();
    ArrayList<String> api_average_successfuldefensiveaction  = new ArrayList<String>();
    ArrayList<String> api_average_yellowcards  = new ArrayList<String>();
    ArrayList<String> api_average_redcards  = new ArrayList<String>();
    ArrayList<String> api_average_directredcards  = new ArrayList<String>();
    ArrayList<String> api_average_successfulattackingactions  = new ArrayList<String>();
    ArrayList<String> api_average_freekicks  = new ArrayList<String>();
    ArrayList<String> api_average_directfreekicks  = new ArrayList<String>();
    ArrayList<String> api_average_corners  = new ArrayList<String>();
    ArrayList<String> api_average_penalties  = new ArrayList<String>();
    ArrayList<String> api_average_accelerations  = new ArrayList<String>();
    ArrayList<String> api_average_looseballduels  = new ArrayList<String>();
    ArrayList<String> api_average_missedballs  = new ArrayList<String>();
    ArrayList<String> api_average_forwardpasses  = new ArrayList<String>();
    ArrayList<String> api_average_backpasses  = new ArrayList<String>();
    ArrayList<String> api_average_throughpasses  = new ArrayList<String>();
    ArrayList<String> api_average_keypasses  = new ArrayList<String>();
    ArrayList<String> api_average_verticalpasses  = new ArrayList<String>();
    ArrayList<String> api_average_longpasses  = new ArrayList<String>();
    ArrayList<String> api_average_shotassists  = new ArrayList<String>();
    ArrayList<String> api_average_shotontargetassists  = new ArrayList<String>();
    ArrayList<String> api_average_linkupplays  = new ArrayList<String>();
    ArrayList<String> api_average_opponenthalfrecoveries  = new ArrayList<String>();
    ArrayList<String> api_average_dangerousopponenthalfrecoveries  = new ArrayList<String>();
    ArrayList<String> api_average_balllosses  = new ArrayList<String>();
    ArrayList<String> api_average_losses  = new ArrayList<String>();
    ArrayList<String> api_average_ownhalflosses  = new ArrayList<String>();
    ArrayList<String> api_average_dangerousownhalflosses  = new ArrayList<String>();
    ArrayList<String> api_average_duelswon  = new ArrayList<String>();
    ArrayList<String> api_average_defensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_offensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_successfulpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulsmartpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulcrosses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulforwardpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulbackpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulthroughpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulkeypasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulverticalpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfullongpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfuldribbles  = new ArrayList<String>();
    ArrayList<String> api_average_defensiveactions  = new ArrayList<String>();
    ArrayList<String> api_average_attackingactions  = new ArrayList<String>();
    ArrayList<String> api_average_freekicksontarget  = new ArrayList<String>();
    ArrayList<String> api_average_directfreekicksontarget  = new ArrayList<String>();
    ArrayList<String> api_average_successfulpenalties  = new ArrayList<String>();
    ArrayList<String> api_average_successfullinkupplays  = new ArrayList<String>();
    ArrayList<String> api_average_looseballduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_successfulpassestofinalthird  = new ArrayList<String>();
    ArrayList<String> api_average_xgshot  = new ArrayList<String>();
    ArrayList<String> api_average_xgassist  = new ArrayList<String>();
    ArrayList<String> api_average_xgsave  = new ArrayList<String>();
    ArrayList<String> api_average_receivedpass  = new ArrayList<String>();
    ArrayList<String> api_average_touchinbox  = new ArrayList<String>();
    ArrayList<String> api_average_progressiverun  = new ArrayList<String>();
    ArrayList<String> api_average_offsides  = new ArrayList<String>();
    ArrayList<String> api_average_clearances  = new ArrayList<String>();
    ArrayList<String> api_average_secondassists  = new ArrayList<String>();
    ArrayList<String> api_average_thirdassists  = new ArrayList<String>();
    ArrayList<String> api_average_foulssuffered  = new ArrayList<String>();
    ArrayList<String> api_average_progressivepasses  = new ArrayList<String>();
    ArrayList<String> api_average_counterpressingrecoveries  = new ArrayList<String>();
    ArrayList<String> api_average_slidingtackles  = new ArrayList<String>();
    ArrayList<String> api_average_goalkicks  = new ArrayList<String>();
    ArrayList<String> api_average_shotsblocked  = new ArrayList<String>();
    ArrayList<String> api_average_shotsontarget  = new ArrayList<String>();
    ArrayList<String> api_average_successfulprogressivepasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfulslidingtackles  = new ArrayList<String>();
    ArrayList<String> api_average_successfulgoalkicks  = new ArrayList<String>();
    ArrayList<String> api_average_dribblesagainst  = new ArrayList<String>();
    ArrayList<String> api_average_dribblesagainstwon  = new ArrayList<String>();
    ArrayList<String> api_average_goalkicksshort  = new ArrayList<String>();
    ArrayList<String> api_average_goalkickslong  = new ArrayList<String>();
    ArrayList<String> api_average_fieldaerialduels  = new ArrayList<String>();
    ArrayList<String> api_average_fieldaerialduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_gkconcededgoals  = new ArrayList<String>();
    ArrayList<String> api_average_gkshotsagainst  = new ArrayList<String>();
    ArrayList<String> api_average_gkexits  = new ArrayList<String>();
    ArrayList<String> api_average_gkaerialduels  = new ArrayList<String>();
    ArrayList<String> api_average_gksaves  = new ArrayList<String>();
    ArrayList<String> api_average_gksuccessfulexits  = new ArrayList<String>();
    ArrayList<String> api_average_gkaerialduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_newduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_newdefensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_newoffensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_average_newsuccessfuldribbles  = new ArrayList<String>();
    ArrayList<String> api_average_lateralpasses  = new ArrayList<String>();
    ArrayList<String> api_average_successfullateralpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_duelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_defensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_offensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_aerialduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulsmartpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulpassestofinalthird  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulcrosses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfuldribbles  = new ArrayList<String>();
    ArrayList<String> api_percent_shotsontarget  = new ArrayList<String>();
    ArrayList<String> api_percent_headshotsontarget  = new ArrayList<String>();
    ArrayList<String> api_percent_goalconversion  = new ArrayList<String>();
    ArrayList<String> api_percent_directfreekicksontarget  = new ArrayList<String>();
    ArrayList<String> api_percent_penaltiesconversion  = new ArrayList<String>();
    ArrayList<String> api_percent_win  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulforwardpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulbackpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulthroughpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulkeypasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulverticalpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfullongpasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulshotassists  = new ArrayList<String>();
    ArrayList<String> api_percent_successfullinkupplays  = new ArrayList<String>();
    ArrayList<String> api_percent_yellowcardsperfoul  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulprogressivepasses  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulslidingtackles  = new ArrayList<String>();
    ArrayList<String> api_percent_successfulgoalkicks  = new ArrayList<String>();
    ArrayList<String> api_percent_dribblesagainstwon  = new ArrayList<String>();
    ArrayList<String> api_percent_fieldaerialduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_gksaves  = new ArrayList<String>();
    ArrayList<String> api_percent_gksuccessfulexits  = new ArrayList<String>();
    ArrayList<String> api_percent_gkaerialduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_newduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_newdefensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_newoffensiveduelswon  = new ArrayList<String>();
    ArrayList<String> api_percent_newsuccessfuldribbles  = new ArrayList<String>();
    ArrayList<String> api_percent_successfullateralpasses  = new ArrayList<String>();
    ArrayList<String> api_roundid  = new ArrayList<String>();

}

