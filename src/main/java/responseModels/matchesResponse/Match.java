package responseModels.matchesResponse;

import lombok.Data;

@Data
public class Match {
    public int matchId;
    public String label;
    public String date;
    public String dateutc;
    public String status;
    public int competitionId;
    public int seasonId;
    public int roundId;
    public int gameweek;
}
