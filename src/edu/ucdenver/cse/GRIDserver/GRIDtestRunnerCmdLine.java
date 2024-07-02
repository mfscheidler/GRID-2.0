package edu.ucdenver.cse.GRIDserver;

import org.apache.commons.cli.*;

public class GRIDtestRunnerCmdLine {
    private String[] theArgs;
    private Options theOptions;

    public GRIDtestRunnerCmdLine(String[] args) {
        this.theArgs = args;
        this.theOptions = new Options();

        final Option weightOptSpeed = Option.builder("s")
                .argName("speed")
                .hasArg(false)
                .required(false)
                .desc("weight type speed")
                .build();

        this.theOptions.addOption(weightOptSpeed);

        final Option weightOptTime = Option.builder("t")
                .argName("time")
                .hasArg(false)
                .required(false)
                .desc("weight type time")
                .build();

        this.theOptions.addOption(weightOptTime);
    }

    public CommandLine parseArgs() throws ParseException {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(theOptions, theArgs);
        } catch (ParseException e) {
            // This is bad, what should we do?
            System.out.println("Parser error - invalid input");
            throw e;
        }

        return cmd;
    }
}
