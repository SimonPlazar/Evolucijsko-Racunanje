import numpy as np


class StatisticsUtility:
    @staticmethod
    def calculate_best_solution(values):
        return np.min(values)

    @staticmethod
    def calculate_mean(values):
        return np.mean(values)

    @staticmethod
    def calculate_standard_deviation(values):
        return np.std(values)
