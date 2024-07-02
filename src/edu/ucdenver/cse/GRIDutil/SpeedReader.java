package edu.ucdenver.cse.GRIDutil;

import java.nio.file.*;
import java.io.IOException;
import java.lang.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SpeedReader {
    public EmissionsContainer readWriteSpeedString(String finalRoute, String fileName) throws Exception {

        EmissionsContainer emissionsContainer = new EmissionsContainer();
        String[] links = finalRoute.split(" ");
        Charset encoding = StandardCharsets.UTF_8;

        // reading in map file as a string
        String mapData = "";
        String outData = "";
        int startIndex = 0;
        int stopIndex = 0;
        String speed ="";

        try {
            mapData = readFileAsString(fileName);
        } catch (IOException e) {
            throw new Exception(e);
        }

        // write pertinent data to output file via string
        for(int i = 1; i < links.length; i++){
            // string is tab-delimited for ease of use
            outData += "Link: " + links[i] + "\t";

            // grab length
            startIndex = mapData.indexOf(links[i]);
            startIndex = mapData.indexOf("length=", startIndex);
            stopIndex = mapData.indexOf(" ", startIndex);
            speed = mapData.substring(startIndex, stopIndex) + "\t";

            // grab speed
            startIndex = mapData.indexOf(links[i]);
            startIndex = mapData.indexOf("freespeed=", startIndex);
            stopIndex = mapData.indexOf(" ", startIndex);
            speed += mapData.substring(startIndex, stopIndex);
            outData += speed + "\t\n";
        }

        // send speed data & road length as string to calculate emissions; weightType sent for output purposes
        emissionsContainer = EmissionsCalculator.initEmissionsCalculator(outData);

        System.out.println("end of line\n");

        return emissionsContainer;
    }

    public static String readFileAsString(String filename)
            throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(filename)));
        return data;
    }

}
