from abc import ABC, abstractmethod
from numpy import ndarray


class Problem(ABC): # Abstract class so it cannot be instantiated
    d: int # Dimension
    lowerBound: int | float | ndarray
    upperBound: int | float | ndarray
    name: str

    def __init__(self, d, lowerBound, upperBound, name):
        self.d = d
        self.lowerBound = lowerBound
        self.upperBound = upperBound
        self.name = name

    @abstractmethod
    def evaluate(self, x):
        pass

    @abstractmethod
    def generateRandomSolution(self):
        pass