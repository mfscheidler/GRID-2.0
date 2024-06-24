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
        /* BEGIN test output */
        /*System.out.println("ideal time: "+roadLength/idealSpeed);
        System.out.println("roadLength: "+roadLength);
        System.out.println("current speed: "+currentSpeed);
        System.out.println("negative: "+(currentSpeed-idealSpeed));
        /* System.out.println("emissions: "+emissions);*/
        /* END test output */

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
        else if(currentSpeed < idealSpeedLow){
            emissions = ((idealSpeedLow/currentSpeed)*sweetSpotEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed >= idealSpeedLow && currentSpeed <= idealSpeedHigh){
            emissions = sweetSpotEmissions*(roadLength/mileInMeters);
        }
        else if(currentSpeed <= tierThree){
            emissions = ((tierThree/currentSpeed)*tierThreeEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierFour){
            emissions = ((tierFour/currentSpeed)*tierFourEmissions)*roadLength/mileInMeters;
        }
        else{
            emissions = ((currentSpeed/tierFour)*tierFourEmissions)*roadLength/mileInMeters;
        }

        return emissions;
    }

    public GRIDroute resetEmissionsForAgent() {
        GRIDroute newRoute = new GRIDroute();
        return newRoute;
    }
}
