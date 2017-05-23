/*
    Code Citation:

    Original designer: Keith Schwarz, Stanford CS Dept.

    Using code from the following website as the basis for the Fibonacci heap
    data structure for our Dijkstra's implementation:

    http://www.keithschwarz.com/interesting/
    http://www.keithschwarz.com/interesting/code/?dir=fibonacci-heap
*/

package edu.ucdenver.cse.GRIDserver;

import edu.ucdenver.cse.GRIDcommon.GRIDagent;
import edu.ucdenver.cse.GRIDcommon.GRIDroute;
import edu.ucdenver.cse.GRIDcommon.GRIDrouteSegment;
import edu.ucdenver.cse.GRIDcommon.logWriter;
import edu.ucdenver.cse.GRIDmap.*;
import edu.ucdenver.cse.GRIDweight.*;
import edu.ucdenver.cse.GRIDcommon.logWriter;
import java.util.logging.Level;


import java.util.concurrent.*;

import java.util.*;
import java.util.ListIterator;

public class GRIDpathfinder {
    private GRIDmap ourMap;
    private GRIDDirectedGraph graph;
    
    private ConcurrentMap<String, GRIDnode> currentPathTotal;
    private ConcurrentHashMap<String, String> previousIntersections;
    private ConcurrentMap<String, GRIDrouteSegment> finalRouteSegments;
    private GRIDweight theWeighter;

    public GRIDpathfinder(GRIDmap theMap) {
    	this.ourMap = theMap;
    	
    	// RCS change to init the directed graph. MOVE TO INIT FUNCTION???
        // RCS I "Think" This is taken care of in the map
    	//graph = graphLoadEdges(theMap);
        
        currentPathTotal = new ConcurrentHashMap<String, GRIDnode>();
        previousIntersections = new ConcurrentHashMap<String, String>();
        finalRouteSegments = new ConcurrentHashMap<String, GRIDrouteSegment>();
        
        graph = new GRIDDirectedGraph();
        
        // This is the class to change in order to use different weighting schemes
        theWeighter = new GRIDweightTime(ourMap);
        
        // This is where we change WHICH weighting scheme we are using. There has to be a better
        // way to change it other than hard coding
             
    }
    
    public void init() {
    	// Set things up here
    	graph.loadEdges(ourMap);
    }

