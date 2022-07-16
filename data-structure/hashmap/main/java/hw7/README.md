# Discussion
OpenAddressingHashMap() is a better choice to be used for the search engine.
The benchmarking data for my final implementations for both mappings are

[time]

file ChainingHashMap  OpenAddressingHashMap

apache.txt  481.472   291.445

jhu.txt  0.499   0.382

joanne.txt 0.226  0.150

newegg.txt   200.139  169.164

random164.txt  1227.295  1153.372

urls.txt  0.038  0.032

[space]

apache.txt   958087520.000   85432560.000

jhu.txt 23844964.000   21147788.000

joanne.txt  23772224.000   20049368.000

newegg.txt  105705400.000    78450640.000

random164.txt  1057898520.000  1030926664.000

urls.txt  23290504.000  18191364.000

The final implementation is the most efficient way I generated for each file so far. As we see, OpenAddressingHashMa (the 3rd columns) have overly lower scores on both time and space, which means it takes less time and space and it more efficient.
It's expected that open addressing use less space because chaining will need a pointer in each node.

During operation like insert, ChainingHashMap generally takes more operations because it needs to access pointer and get to the target, while for OpenAddressingHashMap, when there is no collision, can immediately obatin the target value through index. Therefore, I expect OpenAddressingHashMap to be faster and takes less time.

Below are all benchmarking data, results and analysis that contributed to my final decision.

# ChainingHashMap()
I used linear probing because it's easier to compute and therefore save some operations.
I tried two table size versions for ChainingHashMap(). At first, I just set the initial value to 17 and double the table capacity when the table needs to grow.
The benchmarking data of this version is as following:

[time]

apache.txt  481.472

jhu.txt  0.499

joanne.txt 0.226   

newegg.txt   200.139 

random164.txt  1227.295 

urls.txt  0.038

[space]

apache.txt   958087520.000

jhu.txt 23844964.000

joanne.txt  23772224.000

newegg.txt  105705400.000 

random164.txt  1057898520.000  

urls.txt  23290504.000

With respect to both time and space, the scores for urls.txt is lowest, then joanne.txt, then jhu.txt, ..., the highest is random164.txt, corresponding to data file size. It's reasonable because large files took more time to process and took up more space.

Something I didn't expect is that the difference between the space score of very large file (random164.txt) and very small file (urls.txt) is not that big. Space score for random164.txt is approximately 50 times (1057898520.000) as big as space score for urls.txt (23290504.000), but random164.txt's length is much longer than 50 times urls.txt's length.
On the other hand, the difference in time (1227.295 for random164.txt and 0.038 for urls.txt) is more reasonable. Therefore, file length effect the space it used to some degree but doesn't make that large difference.

Later, I remembered on class we talked about this strategy can bias the entries in the table to only occupy the even indices therefore only half the table positions may be used.
Therefore, to minimize collisions, I tabulate primes and use it as a look-up table to pick the table size. The benchmarking data of the prime size version is as following:

[time]

apache.txt  544.598    

jhu.txt  0.631

joanne.txt  0.361

newegg.txt  290.137 

random164.txt 2560.132

urls.txt  0.070           

[space]

apache.txt  151920176.000 

jhu.txt 24189492.000     

joanne.txt  23427752.000  

newegg.txt  149013888.000  

random164.txt  1191263728.000   

urls.txt  24828600.000    

The result is a bit surprising.

The space scores in both versions are similar (prime version slightly higher), but the prime number version has a higher time score, which means the search engine become less efficient.
I guess it's because for "prime" version, the amount of empty blocks each time the grow() function make is less than the double version, so it needs to grow more times for the same data file, taking up more time and space.
So I decide the double size rehashing version is more efficient for ChainingHashMap and make it the final one.

# OpenAddressingHashMap()
Dut to previous attempt on ChainingHashMap, for OpenAddressingHashMap I also test two table size versions.

prime size version:

[time]

apache.txt 585.158

jhu.txt  0.473

joanne.txt 0.190

newegg.txt  148.770

random164.txt 1124.290

urls.txt 0.052

[space]

apache.txt  94855988.000  

jhu.txt  23959344.000  

joanne.txt  23821220.000 

newegg.txt  62525364.000 

random164.txt 1033083652.000   

urls.txt  22865996.000

________________________
double size version:

[time]

apache.txt  312.673   

jhu.txt  0.432 

joanne.txt   0.178 

newegg.txt   169.076 

random164.txt  1712.996  

urls.txt  0.045        

[space]

apache.txt   177840696.000  

jhu.txt  24051852.000     

joanne.txt  23852140.000  

newegg.txt 97622232.000   

random164.txt  1018065536.000 

urls.txt  22971192.000

This time using prime number as sizes has overly lower scores for both time and space and is therefore more efficient. But the same issue persists.
I was using prime number 2 as the starting size and I suspect it's too small and will waste unwanted time and space at the beginning. Therefore, I adjusted the starting size to a larger one 47. Here's the benchmarking data.

[time]
apache.txt  291.445

jhu.txt   0.382   

joanne.txt  0.150

newegg.txt  169.164

random164.txt   1153.372

urls.txt  0.042

[space]

apache.txt  85432560.000

jhu.txt 21147788.000

joanne.txt   20049368.000

newegg.txt    78450640.000  

random164.txt  1030926664.000

urls.txt  18191364.000 

As I expected, the engine is more efficient on both time and space perspectives. Having size 47 as starting size save time and space for serveral rehashing at the beginning. So this is the final version I choose.