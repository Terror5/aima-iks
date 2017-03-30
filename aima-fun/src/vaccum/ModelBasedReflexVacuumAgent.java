package vaccum;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Model;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicState;
import aima.core.agent.impl.NoOpAction;
import aima.core.agent.impl.aprog.ModelBasedReflexAgentProgram;
import aima.core.agent.impl.aprog.simplerule.ANDCondition;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;

/**
 * @author Florian Unger
 */
public class ModelBasedReflexVacuumAgent extends AbstractAgent {

	private static final String ATTRIBUTE_CURRENT_STATE = "currentState";
	private static final String ATTRIBUTE_REACHED_LEFT = "reachedLeft";
	private static final String ATTRIBUTE_REACHED_RIGHT = "reachedRight";

	public ModelBasedReflexVacuumAgent() {
		super(new ModelBasedReflexAgentProgram() {
			@Override
			protected void init() {
				setState(new DynamicState());
				setRules(getRuleSet());
			}

			protected DynamicState updateState(DynamicState state,
					Action anAction, Percept percept, Model model) {

				LocalVacuumEnvironmentPercept vep = (LocalVacuumEnvironmentPercept) percept;
				
				Object left = state.getAttribute(ATTRIBUTE_REACHED_LEFT);
				Object right = state.getAttribute(ATTRIBUTE_REACHED_RIGHT);
				Boolean l = (left == null)?false:((Boolean) left);
				Boolean r = (right == null)?false:((Boolean) right);
				
				
				//only set boundary reached to false if it hasnt been reached
				if(l == false) 
					state.setAttribute(ATTRIBUTE_REACHED_LEFT, l);
				if(r == false) 
					state.setAttribute(ATTRIBUTE_REACHED_RIGHT, r);			
				
				System.out.println(l + "  " + r);
				
				//set boundaries reached to true if agent is at boundary	
				if(vep.isAgentAtLeftBoundary()) {
					System.out.println("left");
					state.setAttribute(ATTRIBUTE_REACHED_LEFT,
							vep.isAgentAtLeftBoundary());
				}
				if(vep.isAgentAtRightBoundary()){
					System.out.println("right");
					state.setAttribute(ATTRIBUTE_REACHED_RIGHT,
							vep.isAgentAtRightBoundary());
				}
				state.setAttribute(ATTRIBUTE_CURRENT_STATE,
						vep.getLocationState());
				
				return state;
			}
		});
	}

	//
	// PRIVATE METHODS
	//
	private static Set<Rule> getRuleSet() {
		// Note: Using a LinkedHashSet so that the iteration order (i.e. implied
		// precedence) of rules can be guaranteed.
		Set<Rule> rules = new LinkedHashSet<Rule>();

		rules.add(new Rule(new ANDCondition(new EQUALCondition(
				ATTRIBUTE_CURRENT_STATE,
				VacuumEnvironment.LocationState.Clean), new ANDCondition(
				new EQUALCondition(ATTRIBUTE_REACHED_LEFT, true),
				new EQUALCondition(ATTRIBUTE_REACHED_RIGHT, true))), NoOpAction.NO_OP));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_CURRENT_STATE,
				VacuumEnvironment.LocationState.Dirty),
				VacuumEnvironment.ACTION_SUCK));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_REACHED_LEFT,
				true),
				VacuumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_REACHED_RIGHT,
				true),
				VacuumEnvironment.ACTION_MOVE_LEFT));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_REACHED_RIGHT,
				false),
				VacuumEnvironment.ACTION_MOVE_RIGHT));
		rules.add(new Rule(new EQUALCondition(ATTRIBUTE_REACHED_LEFT,
				false),
				VacuumEnvironment.ACTION_MOVE_LEFT));

		return rules;
	}
}
