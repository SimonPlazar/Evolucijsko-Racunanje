package org.example;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.ceo.CEO;
import org.um.feri.ears.algorithms.so.de.jade.JADE;
import org.um.feri.ears.algorithms.so.gwo.GWO;
import org.um.feri.ears.algorithms.so.pso.PSO;
import org.um.feri.ears.algorithms.so.random.RandomSearch;
import org.um.feri.ears.benchmark.Benchmark;

import java.util.ArrayList;

public class CEC2015Benchmark {
    public static void main(String[] args) {
        Benchmark.printInfo = true; //prints one on one results
        //add algorithms to a list

        ArrayList<NumberAlgorithm> algorithms = new ArrayList<NumberAlgorithm>();
        algorithms.add(new RandomSearch());
        algorithms.add(new ABC());
        algorithms.add(new PSO());
        algorithms.add(new GWO());
        algorithms.add(new JADE());
        algorithms.add(new CEO());

        org.um.feri.ears.benchmark.CEC2015Benchmark cec2015 = new org.um.feri.ears.benchmark.CEC2015Benchmark();

        cec2015.addAlgorithms(algorithms);

        cec2015.run(10);
    }
}
