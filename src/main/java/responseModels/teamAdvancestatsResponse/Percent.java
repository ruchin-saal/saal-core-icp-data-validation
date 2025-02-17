package responseModels.teamAdvancestatsResponse;

import lombok.Data;

@Data
public class Percent {
    public double duelsWon;
    public double defensiveDuelsWon;
    public double offensiveDuelsWon;
    public double aerialDuelsWon;
    public double successfulPasses;
    public double successfulSmartPasses;
    public int successfulPassesToFinalThird;
    public double successfulCrosses;
    public double successfulDribbles;
    public double shotsOnTarget;
    public double headShotsOnTarget;
    public double goalConversion;
    public double yellowCardsPerFoul;
    public int directFreeKicksOnTarget;
    public int penaltiesConversion;
    public double win;
    public double successfulForwardPasses;
    public double successfulBackPasses;
    public double successfulThroughPasses;
    public double successfulKeyPasses;
    public double successfulVerticalPasses;
    public double successfulLongPasses;
    public double successfulShotAssists;
    public double successfulLinkupPlays;
    public double fieldAerialDuelsWon;
    public int gkSaves;
    public int gkSuccessfulExits;
    public int gkAerialDuelsWon;
    public double successfulTouchInBox;
    public double newDuelsWon;
    public double newDefensiveDuelsWon;
    public double newOffensiveDuelsWon;
    public double newSuccessfulDribbles;
    public double successfulLateralPasses;
}
