package gui;

import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import aima.core.agent.impl.SimpleEnvironmentView;
import vaccum.ReflexVacuumAgent;
import vaccum.VacuumEnvironment;


/**
 * Demonstrates, how to set up a simple environment, place an agent in it,
 * and run it. The vacuum world is used as a simple example.
 * 
 * @author Ruediger Lunde
 */
public class TrivialVacuumDemo {
	public static void main(String[] args) {
		// create environment with random state of cleaning.
		Environment env = new VacuumEnvironment();
		EnvironmentView view = new SimpleEnvironmentView();
		env.addEnvironmentView(view);
		
		Agent a = null;
		a = new ReflexVacuumAgent();
		
		env.addAgent(a);
		env.step(20);
		env.notifyViews("Performance=" + env.getPerformanceMeasure(a));
	}
}
