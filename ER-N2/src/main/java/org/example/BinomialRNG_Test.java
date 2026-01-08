package org.example;

import org.um.feri.ears.util.random.PredefinedRandom;
import org.um.feri.ears.util.random.RNG;
import org.um.feri.ears.util.random.RandomGenerator;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class BinomialRNG_Test {
    public static void main(String[] args) {
        System.out.println("cwd: " + new java.io.File(".").getAbsolutePath());

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

        int N = 3;
        int dim = 5;
        double CR = 0.7;

        for (int n = 0; n < N; n++) {
            System.out.println("Trial " + n);

            // mask
            for (int d = 0; d < dim; d++) {
                System.out.println("mask[" + d + "] = " + RNG.nextDouble());
            }

            // jRand
            System.out.println("jRand = " + RNG.nextInt(dim));
            System.out.println();
        }
    }
}
