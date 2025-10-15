from problems.problem import Problem
import numpy as np

class Sphere(Problem):
    def __init__(self, d: int):
        lower_bounds = np.array([-100 if i % 2 == 0 else -10 for i in range(d)])
        upper_bounds = np.array([100 if i % 2 == 0 else 10 for i in range(d)])

        super().__init__(d=d, lowerBound=lower_bounds, upperBound=upper_bounds, name="Sphere")

    def evaluate(self, x: np.ndarray) -> float:
        return np.sum(x ** 2)

    def generateRandomSolution(self):
        return np.array([np.random.uniform(self.lowerBound[i], self.upperBound[i]) for i in range(self.d)])