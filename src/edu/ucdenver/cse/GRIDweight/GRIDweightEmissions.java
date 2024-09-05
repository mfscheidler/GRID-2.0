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
    public double calcWeight (String fromNode, String toNode, long startTime, double speedModifier) {

        GRIDroad road = theMap.hasRoad(fromNode, toNode);

        double currentSpeed = road.getCurrentSpeed()*speedModifier;
        double roadLength = road.getLength();

        double emissions = 0.0;

        if(currentSpeed == 0){
            System.out.println("*****MAX_WEIGHT*****\n");
            return MAX_WEIGHT;
        }
        else if(currentSpeed == tierOne*speedModifier){
            emissions = ((tierOne/currentSpeed)*tierOneEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed == tierTwo*speedModifier){
            emissions = ((tierTwo/currentSpeed)*tierTwoEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed == tierThree*speedModifier){
            emissions = ((tierThree/currentSpeed)*tierThreeEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed == tierFour*speedModifier){
            emissions = ((tierFour/currentSpeed)*tierFourEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed == tierFive*speedModifier){
            emissions = ((tierFive/currentSpeed)*tierFiveEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed == tierSix*speedModifier){
            emissions = ((tierSix/currentSpeed)*tierSixEmissions)*roadLength/mileInMeters;
            if(speedModifier == 0) {
                System.out.println("calcWeight says...\nSpeed Modifier: " + speedModifier + " Emissions: " + emissions + "\n");
            }
        }
        else if(currentSpeed == tierSeven*speedModifier){
            emissions = ((tierSeven/currentSpeed)*tierSevenEmissions)*roadLength/mileInMeters;
        }
        else if(currentSpeed == tierEight*speedModifier){
            emissions = ((tierEight/currentSpeed)*tierEightEmissions)*roadLength/mileInMeters;
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
