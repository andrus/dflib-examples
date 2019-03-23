package org.objectstyle.dflib.object.model;

public class OGame {

    private OTeam homeTeam;
    private OTeam visitingTeam;
    private int homeScore;
    private int visitingScore;

    public OGame(OTeam homeTeam, OTeam visitingTeam, int homeScore, int visitingScore) {
        this.homeTeam = homeTeam;
        this.visitingTeam = visitingTeam;
        this.homeScore = homeScore;
        this.visitingScore = visitingScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getVisitingScore() {
        return visitingScore;
    }

    public OTeam getHomeTeam() {
        return homeTeam;
    }

    public OTeam getVisitingTeam() {
        return visitingTeam;
    }

    // using soccer points system: 3 points for a win, 1 point for a tie, 0 points for a loss
    public int getHomePoints() {
        if (homeScore > visitingScore) {
            return 3;
        } else if (homeScore < visitingScore) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getVisitingPoints() {
        if (homeScore < visitingScore) {
            return 3;
        } else if (homeScore > visitingScore) {
            return 0;
        } else {
            return 1;
        }
    }
}
