package org.objectstyle.dflib.df.factory;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Index;

import java.util.Random;

public class DFGamesFactory {

    public DataFrame games(DataFrame teams, int gamesBetweenEachPairOfTeams) {

        Random rnd = new Random();

        DataFrame pairs = teams
                .innerJoin(teams, (lr, rr) -> !lr.get(0).equals(rr.get(0)))
                .renameColumns("home_team", "visiting_team");

        // TODO: This is good enough for now, but there has to be a more explicit operation to expand each row N times
        DataFrame gamePairs = pairs;
        for (int i = 1; i < gamesBetweenEachPairOfTeams; i++) {
            gamePairs = gamePairs.vConcat(pairs);
        }

        return gamePairs.map(
                Index.withNames("home_team", "visiting_team", "home_score", "visiting_score"),
                (from, to) -> to.setValues(from.get(0), from.get(1), rnd.nextInt(6), rnd.nextInt(6)));
    }
}
