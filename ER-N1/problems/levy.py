from problems.problem import Problem
import numpy as np


class Levy(Problem):
    def __init__(self, d: int):
        super().__init__(d=d, lowerBound=-10, upperBound=10, name="Levy")

    def evaluate(self, x) -> float:
        w = 1 + (x - 1) / 4
        term1 = (np.sin(np.pi * w[0])) ** 2
        term3 = (w[-1] - 1) ** 2 * (1 + (np.sin(2 * np.pi * w[-1])) ** 2)
        term2 = np.sum((w[:-1] - 1) ** 2 * (1 + 10 * (np.sin(np.pi * w[:-1] + 1)) ** 2))
        return term1 + term2 + term3

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)