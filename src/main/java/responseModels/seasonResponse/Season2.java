package responseModels.seasonResponse;

import lombok.Data;

@Data
public class Season2 {
    public int wyId;
    public String name;
    public String startDate;
    public String endDate;
    public boolean active;
    public int competitionId;
}