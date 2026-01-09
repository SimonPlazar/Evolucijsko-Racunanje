import numpy as np

class PredefinedRandom:
    def __init__(self, path):
        self.numbers = np.loadtxt(path, dtype=np.float64)
        self.size = self.numbers.size
        self.index = 0

        if self.size == 0:
            raise ValueError("Random number file is empty")

        print(f"PredefinedRandom initialized with {self.size} numbers from '{path}'")

    # -------------------------------------------------
    # Core
    # -------------------------------------------------
    # def _next(self):
    #     if self.index >= self.size:
    #         raise RuntimeError(
    #             f"RNG exhausted at index {self.index} (size={self.size})"
    #         )
    #     val = self.numbers[self.index]
    #     self.index += 1
    #     return val
    def _next(self):
        # if not self.numbers:
        #     raise RuntimeError("RNG has no predefined numbers")
        val = self.numbers[self.index]
        self.index = (self.index + 1) % len(self.numbers)
        return val

    # -------------------------------------------------
    # Java Random.nextDouble()
    # -------------------------------------------------
    def nextDouble(self):
        return self._next()

    # -------------------------------------------------
    # Java Random.nextInt(bound)
    # -------------------------------------------------
    def nextInt(self, bound):
        if bound <= 0:
            raise ValueError("bound must be positive")
        return int(self._next() * bound)

    # -------------------------------------------------
    # Java Random.shuffle(int[])
    # Fisher–Yates
    # -------------------------------------------------
    def shuffle(self, array):
        for i in range(len(array) - 1, 0, -1):
            j = self.nextInt(i + 1)
            if i != j:
                array[i], array[j] = array[j], array[i]

    # -------------------------------------------------
    # Java-style random permutation
    # -------------------------------------------------
    def randomPermutation(self, length):
        perm = list(range(length))
        self.shuffle(perm)
        return perm

    # -------------------------------------------------
    # Utilities (optional, but useful)
    # -------------------------------------------------
    def remaining(self):
        return self.size - self.index

    def reset(self):
        self.index = 0
