package org.objectstyle.dflib.object;

import org.objectstyle.dflib.object.factory.OChampionshipFactory;
import org.objectstyle.dflib.object.model.OChampionship;
import org.objectstyle.dflib.object.model.OGame;
import org.objectstyle.dflib.object.model.OTeam;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OStandingsCalculator {

    public static void main(String[] args) {

        // 10 teams, 2 games between each team
        OChampionship championship = new OChampionshipFactory().championship(10, 2);

        // assume these teams are tied (to demonstrate tie resolution algorithm, not because they are in fact tied)
        List<OTeam> untied = untie(championship, "t3", "t6", "t7");
        untied.forEach(t -> System.out.println(t.getName()));
    }

    public static List<OTeam> untie(OChampionship championship, String... tiedTeams) {

        List<OTeam> teams = championship.teamsNamed(tiedTeams);

        // organize games into lists by a team pair
        Map<Set<OTeam>, Set<OGame>> gamesByPairs = new HashMap<>();
        for (OGame game : championship.getGames()) {

            Set<OTeam> pair = pair(game.getHomeTeam(), game.getVisitingTeam());

            // skip games for teams that are not among the ones we are evaluating
            if (!teams.containsAll(pair)) {
                continue;
            }

            gamesByPairs.computeIfAbsent(pair, p -> new HashSet<>()).add(game);
        }

        // will contain the sum of pair wins at the end of the calculation
        Map<OTeam, AtomicInteger> score = new HashMap<>();
        for (OTeam t : teams) {
            score.put(t, new AtomicInteger(0));
        }

        // score winners in each pair
        for (Map.Entry<Set<OTeam>, Set<OGame>> e : gamesByPairs.entrySet()) {

            Map<OTeam, AtomicInteger> pointsInPair = new HashMap<>(3);
            for (OTeam t : e.getKey()) {
                pointsInPair.put(t, new AtomicInteger(0));
            }

            for (OGame g : e.getValue()) {
                AtomicInteger hs = pointsInPair.get(g.getHomeTeam());
                hs.set(hs.get() + g.getHomeScore());

                AtomicInteger vs = pointsInPair.get(g.getVisitingTeam());
                vs.set(vs.get() + g.getVisitingScore());
            }

            OTeam winningInPair = pointsInPair.entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(pe -> -pe.getValue().intValue()))
                    .limit(1)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .get();

            // winner in each pair gets a score of 1 in cross-pair scoring
            score.get(winningInPair).incrementAndGet();
        }

        return score.entrySet().stream().sorted(Comparator.comparingInt(e -> -e.getValue().intValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static <T> Set<T> pair(T t1, T t2) {
        Set<T> pair = new HashSet<>(3);
        pair.add(t1);
        pair.add(t2);
        return pair;
    }
}
