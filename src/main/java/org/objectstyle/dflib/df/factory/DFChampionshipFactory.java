package org.objectstyle.dflib.df.factory;

import com.nhl.dflib.DataFrame;
import org.objectstyle.dflib.df.model.DFChampionship;

public class DFChampionshipFactory {

    public DFChampionship championship(int totalTeams) {
        DataFrame teams = new DFTeamsFactory().teams(totalTeams);
        DataFrame games = new DFGamesFactory().games(teams);
        return new DFChampionship(teams, games);
    }
}
