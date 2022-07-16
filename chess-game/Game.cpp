// Jessi (Jiaxin) Luo, JHED: jluo30
// Eric Corona, JHED: ecorona2
// Siyu Li, JHED: sli187

#include <cassert>
#include "Game.h"
#include "CreatePiece.h"
#include <list>
#include <cmath>

using std::list;
using std::pair;
using std::make_pair;

namespace Chess
{
  /////////////////////////////////////
  // DO NOT MODIFY THIS FUNCTION!!!! //
  /////////////////////////////////////
  Game::Game() : is_white_turn(true) {
    // Add the pawns
    for (int i = 0; i < 8; i++) {
      board.add_piece(std::pair<char, char>('A' + i, '1' + 1), 'P');
      board.add_piece(std::pair<char, char>('A' + i, '1' + 6), 'p');
    }

    // Add the rooks
    board.add_piece(std::pair<char, char>( 'A'+0 , '1'+0 ) , 'R' );
    board.add_piece(std::pair<char, char>( 'A'+7 , '1'+0 ) , 'R' );
    board.add_piece(std::pair<char, char>( 'A'+0 , '1'+7 ) , 'r' );
    board.add_piece(std::pair<char, char>( 'A'+7 , '1'+7 ) , 'r' );

    // Add the knights
    board.add_piece(std::pair<char, char>( 'A'+1 , '1'+0 ) , 'N' );
    board.add_piece(std::pair<char, char>( 'A'+6 , '1'+0 ) , 'N' );
    board.add_piece(std::pair<char, char>( 'A'+1 , '1'+7 ) , 'n' );
    board.add_piece(std::pair<char, char>( 'A'+6 , '1'+7 ) , 'n' );

    // Add the bishops
    board.add_piece(std::pair<char, char>( 'A'+2 , '1'+0 ) , 'B' );
    board.add_piece(std::pair<char, char>( 'A'+5 , '1'+0 ) , 'B' );
    board.add_piece(std::pair<char, char>( 'A'+2 , '1'+7 ) , 'b' );
    board.add_piece(std::pair<char, char>( 'A'+5 , '1'+7 ) , 'b' );

    // Add the kings and queens
    board.add_piece(std::pair<char, char>( 'A'+3 , '1'+0 ) , 'Q' );
    board.add_piece(std::pair<char, char>( 'A'+4 , '1'+0 ) , 'K' );
    board.add_piece(std::pair<char, char>( 'A'+3 , '1'+7 ) , 'q' );
    board.add_piece(std::pair<char, char>( 'A'+4 , '1'+7 ) , 'k' );
  }

  // tries to make a move specified by given positions
  void Game::make_move(std::pair<char, char> start, std::pair<char, char> end) {

    // start and end characters
    char c1 = start.first;
    char c2 = start.second;
    char c3 = end.first;
    char c4 = end.second;

    // check for first three exceptions on the list
    throwTopThreeException(c1, c2, c3, c4);

    // variables related to the piece at the start position
    bool is_start_white = board(start)->is_white();
    char piece_designator = (*board(start)).to_ascii();

    // check for piece colors and turn
    if ((turn_white() && !is_start_white) || (!turn_white() && is_start_white)) {                     
      throw Chess::Exception("piece color and turn do not match");                                     
    }

    // check for other exceptions
    throwBottomFiveException(c1,c2,c3,c4);
    
    // checks if piece should be promoted
    // promotes the piece if this is the case
    if(piece_designator == 'P' || piece_designator == 'p'){
      piece_designator = board.pawn_promotion(start, c4);
    }
    
    // moves the piece and changes the turn
    if(board(end) != nullptr){
      board.remove_piece(end);
    }
    board.add_piece(end, piece_designator);
    board.remove_piece(start);
    is_white_turn = !is_white_turn;
  }

  void Game::throwTopThreeException(char c1, char c2, char c3, char c4) const {
    // first check that positions are valid
    if (c1 < 'A' || c1 > 'H' || c2 < '1' || c2 > '8') {
      throw Chess::Exception("start position is not on board");
    }
    if (c3 < 'A' || c3 > 'H' || c4 < '1' || c4 > '8') {
      throw Chess::Exception("end position is not on board");
    }

    // the start position being checked
    pair<char, char> start = make_pair(c1, c2);

    // must check that a piece exists at the start position before trying to access a piece
    if (board(start) == nullptr) {
      throw Chess::Exception("no piece at start position");
    }
  }

