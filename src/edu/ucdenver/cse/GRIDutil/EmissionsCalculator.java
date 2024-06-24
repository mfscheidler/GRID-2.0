package edu.ucdenver.cse.GRIDutil;

import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class EmissionsCalculator {

    public static void initEmissionsCalculator(String inData, String weightType) throws Exception {
        Charset encoding = StandardCharsets.UTF_8;
        String[] splitSpeedResults = inData.split("\n");
        String outData = "Weight Type: " + weightType + "\n";

        outData += calculateEmissions(splitSpeedResults);

        try{
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd") ;
            File file = new File("Emissions_Test_Results\\" + dateFormat.format(date) + "_test_results.txt") ;
            writeStringToFile(new File(String.valueOf(file)), outData, encoding, true);
        } catch (IOException e) {
            throw new Exception(e);
        }

    }

    public static String calculateEmissions(String[] splitSpeedResults){
        double roadLength = 0.0;
        double currentSpeed = 0.0;
        double emissions = 0.0;
        double totalRoadLength = 0.0;
        double mileInMeters = 1609.34;
        String tempString = "";

        //System.out.println("Array Length: " + splitSpeedResults.length);

        for(int i = 0; i < splitSpeedResults.length; i++){
            int startIndex = 0;
            int stopIndex = 0;
            startIndex = splitSpeedResults[i].indexOf('\"', startIndex);
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
                emissions += (roadLength/mileInMeters)*760;
            }
            else if(currentSpeed == 8.333333333333334){
                emissions += (roadLength/mileInMeters)*450;
            }
            else if(currentSpeed == 12.5){
                emissions += (roadLength/mileInMeters)*360;
            }
            else if(currentSpeed == 13.88888888888889){
                emissions += (roadLength/mileInMeters)*350;
            }
            else if(currentSpeed >= 15.8333 && currentSpeed < 24.4444){ // sweet spot
                emissions += (roadLength/mileInMeters)*325;
            }
            else if(currentSpeed == 33.333333333333336){
                emissions += (roadLength/mileInMeters)*450;
            }
        }

        double lengthInMiles = (double)Math.round(totalRoadLength/mileInMeters * 10000d) / 10000d;
        double gramsPerMile = (double)Math.round(emissions/lengthInMiles * 10000d) / 10000d;
        emissions = (double)Math.round(emissions * 10000d) / 10000d;

        System.out.println("\nTotal Road Length:\t" + lengthInMiles + " miles");
        System.out.println("Total Emissions:\t" + emissions + " G");
        System.out.println("Grams per Mile:\t\t" + gramsPerMile + "\n");

        String endString = "";
        endString += "Total Road Length:\t" + lengthInMiles + " miles";
        endString += "\nTotal Emissions:\t" + emissions + " G";
        endString += "\nGrams per MIle:\t\t" + gramsPerMile + "\n\n";

        return endString;
    }
}
