package vaccum;

import aima.core.agent.Agent;
import aima.core.agent.Percept;

/**
 * Implements a fully observable environment percept, in accordance with page
 * 134, AIMAv3.
 *
 * @author Andrew Brown
 */
public interface FullyObservableVacuumEnvironmentPercept extends Percept {
	/**
     * Returns the agent location
     *
     * @param a
     * @return the agents location
     */
    Integer getAgentLocation(Agent a);
    
    /**
     * Returns the location state
     *
     * @param location
     * @return the location state
     */
    VacuumEnvironment.LocationState getLocationState(Integer location);
}
