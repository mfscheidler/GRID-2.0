package edu.ucdenver.cse.GRIDutil;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.lang.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import static org.apache.commons.io.FileUtils.writeStringToFile;

public class TestFileFormatter {

    public static void main(String[] args) throws Exception {

        Charset encoding = StandardCharsets.UTF_8;
        File mapFile = fileChooser();

        String inData = "";
        String outData = "";

        try {
            if(mapFile != null){
                int startIndex = 0;
                int stopIndex = 0;
                String outputString = "";
                inData = readFileAsString(mapFile.getPath());

                while(stopIndex >= 0) {
                    startIndex = inData.indexOf("type=\"h\" link=\"", startIndex);
                    stopIndex = inData.indexOf(" end_time", startIndex);

                    // failsafe against out of inrange exception
                    if(stopIndex < 0 || startIndex < 0) {
                        break;
                    }

                    outputString += inData.substring(startIndex, stopIndex).replaceAll("[^_r\\d.]", "") + "\t";

                    startIndex = inData.indexOf("type=\"w\" link=\"", stopIndex);
                    stopIndex = inData.indexOf(" end_time", startIndex);
                    outputString += inData.substring(startIndex, stopIndex).replaceAll("[^_r\\d.]", "") + "\t\n";
                    startIndex = inData.indexOf("</person>", stopIndex);
                }
                try{
                    String tempString = FilenameUtils.getBaseName(mapFile.getName());
                    writeStringToFile(new File("Emissions_Test_Config\\"+tempString+"_Test_Config.txt"), outputString, encoding);
                } catch (IOException e) {
                    throw new Exception(e);
                }
            }
            else{
                System.out.println("File selection cancelled.");
            }

        } catch (IOException e) {
            throw new Exception(e);
        }

        System.out.println("end of line");
    }

    public static String readFileAsString(String filename)
            throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(filename)));
        return data;
    }

    public static File fileChooser() {
        String currdir = "..\\data\\PopulationFiles";
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
