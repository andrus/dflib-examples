package org.objectstyle.dflib.df;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Index;
import com.nhl.dflib.aggregate.Aggregator;
import com.nhl.dflib.map.Hasher;
import com.nhl.dflib.print.TabularPrinter;
import com.nhl.dflib.row.RowBuilder;
import com.nhl.dflib.row.RowProxy;
import org.objectstyle.dflib.df.factory.DFChampionshipFactory;
import org.objectstyle.dflib.df.model.DFChampionship;
import org.objectstyle.dflib.df.model.DFGame;

import java.util.Collection;

public class DFStandingsCalculator {

    public static void main(String[] args) {

        // 10 teams, 2 games between each team
        DFChampionship championship = new DFChampionshipFactory().championship(10, 2);

        // assume these teams are tied (to demonstrate tie resolution algorithm, not because they are in fact tied)
        DataFrame untied = untie(championship, "t3", "t6", "t7");
        System.out.println(TabularPrinter.getInstance().print(new StringBuilder(), untied));
    }

    public static DataFrame untie(DFChampionship championship, String... tiedTeams) {

        DataFrame teams = championship.teamsNamed(tiedTeams);
        Collection<Object> pairKeysCollection = teams
                .innerJoin(teams, (lr, rr) -> !lr.get(0).equals(rr.get(0)))
                .renameColumns("home_team", "visiting_team")
                .groupBy(DFStandingsCalculator::pairKey) // TODO: "groupBy" is heavy-handed.. change this to "series.unique()"
                .getGroups();

        DataFrame pairKeys = DataFrame.fromStreamFoldByRow(Index.withNames("pair_key"), pairKeysCollection.stream());

        DataFrame gamePointsDiff = championship.getGames()
                .innerJoin(pairKeys, DFStandingsCalculator::pairKey, Hasher.forColumn("pair_key"))
                .addColumn("pair_points_diff", DFStandingsCalculator::pairPointsDiff);

        DataFrame pairPointsDiff = gamePointsDiff.groupBy("pair_key").agg(
                Aggregator.first("pair_key"),
                Aggregator.sum("pair_points_diff"));

        Index psIndex = Index.withNames("team", "pair_score");
        DataFrame leftPairScores = pairPointsDiff.map(psIndex, DFStandingsCalculator::scoreLeftPoints);
        DataFrame rightPairScores = pairPointsDiff.map(psIndex, DFStandingsCalculator::scoreRightPoints);

        // TODO: this is is asking for an operation similar to pandas DF.melt(..)
        return leftPairScores.vConcat(rightPairScores)
                .groupBy("team")
                .agg(Aggregator.first("team"), Aggregator.sum("pair_score"))
                .sortByColumns("pair_score");
    }

    private static void scoreLeftPoints(RowProxy from, RowBuilder to) {
        String pair = (String) from.get("pair_key");
        long diff = (Long) from.get("pair_points_diff");

        to.set("team", pair.substring(0, pair.indexOf("_")));
        to.set("pair_score", diff < 0 ? -1 : 0);
    }

    private static void scoreRightPoints(RowProxy from, RowBuilder to) {
        String pair = (String) from.get("pair_key");
        long diff = (Long) from.get("pair_points_diff");

        to.set("team", pair.substring(pair.indexOf("_") + 1));
        to.set("pair_score", diff > 0 ? -1 : 0);
    }

    private static int pairPointsDiff(RowProxy row) {

        String s1 = (String) row.get("home_team");
        String s2 = (String) row.get("visiting_team");

        int hs = (Integer) row.get("home_score");
        int vs = (Integer) row.get("visiting_score");

        return s1.compareTo(s2) < 0
                ? -DFGame.getHomePoints(hs, vs) + DFGame.getVisitingPoints(hs, vs)
                : DFGame.getHomePoints(hs, vs) - DFGame.getVisitingPoints(hs, vs);
    }

    private static String pairKey(RowProxy row) {
        String s1 = (String) row.get("home_team");
        String s2 = (String) row.get("visiting_team");

        return s1.compareTo(s2) < 0 ? s1 + "_" + s2 : s2 + "_" + s1;
    }
}
