package aima.osm;

import java.util.HashMap;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

public class FunBikeHeuristicFunction implements HeuristicFunction {

	MapNode goalState;
	private static HashMap<String, Double> ruleMap = new HashMap<String, Double>();
	
	static{
		ruleMap.put("motorway",18.0d);
		ruleMap.put("motorway_link",17.0d);
		ruleMap.put("trunk",16.0d);
		ruleMap.put("trunk_link",15.0d);
		ruleMap.put("primary",14.0d);
		ruleMap.put("primary_link",13.0d);
		ruleMap.put("secondary",12.0d);
		ruleMap.put("tertiary",11.0d);
		ruleMap.put("road",10.0d);
		ruleMap.put("residential",9.0d);
		ruleMap.put("living_street",8.0d);
		ruleMap.put("pedestrian",7.0d);
		ruleMap.put("service",6.0d);
		ruleMap.put("track",5.0d);
		ruleMap.put("cycleway",4.0d);
		ruleMap.put("path",3.0d);
		ruleMap.put("footway",2.0d);
		ruleMap.put("steps",1.0d);
		ruleMap.put("unclassified",0.0d);
	}
	
	public FunBikeHeuristicFunction(MapNode goalState) {
		this.goalState = goalState;
	}
	
	@Override
	public double h(Object state) {
		MapNode currState = (MapNode) state;
		// TODO Heuristics evaluating motor and highways badly
		double d = (new Position(currState)).getDistKM(goalState);
		return d;
	}

}