    public GRIDroute findPath(GRIDagent thisAgent, Long currentTime) {
        GRIDfibHeap pq = new GRIDfibHeap();

        Map<String, GRIDfibHeap.Entry> entries = new HashMap<>();
        GRIDnode startNodeValues;
        //ConcurrentMap<String, GRIDnodeWtTmEm> currentPathTotal = new ConcurrentHashMap<>();
        //ConcurrentHashMap<String, String> previousIntersections = new ConcurrentHashMap<>();
        /* BEGIN here is the new data structure for segments */
        //ConcurrentMap<String, GRIDrouteSegment> finalRouteSegments = new ConcurrentHashMap<>();
        /* END */
        Long thisTimeslice = currentTime/1000;
        Long totalTravelTime = thisTimeslice;
        String agtFrom; 
        String agtTo;
        String agtID;

        /* The agent is already on the link, so we need its endpoint
         */
        
        agtID = thisAgent.getId();
        		     
        // RCS Change to get from the directed graph
        //agtFrom = graph.getRoad(thisAgent.getCurrentLink()).getTo();
        String testRoadName = thisAgent.getCurrentLink();
        GRIDroad testRoad = ourMap.getRoad(testRoadName);
        String dummmmmm = testRoad.getTo();
        
        agtFrom = ourMap.getRoad(thisAgent.getCurrentLink()).getTo();
        
        /* The agent will end somewhere on the final link, so we need to get to its "from end"
         */

        // RCS Change to get from the directed graph
        //agtTo = graph.getRoad(thisAgent.getDestination()).getFrom();
        agtTo = ourMap.getRoad(thisAgent.getDestination()).getFrom();

        startNodeValues = new GRIDnode();
        startNodeValues.setNodeWeighttTotal(0.0);
        startNodeValues.setNodeTimeTotal(thisTimeslice);
        GRIDnode tempNode = startNodeValues;

        /* source/destination check
         */
        //System.out.println("agtFrom: "+agtFrom);
        //System.out.println("agtTo: "+agtTo);

        /* DUMB check - prevent elsewhere
         */
        if (agtTo.equals(agtFrom)) {
            return null;
        }

        /* roadList creation--necessary for fibHeap mGraph data structure
         */
        // RCS is this already done in the map? Do we need to do it in the directed graph instead?
        //graph.initMap();;

        // Replace the iterable part of GRIDmap
        
        for (String node : graph)
            entries.put(node, pq.enqueue(node, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0L));

        pq.decreaseKey(entries.get(agtFrom), 0.0, 0.0, thisTimeslice);

        /* prime the while loop with the start node, which is the starting min
         */
        GRIDfibHeap.Entry curr = pq.dequeueMin();

        
        
        while (!pq.isEmpty())
        {
            currentPathTotal.put(curr.getValue(), tempNode);

            // Update the priorities/weights of all of its edges.
            
            GRIDrouteSegment tempSegment = null; 
            String tempRoadID = "";
            
            for (Map.Entry<String, Double> arc : graph.edgesFrom(curr.getValue()).entrySet()) {
                if (currentPathTotal.containsKey(arc.getKey())) continue;

                /* Compute the cost of the path from the source to this node,
                 * which is the cost of this node plus the cost of this edge.
                 */
                
                // RCS incorporate the new helper classes here
                // RCS this will need the map, not the graph
                
                double tempWeight;
                
                tempWeight = theWeighter.calcWeight(curr.getValue(), 
                		                          arc.getKey(),
                                                  currentPathTotal.get(curr.getValue()).getNodeTimeTotal());
               
                tempNode.setNodeWeighttTotal(tempWeight);
                
                /* If the length of the best-known path from the source to
                 * this node is longer than this potential path cost, update
                 * the cost of the shortest path.
                 */
                GRIDfibHeap.Entry dest = entries.get(arc.getKey());
                //Double newWeight = tempNode.getNodeWtTotal()+curr.getWtTotal();
                /* BEGIN new code for different weight calculations*/
                double newWeight = getEdgeWeight(tempNode)+curr.getWtTotal();
                /* END */

                if (newWeight < dest.getWtTotal())
                {
                    Long tempTime = currentPathTotal.get(curr.getValue()).getNodeTimeTotal();

                    tempNode.setNodeTimeTotal(tempTime+tempNode.getNodeTimeTotal());
                    Long tempTmTotal = tempNode.getNodeTimeTotal();
                    //Double tempEmissions = tempNode.getNodeEmissions();

                    pq.decreaseKey(dest, 0D, newWeight, tempTmTotal);
                    previousIntersections.put(dest.getValue(),curr.getValue());

                    /* BEGIN here is the new data structure for segments */
                    // RCS fix to get road ID
                    //String tempString = graph.getRoadListItem(curr.getValue()+dest.getValue()).getId();
                    
                    //RCS changed 
                    //String tempString = graph.getRoad(curr.getValue()+dest.getValue()).getId();
                    
                    // getRoad requires the road ID, not the combination of the start / end values
                    tempRoadID = ourMap.hasRoad(curr.getValue(), dest.getValue()).getId();
                    
                    if (tempRoadID.equals(null)) {
                    	// THIS IS BAD
                    	logWriter.log(Level.WARNING, "ATTEMPT TO USE NULL ROAD ID");
                    	continue;
                    }
                    
                    tempSegment = new GRIDrouteSegment();
                    
                    tempSegment.setRoad_ID(tempRoadID);
                    tempSegment.setStartIntersection(curr.getValue());
                    tempSegment.setEndIntersection(dest.getValue());
                    tempSegment.setTimeAtRoadExit(tempTime);
                    
                   // finalRouteSegments.put(tempString, tempSegment);
                    
                    //GRIDrouteSegment tempSegment = new GRIDrouteSegment(tempString, tempTmTotal, tempEmissions);
                    /* END */
                }
            }
            
            if(tempSegment != null) {
            	finalRouteSegments.put(tempRoadID, tempSegment);
            }
            
           // 

            
            /* Grab the current node.  The algorithm guarantees that we now
             * have the shortest distance to it.
             */
            curr = pq.dequeueMin();
            tempNode.setNodeTimeTotal(curr.getTmTotal());

            /* this conditional statement is necessary to correct for not starting
             * at the actual starting, i.e., from node for the starting link; we
             * can look at correcting this...
             */
            if(curr.getValue().equals(agtTo)) {
            	totalTravelTime = curr.getTmTotal();
            }
        
        }
        // RCS remove this?
        
        /* BEGIN weight/time testing
         * 38347489_0 (404)
         * 38347521_0 (404)
         * 1779115801223351743
        */
        //GRIDroad tempTestRoad = graph.getRoadListItem("1779115801223351743");
        //System.out.println("weight on 38347521_0 at 404: "+tempTestRoad.getWeightAtTime(404L));
        /* END weight/time testing */

        // List, in order, of the intersections our route connects
        
        List<String> theIntersections = new ArrayList<String>();
        

        String step = agtTo;
        theIntersections.add(step);
        
        if(previousIntersections.get(step) == null)
        {
            System.out.println("\nI guess it's null, friend.");
            return null;
        }

        /* Create the final path from source to destination
         */
        
        // RCS we need to figure out how to do this with the routeSegments
        while(previousIntersections.get(step)!= null)
        {
            step = previousIntersections.get(step);
            theIntersections.add(step);
        }

        GRIDroute finalRoute = new GRIDroute();
        finalRoute.setAgent_ID(agtID);

        /* BEGIN build segment list */
        
        // Iterate over the list BACKWARDS
        
        // RCS this won't be necessary if we fix the segments created above
        String startIntersection = theIntersections.get(theIntersections.size()-1);     
        GRIDrouteSegment tempSegment;
        
        ListIterator<String> iter = theIntersections.listIterator(theIntersections.size() -1);
        while(iter.hasPrevious()) {
        	String destIntersection = iter.previous();
        	String tempRoadID = ourMap.hasRoad(startIntersection, destIntersection).getId();
        	
        	tempSegment = new GRIDrouteSegment(startIntersection, destIntersection, tempRoadID);
    	
        	finalRoute.addSegment(tempSegment);
        	startIntersection = tempSegment.getEndIntersection();
        }
             
        return finalRoute;
    }

