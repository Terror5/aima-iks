package vaccumExt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;

public class GenericVacuumEnvironmentState<T> implements EnvironmentState, FullyObservableVacuumEnvironmentPercept<T>, Cloneable {

		private Map<T, LocationState> state;
		private Map<Agent, T> agentLocations;
		 

		/**
		 * Constructor
		 */
		public GenericVacuumEnvironmentState() {
			state = new LinkedHashMap<T, LocationState>();
			agentLocations = new LinkedHashMap<Agent, T>();
		}

		/**
		 * Constructor
		 * 
		 * @param set
		 * @param collection
		 */
		public GenericVacuumEnvironmentState(Map<T, LocationState> stateMap) {
			this();
			state = stateMap;
		}
		
		public List<T> getLocations() {
			Iterator<T> keyIterator = state.keySet().iterator();
			List<T> locations = new ArrayList<T>();
			while(keyIterator.hasNext())
				locations.add(keyIterator.next());
			return locations;
		}

		@Override
		public T getAgentLocation(Agent a) {
			return agentLocations.get(a);
		}

		/**
		 * Sets the agent location
		 * 
		 * @param a
		 * @param location
		 */
		public void setAgentLocation(Agent a, T location) {
			agentLocations.put(a, location);
		}

		@Override
		public LocationState getLocationState(T location) {
			return state.get(location);
		}

		/**
		 * Sets the location state
		 * 
		 * @param location
		 * @param s
		 */
		public void setLocationState(T location, LocationState s) {
			state.put(location, s);
		}

		@Override
		public boolean equals(Object obj) {
			if (getClass() == obj.getClass()) {
				GenericVacuumEnvironmentState<T> s = (GenericVacuumEnvironmentState<T>) obj;
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
		public GenericVacuumEnvironmentState<T> clone() {
			GenericVacuumEnvironmentState<T> result = null;
			try {
				result = (GenericVacuumEnvironmentState<T>) super.clone();
				result.state = new LinkedHashMap<T, LocationState>(state);
				agentLocations = new LinkedHashMap<Agent, T>(agentLocations);
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

		public void moveAgentTo(Agent a, AgentMove move) {
			T location = agentLocations.get(a);
			//TODO mapping from T location to idx
			int idx = 1;
			if(move == AgentMove.Left && idx != 0) {
				idx -= 1;
				setAgentLocation(a, location);
			}
				
			if(move == AgentMove.Right && idx != (agentLocations.size() - 1)) {
				idx += 1;
				setAgentLocation(a, location);
			}
		}
}
