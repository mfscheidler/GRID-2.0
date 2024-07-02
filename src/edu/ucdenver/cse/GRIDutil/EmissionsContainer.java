package edu.ucdenver.cse.GRIDutil;

public class EmissionsContainer {
    private double emissionsTotal = 0.0;
    private double roadLengthTotal = 0.0;
    private int numberOfAgents = 0;
    private String weightType = "";
    private String outputString = "";

    public double getEmissionsTotal(){ return this.emissionsTotal; }
    public double getRoadLengthTotal(){ return this.roadLengthTotal; }
    public int getNumberOfAgents(){ return this.numberOfAgents; }
    public String getWeightType(){ return this.weightType; }
    public String getOutputString(){ return this.outputString; }

    public void setEmissionsTotal(double inEmissions){ this.emissionsTotal = inEmissions; }
    public void setRoadLengthTotal(double inRoadLength) { this.roadLengthTotal = inRoadLength; }
    public void setNumberOfAgents(int inNumberOfAgents){ this.numberOfAgents = inNumberOfAgents; }
    public void setWeightType(String weightType){ this.weightType = weightType; }
    public void setOutputString(String outputString){ this.outputString = outputString; }

    public void sumEmissionsTotal(double inEmissions){ this.emissionsTotal += inEmissions; }
    public void sumRoadLengthTotal(double inRoadLength){ this.roadLengthTotal += inRoadLength; }
    public void sumNumberOfAgents(int inNumberOfAgents){ this.numberOfAgents += inNumberOfAgents; }
    public void appendOutputString(String inputString){ this.outputString += inputString; }
}
