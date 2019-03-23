package org.objectstyle.dflib.df.factory;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Index;

import java.util.Random;

public class DFGamesFactory {

    public DataFrame games(DataFrame teams) {

        Random rnd = new Random();

        DataFrame pairs = teams
                .innerJoin(teams, (lr, rr) -> !lr.get(0).equals(rr.get(0)))
                .renameColumns("home_team", "visiting_team");

        return pairs.map(
                Index.withNames("home_team", "visiting_team", "home_score", "visiting_score"),
                (from, to) -> to.setValues(from.get(0), from.get(1), rnd.nextInt(6), rnd.nextInt(6)));
    }
}
