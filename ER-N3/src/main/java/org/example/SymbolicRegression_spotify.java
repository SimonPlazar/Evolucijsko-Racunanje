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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymbolicRegression_spotify {
    public static void main(String[] args) {
        // popularity = f(danceability, energy, loudness, ..., tempo)

        RNG.setSeed(42);

        List<Class<? extends Node>> baseFunctionNodeTypes = Arrays.asList(
                AddNode.class,
                SubNode.class,
                MulNode.class,
                DivNode.class,
                LogNode.class
        );

        List<Class<? extends Node>> baseTerminalNodeTypes = Arrays.asList(
                ConstNode.class,
                VarNode.class
        );

        VarNode.variables = Arrays.asList(
                "danceability",
                "energy",
                "loudness",
                "speechiness",
                "acousticness",
                "instrumentalness",
                "liveness",
                "valence",
                "tempo"
        );


        List<Target> allData = loadSpotifyData("C:\\Users\\simon\\Desktop\\ER\\Evolucijsko-Racunanje\\ER-N3\\spotify_numeric_sample.csv");
        System.out.println("Total rows loaded: " + allData.size());

        RNG.shuffle(allData);

        int SAMPLE_SIZE = 50000;

        if (allData.size() > SAMPLE_SIZE) {
            allData = allData.subList(0, SAMPLE_SIZE);
        }

        System.out.println("Total rows for regression: " + allData.size());

        int trainSize = (int)(allData.size() * 0.7);

        List<Target> trainingData = allData.subList(0, trainSize);
        List<Target> testingData = allData.subList(trainSize, allData.size());

        SymbolicRegressionProblem trainingProblem =
                new SymbolicRegressionProblem(
                        baseFunctionNodeTypes,
                        baseTerminalNodeTypes,
                        trainingData
                );

        SymbolicRegressionProblem testingProblem =
                new SymbolicRegressionProblem(
                        baseFunctionNodeTypes,
                        baseTerminalNodeTypes,
                        testingData
                );

        Task<ProgramSolution, ProgramProblem> task =
                new Task<>(
                        trainingProblem,
                        StopCriterion.EVALUATIONS,
                        50000,
                        0,
                        0
                );

        GPAlgorithm alg = new ElitismGPAlgorithm();

        try {
            ProgramSolution solution = alg.execute(task);

            System.out.println("Training fitness (MSE): " + solution.getEval());
            System.out.println("Best expression:");
            System.out.println(solution);

            solution.getTree().displayTree("SpotifyModel", true);

            // Test
            testingProblem.evaluate(solution);
            System.out.println("Testing fitness (MSE): " + solution.getEval());

        } catch (StopCriterionException e) {
            e.printStackTrace();
        }
    }

    public static List<Target> loadSpotifyData(String path) {
        List<Target> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                double popularity = Double.parseDouble(tokens[0]);

                Target t = new Target()
                        .when("danceability", Double.parseDouble(tokens[1]))
                        .when("energy", Double.parseDouble(tokens[2]))
                        .when("loudness", Double.parseDouble(tokens[3]))
                        .when("speechiness", Double.parseDouble(tokens[4]))
                        .when("acousticness", Double.parseDouble(tokens[5]))
                        .when("instrumentalness", Double.parseDouble(tokens[6]))
                        .when("liveness", Double.parseDouble(tokens[7]))
                        .when("valence", Double.parseDouble(tokens[8]))
                        .when("tempo", Double.parseDouble(tokens[9]))
                        .targetIs(popularity);

                data.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}
