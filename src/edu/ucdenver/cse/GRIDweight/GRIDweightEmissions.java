package edu.ucdenver.cse.GRIDweight;

import edu.ucdenver.cse.GRIDcommon.GRIDroute;
import edu.ucdenver.cse.GRIDmap.GRIDmap;
import edu.ucdenver.cse.GRIDmap.GRIDroad;

public class GRIDweightEmissions implements GRIDweight {

    final GRIDmap theMap;

    public GRIDweightEmissions(GRIDmap map) {
        this.theMap = map;
    }

    @Override
    public double calcWeight (String fromNode, String toNode, long startTime) {

        GRIDroad road = theMap.hasRoad(fromNode, toNode);

        double currentSpeed = road.getCurrentSpeed();
        double roadLength = road.getLength();

        double emissions = 0.0;

        if(currentSpeed == 0){
            return MAX_WEIGHT;
        }
        else if(currentSpeed <= tierOne){
            emissions = ((tierOne/currentSpeed)*tierOneEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierTwo){
            emissions = ((tierTwo/currentSpeed)*tierTwoEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierThree){
            emissions = ((tierThree/currentSpeed)*tierThreeEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierFour){
            emissions = ((tierFour/currentSpeed)*tierFourEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierFive){
            emissions = ((tierFive/currentSpeed)*tierFiveEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierSix){
            emissions = ((tierSix/currentSpeed)*tierSixEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierSeven){
            emissions = ((tierSeven/currentSpeed)*tierSevenEmissions)*roadLength/mileInMeters;
        }
        else{
            System.out.println("*****UNCOVERED SPEED***** " + currentSpeed);
        }

        return emissions;
    }

    public GRIDroute resetEmissionsForAgent() {
        GRIDroute newRoute = new GRIDroute();
        return newRoute;
    }
}
