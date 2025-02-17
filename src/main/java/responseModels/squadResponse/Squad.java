package responseModels.squadResponse;

import lombok.Data;

@Data
public class Squad {
    public int wyId;
    public String shortName;
    public String firstName;
    public String middleName;
    public String lastName;
    public int height;
    public int weight;
    public String birthDate;
    public BirthArea birthArea;
    public PassportArea passportArea;
    public Role role;
    public String foot;
    public int currentTeamId;
    public int currentNationalTeamId;
    public String gender;
    public String status;
    public String imageDataURL;
}