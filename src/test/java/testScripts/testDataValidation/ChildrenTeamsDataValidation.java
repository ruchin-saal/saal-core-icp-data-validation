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

import java.util.ArrayList;
import java.util.List;

@Tag("SanityTest")
public class ChildrenTeamsDataValidation extends BaseClass {
    private static final Logger logger = LogManager.getLogger(ChildrenTeamsDataValidation.class);

    @Test
    @Title("Verify Children table Sample Data")
    @Tag("ChildrenTeams")
    public void teamsChildrenSampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        String[] competionArray = GlbVar.competitionIdList.split(",\\s*");
        int competionIDSize;
        if (GlbVar.competitionIdSize.equals("all")) {
            competionIDSize = competionArray.length;
        } else {
            competionIDSize = Integer.parseInt(GlbVar.competitionIdSize);
        }
        Response response;
        ArrayList<String> api_teamid = new ArrayList<String>();
        ArrayList<String> api_name = new ArrayList<String>();
        ArrayList<String> api_parent_teamid = new ArrayList<String>();
        ArrayList<String> api_competitionid = new ArrayList<String>();

        List<String> final_competitionID = new ArrayList<String>();
        logger.info("Fetching data for following competition ID: ");
        for (int h = 0; h < competionIDSize; h++) {
            logger.info(competionArray[h]);
            final_competitionID.add(competionArray[h]);
            String competition_endPoint = "/v3/competitions/" + competionArray[h] + "/teams";
            Thread.sleep(2000);
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            TeamsResponse teamsResponse = response.as(TeamsResponse.class);
            for (int i = 0; i < teamsResponse.getTeams().size(); i++) {
                if (teamsResponse.getTeams().get(i).getChildren() != null) {
                    for (int j = 0; j < teamsResponse.getTeams().get(i).getChildren().size(); j++) {
                        api_teamid.add(String.valueOf(teamsResponse.getTeams().get(i).getChildren().get(j).getWyId()));
                        api_name.add(String.valueOf(teamsResponse.getTeams().get(i).getChildren().get(j).getName()));
                        api_parent_teamid.add(String.valueOf(teamsResponse.getTeams().get(i).getWyId()));
                        api_competitionid.add(competionArray[h]);
                    }
                }
            }
        }
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.children_teams_tablename;
        String whereClauseColumnName = "competitionid";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        logger.info("DB column names: " + columnNames);
        ArrayList<String> db_teamid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(0), whereClauseColumnName, String.valueOf(api_competitionid)));
        ArrayList<String> db_name = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(1), whereClauseColumnName, String.valueOf(api_competitionid)));
        ArrayList<String> db_parent_teamid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(2), whereClauseColumnName, String.valueOf(api_competitionid)));
        ArrayList<String> db_competitionid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(3), whereClauseColumnName, String.valueOf(api_competitionid)));
        logger.info("Executing for the db_teamid column");
        AssertHelpers.softAssertTrue(api_teamid, db_teamid);
        logger.info("Executing for the db_name column");
        AssertHelpers.softAssertTrue(api_name, db_name);
        logger.info("Executing for the db_parent_teamid column");
        AssertHelpers.softAssertTrue(api_parent_teamid, db_parent_teamid);
        logger.info("Executing for the db_competitionid column");
        AssertHelpers.softAssertTrue(api_competitionid, db_competitionid);
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify Children table duplicate records")
    @Tag("ChildrenTeams")
    public void teamsChildrenTableDuplicateRecords() throws Exception {
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.children_teams_tablename);
        logger.info("Fetching duplicate value for 'teams' table and column name is '" + columnNames.get(0) + "' and " + columnNames.get(1));
        ArrayList<String> db_teamid = tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.children_teams_tablename
                , columnNames.get(0), columnNames.get(1), columnNames.get(3));
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_teamid);
    }

    @Test
    @Title("Verify Children table Number Of Records")
    @Tag("ChildrenTeams")
    public void teamsChildrenNumberOfRecordsValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        String[] competionArray = GlbVar.competitionIdList.split(",\\s*");
        Response response;
        ArrayList<String> api_teamid = new ArrayList<String>();
        ArrayList<String> api_competitionid = new ArrayList<String>();
        for (int h = 0; h < competionArray.length; h++) {
            String competition_endPoint = "/v3/competitions/" + competionArray[h] + "/teams";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode());
            TeamsResponse teamsResponse = response.as(TeamsResponse.class);
            for (int i = 0; i < teamsResponse.getTeams().size(); i++) {
                if (teamsResponse.getTeams().get(i).getChildren() != null) {
                    for (int j = 0; j < teamsResponse.getTeams().get(i).getChildren().size(); j++) {
                        api_teamid.add("{"+String.valueOf(teamsResponse.getTeams().get(i).getChildren().get(j).getWyId() + ", " + competionArray[h]+"}"));
                        api_competitionid.add(competionArray[h]);
                    }
                }
            }

        }
        logger.info("All competitionId is: "+GenericFun.removeDuplicateDataFromList(api_competitionid));

        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.children_teams_tablename;
        String columnName = "(teamid, competitionid) as TeamAndCompetitionID";
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        System.out.println("DB column names: " + columnNames);
        String sqlQuery = "Select " + columnName + " from " + GlbVar.catalog_name + "."
                + GlbVar.schemas_name + "." + GlbVar.children_teams_tablename;
        ArrayList<String> db_teamid = tableMetadata.fetchTableDataByQuery(sqlQuery);
        AssertHelpers.softAssertTrue(api_teamid, db_teamid);
        logger.info("Missing data in DB List: " + GenericFun.missingDataInSecondList(api_teamid, db_teamid));
        logger.info("Missing data in API List: " + GenericFun.missingDataInSecondList(db_teamid, api_teamid));
        AssertHelpers.softAssertAll();
    }
}