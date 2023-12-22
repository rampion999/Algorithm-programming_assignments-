# Information
Exercise in Algorithm part 2 week 5 "Data Compresstion" lecture.

# Spec.
https://coursera.cs.princeton.edu/algs4/assignments/burrows/specification.php

# Run it
- MoveToFront test.
    - Type `java MoveToFront - < abra.txt | java MoveToFront +` will get "ABRACADABRA!"
- CircularSuffixArray test
    - Type `java CircularSuffixArray` will get
        - index[0] = 11
        - index[1] = 10
        - index[2] = 7
        - index[3] = 0
        - index[4] = 3
        - index[5] = 5
        - index[6] = 8
        - index[7] = 1
        - index[8] = 4
        - index[9] = 6
        - index[10] = 9
        - index[11] = 2
- Burrows–Wheeler test
    - Type `java BurrowsWheeler - < abra.txt | java BurrowsWheeler +` will get "ABRACADABRA!"