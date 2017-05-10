package osm.agent;

import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.Problem;
import aima.core.search.online.OnlineSearchProblem;
import aimax.osm.gui.fx.applications.OsmLRTAStarAgentApp;

//import aima.core.search.online.LRTAStarAgent;

public class MyOsmLRTAStarAgentApp extends OsmLRTAStarAgentApp {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public String getTitle() {
		return "My OSM LRTA* Agent App";
	}
	
	
	@Override
	protected Agent createAgent(List<String> locations) {
		HeuristicFunction heuristic;
		switch (simPaneCtrl.getParamValueIndex(PARAM_HEURISTIC)) {
		case 0:
			heuristic = MapFunctionFactory.getZeroHeuristicFunction();
			break;
		default:
			heuristic = MapFunctionFactory.getSLDHeuristicFunction(locations.get(1), map);
		}
		Problem p = new BidirectionalMapProblem(map, null, locations.get(1));
		OnlineSearchProblem osp = new OnlineSearchProblem(p.getActionsFunction(), p.getGoalTest(),
				p.getStepCostFunction());
		return new LRTAStarAgent(osp, MapFunctionFactory.getPerceptToStateFunction(), heuristic);
	}
}
