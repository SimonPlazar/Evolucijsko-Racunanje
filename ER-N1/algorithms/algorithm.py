from abc import ABC, abstractmethod

from problems.problem import Problem


class Algorithm(ABC):  # Abstract class so it cannot be instantiated
    @abstractmethod
    def execute(self, problem: Problem, maxFes: int):
        pass