  // throws the bottom five exceptions in the exception list if the move violates the rules
  void Game::throwBottomFiveException(char c1, char c2, char c3, char c4) const {
    
    // the start and end positions being checked
    pair<char, char> start = make_pair(c1, c2);
    pair<char, char> end = make_pair(c3, c4);

    // variables used to check the piece at the start position
    bool legal_capture = board(start)->legal_capture_shape(start, end);
    bool legal_move = board(start)->legal_move_shape(start, end);

    if (board(end) == nullptr && !legal_move) {
      throw Chess::Exception("illegal move shape");
    }

    if (board(end)!= nullptr && (*board(start)).is_white() == (*board(end)).is_white()) {
      throw Chess::Exception("cannot capture own piece");
    }

    if (board(end) != nullptr && !legal_capture) {
      throw Chess::Exception("illegal capture shape");
    }

    if (board(start)->to_ascii() != 'N' && board(start)->to_ascii() != 'n'){
      BoardNotClearException(start,end);
    }

    // create replica game and board
    Game replicaGame = Game();
    replicaGame.board = this->board; 

    // moves piece in replica board
    char piece_identifier = board(start)->to_ascii();
    replicaGame.board.remove_piece(end);
    replicaGame.board.add_piece(end, piece_identifier);
    replicaGame.board.remove_piece(start);

    // checks if the proposed move would expose check
    if (replicaGame.in_check(turn_white())) {
      throw Chess::Exception("move exposes check");
    }    
  }

  // checks if there are pieces blocking the given path
  void Game::BoardNotClearException(std::pair<char, char> start, std::pair<char, char> end) const{

    std::pair<char, char> trace = start;
    char c1 = start.first;
    char c2 = start.second;
    char c3 = end.first;
    char c4 = end.second;

    if ((c1 != c3) && (c2 != c4)) {
      // check for diagonal path
      if (abs(c3 - c1) == abs(c4 - c2)){
        int diagRow = (c3 - c1) / abs(c3 - c1);   // indicate direction
        int diagCol = (c4 - c2) / abs(c4 - c2);
        int i = 1;
        while (trace != end){     // check the path one by one gradually until the end position
          trace = std::pair<char, char>(c1 + i * diagRow, c2 + i * diagCol);
          if (trace != end && board(trace) != nullptr) { 
            throw Exception("path is not clear");
          }
          i++;
        }
      }
    } else if (c4 == c2){
      // check for horizontal path
      int horizontal = (c3 - c1) / abs(c3 - c1);  // indicate direction
      int i = 1;
      while (trace != end){
        trace = std::pair<char, char>(c1 + i * horizontal, c2);
        if (trace != end && board(trace) != nullptr) {
          throw Exception("path is not clear");
        }
        i++;
      }
    } else {
      // check for vertical path
      int vertical = (c4 - c2) / abs(c4 - c2);
      int i = 1;
      while (trace != end){
        trace = std::pair<char, char>(c1, c2 + i * vertical);
        if (trace != end && board(trace) != nullptr) {
          throw Exception("path is not clear");
        }
        i++;
      }
    }
  }

  // makes checks related to making legal moves
  bool Game::MoveLegalCheck(std::pair<char, char> start, std::pair<char, char> end) const{

    // four characters represent start and end positions
    char c1 = start.first;
    char c2 = start.second;
    char c3 = end.first;
    char c4 = end.second;

    // a move is legal if no exceptions are thrown in the following checks
    try {
      throwTopThreeException(c1, c2, c3, c4);
      throwBottomFiveException(c1,c2,c3,c4);
    } catch (const Chess::Exception& ex){
      return false;
    }
    return true;
  }

  // makes checks related to moving a piece for capture
  bool Game::MoveLegalCheckForAttack(std::pair<char, char> start, std::pair<char, char> end) const{
    try {
      
      // used for attack-related checks 
      const Piece* start_piece = board(start);
      bool legal_capture = (*start_piece).legal_capture_shape(start, end);
      const Piece* end_piece = board(end);

      // checks for legal capture and clear path
      if (end_piece != nullptr && !legal_capture) {
	throw Chess::Exception("illegal capture shape");
      }
      if (start_piece->to_ascii() != 'N' && start_piece->to_ascii() != 'n'){
	BoardNotClearException(start,end);
      }
    } catch (const Chess::Exception& ex) {
      return false;
    }
    return true;
  }
  
  // Check if the king is under attack and return a list of threatening position.
  // if the list is empty, then the king is not under attack.
  list<std::pair<char, char>> Game::UnderAttack(bool white) const { 
    
    // stores the enemy pieces in enemy list and finds the king's position
    list<char> enemy;
    list<std::pair<char, char>> king;
    if (white) {
      enemy = {'k','p','r','n','b','q'};
      king = board.findPosition('K');
    } else {
      enemy = {'K','P','R','N','B','Q'};
      king = board.findPosition('k');
    }
    std::pair<char, char> KingPosition = king.front();

    // stores all the threatening opponent locations in threatening
    list<std::pair<char, char>> threatening;
    while (!enemy.empty()){

      // considers one enemy piece type at a time
      char next = enemy.front();
      enemy.remove(next);

      // gets the positions of the piece type being considered
      list<std::pair<char, char>> piecePositions = board.findPosition(next);
      int size = piecePositions.size();

      // iterates through all enemy positions for certain piece type
      for (int i = 0; i < size; i++) {

	// considers one enemy position at a time and prepares to check the next piece
	const std::pair<char, char> enemyPosition = piecePositions.front();
	piecePositions.remove(enemyPosition);

	// enemy position is stored in threatening if enemy piece can attack king
        if (MoveLegalCheckForAttack(enemyPosition, KingPosition)) {
          threatening.push_back(enemyPosition);
        } 
      }
    }
    return threatening;
  }

