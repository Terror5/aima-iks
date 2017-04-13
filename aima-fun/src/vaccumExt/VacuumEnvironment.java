package vaccumExt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.vacuum.NondeterministicVacuumAgent;
import vaccum.VacuumEnvironmentState.AgentMove;

/**
 * @author Florian Unger
 */
public class VacuumEnvironment<T> extends AbstractEnvironment {
	// Allowable Actions within the Vacuum Environment
	public static final Action ACTION_MOVE_LEFT = new DynamicAction("Left");
	public static final Action ACTION_MOVE_RIGHT = new DynamicAction("Right");
	public static final Action ACTION_SUCK = new DynamicAction("Suck");
	
	public static final Integer[] LOCATION_MAP = {0,1,2,3,4,5,6,7};
	public static final int LEFT_BOUND = 0;
	public static final int RIGHT_BOUND = 7;


	//
	protected GenericVacuumEnvironmentState<T> envState = null;
	protected boolean isDone = false;

	/**
	 * Constructs a vacuum environment with two locations, in which dirt is
	 * placed at random.
	 */
	public VacuumEnvironment() {
		Random r = new Random();
		
		Map<T, LocationState> locationStates = new HashMap<T, LocationState>();
		for(T i : LOCATION_MAP)
				locationStates.put(i, (0 == r.nextInt(2)) ? LocationState.Clean : LocationState.Dirty);
		envState = new GenericVacuumEnvironmentState<T> (locationStates);
	}

	public GenericVacuumEnvironmentState<T>  getCurrentState() {
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
		T agentLocation = envState.getAgentLocation(anAgent);
		return new LocalVacuumEnvironmentPercept(agentLocation,
				envState.getLocationState(agentLocation));
	}

	@Override
	public boolean isDone() {
		return super.isDone() || isDone;
	}

	@Override
	public void addAgent(Agent a) {
		T idx = new Random().nextInt(8);
		envState.setAgentLocation(a, idx);
		super.addAgent(a);
	}

	public void addAgent(Agent a, T location) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		envState.setAgentLocation(a, location);
		super.addAgent(a);
	}

	public LocationState getLocationState(T location) {
		return envState.getLocationState(location);
	}

	public T getAgentLocation(Agent a) {
		return envState.getAgentLocation(a);
	}
}