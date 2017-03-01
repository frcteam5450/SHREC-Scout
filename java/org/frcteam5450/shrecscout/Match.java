package org.frcteam5450.shrecscout;

import java.util.ArrayList;

/**
 * Created by SHREC5450 on 1/13/2017.
 */

public class Match extends ArrayList<MatchTeam> {
    private int matchNumber;

    public Match(int _matchNumber) {
        super();
        matchNumber = _matchNumber;
    }

    public int getMatchNumber() { return matchNumber; }

    public void setMatchNumber(int _matchNumber) { matchNumber = _matchNumber; }
}
