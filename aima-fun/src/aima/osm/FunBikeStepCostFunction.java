package aima.osm;

import java.util.HashMap;

import aima.core.agent.Action;
import aima.core.search.framework.problem.StepCostFunction;
import aimax.osm.data.entities.MapWay;
import aimax.osm.routing.OsmMoveAction;

public class FunBikeStepCostFunction implements StepCostFunction {
	
	private static HashMap<String, Double> ruleMap = new HashMap<String, Double>();
	
	static{
		ruleMap.put("primary",0.0d);
		ruleMap.put("primary_link",0.0d);
		ruleMap.put("secondary",0.0d);
		ruleMap.put("tertiary",0.0d);
		ruleMap.put("road",1.0d);
		ruleMap.put("residential",1.0d);
		ruleMap.put("living_street",10.0d);
		ruleMap.put("pedestrian",0.0d);
		ruleMap.put("service",1.0d);
		ruleMap.put("track",15.0d);
		ruleMap.put("cycleway",20.0d);
		ruleMap.put("path",5.0d);
		ruleMap.put("footway",2.0d);
		ruleMap.put("unclassified",0.0d);
	}
	
	private static double getFunValue(MapWay way) {
		
		Double fun = ruleMap.get(way.getAttributeValue("highway"));
		//just in case - highwayType was not in rulemap
		return (fun == null) ? 1.0d : fun;
	}

	public FunBikeStepCostFunction() {
		System.out.println("Using FunBikeStepCostFunction");
	}
	
	
	@Override
	public double c(Object s, Action a, Object sDelta) {
		
		OsmMoveAction action = (OsmMoveAction) a;
		
		double distance = ((OsmMoveAction) a).getTravelDistance();
		double fun = getFunValue(action.getWay());
		
		return distance * (1 / fun);
	}

}
