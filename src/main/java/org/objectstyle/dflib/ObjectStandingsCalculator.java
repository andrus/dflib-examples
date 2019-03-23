package org.objectstyle.dflib;

import org.objectstyle.dflib.factory.ChampionshipFactory;
import org.objectstyle.dflib.model.Championship;
import org.objectstyle.dflib.model.Game;
import org.objectstyle.dflib.model.Team;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ObjectStandingsCalculator {

    public static void main(String[] args) {

        // 10 teams, 2 games between each team
        Championship championship = new ChampionshipFactory().championship(10, 2);

        // assume these teams are tied (to demonstrate tie resolution algorithm, not because they are in fact tied)
        List<Team> untied = untie(championship, "t3", "t6", "t7");
        untied.forEach(t -> System.out.println(t.getName()));
    }

    public static List<Team> untie(Championship championship, String... tiedTeams) {

        List<Team> teams = championship.teamsNamed(tiedTeams);

        // organize games into lists by a team pair
        Map<Set<Team>, Set<Game>> gamesByPairs = new HashMap<>();
        for (Game game : championship.getGames()) {

            Set<Team> pair = pair(game.getHomeTeam(), game.getVisitingTeam());

            // skip games for teams that are not tied
            if (!teams.containsAll(pair)) {
                continue;
            }

            gamesByPairs.computeIfAbsent(pair, p -> new HashSet<>()).add(game);
        }

        // contains the sum of pair wins
        Map<Team, AtomicInteger> score = new HashMap<>();
        for (Team t : teams) {
            score.put(t, new AtomicInteger(0));
        }

        // score winners in each pair
        for (Map.Entry<Set<Team>, Set<Game>> e : gamesByPairs.entrySet()) {

            Map<Team, AtomicInteger> pointsInPair = new HashMap<>(3);
            for (Team t : e.getKey()) {
                pointsInPair.put(t, new AtomicInteger(0));
            }

            for (Game g : e.getValue()) {
                AtomicInteger hs = pointsInPair.get(g.getHomeTeam());
                hs.set(hs.get() + g.getHomeScore());

                AtomicInteger vs = pointsInPair.get(g.getVisitingTeam());
                vs.set(vs.get() + g.getVisitingScore());
            }

            Team winningInPair = pointsInPair.entrySet()
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
