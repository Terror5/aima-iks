package aima.osm;

import java.util.HashMap;

import aima.core.agent.Action;
import aima.core.search.framework.problem.StepCostFunction;
import aimax.osm.routing.OsmMoveAction;

public class FunBikeStepCostFunction implements StepCostFunction {
	
	private static HashMap<String, Double> ruleMap = new HashMap<String, Double>();
	
	static{
		ruleMap.put("primary",0.0d);
		ruleMap.put("primary_link",0.0d);
		ruleMap.put("secondary",0.0d);
		ruleMap.put("tertiary",0.0d);
		ruleMap.put("road",0.0d);
		ruleMap.put("residential",0.0d);
		ruleMap.put("living_street",0.0d);
		ruleMap.put("pedestrian",0.0d);
		ruleMap.put("service",0.0d);
		ruleMap.put("track",0.0d);
		ruleMap.put("cycleway",0.0d);
		ruleMap.put("path",0.0d);
		ruleMap.put("footway",0.0d);
		ruleMap.put("unclassified",0.0d);
	}
	

	@Override
	public double c(Object s, Action a, Object sDelta) {
		return ((OsmMoveAction) a).getTravelDistance();
	}

}
