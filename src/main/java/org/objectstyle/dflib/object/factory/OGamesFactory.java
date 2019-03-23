package org.objectstyle.dflib.object.factory;

import org.objectstyle.dflib.object.model.OGame;
import org.objectstyle.dflib.object.model.OTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OGamesFactory {

    public List<OGame> games(List<OTeam> teams, int gamesBetweenEachPairOfTeams) {

        Random rnd = new Random();

        List<OGame> games = new ArrayList<>();

        for (OTeam ta : teams) {
            for (OTeam tb : teams) {
                if (ta != tb) {
                    for (int i = 0; i < gamesBetweenEachPairOfTeams; i++) {
                        int homeScore = rnd.nextInt(6);
                        int visitingScore = rnd.nextInt(6);

                        // create a balanced number of home and team games for each team pair
                        OTeam homeTeam = i % 2 == 0 ? ta : tb;
                        OTeam visitingTeam = i % 2 == 0 ? tb : ta;

                        games.add(new OGame(homeTeam, visitingTeam, homeScore, visitingScore));
                    }
                }
            }
        }

        return games;
    }
}
