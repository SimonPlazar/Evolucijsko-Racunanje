from problems.problem import Problem
import numpy as np


class Bukin(Problem):
    def __init__(self):
        super().__init__(d=2, lowerBound=(-15, -3), upperBound=(-5, 3), name="Bukin")

    def evaluate(self, x) -> float:
        return 100 * np.sqrt(np.abs(x[1] - 0.01 * x[0] ** 2)) + 0.01 * np.abs(x[0] + 10)

    def generateRandomSolution(self):
        return np.array([
            np.random.uniform(self.lowerBound[0], self.upperBound[0]),
            np.random.uniform(self.lowerBound[1], self.upperBound[1]),
        ])