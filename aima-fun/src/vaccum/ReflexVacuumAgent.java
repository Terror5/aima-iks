package vaccum;

import java.util.Random;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import vaccum.VacuumEnvironmentState.AgentMove;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.8, page 48.<br>
 * <br>
 * 
 * <pre>
 * function REFLEX-VACUUM-AGENT([location, status]) returns an action
 *   
 *   if status = Dirty then return Suck
 *   else if location = A then return Right
 *   else if location = B then return Left
 * </pre>
 * 
 * Figure 2.8 The agent program for a simple reflex agent in the two-state
 * vacuum environment. This program implements the action function tabulated in
 * Figure 2.3.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class ReflexVacuumAgent extends AbstractAgent {

	public ReflexVacuumAgent() {
//		Random r = new Random();
//		AgentMove move = (0 == r.nextInt(2))?AgentMove.Left:AgentMove.Right;
		super(new AgentProgram() {
			// function REFLEX-VACUUM-AGENT([location, status]) returns an
			// action
			public Action execute(Percept percept) {
				LocalVacuumEnvironmentPercept vep = (LocalVacuumEnvironmentPercept) percept;
				
				// if status = Dirty then return Suck
				if (VacuumEnvironment.LocationState.Dirty == vep
						.getLocationState()) {
					return VacuumEnvironment.ACTION_SUCK;
				} else if(VacuumEnvironment.LocationState.Clean == vep
							.getLocationState()) {
					Random r = new Random();
					return (0 == r.nextInt(2))?VacuumEnvironment.ACTION_MOVE_LEFT:
							VacuumEnvironment.ACTION_MOVE_RIGHT;	
				} else if (vep.isAgentAtLeftBoundary()) {
					return VacuumEnvironment.ACTION_MOVE_RIGHT;
				} else if (vep.isAgentAtRightBoundary()) {
					return VacuumEnvironment.ACTION_MOVE_LEFT;
				}

				// Note: This should not be returned if the
				// environment is correct
				return NoOpAction.NO_OP;
			}
		});
	}
}
