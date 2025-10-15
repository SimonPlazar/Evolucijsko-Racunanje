from problems.problem import Problem
import numpy as np


class Schwefel26(Problem):
    def __init__(self, d: int):
        super().__init__(d=d, lowerBound=-500, upperBound=500, name="Schwefel26")

    def evaluate(self, x):
        return -sum(x * np.sin(np.abs(x) ** 0.5))

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)