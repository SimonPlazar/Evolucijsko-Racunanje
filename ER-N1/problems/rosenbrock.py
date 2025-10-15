from problems.problem import Problem
import numpy as np


class Rosenbrock(Problem):
    def __init__(self, d: int):
        super().__init__(d=d, lowerBound=-5, upperBound=10, name="Rosenbrock")

    def evaluate(self, x: np.ndarray) -> float:
        return np.sum(100 * (x[1:] - x[:-1] ** 2) ** 2 + (x[:-1] - 1) ** 2)

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)
