// Jessi (Jiaxin) Luo, JHED: jluo30
// Eric Corona, JHED: ecorona2
// Siyu Li, JHED: sli187

#include <iostream>
#include <utility>
#include <map>
#include "Terminal.h"
#include "Board.h"
#include "CreatePiece.h"
#include <set>
#include <vector>

using std::cout;
using std::make_pair;
using std::pair;
using std::list;

namespace Chess
{
  /////////////////////////////////////
  // DO NOT MODIFY THIS FUNCTION!!!! //
  /////////////////////////////////////
  Board::Board(){}

  Board::Board(const Board& cur) { // copy constructor
    *this = cur; // calling the =operator
  }

  Board::~Board() { // destructor
    this->clear(); // calling clear function
  }

  void Board::clear() { // clears board

    // gets the position keys for the occ map
    std::vector<std::pair<char,char>> posit;
    std::map<std::pair<char, char>, Piece*>::iterator itt = this->occ.begin();
    while(itt != this->occ.end()) {
      posit.push_back(itt->first);
      ++itt;
    }

    // clears board by deallocating memory for the pieces stored in occ map
    for (unsigned i =0; i < posit.size(); i++) {
      delete occ[posit[i]];
      occ.erase(posit[i]);
    }
  }

  // assignment operator to copy the board
  Board& Board::operator=(const Board &cur) {

    // clears board
    this->clear();

    // iterates through cur board
    std::map<std::pair<char, char>, Piece*>::const_iterator it = cur.occ.cbegin();
    while(it != cur.occ.cend()) {
      // create the copied Pieces from cur
      char piece_designator = (it->second)->to_ascii();
      this->occ[it->first] = create_piece(piece_designator); 
      ++it;
    }
    return *this;
  }

  // returns the piece at the given position by using the board's occ map
  const Piece* Board::operator()(std::pair<char, char> position) const {
    std::map<std::pair<char, char>, Piece*> occ_copy = (*this).returnOcc();
    return occ_copy[position];
  }

  // attempts to add a piece to the board at a given position and piece designator
  // returns a boolean that indicates whether a piece was successfully added to the board
  bool Board::add_piece(std::pair<char, char> position, char piece_designator) {

    // used for checks
    std::set<char> pieces = {'K','k','Q','q','B','b','N','n','R','r','P','p','M','m'};
    char c1 = position.first;
    char c2 = position.second;
   
    // checks whether a piece can be added at the given position
    if (pieces.find(piece_designator) == pieces.end()) { 
      return false;  // invalid designator
    }
    if (c1 < 'A' || c1 > 'H' || c2 < '1' || c2 > '8') {
      return false;  // not on board
    }
    if (occ[position] != nullptr) { 
      return false;  // the specified location is occupied
    }

    // piece is added after passing checks
    occ[position] = create_piece(piece_designator);
    return true;
  }

  // displays chess board
  void Board::display() const {
    // uses copy of the occ to access the board members
    std::map<std::pair<char, char>, Piece*> occ_copy = (*this).returnOcc();

    // displays the top rows of the chess board with added color
    cout << "    ";
    Terminal::color_fg(false, Terminal::BLACK);
    Terminal::color_bg(Terminal::WHITE);
    cout << " A  B  C  D  E  F  G  H ";
    Terminal::set_default();
    cout << std::endl;
    Terminal::color_fg(false, Terminal::BLACK);
    Terminal::color_bg(Terminal::WHITE);
    cout << "     ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯";
    Terminal::set_default();
    cout << std::endl;

    // goes through the whole board and displays each piece (if applicable)
    for(char r = '8'; r >= '1'; r--) {
      // adds color for each row
      Terminal::color_fg(false, Terminal::BLACK);
      Terminal::color_bg(Terminal::WHITE);
      cout << r << " ｜";

      for(char c = 'A'; c <= 'H'; c++) {
        // gets current position and accesses piece at that position
        pair<char, char> get = make_pair(c, r);
        const Piece* piece = occ_copy[get];

        // outputs piece designator to the board if one exists
        // else displays a dash
        if (piece) {
          cout << " " << piece->to_ascii() << " ";
        } else {
          cout << " - ";
        }
      }
      // only applies color to the chess board
      Terminal::set_default();
      cout << std::endl;
    }
  }

  // checks if a pawn should be promoted, promotes if applicable 
  char Board::pawn_promotion(std::pair<char, char> position, char row){
    
    // used to check the conditions and update the piece if necessary
    const Piece* current_piece = (*this)(position);
    char piece_designator = (*current_piece).to_ascii();
    bool white_piece = (*current_piece).is_white();
    
    // checks if the pawn should be promoted
    // must be a pawn moving to the last row (row 8 for white, row 1 for black)
    // in either pawn promotion case, the pawn is removed and replaced with a queen 
    if(white_piece && (row == '8')){
      piece_designator = 'Q';
      (*this).remove_piece(position);
      (*this).add_piece(position, piece_designator);
    }

    if(!white_piece && (row == '1')){
      piece_designator = 'q';
      (*this).remove_piece(position);
      (*this).add_piece(position, piece_designator);
    }

    // returns possibly updated piece designator
    return piece_designator;
  }

  // checks that both players still have kings on the board
  bool Board::has_valid_kings() const {
    return (!findPosition('k').empty() && !findPosition('K').empty());
  }

  // deletes a piece in the given position
  void Board::remove_piece(std::pair<char, char> position){
    delete occ[position];
    occ.erase(position);
  }

  // returns a list of positions in the board for a target piece designator
  list<std::pair<char, char>> Board::findPosition(char target) const {
    list<std::pair<char, char>> position_list;

    // tries to match the piece designator at the current position with the target piece identifier
    for (std::map<std::pair<char, char>, Piece*>::const_iterator it = occ.cbegin(); it != occ.cend(); ++it){
      if (it->second->to_ascii() == target){
        position_list.push_back(it->first);
      }
    }
    return position_list;
  }

  // returns the occ map
  std::map<std::pair<char, char>, Piece*> Board::returnOcc() const {
    return occ;
  }

  /////////////////////////////////////
  // DO NOT MODIFY THIS FUNCTION!!!! //
  /////////////////////////////////////
  std::ostream& operator<<(std::ostream& os, const Board& board) {
    for(char r = '8'; r >= '1'; r--) {
      for(char c = 'A'; c <= 'H'; c++) {
        const Piece* piece = board(std::pair<char, char>(c, r));
        if (piece) {
          os << piece->to_ascii();
        } else {
          os << '-';
        }
      }
      os << std::endl;
    }
    return os;
  }
}
