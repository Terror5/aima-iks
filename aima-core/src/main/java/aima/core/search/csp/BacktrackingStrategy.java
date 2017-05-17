package aima.core.search.csp;

import java.util.List;
import java.util.stream.Collectors;
import aima.core.search.csp.inference.*;

/**
 * This backtracking search implementation can be configured with arbitrary strategies for variable selection,
 * value ordering, and inference. These strategies are represented by objects implementing standard interfaces.
 * The design supports experiments with user-defined strategies of all kinds.
 *
 * @author Ruediger Lunde
 */
public class BacktrackingStrategy<VAR extends Variable, VAL> extends AbstractBacktrackingStrategy<VAR, VAL> {

    private CspHeuristics.VariableSelection<VAR, VAL> varSelectionStrategy;
    private CspHeuristics.ValueSelection<VAR, VAL> valSelectionStrategy;
    private InferenceStrategy<VAR, VAL> inferenceStrategy;


    /**
     * Selects the algorithm for SELECT-UNASSIGNED-VARIABLE. Uses the fluent interface design pattern.
     */
    public BacktrackingStrategy<VAR, VAL> set(CspHeuristics.VariableSelection<VAR, VAL> varStrategy) {
        varSelectionStrategy = varStrategy;
        return this;
    }

    /**
     * Selects the algorithm for ORDER-DOMAIN-VALUES. Uses the fluent interface design pattern.
     */
    public BacktrackingStrategy<VAR, VAL> set(CspHeuristics.ValueSelection<VAR, VAL> valStrategy) {
        valSelectionStrategy = valStrategy;
        return this;
    }

    /**
     * Selects the algorithm for INFERENCE. Uses the fluent interface design pattern.
     */
    public BacktrackingStrategy<VAR, VAL> set(InferenceStrategy<VAR, VAL> iStrategy) {
        inferenceStrategy = iStrategy;
        return this;
    }

    /**
     * Selects MRV&DEG for variable selection, LCV for domain ordering and AC3 as inference method.
     */
    public BacktrackingStrategy<VAR, VAL> setAll() {
        set(CspHeuristics.mrvDeg()).set(CspHeuristics.lcv()).set(new AC3Strategy<>());
        return this;
    }

    /**
     * Applies an initial inference step and then calls the super class implementation.
     */
    @Override
    public Assignment<VAR, VAL> solve(CSP<VAR, VAL> csp) {
        if (inferenceStrategy != null) {
            InferenceLog log = inferenceStrategy.apply(csp);
            if (!log.isEmpty()) {
                fireStateChanged(csp);
                if (log.inconsistencyFound())
                    return null;
            }
        }
        return super.solve(csp);
    }

    /**
     * Primitive operation, selecting a not yet assigned variable.
     */
    @Override
    protected VAR selectUnassignedVariable(Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
        List<VAR> vars = csp.getVariables().stream()
                .filter((v) -> !assignment.contains(v)).collect(Collectors.toList());
        if (varSelectionStrategy != null)
            vars = varSelectionStrategy.apply(vars, csp);
        return vars.get(0);
    }

    /**
     * Primitive operation, ordering the domain values of the specified variable.
     */
    @Override
    protected Iterable<VAL> orderDomainValues(VAR var, Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
        if (valSelectionStrategy != null) {
            return valSelectionStrategy.apply(var, assignment, csp);
        } else {
            return csp.getDomain(var);
        }
    }

    /**
     * Primitive operation, which tries to optimize the CSP representation with respect to a new assignment.
     *
     * @return An object which provides informations about (1) whether changes
     * have been performed, (2) possibly inferred empty domains , and
     * (3) how to restore the domains.
     */
    @Override
    protected InferenceLog<VAR, VAL> inference(VAR var, Assignment<VAR, VAL> assignment,
                                  CSP<VAR, VAL> csp) {
        if (inferenceStrategy != null)
            return inferenceStrategy.apply(var, assignment, csp);
        else
            return InferenceLog.emptyLog();
    }
}
