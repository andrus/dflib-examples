package org.objectstyle.dflib.factory;

import org.objectstyle.dflib.model.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamsFactory {

    public List<Team> teams(int count) {
        List<Team> teams = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            teams.add(new Team("t" + i));
        }

        return teams;
    }
}
