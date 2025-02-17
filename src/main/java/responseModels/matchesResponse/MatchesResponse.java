package responseModels.matchesResponse;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MatchesResponse {
    public ArrayList<Match> matches;
}
