package edu.ucdenver.cse.GRIDserver;

import edu.ucdenver.cse.GRIDmap.GRIDmap;
import edu.ucdenver.cse.GRIDmap.GRIDmapReader;
import edu.ucdenver.cse.GRIDcommon.GRIDagent;
import edu.ucdenver.cse.GRIDcommon.GRIDroute;
import edu.ucdenver.cse.GRIDutil.EmissionsContainer;
import edu.ucdenver.cse.GRIDutil.SpeedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;

import static org.apache.commons.io.FileUtils.current;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class GRIDrunner{

    private static CommandLine theCmdLine;

    public static void main(String[] args) throws Exception {
        Charset encoding = StandardCharsets.UTF_8;
        String weightType = "";
        GRIDmapReader myReader = new GRIDmapReader();
        GRIDmap myMap = new GRIDmap();
        GRIDagent testAgent001;
        EmissionsContainer mainEmissionsContainer = new EmissionsContainer();
        String outData = "";
        double totalMiles = 0.0;
        double totalEmissions = 0.0;
        double speedModifier = 1.0;
        String currentRoad = "";
        GRIDrunnerCmdLine cmdLine = new GRIDrunnerCmdLine(args);

        // command line options are -s and -t for speed and time respectively
        try {
            theCmdLine = cmdLine.parseArgs();
        }
        catch (ParseException e) {
            System.out.println("GRIDrunner has encountered an error: " + e.toString());
        }

        if (theCmdLine.hasOption("-s")) {
            weightType = "speed";
            System.out.println("GRIDrunner will use weight type: " + weightType);
        }
        else if(theCmdLine.hasOption("-t")) {
            weightType = "time";
            System.out.println("GRIDrunner will use weight type: " + weightType);
        }
        else {
            System.out.println("No weight type option specified for GRIDrunner"
                    + "--default weight type \"time\" will be used.");
        }

        if(theCmdLine.hasOption("-mod")) {
            System.out.println("GRIDrunner will use speed modifier: " + Double.parseDouble(theCmdLine.getOptionValue("mod")));
        }


        // file chooser for the map file
        File mapFile = fileChooser();

        try{
            if(mapFile != null) {
                String mapFileStr = mapFile.getPath();
                myMap = myReader.readMapFile(mapFileStr);
                myMap.setupMapAsServer();

                // file chooser for the list of to and from links, i.e., config file
                File toFromLinksFile = fileChooser();

                if(toFromLinksFile != null) {
                    String toFromLinksFileStr = FileUtils.readFileToString(toFromLinksFile, encoding);
                    String[] splitToFromLinks = toFromLinksFileStr.split("\n");

                    System.out.println("\nStarting run. . .");
                    String mapFileName = FilenameUtils.getBaseName(mapFile.getName());
                    System.out.println("Map File: " + mapFileName);

                    long startTime = System.nanoTime();

                    for (int i = 1; i < splitToFromLinks.length + 1; i++) {
                        testAgent001 = getTestAgent(splitToFromLinks[i - 1]);

                        String agentFrom = myMap.getRoad(testAgent001.getCurrentLink()).getTo();
                        String agentTo = myMap.getRoad(testAgent001.getDestination()).getFrom();
                        String modRoad = "";

                        int accidentLocation = 4;

                        GRIDpathfinder theALG = new GRIDpathfinder(myMap, weightType);
                        GRIDroute outRoute = theALG.findPath(testAgent001, 0L, speedModifier, currentRoad, modRoad);

                        if (outRoute.getAgent_ID() != "Destination unreachable") {
                            System.out.println("Successfully calculated route for agent " + i);
                            mainEmissionsContainer.appendOutputString("Calculated route for agent "
                                    + i + ": " +
                                    " from: " + agentFrom +
                                    " to: " + agentTo + " is: " + outRoute.toString() + "\n");
                        } else {
                            System.out.println("Destination Unreachable");
                        }

                        String[] splitOutRoute = outRoute.toString().split(" ");
                        ArrayList<String> splitOutRouteList = new ArrayList<>(Arrays.asList(splitOutRoute));
                        splitOutRouteList.ensureCapacity(50);

                        for(int n = 0; n < 20; n++){
                            splitOutRouteList.add("");
                        }

                        System.out.println("List cap: " + splitOutRouteList.size());

                        String tempString = testAgent001.getCurrentLink() + " ";

                        for (int j = 2; j < splitOutRouteList.size(); j++){
                            tempString += splitOutRouteList.get(j) + " ";

                            if(splitOutRouteList.get(j) == ""){
                                continue;
                            }
                            testAgent001.setOrigin(myMap.getRoad(splitOutRouteList.get(j)).getTo());
                            testAgent001.setLink(splitOutRouteList.get(j));

                            if(j == accidentLocation){
                                currentRoad = splitOutRouteList.get(j);
                                modRoad = splitOutRouteList.get(j+1);
                                speedModifier = Double.parseDouble(theCmdLine.getOptionValue("mod"));
                                System.out.println("\nGRIDrunner says. . .\n" +
                                                   "Current Road: " + currentRoad + "\nModified Road: " + modRoad +
                                                   "\nSpeed Modifier: " + speedModifier + "\n");
                            }

                            outRoute = theALG.findPath(testAgent001, 0L, speedModifier, currentRoad, modRoad);

                            if(j == accidentLocation){
                                System.out.println("We're in the if statement");
                                System.out.println("\n***** ROUTE CHANGED *****\n");
                                mainEmissionsContainer.appendOutputString("\n***** ROUTE CHANGED *****\n");
                                String[] splitNewOutRoute = outRoute.toString().split(" ");
                                for(int k = 1; k < splitNewOutRoute.length; k++){
                                    splitOutRouteList.set(k+accidentLocation,splitNewOutRoute[k]);
                                    System.out.println("splitOutRoute[" + (k+accidentLocation) + "]: " + splitOutRouteList.get(k+accidentLocation) +
                                                       " splitNewOutRoute[" + k + "]: " + splitNewOutRoute[k]);
                                }
                            }

                            mainEmissionsContainer.appendOutputString("\nCalculated route for agent "
                                    + i + ": " +
                                    " from: " + agentFrom +
                                    " to: " + agentTo + " is: " + outRoute.toString() + "\n");

                            System.out.println("Link: " + splitOutRouteList.get(j));

                            currentRoad = "";
                            modRoad = "";

                        }

                        System.out.println("tempString: " + tempString);
                        mainEmissionsContainer.appendOutputString("\nFinal Route: " + tempString + "\n");

                        SpeedReader speedReader = new SpeedReader();
                        EmissionsContainer tempEmissionsContainer = new EmissionsContainer();
                        tempEmissionsContainer = speedReader.readWriteSpeedString(tempString, mapFileStr);

                        mainEmissionsContainer.sumEmissionsTotal(tempEmissionsContainer.getEmissionsTotal());
                        mainEmissionsContainer.sumRoadLengthTotal(tempEmissionsContainer.getRoadLengthTotal());
                        mainEmissionsContainer.appendOutputString(tempEmissionsContainer.getOutputString());
                    }

                    // prepare output data
                    outData = mainEmissionsContainer.getOutputString();
                    totalMiles += mainEmissionsContainer.getRoadLengthTotal();
                    totalEmissions += mainEmissionsContainer.getEmissionsTotal();
                    outData += "Network: \t\t\t" + mapFileName + "\n";
                    outData += "Total Agents:\t\t" + splitToFromLinks.length + "\n";
                    outData += "Total Miles:\t\t" + totalMiles + "\n";
                    outData += "Total Emissions:\t" + totalEmissions + " g\n";
                    outData += "Average g/Mile:\t\t" + totalEmissions / totalMiles + "\n";

                    System.out.println("Network:\t\t" + mapFileName);
                    System.out.println("Total Agents:\t\t" + splitToFromLinks.length);
                    System.out.println("Total Miles:\t\t" + totalMiles);
                    System.out.println("Total Emissions:\t" + totalEmissions + " g\n");
                    System.out.println("Average g/Mile:\t\t" + totalEmissions / totalMiles + "\n");

                    long stopTime = System.nanoTime();
                    long timeToRun = ((stopTime - startTime) / 1000000);

                    System.out.print("\nTook " + timeToRun / 1000.0 + " Seconds\n");
                    outData += "\nTook " + timeToRun / 1000.0 + " Seconds\n";

                    try {
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                        ;
                        File file = new File("Emissions_Test_Results\\"
                                + dateFormat.format(date) + "_" + mapFileName + "_" + weightType + "_test_results.txt");
                        writeStringToFile(new File(String.valueOf(file)), outData, encoding, true);
                    } catch (IOException e) {
                        throw new Exception(e);
                    }

                    System.out.print("\nAnd we're done.\n");
                }
                else {
                    System.out.println("File selection cancelled.");
                }
            }
            else{
                System.out.println("File selection cancelled.");
            }

        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    private static GRIDagent getTestAgent(String toAndFromLinks)
    { // String Id, String newLink, String origin, String destination
        String agtID = "testAgent001";
        int startIndex = 0;
        int stopIndex = 0;
        String tempStringTo = "";
        String tempStringFrom = "";

        stopIndex = toAndFromLinks.indexOf('\t', startIndex);
        tempStringFrom = toAndFromLinks.substring(startIndex, stopIndex);
        startIndex = stopIndex+1;
        stopIndex = toAndFromLinks.indexOf('\t', startIndex);
        tempStringTo = toAndFromLinks.substring(startIndex, stopIndex);

        GRIDagent myAgent = new GRIDagent(agtID,tempStringFrom,tempStringFrom,tempStringTo, false, false);

        return myAgent;
    }

    private static GRIDagent getTestAgent(String from, String to){
        GRIDagent myAgent = new GRIDagent("testAgent",from,from,to, false, false);

        return myAgent;
    }

    public static File fileChooser() {
        String currdir = "..\\data\\";
        JFileChooser fileChooser = new JFileChooser(currdir);
        File fileToSelect = null;
        int option = fileChooser.showOpenDialog(null);
        if(option == JFileChooser.APPROVE_OPTION){
            fileToSelect = fileChooser.getSelectedFile();
            return fileToSelect;
        }else{
            fileChooser.cancelSelection();
            return null;
        }
    }

}