package responseModels.standingsResponse;

import lombok.Data;
import java.util.List;

@Data
public class StandingResponse {
    public int competitionId;
    public int seasonId;
    public List<Team> teams;
}