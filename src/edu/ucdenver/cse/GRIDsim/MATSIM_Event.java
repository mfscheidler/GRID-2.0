package edu.ucdenver.cse.GRIDsim;

import java.lang.*;

import org.matsim.api.core.v01.events.Event;

public abstract class MATSIM_Event extends Event {

    private double time;
    private double emissions;

    public MATSIM_Event(double time, double emissions) {
        super(time);
        this.emissions = emissions;
    }

    public double getEmissions(){ return emissions; }
    public final void setEmissions(double newEmissions){ this.emissions = newEmissions; }
}
