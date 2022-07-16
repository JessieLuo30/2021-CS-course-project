// Jessi (Jiaxin) Luo, JHED: jluo30
// Eric Corona, JHED: ecorona2
// Siyu Li, JHED: sli187

#include <iostream>
#include "Piece.h"
#include "Bishop.h"
#include <cmath>

namespace Chess {
  bool Bishop::legal_move_shape(std::pair<char, char> start, std::pair<char, char> end) const {

    // four characters represent start and end positions
    char c1 = start.first;
    char c2 = start.second;
    char c3 = end.first;
    char c4 = end.second;
    
    // legal movement check
    if (c1 == c3 && c2 == c4) {
      return false; // not moving
    }
    if (abs(c3 - c1) != abs(c4 - c2)) {
      return false; // not moving diagonally 
    }
    return true;
  }
}
