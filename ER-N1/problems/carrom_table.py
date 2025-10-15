from problems.problem import Problem
import numpy as np


class CarromTable(Problem):
    def __init__(self):
        super().__init__(d=2, lowerBound=-10, upperBound=10, name="CarromTable")

    def evaluate(self, x) -> float:
        term1 = np.exp(2 * np.abs(1 - (np.sqrt(x[0] ** 2 + x[1] ** 2) / np.pi)))
        term2 = np.cos(x[0]) ** 2 * np.cos(x[1]) ** 2
        return -(1 / 30) * term1 * term2

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, 2)