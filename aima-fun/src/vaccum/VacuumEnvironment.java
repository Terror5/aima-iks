package vaccum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.vacuum.NondeterministicVacuumAgent;
import vaccum.VacuumEnvironmentState.AgentMove;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 58.<br>
 * <br>
 * Let the world contain just two locations. Each location may or may not
 * contain dirt, and the agent may be in one location or the other. There are 8
 * possible world states, as shown in Figure 3.2. The agent has three possible
 * actions in this version of the vacuum world: <em>Left</em>, <em>Right</em>,
 * and <em>Suck</em>. Assume for the moment, that sucking is 100% effective. The
 * goal is to clean up all the dirt.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class VacuumEnvironment extends AbstractEnvironment {
	// Allowable Actions within the Vacuum Environment
	public static final Action ACTION_MOVE_LEFT = new DynamicAction("Left");
	public static final Action ACTION_MOVE_RIGHT = new DynamicAction("Right");
	public static final Action ACTION_SUCK = new DynamicAction("Suck");
	
	public static final Integer[] LOCATION_MAP = {0,1,2,3,4,5,6,7};
	public static final int LEFT_BOUND = 0;
	public static final int RIGHT_BOUND = 7;

	public enum LocationState {
		Clean, Dirty
	};

	//
	protected VacuumEnvironmentState envState = null;
	protected boolean isDone = false;

	/**
	 * Constructs a vacuum environment with two locations, in which dirt is
	 * placed at random.
	 */
	public VacuumEnvironment() {
		Random r = new Random();
		
		Map<Integer, VacuumEnvironment.LocationState> locationStates = new HashMap<Integer, VacuumEnvironment.LocationState>();
		for(Integer i : LOCATION_MAP)
				locationStates.put(i, (0 == r.nextInt(2)) ? LocationState.Clean : LocationState.Dirty);
		envState = new VacuumEnvironmentState(locationStates);
	}

	public EnvironmentState getCurrentState() {
		return envState;
	}
	
	public List<Integer> getLocations() {
		return Arrays.asList(LOCATION_MAP);
	}

	@Override
	public void executeAction(Agent a, Action agentAction) {

		if (ACTION_MOVE_RIGHT == agentAction) {
			envState.moveAgentTo(a, AgentMove.Right);
			updatePerformanceMeasure(a, -1);
		} else if (ACTION_MOVE_LEFT == agentAction) {
			envState.moveAgentTo(a, AgentMove.Left);
			updatePerformanceMeasure(a, -1);
		} else if (ACTION_SUCK == agentAction) {
			if (LocationState.Dirty == envState.getLocationState(envState
					.getAgentLocation(a))) {
				envState.setLocationState(envState.getAgentLocation(a),
						LocationState.Clean);
				updatePerformanceMeasure(a, 10);
			}
		} else if (agentAction.isNoOp()) {
			// In the Vacuum Environment we consider things done if
			// the agent generates a NoOp.
			isDone = true;
		}
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		if (anAgent instanceof NondeterministicVacuumAgent) {
    		// Note: implements FullyObservableVacuumEnvironmentPercept
    		return envState.clone();
    	}
		Integer agentLocation = envState.getAgentLocation(anAgent);
		return new LocalVacuumEnvironmentPercept(agentLocation,
				envState.getLocationState(agentLocation));
	}

	@Override
	public boolean isDone() {
		return super.isDone() || isDone;
	}

	@Override
	public void addAgent(Agent a) {
		Integer idx = new Random().nextInt(8);
		envState.setAgentLocation(a, idx);
		super.addAgent(a);
	}

	public void addAgent(Agent a, Integer location) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		envState.setAgentLocation(a, location);
		super.addAgent(a);
	}

	public LocationState getLocationState(Integer location) {
		return envState.getLocationState(location);
	}

	public Integer getAgentLocation(Agent a) {
		return envState.getAgentLocation(a);
	}
}