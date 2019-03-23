package org.objectstyle.dflib.factory;

import org.objectstyle.dflib.model.Championship;
import org.objectstyle.dflib.model.Game;
import org.objectstyle.dflib.model.Team;

import java.util.List;

public class ChampionshipFactory {

    public Championship championship(int totalTeams, int gamesBetweenEachPairOfTeams) {
        List<Team> teams = new TeamsFactory().teams(totalTeams);
        List<Game> games = new GamesFactory().games(teams, gamesBetweenEachPairOfTeams);
        return new Championship(teams, games);
    }
}
