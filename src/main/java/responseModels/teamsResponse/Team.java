package responseModels.teamsResponse;

import lombok.Data;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Team {
    public int wyId;
    public String name;
    public String officialName;
    public String city;
    public Area area;
    public String type;
    public String category;
    public String gender;
    public ArrayList<Child> children;
    public String imageDataURL;
}
