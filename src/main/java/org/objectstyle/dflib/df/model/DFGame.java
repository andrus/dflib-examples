package org.objectstyle.dflib.df.model;

public class DFGame {

    // using soccer points system: 3 points for a win, 1 point for a tie, 0 points for a loss
    public static int getHomePoints(int homeScore, int visitingScore) {
        if (homeScore > visitingScore) {
            return 3;
        } else if (homeScore < visitingScore) {
            return 0;
        } else {
            return 1;
        }
    }

    public static int getVisitingPoints(int homeScore, int visitingScore) {
        if (homeScore < visitingScore) {
            return 3;
        } else if (homeScore > visitingScore) {
            return 0;
        } else {
            return 1;
        }
    }
}
