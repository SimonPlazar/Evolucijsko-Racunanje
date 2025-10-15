import unittest
import numpy as np

from problems.sphere import Sphere
from problems.ackley import Ackley
from problems.griewank import Griewank
from problems.rastrigin import Rastrigin
from problems.schwefel26 import Schwefel26
from problems.rosenbrock import Rosenbrock
from problems.trid import Trid
from problems.bukin import Bukin
from problems.carrom_table import CarromTable
from problems.styblinski_tang import StyblinskiTang
from problems.levy import Levy
from problems.michalewicz import Michalewicz


class TestProblems(unittest.TestCase):
    dimensions = [2, 5, 10]

    def test_sphere_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Sphere(d=d)
                x_opt = np.zeros(d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, 0.0, places=7)

    def test_ackley_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Ackley(d=d)
                x_opt = np.zeros(d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, 0.0, places=7)

    def test_griewank_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Griewank(d=d)
                x_opt = np.zeros(d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, 0.0, places=7)

    def test_rastrigin_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Rastrigin(d=d)
                x_opt = np.zeros(d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, 0.0, places=7)

    def test_schwefel26_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Schwefel26(d=d)
                x_opt = np.array([420.968746] * d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, -418.982887272433799807913601398 * d, places=7)

    def test_rosenbrock_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Rosenbrock(d=d)
                x_opt = np.ones(d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, 0.0, places=7)

    def test_trid_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Trid(d=d)
                x_opt = np.array([i * (d + 1 - i) for i in range(1, d + 1)])
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, -d * (d + 4) * (d - 1) / 6, places=7)

    def test_bukin_global_optimum(self):
        with self.subTest():
            problem = Bukin()
            x_opt = np.array([-10, 1])
            f = problem.evaluate(x_opt)
            self.assertAlmostEqual(f, 0.0, places=7)

    def test_carrom_table_global_optimum(self):
        with self.subTest():
            problem = CarromTable()
            x_opt = np.full(2, 9.646157266348881)
            f = problem.evaluate(x_opt)
            self.assertAlmostEqual(f, -24.15681551650653, places=7)

    def test_styblinski_tang_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = StyblinskiTang(d=d)
                x_opt = np.full(d, -2.90353401818596)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, -39.16616570377142 * d, places=7)

    def test_levy_global_optimum(self):
        for d in self.dimensions:
            with self.subTest(d=d):
                problem = Levy(d=d)
                x_opt = np.ones(d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, 0.0, places=7)

    def test_michalewicz_global_optimum(self):
        known_optima = {
            1: (2.20290552017261, -0.801303410098552549),
            2: (np.array([2.20290552014618, 1.57079632677565]), -1.80130341009855321)
        }
        for d, (x_opt, expected) in known_optima.items():
            with self.subTest(d=d):
                problem = Michalewicz(d=d)
                f = problem.evaluate(x_opt)
                self.assertAlmostEqual(f, expected, places=7)


if __name__ == '__main__':
    unittest.main()
