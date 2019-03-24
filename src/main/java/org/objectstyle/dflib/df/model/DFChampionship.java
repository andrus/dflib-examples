package org.objectstyle.dflib.df.model;

import com.nhl.dflib.DataFrame;

public class DFChampionship {

    private DataFrame teams;
    private DataFrame games;

    public DFChampionship(DataFrame teams, DataFrame games) {
        this.teams = teams;
        this.games = games;
    }

    public DataFrame getGames() {
        return games;
    }

    public DataFrame getTeams() {
        return teams;
    }
}
