package org.example;

import org.um.feri.ears.util.random.PredefinedRandom;
import org.um.feri.ears.util.random.RandomGenerator;
import org.um.feri.ears.util.random.RNG;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class RNG_Test {

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

        // Equivalent to rng.rand()
        System.out.println(RNG.nextDouble());
        // Equivalent to rng.rand()
        System.out.println(RNG.nextDouble());
        // Equivalent to rng.randint(0, 10)
        System.out.println(RNG.nextInt(0, 10));
        // Equivalent to rng.permutation(5)
        System.out.println(Arrays.toString(RNG.randomPermutation(5)));

//        for (int i = 0; i < 10; i++) {
//            System.out.println(RNG.nextDouble());
//        }
    }
}