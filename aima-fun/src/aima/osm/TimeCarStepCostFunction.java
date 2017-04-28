package aima.osm;

import java.util.HashMap;

import aima.core.agent.Action;
import aima.core.search.framework.problem.StepCostFunction;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.routing.OsmMoveAction;

public class TimeCarStepCostFunction implements StepCostFunction {
	
	private static HashMap<String, Double> ruleMap = new HashMap<String, Double>();
	
	static{
		ruleMap.put("motorway",200.0d);
		ruleMap.put("motorway_link",200.0d);
		ruleMap.put("trunk",120.0d);
		ruleMap.put("trunk_link",120.0d);
		ruleMap.put("primary",100.0d);
		ruleMap.put("primary_link",100.0d);
		ruleMap.put("secondary",80.0d);
		ruleMap.put("tertiary",60.0d);
		ruleMap.put("road",50.0d);
		ruleMap.put("residential",30.0d);
		ruleMap.put("living_street",30.0d);
		ruleMap.put("service",10.0d);
		ruleMap.put("unclassified",5.0d);	
	}
	
	private static double getSpeed(MapWay way) {
		
		Double speed = ruleMap.get(way.getAttributeValue("highway"));
		
		if(way.getAttributeValue("maxspeed") != null) {
			try {
				speed = Double.parseDouble(way.getAttributeValue("maxspeed"));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}	
		}	
		return speed;
	}

	@Override
	public double c(Object s, Action a, Object sDelta) {
		MapNode stateFrom = (MapNode) s;
		MapNode stateTo = (MapNode) sDelta;
		OsmMoveAction action = (OsmMoveAction) a;
		
		
		double speed = getSpeed(action.getWay());
		
		return  1 / (action.getTravelDistance() * speed);
	}

}
