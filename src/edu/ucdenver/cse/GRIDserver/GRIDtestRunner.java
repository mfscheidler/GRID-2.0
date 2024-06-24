package edu.ucdenver.cse.GRIDserver;

import edu.ucdenver.cse.GRIDmap.GRIDmap;
import edu.ucdenver.cse.GRIDmap.GRIDmapReader;
import edu.ucdenver.cse.GRIDcommon.GRIDagent;
import edu.ucdenver.cse.GRIDcommon.GRIDroute;
import edu.ucdenver.cse.GRIDcommon.logWriter;
import edu.ucdenver.cse.GRIDutil.SpeedReader;
import java.util.logging.Level;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;

public class GRIDtestRunner{

    private CommandLine theCmdLine;
    String weightType;
    static logWriter testLW;
    private GRIDmapReader myReader = new GRIDmapReader();
    private GRIDmap myMap = new GRIDmap();
    private GRIDagent testAgent001;

    @Test
    public void runTest() throws Exception {

        String mapFile = "data/DesMoinesNetwork.xml";  // AlamosaNetwork AvocaNetwork DesMoinesNetwork DenverNetwork
        this.weightType = "speed"; // speed BPR weightTimeAvg

        System.out.println("Using weighter: " + this.weightType);
        testLW.log(Level.INFO, "GRIDtestRunner using weighter: " + this.weightType);

        this.myMap = myReader.readMapFile(mapFile);
        myMap.setupMapAsServer();
        this.testAgent001 = getTestAgent();

        String agentFrom = myMap.getRoad(testAgent001.getCurrentLink()).getTo();
        String agentTo = myMap.getRoad(testAgent001.getDestination()).getFrom();

        //System.out.println("Intersections2: " + myMap.getIntersections());

        testLW.log(Level.INFO, "This is another nother test.");

        System.out.println("\nStarting test. . .");

        Long startTime = System.nanoTime();

        GRIDpathfinder theALG = new GRIDpathfinder(myMap, weightType);
        GRIDroute outRoute = theALG.findPath(testAgent001, 0L);

        if(outRoute.getAgent_ID() != "Destination unreachable"){
            testLW.log(Level.INFO, "Calculated route for agent: " + testAgent001.getId() +
                    " from: " + agentFrom +
                    " to: " + agentTo + " is: " + outRoute.toString());

            System.out.println("Calculated route for agent: " + testAgent001.getId() +
                    " from: " + agentFrom +
                    " to: " + agentTo + " is: " + outRoute.toString());
        }
        else{
            System.out.println("Destination Unreachable");
        }

        long stopTime = System.nanoTime();
        long timeToRun = ((stopTime - startTime)/1000000);

        System.out.print("\nTook " + timeToRun/1000.0 + " Seconds\n");

        SpeedReader speedReader = new SpeedReader();
        speedReader.readWriteSpeedString(outRoute.toString(), mapFile, weightType);

        System.out.print("\n\nAnd we're done.\n");
    }

    private GRIDagent getTestAgent()
    { // String Id, String newLink, String origin, String destination
        String agtID = "testAgent001",
                currentLink = "894129097_3",   // alamosa: 17003210_1_r des moines: 1030730161_1 1119737435_0_r
                                                // avoca:   15994253_0_r denver:     341987464_1
                currentIntrx = "894129097_3",

                destIntrx = "51225442_0";    // alamosa: 17004512_0_r des moines: 926664015_0_r 16011360_17
                                                // avoca:   15994270_2   denver:     261801022_0_r

        GRIDagent myAgent = new GRIDagent(agtID,currentLink,currentIntrx,destIntrx, false, false);

        return myAgent;
    }
}