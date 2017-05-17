package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.util.CancelableThread;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.8, Page 221.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function MIN-CONFLICTS(csp, max-steps) returns a solution or failure
 *    inputs: csp, a constraint satisfaction problem
 *            max-steps, the number of steps allowed before giving up
 *    current = an initial complete assignment for csp
 *    for i = 1 to max steps do
 *       if current is a solution for csp then return current
 *       var = a randomly chosen conflicted variable from csp.VARIABLES
 *       value = the value v for var that minimizes CONFLICTS(var, v, current, csp)
 *       set var = value in current
 *    return failure
 * </code>
 * </pre>
 * 
 * Figure 6.8 The MIN-CONFLICTS algorithm for solving CSPs by local search. The
 * initial state may be chosen randomly or by a greedy assignment process that
 * chooses a minimal-conflict value for each variable in turn. The CONFLICTS
 * function counts the number of constraints violated by a particular value,
 * given the rest of the current assignment.
 * 
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class MinConflictsStrategy<VAR extends Variable, VAL> extends SolutionStrategy<VAR, VAL> {
	private int maxSteps;

	/**
	 * Constructs a min-conflicts strategy with a given number of steps allowed
	 * before giving up.
	 * 
	 * @param maxSteps
	 *            the number of steps allowed before giving up
	 */
	public MinConflictsStrategy(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public Assignment<VAR, VAL> solve(CSP<VAR, VAL> csp) {
		Assignment<VAR, VAL> assignment = generateRandomAssignment(csp);
		fireStateChanged(assignment, csp);
		for (int i = 0; i < maxSteps && !CancelableThread.currIsCanceled(); i++) {
			if (assignment.isSolution(csp)) {
				return assignment;
			} else {
				List<VAR> vars = getConflictedVariables(assignment, csp);
				VAR var = Util.selectRandomlyFromList(vars);
				VAL value = getMinConflictValueFor(var, assignment, csp);
				assignment.add(var, value);
				fireStateChanged(assignment, csp);
			}
		}
		return null;
	}

	private Assignment<VAR, VAL> generateRandomAssignment(CSP<VAR, VAL> csp) {
		Assignment<VAR, VAL> assignment = new Assignment<>();
		for (VAR var : csp.getVariables()) {
			VAL randomValue = Util.selectRandomlyFromList(csp.getDomain(var).asList());
			assignment.add(var, randomValue);
		}
		return assignment;
	}

	private List<VAR> getConflictedVariables(Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
		List<VAR> result = new ArrayList<>();
		for (Constraint<VAR, VAL> constraint : csp.getConstraints()) {
			if (!constraint.isSatisfiedWith(assignment))
				for (VAR var : constraint.getScope())
					if (!result.contains(var))
						result.add(var);
		}
		return result;
	}

	private VAL getMinConflictValueFor(VAR var, Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
		List<Constraint<VAR, VAL>> constraints = csp.getConstraints(var);
		Assignment<VAR, VAL> duplicate = assignment.copy();
		int minConflict = Integer.MAX_VALUE;
		List<VAL> resultCandidates = new ArrayList<>();
		for (VAL value : csp.getDomain(var)) {
			duplicate.add(var, value);
			int currConflict = countConflicts(duplicate, constraints);
			if (currConflict <= minConflict) {
				if (currConflict < minConflict) {
					resultCandidates.clear();
					minConflict = currConflict;
				}
				resultCandidates.add(value);
			}
		}
		if (!resultCandidates.isEmpty())
			return Util.selectRandomlyFromList(resultCandidates);
		else
			return null;
	}

	private int countConflicts(Assignment<VAR, VAL> assignment,
			List<Constraint<VAR, VAL>> constraints) {
		int result = 0;
		for (Constraint<VAR, VAL> constraint : constraints)
			if (!constraint.isSatisfiedWith(assignment))
				result++;
		return result;
	}
}
