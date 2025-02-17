package responseModels.seasonResponse;

import lombok.Data;
import java.util.ArrayList;

@Data
public class SeasonResponse {
    public int competitionId;
    public ArrayList<Season> seasons;
}