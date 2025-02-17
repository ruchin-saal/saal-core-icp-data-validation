package network;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import responseModels.loginResponse.LoginResponse;
import utilities.GlbVar;
import utilities.LoadProperty;
import utilities.RestUtils;

public class Configuration extends RestUtils {

    private static final Logger logger = LogManager.getLogger(RestUtils.class);

    static LoadProperty loadProperty = new LoadProperty();
    static MongoCredential credential;
    static MongoClient mongoClient;
//    static DB db;
//    static DBCollection collection;

    public RestAssuredConfig getRestConfigToHandleTimeout() {
        RestAssuredConfig config = RestAssured.config().httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", 5000)
                .setParam("http.socket.timeout", 10000));

        return config;
    }

    public void setAuthToken() {
        String username = readPropertyFile("env_username");
        String password = readPropertyFile("env_password");
        Response response = SerenityRest
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("grant_type", "password")
                .formParam("username", username)
                .formParam("password", password)
                .formParam("client_id", GlbVar.idp_client_id)
                .when()
                .post(GlbVar.idpUrl);
        LoginResponse loginResponse = response.as(LoginResponse.class);
        GlbVar.authToken = loginResponse.getAccess_token();
    }


    public void setBaseURLAndEnvironment() {
        //This option is applicable  when running from Docker
        if ((System.getenv("environment") != null)) {
            GlbVar.currentEnvironment = System.getenv("environment").toLowerCase();
        }
        //This option is true when running from Maven
        else if (System.getProperty("env.PATH") != null) {
            GlbVar.currentEnvironment = System.getProperty("env.PATH").toLowerCase();
        }
        //This option is true when running from Local. Change value below to run from local
        else {
            GlbVar.currentEnvironment = "prod";
            logger.info("Scripts are running on " + GlbVar.currentEnvironment+" environment");
        }

        GlbVar.propertyPath = GlbVar.workingDirectory + "/src/test/resources/Config/" + GlbVar.currentEnvironment + ".properties";
        GlbVar.api_baseUrl = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "base_url");
        logger.info("BASE URL IS ----> " + GlbVar.api_baseUrl);
        GlbVar.baseURL_idp = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "base_urlIdp");
        GlbVar.idp_realm_name = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "idp_realm");
        GlbVar.idp_client_id = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "idp_client");
        GlbVar.idpUrl = GlbVar.baseURL_idp +  "/auth/realms/" + GlbVar.idp_realm_name + "/protocol/openid-connect/token";
        GlbVar.competitionIdList = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "competitionIdList");
        GlbVar.competitionIdSize = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "competitionIdSize");
        GlbVar.children_teams_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "children_teams_tablename");
        GlbVar.competition_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "competition_tablename");
        GlbVar.areaList = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "areaList");
        GlbVar.areaTable_endPoint = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "areaTable_endPoint");
        GlbVar.catalog_name = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "catalog_name");
        GlbVar.schemas_name = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "schemas_name");
        GlbVar.area_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "area_tablename");

        GlbVar.match_advancestats_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "match_advancestats_tablename");
        GlbVar.staticAdvanceCompetitionIDList = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "teamAdvanceCompetitionIDList");
        GlbVar.teamAdvanceCompetitionIDSize = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "teamAdvanceCompetitionIDSize");
        GlbVar.teamIdSize = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "teamIdSize");
        GlbVar.matches_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "matches_tablename");
        GlbVar.player_advancestats_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "player_advancestats_tablename");
        GlbVar.team_advancestats_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "team_advancestats_tablename");
        GlbVar.season_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "season_tablename");
        GlbVar.squad_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "squad_tablename");
        GlbVar.teams_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "teams_tablename");
        GlbVar.seasonIDSize = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "seasonIDSize");
        GlbVar.standings_tablename = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "standings_tablename");
        GlbVar.catalogs_tranformed = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "catalogs_tranformed");
        GlbVar.schemas_tranformed = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "schemas_tranformed");
        GlbVar.compatibility_scores_table = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "compatibility_scores_table");
        GlbVar.players_overview_table = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "players_overview_table");
        GlbVar.striker_scores_table = loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, "striker_scores_table");
    }

    public static String readPropertyFile(String Key) {
        if (!(Key.equals(""))) {
            logger.info("Return value of given Key " + Key + " is " + loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, Key));
            return loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, Key);
        } else {
            return "";
        }
    }

    public static String getJsonBody(String Key) {
        if (!(Key.equals(""))) {
            return loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, Key);
        } else {
            return "";
        }
    }

    public String updateJson(String body, String oldValue, String newValue) {
        if (!(body.equals(""))) {
            body = body.replace(oldValue, newValue);
            return body;
        } else {
            return "";
        }
    }

    public int getRandomNumberInRange() {
        int min = 100;
        int max = 100000;
        if (min >= max) {
            logger.error("Max must be greater than Min");
            throw new IllegalArgumentException("Max must be greater than Min");
        }
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    public String getValueFromProperty(String Key) {
        if (!(Key.equals(""))) {
            return loadProperty.getValueFromPropertyFile(GlbVar.propertyPath, Key);
        } else {
            return "";
        }
    }

    public void connectDataBase() {
        int port = Integer.parseInt(getJsonBody("port"));
        credential = MongoCredential.createCredential("e2_qa", "admin", "e2qa".toCharArray());
//        mongoClient = new MongoClient(new ServerAddress(getJsonBody("host"), port), Arrays.asList(credential));

    }

//    public void removeDocument(String dbName, String collectionName, String key, String value) {
//        db = null;
//        collection = null;
//        try {
//            db = mongoClient.getDB(dbName);
//            collection = db.getCollection(collectionName);
//            BasicDBObject query = new BasicDBObject();
//            query.append(key, value);
//            collection.remove(query);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    public String getDocument(String dbName, String collectionName, String key, String value) {
//        String document = "";
//        db = null;
//        collection = null;
//        try {
//            db = mongoClient.getDB(dbName);
//            collection = db.getCollection(collectionName);
//            BasicDBObject query = new BasicDBObject();
//            query.append(key, value);
//            DBCursor cursor = collection.find(query);
//            while (cursor.hasNext()) {
//                document = cursor.next().get("id").toString();
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return document;
//    }

    public void setTokenByRoleWise(String userName, String password){
        Response response = SerenityRest
                .given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("grant_type", "password")
                .formParam("username", userName)
                .formParam("password", password)
                .formParam("client_id", GlbVar.idp_client_id)
                .when()
                .post(GlbVar.idpUrl);
        LoginResponse loginResponse = response.as(LoginResponse.class);
        GlbVar.authToken = loginResponse.getAccess_token();
    }
}
