package org.example;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.ceo.CEO;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.unconstrained.Sphere;
import org.um.feri.ears.util.random.PredefinedRandom;
import org.um.feri.ears.util.random.RNG;
import org.um.feri.ears.util.random.RandomGenerator;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CEO_Runner {
    public static void main(String[] args) {
        RNG.setSelectedRandomGenerator(RNG.RngType.PREDEFINED_RANDOM);
        RandomGenerator rng = (RandomGenerator) RNG.getSelectedRng();

        if (!(rng instanceof PredefinedRandom)) {
            System.err.println("Failed to select PredefinedRandom generator!");
            return;
        }

        PredefinedRandom pr = (PredefinedRandom) rng;

        // absolute path to check first
        Path abs = Paths.get("C:\\Users\\simon\\Desktop\\ER\\Evolucijsko-Racunanje\\ER-N2-CEOpy\\ceo_random.txt");

        try {
            if (Files.exists(abs)) {
                pr.loadFromFile(abs.toString());
                System.out.println("Loaded from " + abs);
            } else {
                System.err.println("File not found at " + abs + ". Trying classpath fallback `/ceo_random.txt`.");
                try (InputStream in = RNG_Test.class.getResourceAsStream("/ceo_random.txt")) {
                    if (in == null) {
                        System.err.println("Classpath resource `/ceo_random.txt` not found.");
                    } else {
                        Path tmp = Files.createTempFile("ceo_random", ".txt");
                        Files.copy(in, tmp, StandardCopyOption.REPLACE_EXISTING);
                        pr.loadFromFile(tmp.toString());
                        System.out.println("Loaded from classpath fallback (temp file: " + tmp + ").");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load predefined random numbers:");
            e.printStackTrace();
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
                0,
                0,
                MaxFES
        );

        // 3. Create the algorithm
        NumberAlgorithm alg = new CEO(popSize, N);
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