    protected Double getEdgeWeight(GRIDnode thisNode) {
        return thisNode.getNodeWeightTotal();
    }

    // RCS FIX THIS TO USE THE GRIDDirectedGraph code
    //private static GRIDmap graphLoadEdges(GRIDmap myGraph) {
 //   private static GRIDDirectedGraph graphLoadEdges(GRIDmap myGraph) { 
  //  	Long startTime = System.nanoTime();

//        ArrayList<String> networkIntersections = new ArrayList<>(myGraph.getIntersections().keySet());
 //       ArrayList<GRIDroad> networkRoads = new ArrayList<>(myGraph.getRoads().values());

//        for(int i = 0; i < networkIntersections.size(); i++)
//        {
//            myGraph.addNode(networkIntersections.get(i));
//        }

//        for(int i = 0; i < networkRoads.size(); i++)
//        {
//            //if(i+1 == networkRoads.size()){System.out.println("edges: "+(i+1));}
//            myGraph.addEdge(networkRoads.get(i).getFrom(), networkRoads.get(i).getTo(), networkRoads.get(i).getLength());
//        }

//        long stopTime = System.nanoTime();
//        long timeToRun = ((stopTime - startTime)/1000000);

        //System.out.println(timeToRun/1000.0 + "s required for middleware\n");
//        return myGraph;
//    }

/* END GRIDpathfinder CLASS */
}

