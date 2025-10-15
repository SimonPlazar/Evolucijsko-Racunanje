from problems.problem import Problem
import numpy as np

class StyblinskiTang(Problem):
    def __init__(self, d:int):
        super().__init__(d=d, lowerBound=-5, upperBound=5, name="StyblinskiTang")

    def evaluate(self, x: np.ndarray) -> float:
        return 0.5 * np.sum(x**4 - 16*x**2 + 5*x)

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)
