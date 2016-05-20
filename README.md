# GameSearch

#### Typesafe AI library for two players perfect knowledge games.

Most of the code of the negamax and the transposition negamax algorithm was adapted from https://github.com/avianey/minimax4j, the Antoine Avianey implementation.

However, this library separates the various parts to be implemented (state and heuristic) from the algorithm itself, which can be used without having to extend one of the implementation classes.

Currently the avaiable algorithms are negamax and negamax with transposition tables, both with alpha-beta pruning.

This adaptation of the Antoine Avianey library was used to implement the AI for Nine Men's Morris.
