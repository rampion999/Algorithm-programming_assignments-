# Information
Exercise in Algorithm part 1 week 1 "Union Find" lecture.

# Spec.
https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php

# Run it
- `java PercolationStats {n} {T}` performs T independent computational experiments (discussed above) on an n-by-n grid, and prints the sample mean, sample standard deviation, and the 95% confidence interval for the percolation threshold.

- `java PercolationStats 200 100` will show
mean                    = 0.5929934999999997
stddev                  = 0.00876990421552567
95% confidence interval = [0.5912745987737567, 0.5947124012262428]

- `java PercolationStats 2 10000` will show
mean                    = 0.666925
stddev                  = 0.11776536521033558
95% confidence interval = [0.6646167988418774, 0.6692332011581226]