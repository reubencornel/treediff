# treediff

This library implements the algorithm described by Chawathe et al in the following [paper](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.574.7411&rep=rep1&type=pdf)

## Usage

0. Implement the `Node` interface for nodes in your tree
1. Implement a `MatchingCriteria` based on the tree you want to diff.
2. Instantiate an instance of `DefaultMatch`.
3. Calculate a matching by calling `calculateMatching` on the DefaultMatch instance.
4. Instantiate an instance of `EditScriptAlgorithm` passing in the matching obtained in the previous step and an instance of `SimpleLCSAlgorithm`
5. Call `calculateEditScript` to get a the edit script for your tree

Look at `EditScriptAlgorithmTest.case1()` for an example of what your code should look like. 