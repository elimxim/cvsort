# Sorting Algorithm Comparator & Visualizer

Console program for fun with sorting algorithms.

![logo](.img/logo.png)

Contains two main commands:
- **compare:** compares up to 10 sorting algorithms and shows their complexity and statistics
- **visualise:** shows algorithm complexity, pseudocode and/or visualisation with statistics

_BubbleSort_ visualisation:

![BubbleSort visualisation](.img/bubble.gif)

Output of the visualisation command for _BubbleSort_:

```shell
      _____   _____  ___  ___ _____
     / __\ \ / / __|/ _ \| _ \_   _|
    | (__ \ V /\__ \ (_) |   / | |
     \___| \_/ |___/\___/|_|_\ |_|
_________________________________________
Sorting Algorithm Comparator & Visualizer

┌───────────────┬───────────────┬───────────────┬───────────────┬──────────────┐
│Algorithm      │Worst time     │Average time   │Best time      │Space         │
├───────────────┼───────────────┼───────────────┼───────────────┼──────────────┤
│BubbleSort     │O(n^2)         │Θ(n^2)         │Ω(n)           │O(1)          │
└───────────────┴───────────────┴───────────────┴───────────────┴──────────────┘

for i in [0..n) do
    swapped = false
    for j in [1..n-i) do
        if array[j-1] > array[j] then
            swap array[j] and array[j+1]
            swapped = true
        end
    end

    if not swapped then
        break
    end
end

                                ▒▒
                              ▒▒▒▒
                            ▒▒▒▒▒▒
                          ▒▒▒▒▒▒▒▒
                        ▒▒▒▒▒▒▒▒▒▒
                      ▒▒▒▒▒▒▒▒▒▒▒▒
                    ▒▒▒▒▒▒▒▒▒▒▒▒▒▒
                  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
                ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
              ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
            ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
          ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
        ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
      ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
    ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒

<BubbleSort>
 iterations   121  comparisons  110
 array reads/writes  324/104  ratio  3.12
 array swaps  52
```

Supported sorting algorithms:

- BubbleSort
- SelectionSort
- InsertionSort