package edu.ucdenver.cse.GRIDutil;

import java.lang.Exception;
import java.lang.*;

public class EmissionsCalculator {

    public static EmissionsContainer initEmissionsCalculator(String inData) throws Exception {
        String[] splitSpeedResults = inData.split("\n");
        EmissionsContainer emissionsContainer = new EmissionsContainer();

        emissionsContainer = calculateEmissions(splitSpeedResults);

        return emissionsContainer;
    }

    public static EmissionsContainer calculateEmissions(String[] splitSpeedResults){
        double roadLength = 0.0;
        double currentSpeed = 0.0;
        double emissions = 0.0;
        double totalRoadLength = 0.0;
        double mileInMeters = 1609.34;
        String tempString = "";
        String sysString = "";

        for(int i = 0; i < splitSpeedResults.length; i++){
            int startIndex = 0;
            int stopIndex = 0;
            startIndex = splitSpeedResults[i].indexOf("length=", startIndex);
            stopIndex = splitSpeedResults[i].indexOf('\t', startIndex);
            tempString = splitSpeedResults[i].substring(startIndex, stopIndex).replaceAll("[^\\d.]", "");

            roadLength = Double.parseDouble(tempString);
            totalRoadLength += roadLength;

            startIndex = splitSpeedResults[i].indexOf("freespeed=", startIndex);
            stopIndex = splitSpeedResults[i].indexOf("\t", startIndex);
            tempString = splitSpeedResults[i].substring(startIndex, stopIndex).replaceAll("[^\\d.]", "");

            currentSpeed = Double.parseDouble(tempString);

            /* now we want to calculate the grams emitted while on the current link
            this is based on the speed on the link and then the emissions are determined
            by the length of the link converted to miles and multiplied by the corresponding
            G/Mile value */
            if(currentSpeed == 4.166666666666667){
                emissions += (roadLength/mileInMeters)*752.8864229923; // 760
            }
            else if(currentSpeed == 8.333333333333334){
                emissions += (roadLength/mileInMeters)*445.6267061049; // 450
            }
            else if(currentSpeed == 12.5){
                emissions += (roadLength/mileInMeters)*352.4226326999; // 360
            }
            else if(currentSpeed == 13.88888888888889){
                emissions += (roadLength/mileInMeters)*339.3114201648; // 350
            }
            else if(currentSpeed == 16.666666666666668){
                emissions += (roadLength/mileInMeters)*326.286752086;
            }
            else if(currentSpeed >= 17.8815555556 && currentSpeed < 22.3519444444){ // sweet spot
                emissions += (roadLength/mileInMeters)*324.5; // 325
            }
            else if(currentSpeed == 33.333333333333336){
                emissions += (roadLength/mileInMeters)*374.0797563346; // 450
            }
            // else to catch potentially uncovered speeds, this shouldn't happen
            else{
                System.out.println("*****UNCOVERED SPEED***** " + currentSpeed);
                sysString = "\n*****UNCOVERED SPEED***** " + currentSpeed + "\n\n";
            }
        }

        double lengthInMiles = (double)Math.round(totalRoadLength/mileInMeters * 10000d) / 10000d;
        double gramsPerMile = (double)Math.round(emissions/lengthInMiles * 10000d) / 10000d;
        emissions = (double)Math.round(emissions * 10000d) / 10000d;

        System.out.println("\nTotal Road Length for Agent:\t" + lengthInMiles + " miles");
        System.out.println("Total Emissions for Agent:\t" + emissions + " G");
        System.out.println("Grams per Mile for Agent:\t" + gramsPerMile + "\n");

        String endString = "";
        endString += "\nTotal Road Length for Agent:\t" + lengthInMiles + " miles";
        endString += "\nTotal Emissions for Agent:\t\t" + emissions + " G";
        endString += "\nGrams per MIle for Agent:\t\t" + gramsPerMile + "\n\n";
        endString += sysString;

        EmissionsContainer emissionsContainer = new EmissionsContainer();
        emissionsContainer.setEmissionsTotal(emissions);
        emissionsContainer.setRoadLengthTotal(lengthInMiles);
        emissionsContainer.setOutputString(endString);

        return emissionsContainer;
    }
}
