package osm.agent;

import java.util.HashMap;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.online.OnlineSearchProblem;
import aima.core.util.datastructure.TwoKeyHashMap;


public class LRTAStarAgent extends AbstractAgent {

	private OnlineSearchProblem problem;
	private PerceptToStateFunction ptsFunction;
	private HeuristicFunction hf;
	// persistent: result, a table, indexed by state and action, initially empty
	private final TwoKeyHashMap<Object, Action, Object> result = new TwoKeyHashMap<Object, Action, Object>();
	// H, a table of cost estimates indexed by state, initially empty
	private final HashMap<Object, Double> H = new HashMap<Object, Double>();
	// s, a, the previous state and action, initially null
	private Object s = null;
	private Action a = null;

	/**
	 * Constructs a LRTA* agent with the specified search problem, percept to
	 * state function, and heuristic function.
	 * 
	 * @param problem
	 *            an online search problem for this agent to solve.
	 * @param ptsFunction
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 * @param hf
	 *            heuristic function <em>h(n)</em>, which estimates the cost of
	 *            the cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public LRTAStarAgent(OnlineSearchProblem problem,
			PerceptToStateFunction ptsFunction, HeuristicFunction hf) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFunction);
		setHeuristicFunction(hf);
	}

	/**
	 * Returns the search problem of this agent.
	 * 
	 * @return the search problem of this agent.
	 */
	public OnlineSearchProblem getProblem() {
		return problem;
	}

	/**
	 * Sets the search problem for this agent to solve.
	 * 
	 * @param problem
	 *            the search problem for this agent to solve.
	 */
	public void setProblem(OnlineSearchProblem problem) {
		this.problem = problem;
		init();
	}

	/**
	 * Returns the percept to state function of this agent.
	 * 
	 * @return the percept to state function of this agent.
	 */
	public PerceptToStateFunction getPerceptToStateFunction() {
		return ptsFunction;
	}

	/**
	 * Sets the percept to state function of this agent.
	 * 
	 * @param ptsFunction
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public void setPerceptToStateFunction(PerceptToStateFunction ptsFunction) {
		this.ptsFunction = ptsFunction;
	}

	/**
	 * Returns the heuristic function of this agent.
	 */
	public HeuristicFunction getHeuristicFunction() {
		return hf;
	}

	/**
	 * Sets the heuristic function of this agent.
	 * 
	 * @param hf
	 *            heuristic function <em>h(n)</em>, which estimates the cost of
	 *            the cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public void setHeuristicFunction(HeuristicFunction hf) {
		this.hf = hf;
	}

	// function LRTA*-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public Action execute(Percept psDelta) {
		Object sDelta = ptsFunction.getState(psDelta);
		// if GOAL-TEST(s') then return stop
		if (goalTest(sDelta)) {
			a = NoOpAction.NO_OP;
		} else {
			// if s' is a new state (not in H) then H[s'] <- h(s')
			if (!H.containsKey(sDelta)) {
				H.put(sDelta, getHeuristicFunction().h(sDelta));
			}
			// if s is not null
			if (null != s) {
				// result[s, a] <- s'
				result.put(s, a, sDelta);

				// H[s] <- min LRTA*-COST(s, b, result[s, b], H)
				// b (element of) ACTIONS(s)
				double min = Double.MAX_VALUE;
				for (Action b : actions(s)) {
					double cost = lrtaCost(s, b, result.get(s, b));
					
					if (cost < min) {
						min = cost;
					}
				}
				//H.put(s, min);
			}
			// a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b,
			// result[s', b], H)
			double min = Double.MAX_VALUE;
			// Just in case no actions
			a = NoOpAction.NO_OP;
			for (Action b : actions(sDelta)) {
				double cost = lrtaCost(sDelta, b, result.get(sDelta, b));
				
				// if the action is to move back
				if(((MoveToAction) b).getToLocation().equals(s)){
					// prefer other options
					//cost = cost * (Math.random() + 1.0d);
					cost *= 2;
				}
				
				if (cost < min) {
					min = cost;
					a = b;
				}
			}
		}

		// s <- s'
		s = sDelta;

		if (a.isNoOp()) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			setAlive(false);
		}
		// return a
		return a;
	}

	//
	// PRIVATE METHODS
	//
	private void init() {
		setAlive(true);
		result.clear();
		H.clear();
		s = null;
		a = null;
	}

	private boolean goalTest(Object state) {
		return getProblem().isGoalState(state);
	}

	// function LRTA*-COST(s, a, s', H) returns a cost estimate
	private double lrtaCost(Object s, Action action, Object sDelta) {
		// if s' is undefined then return h(s)
		if (null == sDelta) {
			return getHeuristicFunction().h(s);
		}
		// else return c(s, a, s') + H[s']
		return getProblem().getStepCostFunction().c(s, action, sDelta)
				+ H.get(sDelta);
	}

	private Set<Action> actions(Object state) {
		return problem.getActionsFunction().actions(state);
	}
}
