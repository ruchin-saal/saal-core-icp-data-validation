package responseModels.squadResponse;

import lombok.Data;

@Data
public class PassportArea {
    public int id;
    public String alpha2code;
    public String alpha3code;
    public String name;
}