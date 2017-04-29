package aima.osm;

import org.apache.commons.math3.distribution.NormalDistribution;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import aimax.osm.data.entities.MapNode;

public class SimpleMahalanobisDistance implements HeuristicFunction {
	
	private NormalDistribution LonDist;
	private NormalDistribution LatDist;
	private MapNode goalState;
	private double standardDeviation = 1.0d;
	
	public SimpleMahalanobisDistance(MapNode goalState) {
		System.out.println("Using SimpleMahalanobisDistance");
		this.goalState = goalState;
		this.LonDist = new NormalDistribution(this.goalState.getLon(),this.standardDeviation);
		this.LatDist = new NormalDistribution(this.goalState.getLat(),this.standardDeviation);
	}

	@Override
	public double h(Object state) {
		
		MapNode currState = (MapNode) state;
			
		double LonProbability = LonDist.density(currState.getLon());
		double LatProbability = LatDist.density(currState.getLat());
		
		// Not really the malanobis distance ...
		//TODO implement
		return (1/LonProbability) + (1/LatProbability);
	}

}
