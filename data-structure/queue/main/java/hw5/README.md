# Discussion

## Flawed Deque
1.InsertBack(T t) does not insert when the array is full and perform grow().

unit tests:testLengthDelete(); testLengthBack(); testMultiple(); testInsertBack();testRemoveBack()

guess: There is a if statement in insertBack which say when the array is full, the function grow(). But this if case forget to put the new element in the grown array before exiting the function.

2.back() throws exceptions.LengthException instead of EmptyException when empty() == true.

unit tests:backEmptyException()

guess: Throws LengthException in the implementation of back() when empty() == true.


## Hacking Linear Search
MoveToFrontLinkedSet:

1. The remove method remove the first encountered target node in the list and if there is no target node nothing changes.

2. size() doesn't work correctly because there is no "numElements++;" in append.

TransposeArraySet:

before test.remove("4"):
9 3 12 11 4 7 null null

after test.remove("4"):
9 3 12 7 11 null null null

after test.remove("3"):
11 9 7 12 null null null null

The remove() operation not only remove the element at the position, it put the element at the end of the list in the position one place before the target's original position.

This is because remove() use find(), which put the target one step forward once found. Then remove() replace the "moved-one-step-forward" target with the last element in the array.
## Profiling

Setup:

Adds integers from 0 to 999 to the array "data" and then shuffles "data", resulting in a random permutation of natural numbers smaller than 1000. 

Adds integers 1000 to the array "same" 1000 times, resulting in an array containing a thousand same integer 1000.


Experiment:

Adds all the values from the "data" field into the set argument passed to it.

Inserts all the values from the "same" field into the set argument passed to it, notice only the first time 1000 is going to be added successfully because set doesn't change if we try to insert an existing value, but everytime has(t) and find() is performed.

For moveToFront Benchmark, the second set.insert(numSame); moves integer 1000 to the head of a list so it is found much faster for the following times.
For transposeSequence Benchmark, each set.insert(numSame); after the first one moves integer 1000 one step forward, so it is found a little bit faster for the following times.

Then it performs insert(i);insert(i);has(i);remove(i); for the first 400 elements in the array, each operation contains find() which is related with heuristic. 
In the end, perform has(600);has(700);has(800); 500 times on the set, mechanism similar as above, trying to make the difference more apparent.

The resulting scores for each benchmark:

arraySet: 3.055

linkedSet: 5.716      

moveToFront: 1.566  

transposeSequence: 1.877

As we can see from the data, compared to using ArraySet and LinkedSet, moveToFront and transposeSequence heuristic are much more efficient.