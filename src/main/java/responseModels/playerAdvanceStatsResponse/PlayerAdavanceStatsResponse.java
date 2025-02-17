package responseModels.playerAdvanceStatsResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerAdavanceStatsResponse {
    public int playerId;
    public int competitionId;
    public int seasonId;
    public ArrayList<Positions> positions;
    public Total total;
    public Average average;
    public Percent percent;
    public Object roundId;
}
