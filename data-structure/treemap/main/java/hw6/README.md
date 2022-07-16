# Discussion

## Part A: Test-first development

I chose to test insert functions with both cases with no rotations and cases with rotations including right, left, left-right, and right-left rotations. I encountered difficulty that the structure rotate when it didn't. The balance factor, from where I saw it, was 1 but it rotate to the left. Later I figured that it's becuase I didn't update the height of left node, they are still 1 which caused bf to be one larger.

To test remove, I chose remove leaf, remove one child, and remove two child because I want to cover and test all possible cases. To be specific, for remove one child, this is what the tree used to be

15:a

12:b 19:c

03:a 13:b 16:c null

Notice, "03" has to be "03" not "3" because I'm comparing string here. 19 has one child 16. After remove("19");, the tree become

15:a

12:b 16:c

03:a 13:b null null

19 is successfully removed.



## Part D: Benchmarking

[hotel_california.txt] Total time: 00:04:07

Score:

arrayMap    0.422

avlTreeMap  0.309          

bstMap      0.553          

treapMap    0.401


[moby_dick.txt] Total time: 00:04:24
Score:

arrayMap    4145.804

avlTreeMap  257.431

bstMap      263.111

treapMap    278.874 

[pride_and_prejudice.txt] Total time: 00:04:10
Score:

arrayMap    1037.545

avlTreeMap  149.477

bstMap      147.758

treapMap    177.471 

[federalist01.txt] Total time: 00:04:06
Score:

arrayMap    4.135

avlTreeMap  1.865

bstMap      1.687

treapMap    2.215 


Generally, for different files, file with longer length has higher test score.
Scores for hotel_california.txt are <1, for moby_dick.txt are around 200-4000, for pride_and_prejudice.txt are around 100-1000, for federalist01.txt are around 1-4, corresponding to moby_dick.txt is longer than pride_and_prejudice.txt, which is much longer than federalist01.txt, and federalist01.txt is longer than hotel_california.txt.

It makes sense because to build a Map that contains a frequency count on all the "words" that occur in an input (text) file, it took more effort to map larger file since they have more words and probably higher frequency for each word. File with more words took more operations to add each word and update word count accordingly.

However, the total time token for each file is roughly the same, 00:04:07, 00:04:24, 00:04:10, 00:04:06. I believe this is because the key operations took a little amount of time while ohter basic stuffs set in the JmhRuntimeTest like warm-up determined the length

To test my assumption, I double the length of federalist01.txt by making another copy of it, here's the result

[federalist02.txt] Total time: 00:04:07
Score:

arrayMap    8.663

avlTreeMap  3.166

bstMap      3.859

treapMap    4.116 

The total time used is almost exactly the same while the scores are approximately doubled.

For different maps on the same file, except for hotel_california.txt, other files shows much higher score for arrayMap, and others similar with treapMap a little bit higher. For example,
for pride_and_prejudice.txt., score for arrayMap is 1037.545, for avlTreeMap is 149.477, for bstMap is 147.758, for treapMap is 177.471. This is because arrayMap uses linear search and is deliberately made inefficient so all operations take O(n) time, while the other three run in binary search. All core operations in avlTreeMap, bstMap, and treapMap use recursion and run in O(lgn) worst-case time.
I guess it didn't make big difference for hotel_california.txt because it's too short to tell their efficiency.

In the test of all these files, avlTreeMap always has slightly lower score than treapMap, which means avl tree is more efficient than treap in these situations.
My reason for this is the height of the treap is O(lgn) in the best case, O(n) in the worst case, and O(lgn) in the average case; while for avl tree, which is a balanced binary search tree, it is guaranteed that the height of the tree is O(lgN). So in average, avl tree have less height than treap when mapping the same file. Therefore, it took treap more effort to search down more levels of height and get to the target, resulting in higher score.