package edu.ucdenver.cse.GRIDutil;

import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.lang.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.text.SimpleDateFormat;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class SpeedReader {
    public static void readWriteSpeedString(String finalRoute, String fileName, String weightType) throws Exception {

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
        EmissionsCalculator.initEmissionsCalculator(outData, weightType);

        try{
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss") ;
            File file = new File("Emissions_Test_Results\\" + dateFormat.format(date) + "_" + weightType + "_data.txt") ;
            writeStringToFile(new File(String.valueOf(file)), outData, encoding);
        } catch (IOException e) {
            throw new Exception(e);
        }

        System.out.println("end of line");
    }

    public static String readFileAsString(String filename)
            throws Exception
    {
        String data = "";
        data = new String(
                Files.readAllBytes(Paths.get(filename)));
        return data;
    }

}
