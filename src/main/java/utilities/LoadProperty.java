package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;


/**
 * Created by Satya Prakash Solanki
 */
public class LoadProperty {
    private static Properties properties = null;

    private boolean isLoaded = false;


    public String getValueFromPropertyFile(String filepath, String keyForValue) {
        String value = null;
        try {
            File file = new File(filepath);
            FileInputStream fileInput = new FileInputStream(file);
            properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            Enumeration<Object> enuKeys = properties.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();

                if (key.startsWith(keyForValue)) {
                    value = properties.getProperty(key);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }


    public void updatePropertyFile() {
        Properties props = new Properties();
        String propsFileName = System.getProperty("user.dir") + "/src/test/resources/environment.properties";
        try {
            //first load old one:
            FileInputStream configStream = new FileInputStream(propsFileName);
            props.load(configStream);
            configStream.close();

            //modifies existing or adds new property
            props.setProperty("URL", GlbVar.api_baseUrl);
            //save modified property file
            FileOutputStream output = new FileOutputStream(propsFileName);
            props.store(output, "");
            output.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) throws IOException {
        String property = null;
        Properties mainProperties = new Properties();
        FileInputStream file;
        String CurrentDirectory = System.getProperty("user.dir");
        String path = GlbVar.workingDirectory + "/src/test/resources/Config/" + GlbVar.currentEnvironment + ".properties";
        file = new FileInputStream(path);
        mainProperties.load(file);
        file.close();
        property = mainProperties.getProperty(key);
        return property;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
