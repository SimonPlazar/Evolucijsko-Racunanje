package org.example;

import org.um.feri.ears.algorithms.GPAlgorithm;
import org.um.feri.ears.algorithms.gp.ElitismGPAlgorithm;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Target;
import org.um.feri.ears.individual.representations.gp.symbolic.regression.*;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.StopCriterionException;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.gp.ProgramProblem;
import org.um.feri.ears.problems.gp.ProgramSolution;
import org.um.feri.ears.problems.gp.SymbolicRegressionProblem;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymbolicRegression_synthetic {

    public static void main(String[] args) {
        // Random number generator
        RNG.setSeed(42);

        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                SubNode.class,
                MulNode.class,
                SafePowNode.class,
                SinNode.class,
                CosNode.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                VarNode.class,
                ConstNode.class
        );

        // f(x) = x^6 - 2x^4 + x^2
        // U[-1, 1], 20 points
        List<Target> trainingData_KOZA3 = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            double x = -1.0 + 2.0 * RNG.nextDouble(); // U[-1,1]
            double y = Math.pow(x, 6) - 2 * Math.pow(x, 4) + Math.pow(x, 2);
            trainingData_KOZA3.add(new Target().when("x", x).targetIs(y));
        }


        // f(x) = sin(x^2) * cos(x) - 1
        // U[-1, 1], 20 points
        List<Target> trainingData_NGUYEN5 = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            double x = -1.0 + 2.0 * RNG.nextDouble(); // U[-1,1]
            double y = Math.sin(x * x) * Math.cos(x) - 1.0;
            trainingData_NGUYEN5.add(new Target().when("x", x).targetIs(y));
        }


        // f(x,y) = 2 sin(x) cos(y)
        // U[-1, 1], 100 points
        List<Target> trainingData_NGUYEN10 = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            double x = -1.0 + 2.0 * RNG.nextDouble(); // U[-1,1]
            double y = -1.0 + 2.0 * RNG.nextDouble(); // U[-1,1]
            double z = 2.0 * Math.sin(x) * Math.cos(y);

            trainingData_NGUYEN10.add(
                    new Target()
                            .when("x", x)
                            .when("y", y)
                            .targetIs(z)
            );
        }


        // ---------------

        List<Class<? extends Node>> FunctionNodeTypes_KOZA3 = Arrays.asList(
                AddNode.class,
                SubNode.class,
                MulNode.class,
                SafePowNode.class
        );

        List<Class<? extends Node>> TerminalNodeTypes = Arrays.asList(
                VarNode.class
        );

        SymbolicRegressionProblem problem_KOZA3 =
                new SymbolicRegressionProblem(
//                        FunctionNodeTypes_KOZA3,
                        baseFunctionNodeTypes,
//                        TerminalNodeTypes,
                        baseTerminalNodeTypes,
                        trainingData_KOZA3
                );

        Task<ProgramSolution, ProgramProblem> task_KOZA3 =
                new Task<>(problem_KOZA3, StopCriterion.EVALUATIONS, 50_000, 0, 0);

        // ---------------

        List<Class<? extends Node>> FunctionNodeTypes_NGUYEN5 = Arrays.asList(
                AddNode.class,
                SubNode.class,
                MulNode.class,
                SafePowNode.class,
                SinNode.class,
                CosNode.class
        );

//        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
//                ConstNode.class,
//                VarNode.class
//        );

        SymbolicRegressionProblem problem_NGUYEN5 =
                new SymbolicRegressionProblem(
//                        FunctionNodeTypes_NGUYEN5,
                        baseFunctionNodeTypes,
                        baseTerminalNodeTypes,
                        trainingData_NGUYEN5
                );

        Task<ProgramSolution, ProgramProblem> task_NGUYEN5 =
                new Task<>(problem_NGUYEN5, StopCriterion.EVALUATIONS, 50_000, 0, 0);

        // ---------------

        List<Class<? extends Node>> FunctionNodeTypes_NGUYEN10 = Arrays.asList(
                MulNode.class,
                SafePowNode.class,
                SinNode.class,
                CosNode.class
        );

        SymbolicRegressionProblem problem_NGUYEN10 =
                new SymbolicRegressionProblem(
//                        FunctionNodeTypes_NGUYEN10,
                        baseFunctionNodeTypes,
                        baseTerminalNodeTypes,
                        trainingData_NGUYEN10
                );

        Task<ProgramSolution, ProgramProblem> task_NGUYEN10 =
                new Task<>(problem_NGUYEN10, StopCriterion.EVALUATIONS, 50_000, 0, 0);

        // ---------------

        GPAlgorithm alg = new ElitismGPAlgorithm();

        try {
            VarNode.variables = Arrays.asList("x");

            System.out.println("--- KOZA3 ---");
            ProgramSolution solution = alg.execute(task_KOZA3);
            System.out.println("Fitness: " + solution.getEval());
            System.out.println("Expression:");
            System.out.println(solution);
            solution.getTree().displayTree("KOZA3", true);

            System.out.println("\n--- NGUYEN5 ---");
            solution = alg.execute(task_NGUYEN5);
            System.out.println("Fitness: " + solution.getEval());
            System.out.println("Expression:");
            System.out.println(solution);
            solution.getTree().displayTree("NGUYEN5", true);

            VarNode.variables = Arrays.asList("x", "y");

            System.out.println("\n--- NGUYEN10 ---");
            solution = alg.execute(task_NGUYEN10);
            System.out.println("Fitness: " + solution.getEval());
            System.out.println("Expression:");
            System.out.println(solution);
            solution.getTree().displayTree("NGUYEN10", true);
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }
}
