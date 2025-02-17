package utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static java.lang.Boolean.TRUE;

public class ApiTableMetaData {
    public ArrayList<String> fetchApiMetaDataFromArrayByObjectName(Response response, String objectName, Boolean objectNameFlag, String separator){
        // Get the status code from the response
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        // Get the response body as a string
        String responseBody = response.getBody().asString();
        ArrayList<String> columnNames=new ArrayList<>();
        try {
            // Parse JSON response string to JsonNode
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            // Get the "areas" JSON array node
            JsonNode areasArrayNode = jsonNode.get(objectName);
            // Check if "areas" array exists and is an array node
            if (areasArrayNode != null && areasArrayNode.isArray()) {
                // Iterate over each object in the "areas" array
                for (JsonNode areaObject : areasArrayNode) {
                    // Get field names from each JSON object and print

                    for (Iterator<String> it = areaObject.fieldNames(); it.hasNext(); ) {
                        String fieldName = it.next();
                        if(objectNameFlag==TRUE) {
                            fieldName = objectName + separator + fieldName;
                        }else {
                            fieldName = separator + fieldName;
                        }
                        columnNames.add(fieldName);
                    }
                    break;
                }
//                System.out.println("NUMBERS OF COLUMNS IN API: "+columnNames.size());
//                System.out.println("COLUMN NAMES IN API: ");
//                Collections.sort(columnNames);
//                System.out.println(columnNames);
            } else {
                System.out.println("No 'areas' array found in JSON.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(columnNames);
        return columnNames;
    }

    public static ArrayList<String> fetchApiMetaDataFromNestedArrayByObjectName(Response response, String fistObjectName, String secondObjectName, Boolean objectNameFlag, String separator) {
        // Get the status code from the response
        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        // Get the response body as a string
        String responseBody = response.getBody().asString();
        ArrayList<String> columnNames=new ArrayList<>();
        // Parse JSON string to JsonObject
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        // Get the "seasons" array
        JsonArray seasonsArray = jsonObject.getAsJsonArray(fistObjectName);
        // Iterate through elements in the "seasons" array
        for (JsonElement element : seasonsArray) {
            JsonObject seasonObject = element.getAsJsonObject().getAsJsonObject(secondObjectName);
            // Get the keys from the nested JsonObject
            for (String key : seasonObject.keySet()) {
                columnNames.add(key);
            }
            break;
        }
        Collections.sort(columnNames);
        return columnNames;
    }


}
