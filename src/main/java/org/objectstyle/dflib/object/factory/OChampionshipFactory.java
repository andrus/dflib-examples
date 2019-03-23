package org.objectstyle.dflib.object.factory;

import org.objectstyle.dflib.object.model.OChampionship;
import org.objectstyle.dflib.object.model.OGame;
import org.objectstyle.dflib.object.model.OTeam;

import java.util.List;

public class OChampionshipFactory {

    public OChampionship championship(int totalTeams, int gamesBetweenEachPairOfTeams) {
        List<OTeam> teams = new OTeamsFactory().teams(totalTeams);
        List<OGame> games = new OGamesFactory().games(teams, gamesBetweenEachPairOfTeams);
        return new OChampionship(teams, games);
    }
}
