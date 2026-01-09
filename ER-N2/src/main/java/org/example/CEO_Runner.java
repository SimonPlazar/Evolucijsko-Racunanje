package org.example;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.ceo.CEO;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.random.PredefinedRandom;
import org.um.feri.ears.util.random.RNG;


public class CEO_Runner {
    public static void main(String[] args) {
        String path = "C:\\Users\\simon\\Desktop\\ER\\Evolucijsko-Racunanje\\ER-N2-CEOpy\\ceo_random.txt";

        RNG.setSelectedRandomGenerator(RNG.RngType.PREDEFINED_RANDOM);
        PredefinedRandom rng = (PredefinedRandom) RNG.getSelectedRng();

        if (!rng.loadFromFile(path)) {
            System.err.println("Failed to load predefined random numbers");
            return;
        }

        int Dim = 100;
        int MaxFES = Dim * 100;
        int popSize = 30;
        int N = 10;

        // 1. Define the problem
        DoubleProblem problem = new Sphere(Dim);

        // 2. Define the task
        Task task = new Task(
                problem,
                StopCriterion.ITERATIONS,
//                StopCriterion.STAGNATION,
                0,
                0,
                MaxFES
//                0
        );

        // 3. Create the algorithm
        NumberAlgorithm alg = new CEO(popSize, N);
        alg.setDebug(true);
        NumberSolution<Double> best;

        // 4. Run the algorithm
        try {
            best = alg.execute(task);
            System.out.println("Best solution found = " + best);
            System.out.println(" Evaluations: " + task.getNumberOfEvaluations());
            System.out.println(" Iterations: " + task.getNumberOfIterations());

        } catch (StopCriterionException e) {
            System.out.println("Algorithm stopped due to stop criterion: " + e.getMessage());
            System.out.println(" Evaluations: " + task.getNumberOfEvaluations());
            System.out.println(" Iterations: " + task.getNumberOfIterations());
            e.printStackTrace();
        }
    }
}
