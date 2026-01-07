import numpy as np

np.random.seed(1337)

NUM = 50_000_000
rand = np.random.rand(NUM)

np.savetxt("ceo_random.txt", rand, fmt="%.17f")
