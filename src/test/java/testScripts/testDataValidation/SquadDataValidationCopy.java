package testScripts.testDataValidation;

import baseConfig.BaseClass;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Title;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import responseModels.squadResponse.SquadResponse;
import utilities.*;
import java.util.ArrayList;
import java.util.List;

@Tag("SanityTest")
public class SquadDataValidationCopy extends BaseClass {
    private static final Logger logger = LogManager.getLogger(SquadDataValidationCopy.class);
    @Test
    @Title("Verify Squad Table Sample Data")
    @Tag("Squad")
    public void sampleDataValidation() throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;
        TeamsDataValidation teamsDataValidation = new TeamsDataValidation();
        List<String> teamID = teamsDataValidation.getTeamID();
        int teamIDSize;
        teamIDSize = GlbVar.teamIdSize.equals("all") ? teamID.size() : Integer.parseInt(GlbVar.teamIdSize);
        logger.info("Fetching records for the following team id");
        for (int h = 0; h < teamIDSize; h++) {
            logger.info(teamID.get(h));
            String competition_endPoint = "/v3/teams/" + teamID.get(h) + "/squad";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
//            Assertions.assertEquals(200, response.getStatusCode(), "API '" + GlbVar.api_baseUrl + competition_endPoint + "' is down");
            SquadResponse squadResponse = response.as(SquadResponse.class);
            for (int i = 0; i < squadResponse.getSquad().size(); i++) {
                api_playerid.add(String.valueOf(squadResponse.getSquad().get(i).getWyId()));
                api_shortname.add(String.valueOf(squadResponse.getSquad().get(i).getShortName()));
                api_firstname.add(String.valueOf(squadResponse.getSquad().get(i).getFirstName()));
                api_middlename.add(String.valueOf(squadResponse.getSquad().get(i).getMiddleName()));
                api_lastname.add(String.valueOf(squadResponse.getSquad().get(i).getLastName()));
                api_height.add(String.valueOf(squadResponse.getSquad().get(i).getHeight()));
                api_weight.add(String.valueOf(squadResponse.getSquad().get(i).getWeight()));
                api_birthdate.add(String.valueOf(squadResponse.getSquad().get(i).getBirthDate()));
                api_birtharea_id.add(String.valueOf(squadResponse.getSquad().get(i).getBirthArea().getId()));
                api_passportarea_id.add(String.valueOf(squadResponse.getSquad().get(i).getPassportArea().getId()));
                api_role_name.add(String.valueOf(squadResponse.getSquad().get(i).getRole().getName()));
                api_role_code2.add(String.valueOf(squadResponse.getSquad().get(i).getRole().getCode2()));
                api_role_code3.add(String.valueOf(squadResponse.getSquad().get(i).getRole().getCode3()));
                api_foot.add(String.valueOf(squadResponse.getSquad().get(i).getFoot()));
                api_current_teamid.add(String.valueOf(squadResponse.getSquad().get(i).getCurrentTeamId()));
                api_currentnational_teamid.add(String.valueOf(squadResponse.getSquad().get(i).getCurrentNationalTeamId()));
                api_gender.add(String.valueOf(squadResponse.getSquad().get(i).getGender()));
                api_status.add(String.valueOf(squadResponse.getSquad().get(i).getStatus()));
                api_imagedataurl.add(String.valueOf(squadResponse.getSquad().get(i).getImageDataURL()));
            }
        }

        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.squad_tablename;
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        String whereClauseColumnName="current_teamid";
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        logger.info("COLUMN NAMES IN DB \n: " + columnNames);
        ArrayList<String> db_playerid = tableMetadata.fetchTableDataSqlQueryWise(tableMetadata.constructQuery(catalogName, schemaName, tabeleName,
                columnNames.get(0), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_shortname = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(1), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_firstname = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(2), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_middlename = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(3), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_lastname = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(4), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_height = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(5), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_weight = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(6), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_birthdate = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(7), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_birtharea_id = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(8), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_passportarea_id = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(9), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_role_name = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(10), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_role_code2 = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(11), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_role_code3 = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(12), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_foot = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(13), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_current_teamid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(14), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_currentnational_teamid = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(15), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_gender = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(16), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_status = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(17), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        ArrayList<String> db_imagedataurl = tableMetadata.fetchTableDataSqlQueryWise(
                tableMetadata.constructQuery(catalogName, schemaName, tabeleName, columnNames.get(18), whereClauseColumnName, String.valueOf(GenericFun.removeDuplicateDataFromList(api_current_teamid))));
        logger.info("Executing assertion for the api_playerid column");
        AssertHelpers.softAssertTrue(api_playerid, db_playerid);
        logger.info("Executing assertion for the api_shortname column");
        AssertHelpers.softAssertTrue(api_shortname, db_shortname);
        logger.info("Executing assertion for the api_firstname column");
        AssertHelpers.softAssertTrue(api_firstname, db_firstname);
        logger.info("Executing assertion for the api_middlename column");
        AssertHelpers.softAssertTrue(api_middlename, db_middlename);
        logger.info("Executing assertion for the api_lastname column");
        AssertHelpers.softAssertTrue(api_lastname, db_lastname);
        logger.info("Executing assertion for the api_height column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_height), db_height);
        logger.info("Executing assertion for the api_weight column");
        AssertHelpers.softAssertTrue(GenericFun.listStringToDecimalConversion(api_weight), db_weight);
        logger.info("Executing assertion for the api_birthdate column");
        AssertHelpers.softAssertTrue(api_birthdate, db_birthdate);
        logger.info("Executing assertion for the api_birtharea_id column");
        AssertHelpers.softAssertTrue(api_birtharea_id, db_birtharea_id);
        logger.info("Executing assertion for the api_passportarea_id column");
        AssertHelpers.softAssertTrue(api_passportarea_id, db_passportarea_id);
        logger.info("Executing assertion for the api_role_name column");
        AssertHelpers.softAssertTrue(api_role_name, db_role_name);
        logger.info("Executing assertion for the api_role_code2 column");
        AssertHelpers.softAssertTrue(api_role_code2, db_role_code2);
        logger.info("Executing assertion for the api_role_code3 column");
        AssertHelpers.softAssertTrue(api_role_code3, db_role_code3);
        logger.info("Executing assertion for the api_foot column");
        AssertHelpers.softAssertTrue(api_foot, db_foot);
        logger.info("Executing assertion for the api_current_teamid column");
        AssertHelpers.softAssertTrue(api_current_teamid, db_current_teamid);
        logger.info("Executing assertion for the api_currentnational_teamid column");
        AssertHelpers.softAssertTrue(GenericFun.removeZeroFromList(api_currentnational_teamid), db_currentnational_teamid);
        logger.info("Executing assertion for the api_gender column");
        AssertHelpers.softAssertTrue(api_gender, db_gender);
        logger.info("Executing assertion for the api_status column");
        AssertHelpers.softAssertTrue(api_status, db_status);
        AssertHelpers.softAssertTrue(api_imagedataurl, db_imagedataurl);
        AssertHelpers.softAssertAll();
    }

    @Test
    @Title("Verify Squad Table Duplicate Records")
    @Tag("Squad")
    public void squadTableDuplicateRecords() throws Exception{
        Config.setConfigs();
        //DB DATA
        TrinoTableMetaData tableMetadata=new TrinoTableMetaData();
        ArrayList<String> columnNames=tableMetadata.getTableMetaDataByTableName(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.squad_tablename);
        logger.info("Fetching duplicate value for 'teams' table and column name is '"+columnNames.get(0)+"' and "+columnNames.get(14));
        ArrayList<String> db_teamid =  tableMetadata.fetchDuplicateRecordsDataColumnsWise(GlbVar.catalog_name, GlbVar.schemas_name, GlbVar.squad_tablename
                , columnNames.get(0), columnNames.get(14),null);
        AssertHelpers.softAssertTrueToCheckDuplicateValue(db_teamid);
    }


    @Test
    @Title("Verify Squad Table Number Of Records")
    @Tag("Squad")
    public void squadNumberOfRecordsValidation() throws Exception {
        Config.setConfigs();
        //DB DATA
        String catalogName = GlbVar.catalog_name;
        String schemaName = GlbVar.schemas_name;
        String tabeleName = GlbVar.squad_tablename;
        TrinoTableMetaData tableMetadata = new TrinoTableMetaData();
        ArrayList<String> columnNames = tableMetadata.getTableMetaDataByTableName(catalogName, schemaName, tabeleName);
        logger.info("COLUMN NAMES IN DB \n: " + columnNames);
        ArrayList<String> db_playerid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(0));
//        ArrayList<String> db_shortname = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(1));
//        ArrayList<String> db_firstname = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(2));
//        ArrayList<String> db_middlename = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(3));
//        ArrayList<String> db_lastname = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(4));
//        ArrayList<String> db_height = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(5));
//        ArrayList<String> db_weight = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(6));
//        ArrayList<String> db_birthdate = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(7));
//        ArrayList<String> db_birtharea_id = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(8));
//        ArrayList<String> db_passportarea_id = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(9));
//        ArrayList<String> db_role_name = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(10));
//        ArrayList<String> db_role_code2 = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(11));
//        ArrayList<String> db_role_code3 = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(12));
//        ArrayList<String> db_foot = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(13));
//        ArrayList<String> db_current_teamid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(14));
//        ArrayList<String> db_currentnational_teamid = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(15));
//        ArrayList<String> db_gender = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(16));
//        ArrayList<String> db_status = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(17));
//        ArrayList<String> db_imagedataurl = tableMetadata.fetchTableDataColumnsWise(catalogName, schemaName, tabeleName, columnNames.get(18));
//        ArrayList<String> db_imagedataurlFinalList=tableMetadata.fetchTableDataFromMultipleColumns(catalogName, schemaName, tabeleName, columnNames.get(18), columnNames.get(14), columnNames.get(0));

        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;
        ArrayList<String> api_playerid = new ArrayList<String>();
//        ArrayList<String> api_shortname = new ArrayList<String>();
//        ArrayList<String> api_firstname = new ArrayList<String>();
//        ArrayList<String> api_middlename = new ArrayList<String>();
//        ArrayList<String> api_lastname = new ArrayList<String>();
//        ArrayList<String> api_height = new ArrayList<String>();
//        ArrayList<String> api_weight = new ArrayList<String>();
//        ArrayList<String> api_birthdate = new ArrayList<String>();
//        ArrayList<String> api_birtharea_id = new ArrayList<String>();
//        ArrayList<String> api_passportarea_id = new ArrayList<String>();
//        ArrayList<String> api_role_name = new ArrayList<String>();
//        ArrayList<String> api_role_code2 = new ArrayList<String>();
//        ArrayList<String> api_role_code3 = new ArrayList<String>();
//        ArrayList<String> api_foot = new ArrayList<String>();
//        ArrayList<String> api_current_teamid = new ArrayList<String>();
//        ArrayList<String> api_currentnational_teamid = new ArrayList<String>();
//        ArrayList<String> api_gender = new ArrayList<String>();
//        ArrayList<String> api_status = new ArrayList<String>();
//        ArrayList<String> api_imagedataurl = new ArrayList<String>();
//        List<String> api_imagedataurl_finallist = new ArrayList<String>();

        TeamsDataValidation teamsDataValidation = new TeamsDataValidation();
        List<String> teamID = teamsDataValidation.getTeamID();

//        List<String> teamID = new ArrayList<>();
//        teamID.add("5641");
//        teamID.add("5714");
//        teamID.add("13925");
        for (int h = 0; h < teamID.size(); h++) {
            String competition_endPoint = "/v3/teams/" + teamID.get(h) + "/squad";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            SquadResponse squadResponse = response.as(SquadResponse.class);
//            api_current_teamid.clear();
//            api_imagedataurl.clear();
//            api_imagedataurl_finallist.clear();
            for (int i = 0; i < squadResponse.getSquad().size(); i++) {
//                System.out.println("api_imagedataurl_finallist size(): " + api_imagedataurl_finallist.size());
//                System.out.println("squadResponse.getSquad().size(): "+squadResponse.getSquad().size()+" at index: "+h);
                api_playerid.add(String.valueOf(squadResponse.getSquad().get(i).getWyId()));
//                api_shortname.add(String.valueOf(squadResponse.getSquad().get(i).getShortName()));
//                api_firstname.add(String.valueOf(squadResponse.getSquad().get(i).getFirstName()));
//                api_middlename.add(String.valueOf(squadResponse.getSquad().get(i).getMiddleName()));
//                api_lastname.add(String.valueOf(squadResponse.getSquad().get(i).getLastName()));
//                api_height.add(String.valueOf(squadResponse.getSquad().get(i).getHeight()));
//                api_weight.add(String.valueOf(squadResponse.getSquad().get(i).getWeight()));
//                api_birthdate.add(String.valueOf(squadResponse.getSquad().get(i).getBirthDate()));
//                api_birtharea_id.add(String.valueOf(squadResponse.getSquad().get(i).getBirthArea().getId()));
//                api_passportarea_id.add(String.valueOf(squadResponse.getSquad().get(i).getPassportArea().getId()));
//                api_role_name.add(String.valueOf(squadResponse.getSquad().get(i).getRole().getName()));
//                api_role_code2.add(String.valueOf(squadResponse.getSquad().get(i).getRole().getCode2()));
//                api_role_code3.add(String.valueOf(squadResponse.getSquad().get(i).getRole().getCode3()));
//                api_foot.add(String.valueOf(squadResponse.getSquad().get(i).getFoot()));
//                api_current_teamid.add(String.valueOf(squadResponse.getSquad().get(i).getCurrentTeamId()));
//                api_currentnational_teamid.add(String.valueOf(squadResponse.getSquad().get(i).getCurrentNationalTeamId()));
//                api_gender.add(String.valueOf(squadResponse.getSquad().get(i).getGender()));
//                api_status.add(String.valueOf(squadResponse.getSquad().get(i).getStatus()));
//                api_imagedataurl.add(String.valueOf(squadResponse.getSquad().get(i).getImageDataURL()));
//                api_imagedataurl_finallist.add(api_imagedataurl.get(i) + "QADATA" + api_current_teamid.get(i) + "QADATA" + api_playerid.get(i));
            }

        }
//        logger.info("api_imagedataurl_finallist: "+api_imagedataurl_finallist);
//        System.out.println("db_playerid: "+db_playerid.size()+"api_playerid: "+api_playerid.size());
        logger.info("Executing assertion for the api_playerid column");
        AssertHelpers.softAssertTrue(api_playerid, db_playerid);
//        logger.info("Executing assertion for the api_shortname column");
//        AssertHelpers.softAssertTrue(api_shortname, db_shortname);
//        logger.info("Executing assertion for the api_firstname column");
//        AssertHelpers.softAssertTrue(api_firstname, db_firstname);
//        logger.info("Executing assertion for the api_middlename column");
//        AssertHelpers.softAssertTrue(api_middlename, db_middlename);
//        logger.info("Executing assertion for the api_lastname column");
//        AssertHelpers.softAssertTrue(api_lastname, db_lastname);
//        logger.info("Executing assertion for the api_height column");
//        AssertHelpers.softAssertTrue(api_height, db_height);
//        logger.info("Executing assertion for the api_weight column");
//        AssertHelpers.softAssertTrue(api_weight, db_weight);
//        logger.info("Executing assertion for the api_birthdate column");
//        AssertHelpers.softAssertTrue(api_birthdate, db_birthdate);
//        logger.info("Executing assertion for the api_birtharea_id column");
//        AssertHelpers.softAssertTrue(api_birtharea_id, db_birtharea_id);
//        logger.info("Executing assertion for the api_passportarea_id column");
//        AssertHelpers.softAssertTrue(api_passportarea_id, db_passportarea_id);
//        logger.info("Executing assertion for the api_role_name column");
//        AssertHelpers.softAssertTrue(api_role_name, db_role_name);
//        logger.info("Executing assertion for the api_role_code2 column");
//        AssertHelpers.softAssertTrue(api_role_code2, db_role_code2);
//        logger.info("Executing assertion for the api_role_code3 column");
//        AssertHelpers.softAssertTrue(api_role_code3, db_role_code3);
//        logger.info("Executing assertion for the api_foot column");
//        AssertHelpers.softAssertTrue(api_foot, db_foot);
//        logger.info("Executing assertion for the api_current_teamid column");
//        AssertHelpers.softAssertTrue(api_current_teamid, db_current_teamid);
//        logger.info("Executing assertion for the api_currentnational_teamid column");
//        AssertHelpers.softAssertTrue(api_currentnational_teamid, db_currentnational_teamid);
//        logger.info("Executing assertion for the api_gender column");
//        AssertHelpers.softAssertTrue(api_gender, db_gender);
//        logger.info("Executing assertion for the api_status column");
//        AssertHelpers.softAssertTrue(api_status, db_status);
//        logger.info("Executing assertion for the api_imagedataurl column >>QADATA+api_current_teamid.get(i)+QADATA+api_playerid.get(i)<<");
//        AssertHelpers.softAssertTrueCompareListElementWiseWithContains(api_imagedataurl_finallist, db_imagedataurlFinalList);
        AssertHelpers.softAssertAll();
    }

    public ArrayList<String> getPlayerID(List<String> teamID, int teamIDSize) throws Exception {
        Config.setConfigs();
        //Fetching from API
        RestUtils restUtils = new RestUtils();
        Response response;
        logger.info("Fetching players records for the following team id");
        for (int h = 0; h < teamIDSize; h++) {
            logger.info(teamID.get(h));
            String competition_endPoint = "/v3/teams/" + teamID.get(h) + "/squad";
            response = restUtils.sendGetRequestWithCredentials(GlbVar.api_baseUrl, competition_endPoint);
            Assertions.assertEquals(200, response.getStatusCode(), "API '" + GlbVar.api_baseUrl + competition_endPoint + "' is down");
            SquadResponse squadResponse = response.as(SquadResponse.class);

            for (int i = 0; i < squadResponse.getSquad().size(); i++) {
                api_playerid.add(String.valueOf(squadResponse.getSquad().get(i).getWyId()));
            }
            logger.info("All players for above team is: "+api_playerid);
        }
        logger.info(">>>>Final all players are: "+api_playerid);
        return api_playerid;
    }
    ArrayList<String> api_playerid = new ArrayList<String>();
    ArrayList<String> api_shortname = new ArrayList<String>();
    ArrayList<String> api_firstname = new ArrayList<String>();
    ArrayList<String> api_middlename = new ArrayList<String>();
    ArrayList<String> api_lastname = new ArrayList<String>();
    ArrayList<String> api_height = new ArrayList<String>();
    ArrayList<String> api_weight = new ArrayList<String>();
    ArrayList<String> api_birthdate = new ArrayList<String>();
    ArrayList<String> api_birtharea_id = new ArrayList<String>();
    ArrayList<String> api_passportarea_id = new ArrayList<String>();
    ArrayList<String> api_role_name = new ArrayList<String>();
    ArrayList<String> api_role_code2 = new ArrayList<String>();
    ArrayList<String> api_role_code3 = new ArrayList<String>();
    ArrayList<String> api_foot = new ArrayList<String>();
    ArrayList<String> api_current_teamid = new ArrayList<String>();
    ArrayList<String> api_currentnational_teamid = new ArrayList<String>();
    ArrayList<String> api_gender = new ArrayList<String>();
    ArrayList<String> api_status = new ArrayList<String>();
    ArrayList<String> api_imagedataurl = new ArrayList<String>();
}