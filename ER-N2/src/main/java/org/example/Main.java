package org.example;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Sphere;

public class Main {

    public static void main(String[] args) {
        // 1. Define the problem
        DoubleProblem problem = new Sphere(5);

        // 2. Define the task (stop after 10,000 evaluations)
        Task task = new Task(
                problem,
                StopCriterion.EVALUATIONS,
                10000,
                0,
                0
        );

        // 3. Create the algorithm
        NumberAlgorithm alg = new GWO();
        NumberSolution<Double> best;

        // 4. Run the algorithm
        try {
            best = alg.execute(task);
            System.out.println("Best solution found = " + best); // print the best solution found after 10000 evaluations
        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }
}
