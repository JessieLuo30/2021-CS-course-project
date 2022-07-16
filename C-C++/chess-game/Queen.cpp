// Jessi (Jiaxin) Luo, JHED: jluo30
// Eric Corona, JHED: ecorona2
// Siyu Li, JHED: sli187

#include <iostream>
#include "Piece.h"
#include "Queen.h"
#include <cmath>

namespace Chess
{
  bool Queen::legal_move_shape(std::pair<char, char> start, std::pair<char, char> end) const {

    // four characters represent start and end positions
    char c1 = start.first;
    char c2 = start.second;
    char c3 = end.first;
    char c4 = end.second;
    
    // legal movement check
    if (c1 == c3 && c2 == c4) {
      return false; // not moving
    }
    if (c1 != c3 && c2 == c4) {
      return true; // move horizontally
    }
    if (c1 == c3 && c2 != c4) {
      return true; // move vertically
    }
    if (abs(c1 - c3) == abs(c2 - c4)) {
      return true; // move diagonally
    }
    return false;
  }
}
