package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for CSP solver implementations. Solving a CSP means finding an
 * assignment, which is consistent and complete with respect to a CSP. This
 * abstract class provides the central interface method and additionally an
 * implementation of an observer mechanism.
 * 
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public abstract class SolutionStrategy<VAR extends Variable, VAL> {
	private List<CspListener<VAR, VAL>> listeners = new ArrayList<>();

	/**
	 * Adds a CSP state listener to the solution strategy.
	 * 
	 * @param listener
	 *            a listener which follows the progress of the solution strategy
	 *            step-by-step.
	 */
	public void addCSPStateListener(CspListener<VAR, VAL> listener) {
		listeners.add(listener);
	}

	/**
	 * Removes a CSP listener from the solution strategy.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeCSPStateListener(CspListener<VAR, VAL> listener) {
		listeners.remove(listener);
	}

	protected void fireStateChanged(CSP<VAR, VAL> csp) {
		for (CspListener<VAR, VAL> listener : listeners)
			listener.stateChanged(csp.copyDomains());
	}

	protected void fireStateChanged(Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
		for (CspListener<VAR, VAL> listener : listeners)
			listener.stateChanged(assignment.copy(), csp.copyDomains());
	}

	/**
	 * Returns a solution to the specified CSP, which specifies values for all
	 * the variables such that the constraints are satisfied.
	 * 
	 * @param csp
	 *            a CSP to solve
	 * 
	 * @return a solution to the specified CSP, which specifies values for all
	 *         the variables such that the constraints are satisfied.
	 */
	public abstract Assignment<VAR, VAL> solve(CSP<VAR, VAL> csp);
}
