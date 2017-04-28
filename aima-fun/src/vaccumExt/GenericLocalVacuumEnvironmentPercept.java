package vaccumExt;

import aima.core.agent.Agent;
import aima.core.agent.impl.DynamicPercept;

/**
 * @author Florian Unger
 */
public class GenericLocalVacuumEnvironmentPercept<T> extends DynamicPercept {

	public static final String ATTRIBUTE_AGENT_LOCATION = "agentLocation";
	public static final String ATTRIBUTE_STATE = "state";

	/**
	 * Construct a vacuum environment percept from the agent's perception of the
	 * current location and state.
	 * 
	 * @param agentLocation
	 *            the agent's perception of the current location.
	 * @param state
	 *            the agent's perception of the current state.
	 */
	public GenericLocalVacuumEnvironmentPercept(T agentLocation,
			LocationState state) {
		setAttribute(ATTRIBUTE_AGENT_LOCATION, agentLocation);
		setAttribute(ATTRIBUTE_STATE, state);
	}

	/**
	 * Return the agent's perception of the current location, which is either A
	 * or B.
	 * 
	 * @return the agent's perception of the current location, which is either A
	 *         or B.
	 */
	public T getAgentLocation() {
		return (T) getAttribute(ATTRIBUTE_AGENT_LOCATION);
	}
	
	//TODO get Left/Right bound from GenericEnvironmentState
	public boolean isAgentAtRightBoundary() {
		return (false);
	}
	
	public boolean isAgentAtLeftBoundary() {
		return (false);
	}

	/**
	 * Return the agent's perception of the current state, which is either
	 * <em>Clean</em> or <em>Dirty</em>.
	 * 
	 * @return the agent's perception of the current state, which is either
	 *         <em>Clean</em> or <em>Dirty</em>.
	 */
	public LocationState getLocationState() {
		return (LocationState) getAttribute(ATTRIBUTE_STATE);
	}

	/**
	 * Determine whether this percept matches an environment state
	 * 
	 * @param state
	 * @param agent
	 * @return true of the percept matches an environment state, false otherwise.
	 */
	public boolean matches(GenericVacuumEnvironmentState<T> state, Agent agent) {
		if (!this.getAgentLocation().equals(state.getAgentLocation(agent))) {
			return false;
		}
		if (!this.getLocationState().equals(
				state.getLocationState(this.getAgentLocation()))) {
			return false;
		}
		return true;
	}

	/**
	 * Return string representation of this percept.
	 * 
	 * @return a string representation of this percept.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(getAgentLocation());
		sb.append(", ");
		sb.append(getLocationState());
		sb.append("]");
		return sb.toString();
	}
}
