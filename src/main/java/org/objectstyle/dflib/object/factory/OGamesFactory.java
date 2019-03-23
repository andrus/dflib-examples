package org.objectstyle.dflib.object.factory;

import org.objectstyle.dflib.object.model.OGame;
import org.objectstyle.dflib.object.model.OTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OGamesFactory {

    public List<OGame> games(List<OTeam> teams) {

        Random rnd = new Random();

        List<OGame> games = new ArrayList<>();

        for (OTeam ta : teams) {
            for (OTeam tb : teams) {
                if (ta != tb) {
                    games.add(new OGame(ta, tb, rnd.nextInt(6), rnd.nextInt(6)));
                }
            }
        }

        return games;
    }
}
