package aima.osm;

import java.util.HashMap;
import java.util.List;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.WayRef;

public class TimeCarHeuristicFunction implements HeuristicFunction {
	
	MapNode goalState;
	
	public TimeCarHeuristicFunction(MapNode goalState) {
		this.goalState = goalState;
	}
	
	@Override
	public double h(Object state) {
		MapNode currState = (MapNode) state;
		
		List<WayRef> ways = currState.getWayRefs();
		List<MapNode> nextNodes = ways.get(0).getWay().getNodes();
		EntityAttribute[] attributes = ways.get(0).getWay().getAttributes();
		
		//TODO
		
		double d = (new Position(currState)).getDistKM(goalState);
		return d;
	}

}
