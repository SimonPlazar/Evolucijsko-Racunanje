from problems.problem import Problem
import numpy as np


class Michalewicz(Problem):
    def __init__(self, d: int):
        super().__init__(d=d, lowerBound=0, upperBound=np.pi, name="Michalewicz")

    def evaluate(self, x: np.ndarray) -> float:
        i = np.arange(1, self.d + 1)
        return -np.sum(np.sin(x) * (np.sin(i * x ** 2 / np.pi)) ** 20)

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)
