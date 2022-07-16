CONSERVATIVE_FLAGS = -std=c++11 -Wall -Wextra -pedantic
DEBUGGING_FLAGS = -g -O0
CFLAGS = $(CONSERVATIVE_FLAGS) $(DEBUGGING_FLAGS)

all: cTrieTest tTrieTest wordHunt

cTrieTest: CTrieTest.o CTrie.o
	g++ -o cTrieTest CTrieTest.o CTrie.o

cTrieTest.o: CTrieTest.cpp CTrie.h
	g++ -c CTrieTest.cpp $(CFLAGS)

tTrieTest: TTrieTest.o
	g++ -o tTrieTest TTrieTest.o

tTrieTest.o: TTrieTest.cpp TTrie.h Trie.inc
	g++ -c TTrieTest.cpp Trie.inc $(CFLAGS)

wordHunt: wordHuntMain.o CTrie.o WordHunt.o
	g++ -o wordHunt wordHuntMain.o CTrie.o WordHunt.o

wordHunt.o: WordHunt.cpp WordHunt.h
	g++ -c WordHunt.cpp $(CFLAGS)

wordHuntMain.o: wordHuntMain.cpp
	g++ -c wordHuntMain.cpp $(CFLAGS)

CTrie.o: CTrie.cpp CTrie.h
	g++ -c CTrie.cpp $(CFLAGS)

clean:
	rm -f cTrieTest tTrieTest wordHunt *.o main?  *~