  // Checks if a king can avoid checkmate.
  // Returns true if there is a legal move/capture that avoids defeat.
  // Returns false if there is no legal move that protects the king.
  bool Game::SolveAttack(bool white) const {

    // stores the friendly pieces in myPiece
    list<char> myPiece;
    if (!white) {
      myPiece = {'k','p','r','n','b','q'};
    } else {
      myPiece = {'K','P','R','N','B','Q'};
    }

    // goes through all pieces and checks if there are any moves the player can make
    while (!myPiece.empty()){
      
      // considers one friendly piece type at a time
      char next = myPiece.front();
      myPiece.remove(next);

      // gets the positions of the piece type being considered
      list<std::pair<char, char>> piecePositions = board.findPosition(next);
      int size = piecePositions.size();

      // for each friendly piece, check if there is a legal move
      for (int i = 0; i < size; i++) {

	// gets the positions of the piece type being considered
        const std::pair<char, char> myfrom = piecePositions.front();
	piecePositions.remove(myfrom);

	// checks if piece under consideration has a legal move by going through all possible squares
	// if successful, the attack can be solved
	for (char i = 'A'; i <= 'H'; i++) {
          for (char j = '1'; j <= '8'; j++) {
            std::pair<char, char> pos = std::make_pair(i, j);
            if (MoveLegalCheck(myfrom, pos)) {
	      return true;
            }
          }
        }
      }
    }
    
    // player has no possible moves
    return false;
  }

  // checks for check situation
  bool Game::in_check(bool white) const {
    // if the king is not under attack, then it is not in check
    if (UnderAttack(white).empty()) return false;
    
    return true;
  }

  // checks for checkmate
  bool Game::in_mate(bool white) const {
    // if the king is not under attack, then it is not in mate
    if (UnderAttack(white).empty()) return false;
   
    // if the king is under attack and we can save the king, then the player is not in mate
    if (SolveAttack(white)) return false;

    return true; // the player lose the game
  }

  // checks for stalemate
  bool Game::in_stalemate(bool white) const {

    // check if any player is in check
    if (!UnderAttack(white).empty() || !UnderAttack(!white).empty()) return false;

    // gets the list of "friendly" pieces 
    list<char> PieceList;
    if (white){
      PieceList = {'K','P','R','N','B','Q'};
    } else {
      PieceList = {'p','r','n','b','q','k'};
    }

    // iterates through each piece and checks if it has any valid moves
    while (!PieceList.empty()){

      // gets a new piece from list and finds where each is located
      char next = PieceList.front();
      PieceList.remove(next);
      list<std::pair<char, char>> from = board.findPosition(next);

      // checks if the current piece has any legal moves by checking each position on the board
      // if a legal move is found, the game is not in stalemate (returns false)
      for (std::pair<char, char> each : from) {
	for (char i = 'A'; i <= 'H'; i++) {
	  for (char j = '1'; j <= '8'; j++) {
	    std::pair<char, char> pos = std::make_pair(i, j);
	    if (MoveLegalCheck(each, pos)) return false;
	  }
        }
      }
    }

    // no possible moves, the game is in stalemate
    return true;
  }

  // overloaded extraction operator
  std::istream& operator>> (std::istream& is, Game& game) {
    // to hold the current character being considered
    char current_char = '\0';

    // new board being created
    Board new_board;
    
    // starts from the top of the board and goes down
    for(char i = '8'; i >= '1'; i--) {
      for(char j = 'A'; j <= 'H'; j++) {

	// current position and piece being considered
	std::pair<char, char> current_pos = make_pair(j, i);

	// consider the current character
        current_char = '\0';
        is >> current_char;

	// a dash signifies no piece is present at the position 
        if(current_char != '-') {
	  new_board.add_piece(current_pos, current_char);
	}
      }
    }

    // gets the last character that says whose turn it is
    current_char = '\0';
    is >> current_char;
    bool is_white = false;
    if(current_char == 'w') {
      is_white = true;
    }

    // updates game with the new board and turn
    game.board = new_board;
    game.is_white_turn = is_white;

    return is;
  }

  /////////////////////////////////////
  // DO NOT MODIFY THIS FUNCTION!!!! //
  /////////////////////////////////////
  std::ostream& operator<< (std::ostream& os, const Game& game) {
    // Write the board out and then either the character 'w' or the character 'b',
    // depending on whose turn it is
    return os << game.get_board() << (game.turn_white() ? 'w' : 'b');
  }
}
