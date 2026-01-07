//package org.um.feri.ears.algorithms.so.ceo;
//
//import org.um.feri.ears.algorithms.*;
//import org.um.feri.ears.problems.*;
//import org.um.feri.ears.util.*;
//import org.um.feri.ears.util.annotation.AlgorithmParameter;
//import org.um.feri.ears.util.comparator.ProblemComparator;
//import org.um.feri.ears.util.random.PredefinedRandom;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class CEO extends NumberAlgorithm {
//
//    @AlgorithmParameter(name = "population size")
//    private int popSize;
//
//    @AlgorithmParameter(name = "chaotic samples")
//    private int N;
//
//    private ArrayList<NumberSolution<Double>> population;
//    private NumberSolution<Double> best;
//
//    private PredefinedRandom rng;
//
//    private final double[] lowChacos = {-0.5, -0.25};
//    private final double[] upChacos  = { 0.5,  0.25};
//
//    public CEO() {
//        this(30, 10);
//    }
//
//    public CEO(int popSize, int N) {
//        super();
//        this.popSize = popSize;
//        this.N = N;
//    }
//
//    @Override
//    public NumberSolution<Double> execute(Task<NumberSolution<Double>, DoubleProblem> task)
//            throws StopCriterionException {
//
//        this.task = task;
//        this.rng = (PredefinedRandom) task.getRandom();
//
//        initPopulation();
//
//        int dim = task.problem.getNumberOfDimensions();
//
//        while (!task.isStopCriterion()) {
//
//            double[] lb = new double[dim];
//            double[] ub = new double[dim];
//            computeBounds(lb, ub);
//
//            int[] perm = rng.randomPermutation(popSize);
//
//            for (int p = 0; p < popSize; p += 2) {
//
//                int i1 = perm[p];
//                int i2 = perm[p + 1];
//
//                NumberSolution<Double> x = population.get(i1);
//                NumberSolution<Double> y = population.get(i2);
//
//                processIndividual(i1, x, y, lb, ub);
//                processIndividual(i2, y, x, lb, ub);
//            }
//
//            updateBest();
//            task.incrementNumberOfIterations();
//        }
//
//        return best;
//    }
//
//    /* ================= INITIALIZATION ================= */
//
//    private void initPopulation() throws StopCriterionException {
//        population = new ArrayList<>();
//        int dim = task.problem.getNumberOfDimensions();
//
//        double[] min = task.problem.getLowerLimit();
//        double[] max = task.problem.getUpperLimit();
//
//        for (int i = 0; i < popSize; i++) {
//
//            double[] pos = new double[dim];
//            for (int d = 0; d < dim; d++) {
//                pos[d] = rng.nextDouble() * (max[d] - min[d]) + min[d];
//            }
//
//            NumberSolution<Double> sol =
//                    new NumberSolution<>(Util.toDoubleArrayList(pos));
//            task.eval(sol);
//            population.add(sol);
//        }
//
//        updateBest();
//    }
//
//    /* ================= CORE ================= */
//
//    private void processIndividual(
//            int idx,
//            NumberSolution<Double> current,
//            NumberSolution<Double> other,
//            double[] lb,
//            double[] ub
//    ) throws StopCriterionException {
//
//        int dim = task.problem.getNumberOfDimensions();
//        double[][] chaos = EDM(
//                mapToChaotic(current.getValues(), lb, ub, 0),
//                mapToChaotic(other.getValues(), lb, ub, 1),
//                dim
//        );
//
//        NumberSolution<Double> bestTrial = null;
//
//        for (int j = 0; j < N; j++) {
//
//            double[] candidate = new double[dim];
//            double r = rng.nextDouble();
//
//            for (int d = 0; d < dim; d++) {
//                if (rng.nextDouble() < 0.5) {
//                    candidate[d] =
//                            current.getValue(d) + r * (chaos[j][d] - current.getValue(d));
//                } else {
//                    candidate[d] =
//                            best.getValue(d) + r * (chaos[j][d] - current.getValue(d));
//                }
//            }
//
//            task.problem.makeFeasible(candidate);
//
//            if (task.isStopCriterion())
//                return;
//
//            NumberSolution<Double> trial =
//                    new NumberSolution<>(Util.toDoubleArrayList(candidate));
//            task.eval(trial);
//
//            if (bestTrial == null ||
//                    task.problem.isFirstBetter(trial, bestTrial)) {
//                bestTrial = trial;
//            }
//        }
//
//        if (task.problem.isFirstBetter(bestTrial, current)) {
//            population.set(idx, bestTrial);
//        }
//    }
//
//    /* ================= UTIL ================= */
//
//    private void updateBest() {
//        population.sort(new ProblemComparator<>(task.problem));
//        best = population.get(0);
//    }
//
//    private void computeBounds(double[] lb, double[] ub) {
//        Arrays.fill(lb, Double.POSITIVE_INFINITY);
//        Arrays.fill(ub, Double.NEGATIVE_INFINITY);
//
//        for (NumberSolution<Double> s : population) {
//            for (int d = 0; d < lb.length; d++) {
//                lb[d] = Math.min(lb[d], s.getValue(d));
//                ub[d] = Math.max(ub[d], s.getValue(d));
//            }
//        }
//    }
//
//    private double[] mapToChaotic(double[] x, double[] lb, double[] ub, int k) {
//        double[] out = new double[x.length];
//        for (int d = 0; d < x.length; d++) {
//            out[d] =
//                    (x[d] - lb[d]) / ((ub[d] - lb[d]) + 1e-16) *
//                            (upChacos[k] - lowChacos[k]) + lowChacos[k];
//        }
//        return out;
//    }
//
//    private double[][] EDM(double[] x0, double[] y0, int dim) {
//        double k = 2.66;
//        double[][] out = new double[N][dim];
//        double[] x = x0.clone();
//        double[] y = y0.clone();
//
//        for (int i = 0; i < N; i++) {
//            for (int d = 0; d < dim; d++) {
//                double newX =
//                        k * (Math.exp(-Math.cos(Math.PI * y[d])) - 1) * x[d];
//                double newY = y[d] + x[d];
//                x[d] = newX;
//                y[d] = newY;
//                out[i][d] = newX;
//            }
//        }
//        return out;
//    }
//
//    @Override
//    public void resetToDefaultsBeforeNewRun() {
//    }
//}
