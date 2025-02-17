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
public class CompatibilityScoresTableValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(CompatibilityScoresTableValidation.class);

    @Tag("CompatibilityScore")
    @Test
    @Title("Verify duplicate entry for Compatibility Score Table")
    public void compatibilityScoreTableDuplicateRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalogs_tranformed, GlbVar.schemas_tranformed, GlbVar.compatibility_scores_table);
        logger.info("Fetching duplicate value for 'compatibility score' table and column name is '" + columnNames.get(3));
        ArrayList<String> db_playerID = tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalogs_tranformed, GlbVar.schemas_tranformed, GlbVar.compatibility_scores_table
                , columnNames.get(3), null, null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_playerID);
        AssertHelpers.softAssertAll();
    }

    @Tag("CompatibilityScore")
    @Title("Verify 'Compatibility Scores' table is having all PlayerID as compared with playerAdvanceStats table")
    @Test
    public void compatibilityScorePlayerIDRecordValidation() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        //Fetching playerId record of player advance table
        String playerAdvanceColumnName1 = "playerid";
        String playerAdvanceSQLQuery="SELECT playerid FROM raw.alain_wyscout.player_advancestats INNER JOIN raw.alain_wyscout.season " +
                "ON raw.alain_wyscout.player_advancestats.seasonid = raw.alain_wyscout.season.seasonid " +
                "WHERE raw.alain_wyscout.season.active = 'true'";
        logger.info("SQL QUERY of Player Advance table: " + playerAdvanceSQLQuery);
        ArrayList<String> playerAdvanceResult = tableMetadata.fetchTableDataByQuery(playerAdvanceSQLQuery);
        //Fetching playerid record of Compatibility Score table
        String compatibilityScoresColumnName1 = "playerid";
        String compatibilityScoresSQLQuery = "Select " + compatibilityScoresColumnName1 +" from " + GlbVar.catalogs_tranformed + "."
                + GlbVar.schemas_tranformed + "." + GlbVar.compatibility_scores_table;
        logger.info("SQL QUERY of Compatibility Score table: " + compatibilityScoresSQLQuery);
        ArrayList<String> compatibilityScoresResult = tableMetadata.fetchTableDataByQuery(compatibilityScoresSQLQuery);
        logger.info("Missing player id in Compatibility Score Table: "+GenericFun.missingDataInSecondList(playerAdvanceResult,compatibilityScoresResult));
        AssertHelpers.softAssertMatchTwoList(GenericFun.getUniqueList(playerAdvanceResult),compatibilityScoresResult);
        AssertHelpers.softAssertAll();
    }
}