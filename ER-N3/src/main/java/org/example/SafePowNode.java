package org.example;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.ConstNode;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.OperatorNode;

import java.util.List;
import java.util.Map;

public class SafePowNode extends OperatorNode {

    public SafePowNode() {
        super("^");
    }

    public SafePowNode(List<Node> children) {
        super("^", 2, children);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {

        double base = children.get(0).evaluate(variables);
        Node exponentNode = children.get(1);

        // Exponent must be ConstNode
        if (!(exponentNode instanceof ConstNode)) {
            return 1.0;
        }

        double exponentValue = exponentNode.evaluate(variables);

        // Exponent must be integer
        if (exponentValue != Math.floor(exponentValue)) {
            return 1.0;
        }

        int exponent = (int) exponentValue;

        // Limit exponent range (IMPORTANT)
        if (exponent < -10 || exponent > 10) {
            return 1.0;
        }

        // Handle zero base with negative exponent
        if (base == 0.0 && exponent < 0) {
            return 1.0;
        }

        double result = Math.pow(base, exponent);

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return 1.0;
        }

        return result;
    }
}
