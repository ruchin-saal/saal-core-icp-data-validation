package utilities;

import java.io.IOException;

public class Config {

    public static String scale_factor;
    public static String trino_host;
    public static String trino_port;
    public static String trino_username;
    public static String trino_password;
    public static String username;
    public static String password;
    public static String Client_NAME;
    public static String access_token;
    public static String Login_URL;
    public static String BASE_URL_IDP;
    public static String BASE_URL_SERVICE;
    public static String trino_ui_host;
    public static String  catalog_name;
    public static String api_baseUrl;
    public static String api_username;
    public static String api_password;
    public static String areaTable_jsonObjectName;
    public static String areaTable_endPoint;
    public static String areaTable_sqlQuery;
    public static String seasonTable_endPoint;
    public static String seasonTable_sqlQuery;
    public static String schemas_name;
    public static String area_tablename;
    public static String competition_tablename;
    public static String season_tablename;
    public static String standings_tablename;
    public static String teams_tablename;
    public static String squad_tablename;
    public static String matches_tablename;
    public static String  team_advancestats_tablename;
    public static String areaList;
    public static String competitionIdList;
    public static String seasonIDSize;
    public static String competitionIdSize;
    public static String teamIdSize;
    public static String staticAdvanceCompetitionIDList;
    public static String teamAdvanceCompetitionIDSize;
    public static String teamAdvanceTeamIdSize;
    public static String  player_advancestats_tablename;
    public static String  match_advancestats_tablename;
    public static String children_teams_tablename;
    public static String players_overview_table;
    public static String striker_scores_table;
    public static String compatibility_scores_table;
    public static String catalogs_tranformed;
    public static String schemas_tranformed;

    /*Postgresql Config Variables*/
    public static String postgresqlHost;
    public static String port;
    public static String database;
    public static String dbUsername;
    public static String dbPassword;
    // Table Names
    public static String oracle_TableNames;
    public static String pg_schema_name;
    public static String trino_TableNames;


    public static void setConfigs() throws IOException {
        GlbVar.currentEnvironment = "dev";
        trino_host = LoadProperty.getProperty("env_host");
        trino_port = LoadProperty.getProperty("env_port");
        trino_username = LoadProperty.getProperty("env_username");
        trino_password = LoadProperty.getProperty("env_password");

        username = LoadProperty.getProperty("user_name");
        password = LoadProperty.getProperty("user_password");
        Client_NAME = LoadProperty.getProperty("env_Client_NAME");
        Login_URL = BASE_URL_IDP + LoadProperty.getProperty("env_Login_URL");
        BASE_URL_IDP = LoadProperty.getProperty("base_urlIdp");
        BASE_URL_SERVICE = LoadProperty.getProperty("env_BASE_URL_SERVICE");
        Login_URL = BASE_URL_IDP + LoadProperty.getProperty("env_Login_URL");
        trino_ui_host= LoadProperty.getProperty("trino_ui_host");
        scale_factor= LoadProperty.getProperty("catalogs_name");
        catalog_name=LoadProperty.getProperty("catalog_name");
        schemas_name=LoadProperty.getProperty("schemas_name");
        api_baseUrl=LoadProperty.getProperty("api_baseUrl");
        api_username=LoadProperty.getProperty("api_username");
        api_password=LoadProperty.getProperty("api_password");
        /*api properties*/
        areaTable_jsonObjectName=LoadProperty.getProperty("areaTable_jsonObjectName");
        areaTable_endPoint=LoadProperty.getProperty("areaTable_endPoint");
        seasonTable_endPoint=LoadProperty.getProperty("seasonTable_endPoint");
        /*table properties*/
        seasonTable_sqlQuery=LoadProperty.getProperty("seasonTable_sqlQuery");
        areaTable_sqlQuery=LoadProperty.getProperty("areaTable_sqlQuery");
        area_tablename=LoadProperty.getProperty("area_tablename");
        competition_tablename=LoadProperty.getProperty("competition_tablename");
        season_tablename=LoadProperty.getProperty("season_tablename");
        standings_tablename=LoadProperty.getProperty("standings_tablename");
        teams_tablename=LoadProperty.getProperty("teams_tablename");
        squad_tablename=LoadProperty.getProperty("squad_tablename");
        matches_tablename=LoadProperty.getProperty("matches_tablename");
        team_advancestats_tablename=LoadProperty.getProperty("team_advancestats_tablename");
        areaList=LoadProperty.getProperty("areaList");
        competitionIdList=LoadProperty.getProperty("competitionIdList");
        seasonIDSize=LoadProperty.getProperty("seasonIDSize");
        competitionIdSize=LoadProperty.getProperty("competitionIdSize");
        teamIdSize=LoadProperty.getProperty("teamIdSize");
        staticAdvanceCompetitionIDList =LoadProperty.getProperty("teamAdvanceCompetitionIDList");
        teamAdvanceCompetitionIDSize=LoadProperty.getProperty("teamAdvanceCompetitionIDSize");
        teamAdvanceTeamIdSize=LoadProperty.getProperty("teamAdvanceTeamIdSize");
        player_advancestats_tablename=LoadProperty.getProperty("player_advancestats_tablename");
        match_advancestats_tablename=LoadProperty.getProperty("match_advancestats_tablename");
        children_teams_tablename=LoadProperty.getProperty("children_teams_tablename");
        players_overview_table=LoadProperty.getProperty("players_overview_table");
        striker_scores_table=LoadProperty.getProperty("striker_scores_table");
        compatibility_scores_table=LoadProperty.getProperty("compatibility_scores_table");
        catalogs_tranformed =LoadProperty.getProperty("catalogs_tranformed");
        schemas_tranformed =LoadProperty.getProperty("schemas_tranformed");
        postgresqlHost = LoadProperty.getProperty("postgresql_host");
        port = LoadProperty.getProperty("port");
        database = LoadProperty.getProperty("database");
        dbUsername = LoadProperty.getProperty("db_username");
        dbPassword = LoadProperty.getProperty("db_password");
        oracle_TableNames = LoadProperty.getProperty("oracle_TableNames");
        pg_schema_name = LoadProperty.getProperty("pg_schema_name");
        trino_TableNames = LoadProperty.getProperty("trino_TableNames");
    }
}