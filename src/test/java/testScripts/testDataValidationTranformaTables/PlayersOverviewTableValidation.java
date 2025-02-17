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
public class PlayersOverviewTableValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(PlayersOverviewTableValidation.class);
    @Title("Verify 'Players Overview' table is having all PlayerID as compared with playerAdvanceStats table")
    @Tag("PlayersOverview")
    @Test
    public void playersOverviewPlayerIDRecordValidation() throws Exception {
        Config.setConfigs();
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        //Fetching playerid record of player advance table
        String playerAdvanceColumnName1 = "playerid";
        String playerAdvanceSQLQuery="SELECT playerid FROM raw.alain_wyscout.player_advancestats INNER JOIN raw.alain_wyscout.season " +
                "ON raw.alain_wyscout.player_advancestats.seasonid = raw.alain_wyscout.season.seasonid " +
                "WHERE raw.alain_wyscout.season.active = 'true'";
        logger.info("SQL QUERY of Player Advance table: " + playerAdvanceSQLQuery);
        ArrayList<String> playerAdvanceResult = tableMetadata.fetchTableDataByQuery(playerAdvanceSQLQuery);
        //Fetching playerid record of players overview table
        String playerOverviewColumnName1 = "playerid";
        String playerOverviewSQLQuery = "Select " + playerAdvanceColumnName1 +" from " + GlbVar.catalogs_tranformed + "."
                + GlbVar.schemas_tranformed + "." + GlbVar.players_overview_table;
        logger.info("SQL QUERY of Player Overview table: " + playerAdvanceSQLQuery);
        ArrayList<String> playerOverviewResult = tableMetadata.fetchTableDataByQuery(playerOverviewSQLQuery);
        logger.info("Missing player id in Player Overview Table: "+GenericFun.missingDataInSecondList(playerAdvanceResult,playerOverviewResult));
        AssertHelpers.softAssertMatchTwoList(GenericFun.getUniqueList(playerAdvanceResult),playerOverviewResult);
        AssertHelpers.softAssertAll();
    }

    @Tag("PlayersOverview")
    @Test
    @Title("Verify duplicate entry for Players Overview Table")
    public void playersOverviewTableDuplicateRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalogs_tranformed, GlbVar.schemas_tranformed, GlbVar.players_overview_table);
        logger.info("Fetching duplicate value for 'player overview' table and column name is '" + columnNames.get(1));
        ArrayList<String> db_playerId = tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalogs_tranformed, GlbVar.schemas_tranformed, GlbVar.players_overview_table
                , columnNames.get(1), null, null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_playerId);
        AssertHelpers.softAssertAll();
    }
}