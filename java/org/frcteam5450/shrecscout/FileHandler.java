package org.frcteam5450.shrecscout;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by SHREC5450 on 1/13/2017.
 */

public class FileHandler {
    public static final String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/SHRECScoutData";

    public static String getFileName(Match team) {
        return baseDir + "/" + team.getMatchNumber() + ".csv";
    }

    public static List<Match> readAllFiles() {
        List<File> files = getListFiles(new File(baseDir));
        List<Match> teamList = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            // set the team number and create a team object for each file
            int pos = files.get(i).getName().lastIndexOf(".");
            if(pos != -1) {
                teamList.add(new Match(Integer.parseInt(files.get(i).getName().substring(0, pos))));
            } else {
                teamList.add(new Match(0));
            }

            try {
                CSVReader reader = new CSVReader(new FileReader(files.get(i)), ',' , '"' , 0);
                List<String[]> fileData = reader.readAll();

                for (String[] teamData : fileData) {
                    if (teamData.length == 13) {
                        MatchTeam team = new MatchTeam(Integer.parseInt(teamData[0]),
                                Boolean.parseBoolean(teamData[1]),
                                Integer.parseInt(teamData[2]));
                        team.setMatchPoints(Double.parseDouble(teamData[3]));
                        team.setPercentContribution(Double.parseDouble(teamData[4]));
                        team.setAutoCrossedLine(Boolean.parseBoolean(teamData[5]));
                        team.setAutoHighGoalShots(Integer.parseInt(teamData[6]));
                        team.setAutoLowGoalShots(Integer.parseInt(teamData[7]));
                        team.setAutoGearsDelivered(Integer.parseInt(teamData[8]));
                        team.setTeleopHighGoalShots(Integer.parseInt(teamData[9]));
                        team.setTeleopLowGoalShots(Integer.parseInt(teamData[10]));
                        team.setTeleopGearsDelivered(Integer.parseInt(teamData[11]));
                        team.setTeleopLifted(Boolean.parseBoolean(teamData[12]));
                        teamList.get(i).add(team);
                    } else {
                        // Handle a broken file
                    }
                }

                reader.close();
            } catch (IOException e) {
                // You'll need to add proper error handling here
            }
        }
        return teamList;
    }

    public static void writeAllFiles(List<Match> matchList) {
        for (int i = 0; i < matchList.size(); i++) {
            String fileName = Integer.toString(matchList.get(i).getMatchNumber()) + ".csv";
            String filePath = baseDir + File.separator + fileName;
            try {
                CSVWriter writer = new CSVWriter(new FileWriter(filePath , false));

                List<String[]> fileData = new ArrayList<String[]>();

                for (int j = 0; j < matchList.get(i).size(); j++) {
                    String[] teamData = new String[13];
                    teamData[0] = Integer.toString(matchList.get(i).get(j).getTeamNumber());
                    teamData[1] = Boolean.toString(matchList.get(i).get(j).getAlliance());
                    teamData[2] = Integer.toString(matchList.get(i).get(j).getAlliancePosition());
                    teamData[3] = Double.toString(matchList.get(i).get(j).getMatchPoints());
                    teamData[4] = Double.toString(matchList.get(i).get(j).getPercentContribution());
                    teamData[5] = Boolean.toString(matchList.get(i).get(j).getAutoCrossedLine());
                    teamData[6] = Integer.toString(matchList.get(i).get(j).getAutoHighGoalShots());
                    teamData[7] = Integer.toString(matchList.get(i).get(j).getAutoLowGoalShots());
                    teamData[8] = Integer.toString(matchList.get(i).get(j).getAutoGearsDelivered());
                    teamData[9] = Integer.toString(matchList.get(i).get(j).getTeleopHighGoalShots());
                    teamData[10] = Integer.toString(matchList.get(i).get(j).getTeleopLowGoalShots());
                    teamData[11] = Integer.toString(matchList.get(i).get(j).getTeleopGearsDelivered());
                    teamData[12] = Boolean.toString(matchList.get(i).get(j).getTeleopLifted());
                    fileData.add(teamData);
                }

                writer.writeAll(fileData);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<File> getListFiles(File parentDir) {
        if (!parentDir.exists()) parentDir.mkdir();
        List<File> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            } else if (file.getName().endsWith(".csv")) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }
}
