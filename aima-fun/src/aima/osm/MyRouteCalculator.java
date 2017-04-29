package aima.osm;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.Problem;
import aima.core.util.CancelableThread;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.routing.OsmMoveAction;
import aimax.osm.routing.OsmSldHeuristicFunction;
import aimax.osm.routing.RouteCalculator;
import aimax.osm.routing.RouteFindingProblem;

public class MyRouteCalculator extends RouteCalculator {
	
	private static final int CAR_FILTER = 1;
	private static final int BIKE_FILTER = 2;
	
	private int taskSuperIdx = super.getTaskSelectionOptions().length;
	private int taskTimeCarIdx = taskSuperIdx;
	private int taskBikeFunIdx = taskTimeCarIdx + 1;
	private int taskTimeCarManIdx = taskBikeFunIdx + 1;
	private int taskBikeFunManIdx = taskTimeCarManIdx + 1;
	private int taskTimeCarMalaIdx = taskBikeFunManIdx + 1;
	private int taskBikeFunMalaIdx = taskTimeCarMalaIdx + 1;
	
	@Override
	public String[] getTaskSelectionOptions() {
		String[] superOptions = super.getTaskSelectionOptions();
		String[] selectOptions = new String[superOptions.length + 6];
		
		for(int i = 0; i < superOptions.length; ++i)
			selectOptions[i] = superOptions[i];
		
		selectOptions[taskTimeCarIdx] = "Time (Car) SLD";
		selectOptions[taskBikeFunIdx] = "Fun (Bike) SLD";
		selectOptions[taskTimeCarManIdx] = "Time (Car) Manhattan";
		selectOptions[taskBikeFunManIdx] = "Fun (Bike) Manhattan";
		selectOptions[taskTimeCarMalaIdx] = "Time (Car) Mahalanobis";
		selectOptions[taskBikeFunMalaIdx] = "Fun (Bike) Mahalanobis";
 		return selectOptions;
	}
	
	public String[] getHeuristicsSelectionOptions() {
 		return  new String[] { "SLD", "Mahalanobis", "Manhattan" };
	}
	

	@Override
	public List<Position> calculateRoute(List<MapNode> markers, OsmMap map, int taskSelection) {
		List<Position> result = new ArrayList<Position>();
		try {
			MapWayFilter wayFilter = createMapWayFilter(map, taskSelection);
			boolean ignoreOneways = (taskSelection == 0);
			List<MapNode[]> pNodeList = subdivideProblem(markers, map, wayFilter);
			for (int i = 0; i < pNodeList.size()
					&& !CancelableThread.currIsCanceled(); i++) {
				Problem problem = createProblem(pNodeList.get(i), map, wayFilter,
						ignoreOneways, taskSelection);
				HeuristicFunction hf = createHeuristicFunction(pNodeList.get(i),
						taskSelection);
				SearchForActions search = createSearch(hf, taskSelection);
				List<Action> actions = search.findActions(problem);
				if (actions.isEmpty())
					break;
				for (Object action : actions) {
					if (action instanceof OsmMoveAction) {
						OsmMoveAction a = (OsmMoveAction) action;
						for (MapNode node : a.getNodes())
							if (result.isEmpty()
									|| result.get(result.size() - 1) != node) {
								result.add(new Position(node.getLat(), node
										.getLon()));
								//FUN - Add ETA from way to static list -> for time display in the end
								MyOsmRoutePlannerApp.addWay(a.getWay());								
							}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected HeuristicFunction createHeuristicFunction(MapNode[] pNodes, int taskSelection) {
		
		if(taskTimeCarManIdx == taskSelection || taskBikeFunManIdx == taskSelection) {	
			return new ManhattanDistance(pNodes[1]);
		} else if (taskTimeCarMalaIdx == taskSelection || taskBikeFunMalaIdx == taskSelection){	
			return new SimpleMahalanobisDistance(pNodes[1]);
		} else {
			return new OsmSldHeuristicFunction(pNodes[1]);
		}	
	}

	@Override
	protected MapWayFilter createMapWayFilter(OsmMap map, int taskSelection) {
		if(taskSuperIdx > taskSelection) {
			return super.createMapWayFilter(map, taskSelection);
		} else {
			if(taskTimeCarIdx == taskSelection || taskTimeCarManIdx == taskSelection || taskTimeCarMalaIdx == taskSelection) {
				return super.createMapWayFilter(map, CAR_FILTER);
			} else {
				return super.createMapWayFilter(map, BIKE_FILTER);
			}
		}
	}

	@Override
	protected Problem createProblem(MapNode[] pNodes, OsmMap map, MapWayFilter wayFilter, boolean ignoreOneways,
			int taskSelection) {
		if(taskSuperIdx > taskSelection) {
			return super.createProblem(pNodes, map, wayFilter, ignoreOneways, taskSelection);
		} else {
			if(taskTimeCarIdx == taskSelection || taskTimeCarManIdx == taskSelection || taskTimeCarMalaIdx == taskSelection) {
				return new RouteFindingProblem(pNodes[0], pNodes[1], wayFilter,
						ignoreOneways, new TimeCarStepCostFunction());
			} else {
				return new RouteFindingProblem(pNodes[0], pNodes[1], wayFilter,
						ignoreOneways, new FunBikeStepCostFunction());
			}
		}		
	}

}
