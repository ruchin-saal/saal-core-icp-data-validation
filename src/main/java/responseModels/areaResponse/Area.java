package responseModels.areaResponse;

import lombok.Data;

@Data
public class Area {
    public int id;
    public String alpha2code;
    public String alpha3code;
    public String name;
}
