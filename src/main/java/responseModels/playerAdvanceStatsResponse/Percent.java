package responseModels.playerAdvanceStatsResponse;

import lombok.Data;

@Data
public class Percent {
    public double duelsWon;
    public double defensiveDuelsWon;
    public double offensiveDuelsWon;
    public double aerialDuelsWon;
    public double successfulPasses;
    public int successfulSmartPasses;
    public double successfulPassesToFinalThird;
    public int successfulCrosses;
    public double successfulDribbles;
    public int shotsOnTarget;
    public int headShotsOnTarget;
    public int goalConversion;
    public int directFreeKicksOnTarget;
    public int penaltiesConversion;
    public double win;
    public double successfulForwardPasses;
    public double successfulBackPasses;
    public int successfulThroughPasses;
    public int successfulKeyPasses;
    public double successfulVerticalPasses;
    public double successfulLongPasses;
    public int successfulShotAssists;
    public int successfulLinkupPlays;
    public double yellowCardsPerFoul;
    public double successfulProgressivePasses;
    public int successfulSlidingTackles;
    public int successfulGoalKicks;
    public double dribblesAgainstWon;
    public double fieldAerialDuelsWon;
    public int gkSaves;
    public int gkSuccessfulExits;
    public int gkAerialDuelsWon;
    public double newDuelsWon;
    public double newDefensiveDuelsWon;
    public double newOffensiveDuelsWon;
    public double newSuccessfulDribbles;
    public double successfulLateralPasses;
}
