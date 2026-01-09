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
        String path = "C:\\Users\\simon\\Desktop\\ER\\Evolucijsko-Racunanje\\ER-N2-CEOpy\\ceo_random.txt";

        RNG.setSelectedRandomGenerator(RNG.RngType.PREDEFINED_RANDOM);
        PredefinedRandom rng = (PredefinedRandom) RNG.getSelectedRng();

//        if (!rng.loadFromFile(path)) {
//            System.err.println("Failed to load predefined random numbers");
//            return;
//        }

//        System.out.println(RNG.nextDouble());
//        System.out.println(RNG.nextDouble());
//        System.out.println(RNG.nextInt(0, 10));
        // Equivalent to rng.permutation(5)
//        System.out.println(Arrays.toString(RNG.randomPermutation(5)));

        for (int i = 0; i < 10; i++) {
            System.out.println(RNG.nextDouble());
        }
    }
}