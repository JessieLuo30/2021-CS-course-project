#ifndef GAME_H
#define GAME_H

#include <list>
#include <iostream>
#include "Piece.h"
#include "Board.h"
#include "Exceptions.h"

namespace Chess
{

  class Game {

  public:
    // This default constructor initializes a board with the standard
    // piece positions, and sets the state to white's turn
    Game();
    
    // Returns a constant reference to the board
    /////////////////////////////////////
    // DO NOT MODIFY THIS FUNCTION!!!! //
    /////////////////////////////////////
    const Board& get_board() const { return board; }

    // Returns true if it is white's turn
    /////////////////////////////////////
    // DO NOT MODIFY THIS FUNCTION!!!! //
    /////////////////////////////////////
    bool turn_white() const { return is_white_turn; }

    // Attemps to make a move. If successful, the move is made and
    // the turn is switched white <-> black. Otherwise, an exception is thrown
    void make_move(std::pair<char, char> start, std::pair<char, char> end);

    // Returns true if the designated player is in check
    bool in_check(bool white) const;

    // Returns true if the designated player is in mate
    bool in_mate(bool white) const;

    // Returns true if the designated player is in mate
    bool in_stalemate(bool white) const;
  
    // Reads the board in from a stream
    friend std::istream& operator>> (std::istream& is, Game& game);

  private:
    // The board
    Board board;

    // Is it white's turn?
    bool is_white_turn;

    // check if the king is under attack
    std::list<std::pair<char, char>> UnderAttack(bool white) const;

    // check if a piece is under attack
    std::list<std::pair<char, char>> pieceUnderAttack(bool white, std::pair<char,char> position) const;
    
    // check if the king can be saved from attack
    bool SolveAttack(bool white) const;
    
    // check for top three exceptions on the list and throw exception
    void throwTopThreeException(char c1,char c2,char c3,char c4) const;

    // check for bottom five exceptions on the list and throw exception
    void throwBottomFiveException(char c1, char c2, char c3, char c4) const;
    
    // check whether move is legal without considering whose turn and whether it match, don't throw exception, return bool
    bool MoveLegalCheck(std::pair<char, char> start, std::pair<char, char> end) const;

    // check whether there's a piece in between start and end position, throw "path is not clear" exception
    void BoardNotClearException(std::pair<char, char> start, std::pair<char, char> end) const;

    // helper function for UnderAttack(), check for specific exceptions, but don't throw any, return bool 
    bool MoveLegalCheckForAttack(std::pair<char, char> start, std::pair<char, char> end) const;
  };

  // Writes the board out to a stream
  std::ostream& operator<< (std::ostream& os, const Game& game);

}
#endif // GAME_H
