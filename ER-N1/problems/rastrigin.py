from problems.problem import Problem
import numpy as np


class Rastrigin(Problem):
    def __init__(self, d: int):
        super().__init__(d=d, lowerBound=-5.12, upperBound=5.12, name="Rastrigin")

    def evaluate(self, x: np.ndarray) -> float:
        return 10 * self.d + np.sum(x ** 2 - 10 * np.cos(2 * np.pi * x))

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)