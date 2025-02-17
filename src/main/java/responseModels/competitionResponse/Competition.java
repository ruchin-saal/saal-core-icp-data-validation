package responseModels.competitionResponse;

import lombok.Data;

@Data
public class Competition {
    public int wyId;
    public String name;
    public Area area;
    public String format;
    public String type;
    public String category;
    public String gender;
    public int divisionLevel;
}