package org.objectstyle.dflib.factory;

import org.objectstyle.dflib.model.Game;
import org.objectstyle.dflib.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamesFactory {

    public List<Game> games(List<Team> teams, int gamesBetweenEachPairOfTeams) {

        Random rnd = new Random();

        List<Game> games = new ArrayList<>();


        for (Team ta : teams) {
            for (Team tb : teams) {
                if (ta != tb) {
                    for (int i = 0; i < gamesBetweenEachPairOfTeams; i++) {
                        int homeScore = rnd.nextInt(6);
                        int visitingScore = rnd.nextInt(6);

                        // create a balanced number of home and team games for each team pair
                        Team homeTeam = i % 2 == 0 ? ta : tb;
                        Team visitingTeam = i % 2 == 0 ? tb : ta;

                        games.add(new Game(homeTeam, visitingTeam, homeScore, visitingScore));
                    }
                }
            }
        }

        return games;
    }
}
