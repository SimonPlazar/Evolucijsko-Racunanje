package org.example;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.ceo.CEO;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.ba.BA;
import org.um.feri.ears.algorithms.so.sma.SMA;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.CEC2015Benchmark;

import java.util.ArrayList;

public class CEC2015Benchmark_paper {
    public static void main(String[] args) {
        Benchmark.printInfo = false; //prints one on one results
        //add algorithms to a list

        ArrayList<NumberAlgorithm> algorithms = new ArrayList<NumberAlgorithm>();
        algorithms.add(new GWO());
        algorithms.add(new BA());
        algorithms.add(new SMA());

        algorithms.add(new CEO());

        CEC2015Benchmark cec2015 = new CEC2015Benchmark(); // benchmark with prepared tasks and settings

        cec2015.addAlgorithms(algorithms);  // register the algorithms in the benchmark

        cec2015.run(10); //start the tournament with 10 runs/repetitions
    }
}
