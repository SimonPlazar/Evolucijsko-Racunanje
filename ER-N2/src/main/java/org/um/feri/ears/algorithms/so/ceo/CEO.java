package org.um.feri.ears.algorithms.so.ceo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.random.PredefinedRandom;
import org.um.feri.ears.util.random.RNG;
import java.util.ArrayList;
import java.util.List;

public class CEO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;
    @AlgorithmParameter(name = "chaotic samples")
    private int N;

    private List<NumberSolution<Double>> population;
    private NumberSolution<Double> bestSolution; // Naša referenca na globalno najboljšo rešitev

    private final double[] lowChacos = {-0.5, -0.25};
    private final double[] upChacos = {0.5, 0.25};

    private int stagnationCounter = 0;

    public CEO() {
        this(30, 10);
    }

    public CEO(int popSize, int N) {
        super();
        this.popSize = (popSize % 2 == 0) ? popSize : popSize + 1;
        this.N = N;
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        this.stagnationCounter = 0;
        int dim = task.problem.getNumberOfDimensions();

        // 1. Inicializacija in postavitev začetnega bestSolution
        initPopulation();

        while (!task.isStopCriterion()) {
            double fBestOld = bestSolution.getEval();

            int[] indices = RNG.randomPermutation(popSize);
            double[] lb = getPopBounds(false, dim);
            double[] ub = getPopBounds(true, dim);

            for (int i = 0; i < popSize; i += 2) {
                if (task.isStopCriterion()) break;

                int[] pair = {indices[i], indices[i+1]};

                for (int k = 0; k < 2; k++) {
                    NumberSolution<Double> parent = population.get(pair[k]);

                    // EDM Mapiranje
                    double[][] chaosPoints = generateEDM(parent, lb, ub, dim, k);

                    // 1:1 RNG Draws (Vrstni red mora biti identičen Pythonu!)
                    double mutChoice = RNG.nextDouble();
                    double[] r = new double[N];
                    for (int n = 0; n < N; n++) r[n] = RNG.nextDouble();
                    double CR = RNG.nextDouble();

                    List<NumberSolution<Double>> trials = new ArrayList<>();
                    for (int n = 0; n < N; n++) {
                        if (task.isStopCriterion()) break;

                        List<Double> trialValues = new ArrayList<>(dim);
                        for (int d = 0; d < dim; d++) {
                            // Uporabljamo bestSolution.getValue(d) za Eq. 8
                            double base = (mutChoice < 0.5) ? parent.getValue(d) : bestSolution.getValue(d);
                            trialValues.add(base + r[n] * (chaosPoints[n][d] - parent.getValue(d)));
                        }

                        // Binomial Crossover
                        int jRand = RNG.nextInt(dim);
                        for (int d = 0; d < dim; d++) {
                            if (!(RNG.nextDouble() < CR || d == jRand)) {
                                trialValues.set(d, parent.getValue(d));
                            }
                        }

                        NumberSolution<Double> trialSol = new NumberSolution<>(trialValues);
                        handleMirrorBounds(trialSol);
                        task.eval(trialSol);
                        trials.add(trialSol);
                    }

                    // Selekcija (Eq. 10)
                    if (!trials.isEmpty()) {
                        NumberSolution<Double> localBestTrial = getBestFromList(trials);
                        if (task.problem.isFirstBetter(localBestTrial, parent)) {
                            population.set(pair[k], new NumberSolution<>(localBestTrial));
                            // POMEMBNO: Takojšnje preverjanje, če je nova rešitev boljša od globalne
                            if (task.problem.isFirstBetter(localBestTrial, bestSolution)) {
                                bestSolution = new NumberSolution<>(localBestTrial);
                            }
                        }
                    }
                }
            }

            task.incrementNumberOfIterations();

            // Izpis indeksa za debugging (če uporabljate PredefinedRandom)
            printDebug();

            // Stagnacija (1:1 Python logika)
            if (handleStagnation(fBestOld, bestSolution.getEval())) {
                break;
            }
        }
        return bestSolution;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {

    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            if (task.isStopCriterion()) break;
            NumberSolution<Double> s = task.generateRandomEvaluatedSolution();
            population.add(s);
        }
        updateBestSolution(); // Postavi začetni globalni minimum
    }

    private void updateBestSolution() {
        if (population.isEmpty()) return;
        bestSolution = population.get(0);
        for (int i = 1; i < population.size(); i++) {
            if (task.problem.isFirstBetter(population.get(i), bestSolution)) {
                bestSolution = population.get(i);
            }
        }
    }

    private boolean handleStagnation(double oldBest, double newBest) {
        if (task.getStopCriterion() != StopCriterion.STAGNATION) {
            return false;
        }

        if (Math.abs(oldBest - newBest) < 1e-8) {
            stagnationCounter++;
            return stagnationCounter > 50;
        } else {
            stagnationCounter = 0;
            return false;
        }
    }

    private void printDebug() {
        int iter = (task != null) ? task.getNumberOfIterations() : -1;
        double best = (bestSolution != null) ? bestSolution.getEval() : Double.NaN;

        StringBuilder sb = new StringBuilder();
        sb.append("iter=").append(iter).append("  best=").append(best);

        var selectedRng = RNG.getSelectedRng();
        if (selectedRng instanceof PredefinedRandom pr) {
            sb.append("  RNG index: ").append(pr.getCurrentIndex());
        }

        System.out.println(sb.toString());
    }

    private void handleMirrorBounds(NumberSolution<Double> s) {
        for (int i = 0; i < task.problem.getNumberOfDimensions(); i++) {
            double v = s.getValue(i);
            double low = task.problem.getLowerLimit(i);
            double up = task.problem.getUpperLimit(i);
            if (v < low) s.setValue(i, Math.min(up, 2 * low - v));
            else if (v > up) s.setValue(i, Math.max(low, 2 * up - v));
        }
//        task.problem.makeFeasible(s);
    }

    private double[][] generateEDM(NumberSolution<Double> p, double[] lb, double[] ub, int dim, int k) {
        double[] x = new double[dim];
        double[] y = new double[dim];
        double eps = 1e-18;
        for (int d = 0; d < dim; d++) {
            double norm = (p.getValue(d) - lb[d]) / (ub[d] - lb[d] + eps);
            x[d] = norm * (upChacos[0] - lowChacos[0]) + lowChacos[0];
            y[d] = norm * (upChacos[1] - lowChacos[1]) + lowChacos[1];
        }
        double[][] chaos = new double[N][dim];
        for (int n = 0; n < N; n++) {
            for (int d = 0; d < dim; d++) {
                x[d] = 2.66 * (Math.exp(-Math.cos(Math.PI * y[d])) - 1.0) * x[d];
                y[d] = y[d] + x[d];
                double cVal = (k == 0) ? x[d] : y[d];
                chaos[n][d] = ((cVal - lowChacos[k]) / (upChacos[k] - lowChacos[k])) * (ub[d] - lb[d]) + lb[d];
            }
        }
        return chaos;
    }

    private double[] getPopBounds(boolean upper, int dim) {
        double[] b = new double[dim];
        for (int d = 0; d < dim; d++) {
            b[d] = population.get(0).getValue(d);
            for (int i = 1; i < popSize; i++) {
                double val = population.get(i).getValue(d);
                b[d] = upper ? Math.max(b[d], val) : Math.min(b[d], val);
            }
        }
        return b;
    }

    private NumberSolution<Double> getBestFromList(List<NumberSolution<Double>> list) {
        NumberSolution<Double> b = list.get(0);
        for (NumberSolution<Double> s : list) {
            if (task.problem.isFirstBetter(s, b)) b = s;
        }
        return b;
    }
}