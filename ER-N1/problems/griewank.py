from problems.problem import Problem
import numpy as np


class Griewank(Problem):
    def __init__(self, d: int):
        super().__init__(d, -600, 600, "Griewank")

    def evaluate(self, x: np.ndarray) -> float:
        sum_part = np.sum(x ** 2 / 4000)
        prod_part = np.prod(np.cos(x / np.sqrt(np.arange(1, self.d + 1))))
        return sum_part - prod_part + 1

    def generateRandomSolution(self):
        return np.random.uniform(self.lowerBound, self.upperBound, self.d)