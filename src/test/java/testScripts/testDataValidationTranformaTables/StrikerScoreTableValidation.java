package testScripts.testDataValidationTranformaTables;

import baseConfig.BaseClass;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utilities.*;

import java.util.ArrayList;

@Tag("SanityTest")
public class StrikerScoreTableValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(StrikerScoreTableValidation.class);

    @Tag("StrikerScore")
    @Title("Verify 'Striker Score' table is having all PlayerID as compared with playerAdvanceStats table")
    @Test
    public void strikerScorePlayerIDRecordValidation() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        //Fetching playerId record of player advance table
        String playerAdvanceColumnName1 = "playerid";
        String playerAdvanceSQLQuery="SELECT playerid FROM raw.alain_wyscout.player_advancestats INNER JOIN raw.alain_wyscout.season" +
                " ON raw.alain_wyscout.player_advancestats.seasonid = raw.alain_wyscout.season.seasonid" +
                " WHERE raw.alain_wyscout.season.active = 'true'";
        logger.info("SQL QUERY of Player Advance table: " + playerAdvanceSQLQuery);
        ArrayList<String> playerAdvanceResult = tableMetadata.fetchTableDataByQuery(playerAdvanceSQLQuery);
        //Fetching playerid record of StrikerScore table
        String strikerScoreColumnName1 = "playerid";
        String strikerScoreSQLQuery = "Select " + strikerScoreColumnName1 +" from " + GlbVar.catalogs_tranformed + "."
                + GlbVar.schemas_tranformed + "." + GlbVar.striker_scores_table;
        logger.info("SQL QUERY of Striker Score table: " + strikerScoreSQLQuery);
        ArrayList<String> strikerScoreResult = tableMetadata.fetchTableDataByQuery(strikerScoreSQLQuery);
        logger.info("Missing player id in Striker Score Table: "+GenericFun.missingDataInSecondList(playerAdvanceResult,strikerScoreResult));
        AssertHelpers.softAssertMatchTwoList(GenericFun.getUniqueList(playerAdvanceResult),strikerScoreResult);
        AssertHelpers.softAssertAll();
    }

    @Tag("StrikerScore")
    @Test
    @Title("Verify duplicate entry for Striker Score Table")
    public void strikerScoreTableDuplicateRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        String strikerScoreColumnName1 = "playerid";
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalogs_tranformed, GlbVar.schemas_tranformed, GlbVar.striker_scores_table);
        logger.info("Fetching duplicate value for 'player advance' table and column name is '" + columnNames.get(1));
        ArrayList<String> db_playerID = tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalogs_tranformed, GlbVar.schemas_tranformed, GlbVar.striker_scores_table
                , strikerScoreColumnName1, null, null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_playerID);
        AssertHelpers.softAssertAll();
    }
}
