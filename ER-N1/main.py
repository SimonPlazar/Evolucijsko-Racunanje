from algorithms.random_search import RandomSearch
from problems.sphere import Sphere
from problems.ackley import Ackley
from problems.griewank import Griewank
from problems.rastrigin import Rastrigin
from problems.schwefel26 import Schwefel26
from problems.rosenbrock import Rosenbrock
from problems.trid import Trid
from problems.bukin import Bukin
from problems.carrom_table import CarromTable
from problems.styblinski_tang import StyblinskiTang
from problems.levy import Levy
from problems.michalewicz import Michalewicz
from utils.statistics_utility import StatisticsUtility

if __name__ == '__main__':
    problem_classes_dims = [
        (Sphere, [2, 5, 10]),
        (Ackley, [2, 5, 10]),
        (Griewank, [2, 5, 10]),
        (Rastrigin, [2, 5, 10]),
        (Schwefel26, [2, 5, 10]),
        (Rosenbrock, [2, 5, 10]),
        (Trid, [2, 5, 10]),
        (Bukin, [2]),
        (CarromTable, [2]),
        (StyblinskiTang, [2, 5, 10]),
        (Levy, [2, 5, 10]),
        (Michalewicz, [1, 2]),
    ]
    runs = 100
    # runs = 10

    for problem_class, dims in problem_classes_dims:
        for d in dims:
            best_values = []
            # Bukin and CarromTable do not take d as argument
            if problem_class in [Bukin, CarromTable]:
                problem = problem_class()
            else:
                problem = problem_class(d)
            maxFes = 3000 * d
            # maxFes = 500 * d
            for _ in range(runs):
                solution = RandomSearch().execute(problem, maxFes)
                best_values.append(solution.fitness)
            min_val = StatisticsUtility.calculate_best_solution(best_values)
            mean_val = StatisticsUtility.calculate_mean(best_values)
            std_val = StatisticsUtility.calculate_standard_deviation(best_values)
            print(
                f"Problem: {problem.name}, Dimensions: {d} Min: {min_val:.6g}, Average: {mean_val:.6g}, Std: {std_val:.6g}")
