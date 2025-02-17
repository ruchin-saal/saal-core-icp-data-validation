package baseConfig;

import network.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import utilities.GenericFun;
import utilities.GlbVar;
import utilities.LoadProperty;
import utilities.RestUtils;


public class BaseClass extends RestUtils {
    static LoadProperty loadProperty = new LoadProperty();
    private static final Logger logger = LogManager.getLogger(RestUtils.class);

    protected static Configuration config;
    protected static RestUtils restUtils;


    @BeforeAll
    public static void setUpDataSet() {
        if (!(GlbVar.setUpIsDone)) {
            GenericFun.setDesignTemplate();
            loadProperty = new LoadProperty();
            config = new Configuration();
            restUtils = new RestUtils();
            config.setBaseURLAndEnvironment();
//            config.setAuthToken();
            GlbVar.setUpIsDone = true;
        }
    }

    @AfterAll
    public static void tearDown() {
//        loadProperty.updatePropertyFile("serenity", "report.customfields.user", config.readPropertyFile("admin"));
//        loadProperty.updatePropertyFile("serenity", "report.customfields.environment", GlbVar.currentEnvironment);
//        loadProperty.updatePropertyFile("serenity", "report.customfields.host", GlbVar.baseURL);
    }

}
