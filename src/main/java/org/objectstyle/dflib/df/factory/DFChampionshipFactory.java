package org.objectstyle.dflib.df.factory;

import com.nhl.dflib.DataFrame;
import org.objectstyle.dflib.df.model.DFChampionship;

public class DFChampionshipFactory {

    public DFChampionship championship(int totalTeams, int gamesBetweenEachPairOfTeams) {
        DataFrame teams = new DFTeamsFactory().teams(totalTeams);
        DataFrame games = new DFGamesFactory().games(teams, gamesBetweenEachPairOfTeams);
        return new DFChampionship(teams, games);
    }
}
