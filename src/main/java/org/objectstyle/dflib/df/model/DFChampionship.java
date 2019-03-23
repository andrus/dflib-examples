package org.objectstyle.dflib.df.model;

import com.nhl.dflib.DataFrame;

import java.util.List;

import static java.util.Arrays.asList;

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

    public DataFrame teamsNamed(String... names) {
        List<String> namesList = asList(names);
        return getTeams().filter(r -> namesList.contains(r.get("name")));
    }
}
