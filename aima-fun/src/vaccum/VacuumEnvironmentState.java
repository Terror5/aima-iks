package vaccum;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a state in the Vacuum World
 * 
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public class VacuumEnvironmentState implements EnvironmentState, FullyObservableVacuumEnvironmentPercept, Cloneable {

	private Map<Integer, VacuumEnvironment.LocationState> state;
	private Map<Agent, Integer> agentLocations;
	
	public enum AgentMove {
		Left, Right
	};

	/**
	 * Constructor
	 */
	public VacuumEnvironmentState() {
		state = new LinkedHashMap<Integer, VacuumEnvironment.LocationState>();
		agentLocations = new LinkedHashMap<Agent, Integer>();
	}

	/**
	 * Constructor
	 * 
	 * @param locAState
	 * @param locBState
	 */
	public VacuumEnvironmentState(Map<Integer, VacuumEnvironment.LocationState> locationStates) {
		this();
		state.putAll(locationStates);
	}

	@Override
	public Integer getAgentLocation(Agent a) {
		return agentLocations.get(a);
	}

	/**
	 * Sets the agent location
	 * 
	 * @param a
	 * @param location
	 */
	public void setAgentLocation(Agent a, Integer location) {
		agentLocations.put(a, location);
	}
	
	public void moveAgentTo(Agent a, AgentMove move) {
		Integer location = agentLocations.get(a);
		if(move == AgentMove.Left && location != VacuumEnvironment.LEFT_BOUND)
			setAgentLocation(a, location);
		if(move == AgentMove.Right && location != VacuumEnvironment.RIGHT_BOUND)
			setAgentLocation(a, location);
	}

	@Override
	public VacuumEnvironment.LocationState getLocationState(Integer location) {
		return state.get(location);
	}

	/**
	 * Sets the location state
	 * 
	 * @param location
	 * @param s
	 */
	public void setLocationState(Integer location, VacuumEnvironment.LocationState s) {
		state.put(location, s);
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() == obj.getClass()) {
			VacuumEnvironmentState s = (VacuumEnvironmentState) obj;
			return state.equals(s.state) && agentLocations.equals(s.agentLocations);
		}
		return false;
	}

	/**
	 * Override hashCode()
	 * 
	 * @return the hash code for this object.
	 */
	@Override
	public int hashCode() {
		return 3 * state.hashCode() + 13 * agentLocations.hashCode();
	}

	@Override
	public VacuumEnvironmentState clone() {
		VacuumEnvironmentState result = null;
		try {
			result = (VacuumEnvironmentState) super.clone();
			result.state = new LinkedHashMap<Integer, VacuumEnvironment.LocationState>(state);
			agentLocations = new LinkedHashMap<Agent, Integer>(agentLocations);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Returns a string representation of the environment
	 * 
	 * @return a string representation of the environment
	 */
	@Override
	public String toString() {
		return this.state.toString();
	}
}