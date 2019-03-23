package org.objectstyle.dflib.object.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class OChampionship {

    private List<OTeam> teams;
    private List<OGame> games;

    public OChampionship(List<OTeam> teams, List<OGame> games) {
        this.teams = teams;
        this.games = games;
    }

    public List<OTeam> getTeams() {
        return teams;
    }

    public List<OGame> getGames() {
        return games;
    }

    public List<OTeam> teamsNamed(String... names) {

        List<String> namesList = asList(names);
        List<OTeam> matchingTeams = new ArrayList<>(names.length);

        for(OTeam t : teams) {

            if(namesList.contains(t.getName())) {
                matchingTeams.add(t);
            }
        }

        return matchingTeams;
    }
}
