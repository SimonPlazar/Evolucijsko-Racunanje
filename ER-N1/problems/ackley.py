from numpy import ndarray

from problems.problem import Problem
import numpy as np


class Ackley(Problem):
    def __init__(self, d: int):
        super().__init__(d=d, lowerBound=-32.768, upperBound=32.768, name="Ackley")

    def evaluate(self, x: ndarray) -> float:
        a = 20
        b = 0.2
        c = 2 * np.pi

        sum1 = np.sum(x ** 2)
        sum2 = np.sum(np.cos(c * x))

        term1 = -a * np.exp(-b * np.sqrt(sum1 / self.d))
        term2 = -np.exp(sum2 / self.d)

        return term1 + term2 + a + np.exp(1)  # Euler's number

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)
