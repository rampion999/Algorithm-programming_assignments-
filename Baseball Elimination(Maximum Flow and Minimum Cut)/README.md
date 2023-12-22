# Information
Exercise in Algorithm part 2 week 3 "Maximum Flow And Minimum Cut" lecture.

# Spec.
https://coursera.cs.princeton.edu/algs4/assignments/baseball/specification.php

# Run it
- `java BaseballElimination teams4.txt` will show<br>
Atlanta is not eliminated<br>
Philadelphia is eliminated by the subset R = { Atlanta New_York }<br>
New_York is not eliminated<br>
Montreal is eliminated by the subset R = { Atlanta }<br>

- `java BaseballElimination teams5.txt` will show<br>
New_York is not eliminated<br>
Baltimore is not eliminated<br>
Boston is not eliminated<br>
Toronto is not eliminated<br>
Detroit is eliminated by the subset R = { New_York Baltimore Boston Toronto }