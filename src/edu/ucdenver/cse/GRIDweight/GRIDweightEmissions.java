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

        if(currentSpeed <= tierOne){
            emissions = ((currentSpeed/tierOne)*tierOneEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed <= tierTwo){
            emissions = ((currentSpeed/tierTwo)*tierTwoEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed < idealSpeedLow){
            emissions = ((currentSpeed/idealSpeedLow)*sweetSpotEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed >= idealSpeedLow && currentSpeed <= idealSpeedHigh){
            emissions = sweetSpotEmissions/mileInMeters;
        }
        else if(currentSpeed <= tierThree){
            emissions = ((currentSpeed/tierThree)*tierThreeEmissions)*roadLength/mileInMeters;
        }
        else{   // this conditional handles values above tierThree and below and above tierFour
            emissions = ((currentSpeed/tierFour)*tierFourEmissions)*roadLength/mileInMeters;
        }

        return emissions;
    }

    public GRIDroute resetEmissionsForAgent() {
        GRIDroute newRoute = new GRIDroute();
        return newRoute;
    }
}
