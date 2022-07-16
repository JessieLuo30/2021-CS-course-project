#ifndef BOARD_H
#define BOARD_H

#include <iostream>
#include <map>
#include "Piece.h"
#include "Pawn.h"
#include "Rook.h"
#include "Knight.h"
#include "Bishop.h"
#include "Queen.h"
#include "King.h"
#include "Mystery.h"
#include <list>

namespace Chess
{
  class Board {

    // Throughout, we will be accessing board positions using an std::pair<char, char>.
    // The assumption is that the first value is the column with values in
    // {'A','B','C','D','E','F','G','H'} (all caps)
    // and the second is the row, with values in {'1','2','3','4','5','6','7','8'}

  public:
    // Default constructor
    Board();

    // declare a copy constructor --- make a copy of the board
    Board(const Board& cur);

    // destructor
    ~Board();

    // helper function, clear that clean up the board and map
    void clear();

    // helper function, Assignment operator overloading
    Board& operator=(const Board &cur);
    
    // Returns a const pointer to the piece at a prescribed location if it exists,
    // or nullptr if there is nothing there.
    const Piece* operator() (std::pair<char, char> position) const;

    // Attempts to add a new piece with the specified designator, at the given location.
    // Returns false if:
    // -- the designator is invalid,
    // -- the specified location is not on the board, or
    // -- if the specified location is occupied
    bool add_piece(std::pair<char, char> position, char piece_designator);

    // Displays the board by printing it to stdout
    void display() const;

    // Returns true if the board has the right number of kings on it
    bool has_valid_kings() const;
    
    // helper function to remove a piece from the position                 
    void remove_piece(std::pair<char, char> position);

    // promotes piece at position to a queen if it meets certain conditions
    // returns the new piece identifier if applicable
    char pawn_promotion(std::pair<char, char> position, char row);
    
    // helper function to return the position of a specific piece        
    std::list<std::pair<char, char>> findPosition(char target) const;

    // helper function to return the occ map
    std::map<std::pair<char, char>, Piece*> returnOcc() const;

  private:
    // The sparse map storing the pieces, keyed off locations
    std::map<std::pair<char, char>, Piece*> occ;
  };

  // Write the board state to an output stream
  std::ostream& operator<< (std::ostream& os, const Board& board);

}
#endif // BOARD_H

