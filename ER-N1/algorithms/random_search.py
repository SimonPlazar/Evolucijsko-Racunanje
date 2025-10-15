from algorithms.algorithm import Algorithm
from algorithms.solution import Solution
from problems.problem import Problem


class RandomSearch(Algorithm):
    def execute(self, problem: Problem, maxFes: int) -> Solution:
        best_solution = None

        for _ in range(maxFes):
            x = problem.generateRandomSolution()
            fitness = problem.evaluate(x)
            candidate = Solution(x, fitness)

            if best_solution is None or candidate.fitness < best_solution.fitness:
                best_solution = candidate

        return best_solution
