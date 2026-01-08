from PredefinedRandom import PredefinedRandom

def rng_order_test(rng):
    N = 3
    dim = 5

    for n in range(N):
        print(f"Trial {n}")
        for d in range(dim):
            print(f"mask[{d}] = {rng.nextDouble()}")
        print(f"jRand = {rng.nextInt(dim)}")
        print()

rng = PredefinedRandom("ceo_random.txt")
rng_order_test(rng)
