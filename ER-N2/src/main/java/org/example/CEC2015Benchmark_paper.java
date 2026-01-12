package org.example;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.ceo.CEO;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.sma.SMA;
import org.um.feri.ears.algorithms.so.de.DE;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.pso.PSO;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.CEC2015Benchmark;

import java.util.ArrayList;

public class CEC2015Benchmark_paper {
    public static void main(String[] args) {
        Benchmark.printInfo = true;

        ArrayList<NumberAlgorithm> algorithms = new ArrayList<NumberAlgorithm>();
        algorithms.add(new GWO());
        algorithms.add(new SMA());
        algorithms.add(new ABC());
        algorithms.add(new PSO());
        algorithms.add(new DE());

        algorithms.add(new CEO());

        CEC2015Benchmark cec2015 = new CEC2015Benchmark(); // benchmark with prepared tasks and settings

        cec2015.addAlgorithms(algorithms);  // register the algorithms in the benchmark

        cec2015.run(10); //start the tournament with 10 runs/repetitions
    }
}
