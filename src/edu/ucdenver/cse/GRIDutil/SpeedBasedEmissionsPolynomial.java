package edu.ucdenver.cse.GRIDutil;

import java.lang.Math;

public class SpeedBasedEmissionsPolynomial {

    public static void main(String[] args) throws Exception {
        double b_0 = 7.613534994965560;
        double b_1 = -0.138565467462594;
        double b_2 = 0.003915102063854;
        double b_3 = -0.000049451361017;
        double b_4 = 0.000000238630156;
        double x = 37.2816666666666696492;
        double y = 0.0;
        double e = Math.E;


        double orderOne = 0.0;
        double orderTwo = 0.0;
        double orderThree = 0.0;
        double orderFour = 0.0;

        orderOne = b_1*x;
        //System.out.println(orderOne);
        orderTwo = b_2*Math.pow(x,2);
        //System.out.println(Math.pow(x,2));
        orderThree = b_3*Math.pow(x,3);
        //System.out.println(Math.pow(x,3));
        orderFour = b_4*Math.pow(x,4);
        //System.out.println(Math.pow(x,4));

        y = b_0 + orderOne + orderTwo + orderThree + orderFour;
        //System.out.println(y);
        y = Math.pow(e,y);
        System.out.println(y);
    }
}
