package org.objectstyle.dflib.df;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Index;
import com.nhl.dflib.Printers;
import com.nhl.dflib.aggregate.Aggregator;
import com.nhl.dflib.filter.ValuePredicate;
import com.nhl.dflib.row.RowBuilder;
import com.nhl.dflib.row.RowProxy;
import org.objectstyle.dflib.df.factory.DFChampionshipFactory;
import org.objectstyle.dflib.df.model.DFChampionship;
import org.objectstyle.dflib.df.model.DFGame;

public class DFStandingsCalculator {

    public static void main(String[] args) {

        // 10 teams, 2 games between each team
        DFChampionship championship = new DFChampionshipFactory().championship(10);

        // assume these teams are tied (to demonstrate tie resolution algorithm, not because they are in fact tied)
        DataFrame untied = untie(championship, "t3", "t6", "t7");
        System.out.println(Printers.tabular.toString(untied));
    }

    public static DataFrame untie(DFChampionship championship, String... tiedTeams) {

        ValuePredicate<String> teamsP = ValuePredicate.isIn(tiedTeams);

        // convert game scores to per-pair game points
        DataFrame pairPoints = championship.getGames()
                .filterByColumn("home_team", teamsP)
                .filterByColumn("visiting_team", teamsP)
                .map(Index.withNames("left", "right", "pair_points_diff"), DFStandingsCalculator::normalize)
                .groupBy("left", "right").agg(
                        Aggregator.first("left"),
                        Aggregator.first("right"),
                        Aggregator.sum("pair_points_diff"));

        // convert per-pair game points to normalized pair scores
        Index psIndex = Index.withNames("team", "pair_score");
        DataFrame leftNormalScores = pairPoints.map(psIndex, DFStandingsCalculator::scoreLeftPoints);
        DataFrame rightNormalScores = pairPoints.map(psIndex, DFStandingsCalculator::scoreRightPoints);
        return leftNormalScores.vConcat(rightNormalScores)
                .groupBy("team")
                .agg(Aggregator.first("team"), Aggregator.sum("pair_score"))
                .sortByColumns("pair_score");
    }

    private static void scoreLeftPoints(RowProxy from, RowBuilder to) {
        long diff = (Long) from.get("pair_points_diff");
        to.set("team", from.get("left"));
        to.set("pair_score", diff < 0 ? -1 : 0);
    }

    private static void scoreRightPoints(RowProxy from, RowBuilder to) {
        long diff = (Long) from.get("pair_points_diff");
        to.set("team", from.get("right"));
        to.set("pair_score", diff > 0 ? -1 : 0);
    }

    private static void normalize(RowProxy from, RowBuilder to) {
        // to be able to group by pairs regardless of home/visiting status organize teams by name

        String s1 = (String) from.get("home_team");
        String s2 = (String) from.get("visiting_team");

        int hs = (Integer) from.get("home_score");
        int vs = (Integer) from.get("visiting_score");

        if (s1.compareTo(s2) < 0) {
            to.set("left", s1);
            to.set("right", s2);
            to.set("pair_points_diff", -DFGame.getHomePoints(hs, vs) + DFGame.getVisitingPoints(hs, vs));
        } else {
            to.set("left", s2);
            to.set("right", s1);
            to.set("pair_points_diff", DFGame.getHomePoints(hs, vs) - DFGame.getVisitingPoints(hs, vs));
        }
    }
}
