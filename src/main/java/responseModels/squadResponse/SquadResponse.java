package responseModels.squadResponse;

import lombok.Data;
import java.util.ArrayList;

@Data
public class SquadResponse {
    public ArrayList<Squad> squad;
    public ArrayList<Coach> coach;
    public ArrayList<Object> staff;
}