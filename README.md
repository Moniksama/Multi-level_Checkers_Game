# Multi-level_Checkers_Game
A position from a multi-level checkers match is given. Also one or more moves description is given. You should compute the final positions of pieces or report that some move is incorrect. In this implementation pieces can move backwards.

Differences with ordinary checkers:

● Captured piece is stored under the capturing tower
instead of being removed from the board.

● If a player captures a tower only the piece on the top
position becomes captured and stored under the capturing tower. 

● A tower can become King. In this case only the piece on
 top position becomes King. 
 
● One tower cannot be captured more than one time during one move.

Exception types:

➢ busy cell -- target cell is occupied by other piece
➢ white cell -- target cell is white
➢ invalid move -- player was able to capture opponent’s piece but made a move without capture.
➢ error -- Some other rule is violated

Input format
1. String with coordinates of white pieces (towers with white piece on the top position)
2. String with coordinates of black pieces (towers with black piece on the top position)
3. List of moves. Each line contains a pair of moves (White’s move, Black’s move)
Tower notation
“xn_aaaaa” (amount of ‘a’ symbols may differ), where:
➢ x -- letter (‘a’ to ‘h’ or ‘A’ to ‘H’ if top-level piece is King).
➢ n -- number from 1 to 8.
➢ aaaaa -- description of the tower from top to bottom. 

Output format
1. String with coordinates of white pieces (towers with white piece on the top position)
2. String with coordinates of black pieces (towers with black piece on the top position)
