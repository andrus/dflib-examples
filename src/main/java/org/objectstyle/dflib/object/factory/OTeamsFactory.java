package org.objectstyle.dflib.object.factory;

import org.objectstyle.dflib.object.model.OTeam;

import java.util.ArrayList;
import java.util.List;

public class OTeamsFactory {

    public List<OTeam> teams(int count) {
        List<OTeam> teams = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            teams.add(new OTeam("t" + i));
        }

        return teams;
    }
}
