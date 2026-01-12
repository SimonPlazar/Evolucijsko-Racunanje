package org.example;

import org.um.feri.ears.algorithms.NumberAlgorithm;
import org.um.feri.ears.algorithms.so.ceo.CEO;
import org.um.feri.ears.algorithms.so.de.DE;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.benchmark.RPUOed30Benchmark;

import java.util.ArrayList;

public class test_Benchmark {
    public static void main(String[] args) {
        Benchmark.printInfo = false; //prints one on one results
        //add algorithms to a list

        ArrayList<NumberAlgorithm> algorithms = new ArrayList<NumberAlgorithm>();
        algorithms.add(new CEO());
        algorithms.add(new DE());

        RPUOed30Benchmark rpuoed30 = new RPUOed30Benchmark(); // benchmark with prepared tasks and settings

        rpuoed30.addAlgorithms(algorithms);  // register the algorithms in the benchmark

        rpuoed30.run(10); //start the tournament with 10 runs/repetitions
    }
}
