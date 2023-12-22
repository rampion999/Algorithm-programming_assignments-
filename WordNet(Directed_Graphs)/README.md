# Information
Exercise in Algorithm part 2 week 5 "Directed Graphs" lecture.

# Spec.
https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php

# Run it
- `java SAP digraph1.txt` to start SAP find. Use Std input to be the parameter.
    - `3 11` will get "length = 4, ancestor = 1"
    - `9 12` will get "length = 3, ancestor = 5"
    - `7 2` will get "length = 4, ancestor = 0"
    - `1 6` will get "length = -1, ancestor = -1"

- `java Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt` To return the most unrelavent in the sets.