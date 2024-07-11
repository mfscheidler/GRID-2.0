package edu.ucdenver.cse.GRIDweight;

public interface GRIDweight {
	// defaults
    public double tierOne = 4.1667;         // 760 G/Mi
    public double tierTwo = 8.3333;        // 450 G/Mi
    public double tierThree = 12.5;      // 360 G/Mi
    public double tierFour = 16.666666666666668;
    public double idealSpeedLow = 17.8815555556;  // 325 G/Mi 15.8333 (~40 mph) to
    public double idealSpeedHigh = 22.3519444444; // 325 G/Mi 24.4444 (~50 mph) in meters per second
    public double tierFive = 33.333333333333336;       // 450 G/Mi
    public double MAX_WEIGHT = 2000000.0;   // High enough to always outweigh any other value

    public double tierOneEmissions = 752.8864229923;
    public double tierTwoEmissions = 445.6267061049;
    public double tierThreeEmissions = 352.4226326999;
    public double tierFourEmissions = 326.286752086;
    public double sweetSpotEmissions = 324.5;
    public double tierFiveEmissions = 374.0797563346;

    public double mileInMeters = 1609.34;
    
    // Must return the weight of traveling from the fromNode to the toNode, starting at time startTime
    double calcWeight(String fromNode, String toNode, long startTime );
}
