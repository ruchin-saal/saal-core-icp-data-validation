package responseModels.teamAdvancestatsResponse;

import lombok.Data;

@Data
public class TeamAdvanceStatsResponse {
        public int teamId;
        public int competitionId;
        public int seasonId;
        public Total total;
        public Average average;
        public Percent percent;
        public Object roundId;
}
