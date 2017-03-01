package org.frcteam5450.shrecscout;

import java.io.Serializable;

/**
 * Created by SHREC5450 on 1/13/2017.
 */

public class MatchTeam implements Serializable {
    private static final double autoHighGoalPoints = 1.0;
    private static final double autoLowGoalPoints = 1.0 / 3.0;
    private static final double autoRotorPoints = 60.0;
    private static final double autoCrossedLinePoints = 5.0;
    private static final double teleopHighGoalPoints = 1.0 / 3.0;
    private static final double teleopLowGoalPoints = 1.0 / 9.0;
    private static final double teleopRotorPoints = 40.0;
    private static final double teleopLiftedPoints = 50.0;

    private int teamNumber;
    private boolean alliance;
    private int alliancePosition;
    private double matchPoints;
    private double percentContribution;
    private int autoHighGoalShots;
    private int autoLowGoalShots;
    private int autoGearsDelivered;
    private boolean autoCrossedLine;
    private int teleopHighGoalShots;
    private int teleopLowGoalShots;
    private int teleopGearsDelivered;
    private boolean teleopLifted;

    public MatchTeam(int _team_number, boolean _alliance, int _alliance_position) {
        teamNumber = _team_number;
        alliance = _alliance;
        alliancePosition = _alliance_position;
        matchPoints = 0;
        percentContribution = 0;

        autoHighGoalShots = 0;
        autoLowGoalShots = 0;
        autoGearsDelivered = 0;
        autoCrossedLine = false;
        teleopHighGoalShots = 0;
        teleopLowGoalShots = 0;
        teleopGearsDelivered = 0;
        teleopLifted = false;
    }

    public void calculatePercentContribution() {
        double expectedPoints = 0;

        // autonomous scoring
        if (autoCrossedLine) expectedPoints += autoCrossedLinePoints;
        expectedPoints += autoHighGoalShots * autoHighGoalPoints +
                autoLowGoalShots * autoLowGoalPoints;
        for (int i = 0; i < autoGearsDelivered; i++) {
            if (i < 1) {
                expectedPoints += autoRotorPoints;
            } else if (i < 3) {
                expectedPoints += autoRotorPoints / 2.0;
            } else if (i < 6) {
                expectedPoints += autoRotorPoints / 3.0;
            } else if (i < 10) {
                expectedPoints += autoRotorPoints / 4.0;
            }
        }

        // teleop scoring
        expectedPoints += teleopHighGoalShots * teleopHighGoalPoints +
                teleopLowGoalShots * teleopLowGoalPoints;
        for (int i = autoGearsDelivered; i < (autoGearsDelivered + teleopGearsDelivered); i++) {
            if (i < 1) {
                expectedPoints += teleopRotorPoints;
            } else if (i < 3) {
                expectedPoints += teleopRotorPoints / 2.0;
            } else if (i < 6) {
                expectedPoints += teleopRotorPoints / 3.0;
            } else if (i < 10) {
                expectedPoints += teleopRotorPoints / 4.0;
            }
        }
        if (teleopLifted) expectedPoints += teleopLiftedPoints;

        // calculate contribution
        percentContribution = 100.0 * expectedPoints / matchPoints;
    }
    public void setPercentContribution(double _percentContribution) { percentContribution = _percentContribution; }
    public double getPercentContribution() { return percentContribution; }

    public void setMatchPoints(double _matchPoints) { matchPoints = _matchPoints; }
    public double getMatchPoints() { return matchPoints; }

    public void setAlliancePosition(int _alliancePosition) { alliancePosition = _alliancePosition; }
    public int getAlliancePosition() { return alliancePosition; }

    public void setAlliance(boolean _alliance) { alliance = _alliance; }
    public boolean getAlliance() { return alliance; }

    public void setTeamNumber(int _teamNumber) { teamNumber = _teamNumber; }
    public int getTeamNumber() { return teamNumber; }

    public void addAutoHighGoal() { autoHighGoalShots++; }
    public void subtractAutoHighGoal() { autoHighGoalShots--; }
    public void setAutoHighGoalShots(int _autoHighGoalShots) { autoHighGoalShots = _autoHighGoalShots; }
    public int getAutoHighGoalShots() { return autoHighGoalShots; }

    public void addAutoLowGoal() { autoLowGoalShots++; }
    public void subtractAutoLowGoal() { autoLowGoalShots--; }
    public void setAutoLowGoalShots(int _autoLowGoalShots) { autoLowGoalShots = _autoLowGoalShots; }
    public int getAutoLowGoalShots() { return autoLowGoalShots; }

    public void addAutoGearsDelivered() { autoGearsDelivered++; }
    public void subtractAutoGearsDelivered() { autoGearsDelivered--; }
    public void setAutoGearsDelivered(int _autoGearsDelivered) { autoGearsDelivered = _autoGearsDelivered; }
    public int getAutoGearsDelivered() { return autoGearsDelivered; }

    public void toggleAutoCrossedLine() { autoCrossedLine = !autoCrossedLine; }
    public void setAutoCrossedLine(boolean _autoCrossedLine) { autoCrossedLine = _autoCrossedLine; }
    public boolean getAutoCrossedLine() { return autoCrossedLine; }

    public void addTeleopHighGoal() { teleopHighGoalShots++; }
    public void subtractTeleopHighGoal() { teleopHighGoalShots--; }
    public void setTeleopHighGoalShots(int _teleopHighGoalShots) { teleopHighGoalShots = _teleopHighGoalShots; }
    public int getTeleopHighGoalShots() { return teleopHighGoalShots; }

    public void addTeleopLowGoal() { teleopLowGoalShots++; }
    public void subtractTeleopLowGoal() { teleopLowGoalShots--; }
    public void setTeleopLowGoalShots(int _teleopLowGoalShots) { teleopLowGoalShots = _teleopLowGoalShots; }
    public int getTeleopLowGoalShots() { return teleopLowGoalShots; }

    public void addTeleopGearsDelivered() { teleopGearsDelivered++; }
    public void subtractTeleopGearsDelivered() { teleopGearsDelivered--; }
    public void setTeleopGearsDelivered(int _teleopGearsDelivered) { teleopGearsDelivered = _teleopGearsDelivered; }
    public int getTeleopGearsDelivered() { return teleopGearsDelivered; }

    public void toggleTeleopLifted() { teleopLifted = !teleopLifted; }
    public void setTeleopLifted(boolean _teleopLifted) { teleopLifted = _teleopLifted; }
    public boolean getTeleopLifted() { return teleopLifted; }

}
