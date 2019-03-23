package org.objectstyle.dflib.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Championship {

    private List<Team> teams;
    private List<Game> games;

    public Championship(List<Team> teams, List<Game> games) {
        this.teams = teams;
        this.games = games;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Game> getGames() {
        return games;
    }

    public List<Team> teamsNamed(String... names) {

        List<String> namesList = asList(names);
        List<Team> matchingTeams = new ArrayList<>(names.length);

        for(Team t : teams) {

            if(namesList.contains(t.getName())) {
                matchingTeams.add(t);
            }
        }

        return matchingTeams;
    }
}
