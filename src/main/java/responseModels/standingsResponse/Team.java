package responseModels.standingsResponse;

import lombok.Data;

@Data
public class Team {
    public int teamId;
    public int totalPoints;
    public int totalPlayed;
    public int totalWins;
    public int totalDraws;
    public int totalLosses;
    public int totalGoalsFor;
    public int totalGoalsAgainst;
    public String groupName;
}