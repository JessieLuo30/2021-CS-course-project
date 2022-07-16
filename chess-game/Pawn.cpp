// Jessi (Jiaxin) Luo, JHED: jluo30
// Eric Corona, JHED: ecorona2
// Siyu Li, JHED: sli187

#include <iostream>
#include "Piece.h"
#include "Pawn.h"
#include <cmath>

namespace Chess
{
  bool Pawn::legal_move_shape(std::pair<char, char> start, std::pair<char, char> end) const{

    // four characters represent start and end positions
    char c1 = start.first;
    char c2 = start.second;
    char c3 = end.first;
    char c4 = end.second;
    
    // checks if the pawn can move two spaces (if the pawn is at the starting position)
    int n = 1;
    if ((c2 == '2' && is_white()) ||(c2 == '7' && !is_white())) {
      n = 2;
    }

    // legal movement check
    if ((is_white() && (c4 < c2)) || (!is_white() && (c4 > c2))) {
      return false; // pawn is not moving in right direction
    }
    if (c1 == c3 && c2 == c4) {
      return false; // not moving
    }
    if (c1 != c3 || abs(c2 - c4) > n) {
      return false; // moving more than 1 or 2 spaces forward
    }
    return true;
  }

  bool Pawn::legal_capture_shape(std::pair<char, char> start,std::pair<char, char> end) const {

    // four characters represent start and end positionss
    char c1 = start.first;
    char c2 = start.second;
    char c3 = end.first;
    char c4 = end.second;

    if (is_white()) {
      // if the current piece is a white piece
      // the capture is invalid when the end is not at the upper left or right corner by 1*1 square
      if(abs(c1 - c3) != 1 || (c4 - c2) != 1) {
	return false;
      }
    } else {
      // if the current piece is a black piece
      // the capture is invalid when the end is not at the lower left or right corner by 1*1 square
      if(abs(c1 - c3) != 1 || (c4 - c2) != -1) {
	return false;
      }
    }
    return true;
  }
}
