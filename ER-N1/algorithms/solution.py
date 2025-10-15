from numpy import ndarray


class Solution:
    position: ndarray
    fitness: float | int

    def __init__(self, position: ndarray, fitness: float | int):
        self.position = position
        self.fitness = fitness

    def __lt__(self, other) -> bool:
        if isinstance(other, Solution):
            return self.fitness < other.fitness
        elif isinstance(other, (float, int)):
            return self.fitness < other
        return NotImplemented

    def __le__(self, other) -> bool:
        if isinstance(other, Solution):
            return self.fitness <= other.fitness
        elif isinstance(other, (float, int)):
            return self.fitness <= other
        return NotImplemented
