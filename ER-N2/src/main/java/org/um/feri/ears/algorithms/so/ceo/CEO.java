package org.um.feri.ears.algorithms.so.ceo;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.AlgorithmInfo;
import org.um.feri.ears.algorithms.Author;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.util.annotation.AlgorithmParameter;
import org.um.feri.ears.util.Util;
import org.um.feri.ears.util.random.RNG;

import java.util.ArrayList;
import java.util.List;

public class CEO extends NumberAlgorithm {

    @AlgorithmParameter(name = "population size")
    private int popSize;

    @AlgorithmParameter(name = "chaotic samples")
    private int N;

    private List<NumberSolution<Double>> population;
    private NumberSolution<Double> best;

    // Constants for chaotic mapping (from Python source)
    private final double[] lowChacos = {-0.5, -0.25};
    private final double[] upChacos = {0.5, 0.25};
    private int stagnationCounter = 0;

    public CEO() {
        this(30, 10);
    }

    public CEO(int popSize, int N) {
        super();
        // Np must be even in this algorithm
        this.popSize = (popSize % 2 == 0) ? popSize : popSize + 1;
        this.N = N;

        au = new Author("student", "student@um.si");
        ai = new AlgorithmInfo("CEO", "Chaotic Evolution Optimization",
                "@article{ceo2024, title={Chaotic evolution optimization}, author={...}, year={2024}}");
    }

