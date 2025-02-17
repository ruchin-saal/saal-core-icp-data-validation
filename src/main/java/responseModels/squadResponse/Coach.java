package responseModels.squadResponse;

import lombok.Data;

@Data
public class Coach {
    public int wyId;
    public String shortName;
    public String firstName;
    public String middleName;
    public String lastName;
    public String birthDate;
    public BirthArea birthArea;
    public PassportArea passportArea;
    public int currentTeamId;
    public Object currentNationalTeamId;
    public String gender;
    public String status;
    public String type;
    public String imageDataURL;
}