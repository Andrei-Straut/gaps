GAPS (*Genetic Algorithm Path Search*)
==============
*(Documentation Work in Progress)*

What is GAPS?
--------------
A webapp started as a small side project doing path searches in graphs using genetic algorithms

How does it work?
--------------
For a description on genetic algorithms, [Wikipedia has it (loosely) covered](http://en.wikipedia.org/wiki/Genetic_algorithm).

Just like any standard genetic algorithm. Picks a random number of initial paths, then it combines and mutates them until a good solution / stop condition is reached.

How well does it perform?
--------------
For small graphs (30 - 100 nodes, 100 - 1000 edges), in ~10% of cases it outperforms [JGraphT's](https://github.com/jgrapht/jgrapht) KShortestPath algorithm when it concerns path costs, and it comes close in terms of runtime performance.

What are the future plans?
--------------
Please check the [issues page](https://github.com/Andrei-Straut/gaps/issues) for a list of upcoming features.