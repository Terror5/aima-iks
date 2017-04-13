package vaccumExt;

import aima.core.agent.Agent;
import aima.core.agent.Percept;

public interface FullyObservableVacuumEnvironmentPercept<T> extends Percept{
	
	/**
     * Returns the agent location
     *
     * @param a
     * @return the agents location
     */
    T getAgentLocation(Agent a);
    
    /**
     * Returns the location state
     *
     * @param location
     * @return the location state
     */
    LocationState getLocationState(T location);

}
