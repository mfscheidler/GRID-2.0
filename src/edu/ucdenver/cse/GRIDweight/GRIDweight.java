package edu.ucdenver.cse.GRIDweight;

public interface GRIDweight {
	// defaults
    public double tierOne = 4.1667;         // 760 G/Mi
    public double tierTwo = 8.3333;        // 450 G/Mi
    public double idealSpeedLow = 15.8333;  // 325 G/Mi 15.8333 (~35 mph) to
    public double idealSpeedHigh = 24.4444; // 325 G/Mi 24.4444 (~55 mph) in meters per second
    public double tierThree = 27.9613;      // 360 G/Mi
    public double tierFour = 74.5633;       // 450 G/Mi
    public double MAX_WEIGHT = 2000000.0;   // High enough to always outweigh any other value

    public int tierOneEmissions = 760;
    public int tierTwoEmissions = 450;
    public int sweetSpotEmissions = 325;
    public int tierThreeEmissions = 360;
    public int tierFourEmissions = 450;

    public double mileInMeters = 1609.34;
    
    // Must return the weight of traveling from the fromNode to the toNode, starting at time startTime
    double calcWeight(String fromNode, String toNode, long startTime );
}
