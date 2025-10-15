from problems.problem import Problem
import numpy as np

class Trid(Problem):
    def __init__(self, d: int):
        super().__init__(d=d, lowerBound=-d * d, upperBound=d * d, name="Trid")

    def evaluate(self, x: np.ndarray) -> float:
        return np.sum((x - 1) ** 2) - np.sum(x[1:] * x[:-1])

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)