package aima.osm;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import aimax.osm.data.entities.MapNode;

public class ManhattanDistance implements HeuristicFunction {
	
	private MapNode goalState;
	
	public ManhattanDistance(MapNode goalState) {
		System.out.println("Using ManhattanDistance");
		this.goalState = goalState;
	}

	@Override
	public double h(Object state) {
		
		MapNode currState = (MapNode) state;
			
		double LatDiff = Math.abs(currState.getLat() - goalState.getLat());
		double LonDiff = Math.abs(currState.getLon() - goalState.getLon());
		double distance = LatDiff + LonDiff;
		
		return distance;
	}
}