    @Override
    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task) throws StopCriterionException {
        this.task = task;
        resetToDefaultsBeforeNewRun();
        initPopulation();

        while (!task.isStopCriterion()) {
            double lastIterBest = best.getEval(); // Use getEval() instead of getFitness()

            // Calculate current search domain bounds based on population (Eq. 4 logic)
            double[] ub = getPopulationBounds(true);
            double[] lb = getPopulationBounds(false);

            int[] randNum = RNG.randomPermutation(popSize);

            for (int i = 0; i < popSize; i += 2) {
                if (task.isStopCriterion()) break;

                int idx1 = randNum[i];
                int idx2 = randNum[i + 1];

                NumberSolution<Double> xInd = population.get(idx1);
                NumberSolution<Double> yInd = population.get(idx2);

                // EDM seeds mapping
                double[] x0 = mapToChaotic(xInd.getVariables(), lb, ub, 0);
                double[] y0 = mapToChaotic(yInd.getVariables(), lb, ub, 1);

                // Generate Chaotic Sequences (Exponential Discrete Memristor)
                double[][] chaosTotal = EDM(x0, y0, N);

                int[] pair = {idx1, idx2};
                for (int k = 0; k < 2; k++) {
                    NumberSolution<Double> currentParent = population.get(pair[k]);
                    NumberSolution<Double> bestTrialOfN = null;

                    for (int j = 0; j < N; j++) {
                        if (task.isStopCriterion()) break;

                        // Map chaotic sample back to problem space
                        double[] xyChaos = chaosTotal[k == 0 ? j : j + N];
                        double[] xyChaosDot = mapToProblemSpace(xyChaos, lb, ub, k);

                        // Use framework to ensure feasibility
                        task.problem.makeFeasible(xyChaosDot);

                        // Mutation (Eq. 7 & 8)
                        double[] xyHat = new double[task.problem.getNumberOfDimensions()];
                        boolean useParent = RNG.nextDouble() < 0.5;
                        for (int d = 0; d < task.problem.getNumberOfDimensions(); d++) {
                            double base = useParent ? currentParent.getValue(d) : best.getValue(d);
                            xyHat[d] = base + RNG.nextDouble() * (xyChaosDot[d] - currentParent.getValue(d));
                        }

                        // Crossover (Eq. 9)
                        double[] trialPos = binomialCrossover(currentParent.getVariables(), xyHat, RNG.nextDouble());
                        task.problem.makeFeasible(trialPos);

                        NumberSolution<Double> trialSol = new NumberSolution<>(Util.toDoubleArrayList(trialPos));
                        task.eval(trialSol);

                        // Find the best among N samples
                        if (bestTrialOfN == null || task.problem.isFirstBetter(trialSol, bestTrialOfN)) {
                            bestTrialOfN = trialSol;
                        }
                    }

                    // Selection (Eq. 10): Replace parent if trial is better
                    if (bestTrialOfN != null && task.problem.isFirstBetter(bestTrialOfN, population.get(pair[k]))) {
                        population.set(pair[k], bestTrialOfN);
                        if (task.problem.isFirstBetter(bestTrialOfN, best)) {
                            best = new NumberSolution<>(bestTrialOfN);
                        }
                    }
                }
            }

            if (handleStagnation(lastIterBest, best.getEval())) break;
            task.incrementNumberOfIterations();
        }
        return best;
    }

    private void initPopulation() throws StopCriterionException {
        population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            NumberSolution<Double> sol = task.generateRandomEvaluatedSolution();
            population.add(sol);
            // task.problem.isFirstBetter handles minimization/maximization automatically
            if (best == null || task.problem.isFirstBetter(sol, best)) {
                best = new NumberSolution<>(sol);
            }
        }
    }

    private double[][] EDM(double[] x0, double[] y0, int iter) {
        int dim = task.problem.getNumberOfDimensions();
        double k = 2.66;
        double[][] x = new double[iter + 1][dim];
        double[][] y = new double[iter + 1][dim];
        x[0] = x0.clone();
        y[0] = y0.clone();

        for (int j = 0; j < iter; j++) {
            for (int d = 0; d < dim; d++) {
                x[j + 1][d] = k * (Math.exp(-Math.cos(Math.PI * y[j][d])) - 1) * x[j][d];
                y[j + 1][d] = y[j][d] + x[j][d];
            }
        }

        double[][] result = new double[2 * iter][dim];
        for (int j = 0; j < iter; j++) {
            result[j] = x[j + 1];
            result[j + iter] = y[j + 1];
        }
        return result;
    }

    private double[] binomialCrossover(List<Double> p, double[] v, double CR) {
        int dim = v.length;
        double[] u = new double[dim];
        int jRand = RNG.nextInt(dim);
        for (int j = 0; j < dim; j++) {
            u[j] = (RNG.nextDouble() < CR || j == jRand) ? v[j] : p.get(j);
        }
        return u;
    }

    private double[] mapToChaotic(List<Double> x, double[] lb, double[] ub, int k) {
        double[] mapped = new double[x.size()];
        for (int i = 0; i < x.size(); i++) {
            double range = ub[i] - lb[i];
            mapped[i] = ((x.get(i) - lb[i]) / (range + 1e-18)) * (upChacos[k] - lowChacos[k]) + lowChacos[k];
        }
        return mapped;
    }

    private double[] mapToProblemSpace(double[] xChaos, double[] lb, double[] ub, int k) {
        double[] mapped = new double[xChaos.length];
        for (int i = 0; i < xChaos.length; i++) {
            mapped[i] = ((xChaos[i] - lowChacos[k]) / (upChacos[k] - lowChacos[k])) * (ub[i] - lb[i]) + lb[i];
        }
        return mapped;
    }

    private double[] getPopulationBounds(boolean upper) {
        int dim = task.problem.getNumberOfDimensions();
        double[] res = new double[dim];
        for (int d = 0; d < dim; d++) {
            double val = population.get(0).getValue(d);
            for (int i = 1; i < popSize; i++) {
                val = upper ? Math.max(val, population.get(i).getValue(d)) : Math.min(val, population.get(i).getValue(d));
            }
            res[d] = val;
        }
        return res;
    }

    private boolean handleStagnation(double oldBest, double newBest) {
        if (Math.abs(oldBest - newBest) < 1e-8) stagnationCounter++;
        else stagnationCounter = 0;
        return stagnationCounter > 50;
    }

    @Override
    public void resetToDefaultsBeforeNewRun() {
        stagnationCounter = 0;
        best = null;
    }
}