package org.objectstyle.dflib.df.factory;

import com.nhl.dflib.DataFrame;
import com.nhl.dflib.Index;

import java.util.ArrayList;
import java.util.List;

public class DFTeamsFactory {

    public DataFrame teams(int count) {
        List<Object[]> teams = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            teams.add(new Object[]{"t" + i});
        }

        return DataFrame.forRows(Index.forLabels("name"), teams);
    }
}
