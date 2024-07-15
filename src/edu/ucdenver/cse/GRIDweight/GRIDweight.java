package edu.ucdenver.cse.GRIDweight;

public interface GRIDweight {
	// defaults
    public double tierOne = 4.1667;         // 760 G/Mi
    public double tierTwo = 8.3333;        // 450 G/Mi
    public double tierThree = 12.5;      // 360 G/Mi
    public double tierFour = 13.88888888888889;
    public double tierFive = 16.666666666666668;       // 450 G/Mi
    public double tierSix = 22.22222222222222; // Sweet Spot
    public double tierSeven = 33.333333333333336;
    public double MAX_WEIGHT = 2000000.0;   // High enough to always outweigh any other value

    public double tierOneEmissions = 752.8777457604738;
    public double tierTwoEmissions = 445.6216862844991;
    public double tierThreeEmissions = 352.42032044830836;
    public double tierFourEmissions = 339.3097647713417;
    public double tierFiveEmissions = 326.28609754868967;
    public double tierSixEmissions= 324.615879491236;
    public double tierSevenEmissions = 374.0869363674296;

    public double mileInMeters = 1609.34;
    
    // Must return the weight of traveling from the fromNode to the toNode, starting at time startTime
    double calcWeight(String fromNode, String toNode, long startTime );
}
