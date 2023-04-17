package checkers.multilevel;


public class Board {

    private static final int COLUMNS = 8;
    private static final int ROWS = 8;    

    private Piece board[][];
    private Color currentPlayer;


    Board() {
        this.board = new Piece[ROWS][COLUMNS];
    }


    Board(String position) throws Error {
        int row = getRow(position);
        int column = getColumn(position);
        if (position.charAt(2) != '_')
            throw new Error ("Invalid position format: " + position);

        this.board = new Piece[ROWS][COLUMNS];
        this.board[row][column] = new Piece(position.substring(3));
        checkStatus(row, column, position);

        this.currentPlayer = null;
    }


    Board(String[] positions) throws Error {
        this.board = new Piece[ROWS][COLUMNS];

        for (String position : positions) {
            int row = getRow(position);
            int column = getColumn(position);
            if (position.charAt(2) != '_')
                throw new Error("Invalid position format: " + position);

            if (board[row][column] != null)
                throw new Error("There is already a piece at position: " + position);

            this.board[row][column] = new Piece(position.substring(3));
            checkStatus(row, column, position);
        }

        this.currentPlayer = null;
    }


    private int getColumn(String position) throws Error {
        int column;
        if (Character.isUpperCase(position.charAt(0)))
            column = position.charAt(0) - 'A';
        else
            column = position.charAt(0) - 'a';
        if (column < 0 || column > COLUMNS - 1)
            throw new Error("Invalid column: " + position);
        return column;
    }


    private int getRow(String position) throws Error {
        int row = position.charAt(1) - '1';
        if (row < 0 || row > ROWS - 1)
            throw new Error("Invalid row: " + position);
        return row;
    }


    private void checkStatus(int row, int column, String position) throws Error {
        if (Character.isUpperCase(position.charAt(0))) {
            if (this.board[row][column].status != Status.KING)
                throw new Error("Invalid position format: " + position);
        } else {
            if (this.board[row][column].status != Status.NORMAL)
                throw new Error("Invalid position format: " + position);
        }
    }


    public void movePieces(String[] moves) throws BusyCell, WhiteCell, InvalidMove, Error {
        int posMoveType;

        for (String move: moves) {
            if ( (posMoveType = move.indexOf('-')) >= 0) {
                // Que haya más de un guión
                String[] moveSequence = move.split("-");
                if (moveSequence.length != 2)
                    throw new Error("Invalid move format: " + move);
                if (!moveSequence[0].substring(3).equals(moveSequence[1].substring(3)))
                    throw new Error("Invalid move format (target and origin piece are not the same): " + move);
                makeMove(moveSequence);
            } else if (move.indexOf(':') >= 0) {
                String[] capturesSequence = move.split(":");
                makeCapture(capturesSequence);
            } else
                throw new Error("Invalid move format: " + move);
        }
    }

    private void makeMove(String[] moveSequence) throws BusyCell, WhiteCell, InvalidMove, Error {
        int startRow = getRow(moveSequence[0]);
        int startColumn = getColumn(moveSequence[0]);
        int endRow = getRow(moveSequence[1]);
        int endColumn = getColumn(moveSequence[1]);

        // Comprobar que haya una ficha en el origen
        if (board[startRow][startColumn] == null)
            throw new Error("Empty origin cell");

        if (!moveSequence[0].substring(3).equals(board[startRow][startColumn].toString()))
            throw new Error("Invalid move format (board piece " + board[startRow][startColumn].toString() + " does not match move piece): " + moveSequence[0] + "-" + moveSequence[1]);

        checkMove(startRow, startColumn, endRow, endColumn);
        checkNotPossibleCapture(moveSequence);

        board[endRow][endColumn] = board[startRow][startColumn];
        board[startRow][startColumn] = null;
    }


    private void checkMove(int startRow, int startColumn, int endRow, int endColumn) throws BusyCell, WhiteCell, Error {
        // Comprobar que no haya una ficha en el destino
        if (board[endRow][endColumn] != null)
            throw new BusyCell("Target cell is occupied");

        // Comprobar que la celda destino no sea blanca
        if (endColumn % 2 == 0) {
            if (endRow % 2 != 0)
                throw new WhiteCell("Target cell is white");
        } else {
            if (endRow % 2 == 0)
                throw new WhiteCell("Target cell is white");
        }

        // Comprobar que sea el turno del jugador de esa ficha
        if (currentPlayer == null) {
            currentPlayer = (board[startRow][startColumn].color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        } else {
            if (board[startRow][startColumn].color == currentPlayer) {
                currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
            } else
                throw new Error("It's the turn of the " + currentPlayer + " player.");
        }

        // Comprobar que el movimiento sea válido:
        // Si no es un rey, el destino tiene que estar vecino
        // Si es un rey, el destino tiene que estar en una diagonal sin fichas intermedias
        if (board[startRow][startColumn].status == Status.NORMAL) {
            if ( (endRow != startRow + 1) && (endRow != startRow - 1) &&
                 (endColumn != startColumn + 1) && (endColumn != startColumn - 1) )
                throw new Error("Invalid target cell for a normal piece");
        } else {
            if ( Math.abs(endColumn - startColumn) != Math.abs(endRow - startRow) )
                throw new Error("Invalid target cell for a king piece");

            checkFreeDiagonal(startRow, startColumn, endRow, endColumn);
        }
    }


    private void checkFreeDiagonal(int startRow, int startColumn, int endRow, int endColumn) throws Error {
        int r = Integer.signum(endRow - startRow);
        int c = Integer.signum(endColumn - startColumn);

        for (int i = startRow + r; i < endRow; i += r) {
            for (int j = startColumn + c; j < endColumn; j += c) {
                if (board[i][j] != null)
                    throw new Error("King piece cannot move over a piece");
            }
        }
    }


    private void checkNotPossibleCapture(String[] moveSequence) throws InvalidMove {
        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++)
                if (board[i][j] != null && board[i][j].color == currentPlayer)
                    if (board[i][j].status == Status.NORMAL) {
                        try {
                            if (board[i - 1][j - 1].color != currentPlayer &&
                                board[i - 2][j - 2] == null)
                                throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}

                        try {
                            if (board[i - 1][j + 1].color != currentPlayer &&
                                    board[i - 2][j + 2] == null)
                                throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}

                        try {
                            if (board[i + 1][j - 1].color != currentPlayer &&
                                    board[i + 2][j - 2] == null)
                                throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}

                        try {
                            if (board[i + 1][j + 1].color != currentPlayer &&
                                    board[i + 2][j + 2] == null)
                                throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}
                    } else {
                        try {
                            for (int i1 = i - 1, j1 = j - 1; i1 >= 0 && j1 >= 0; i1--, j1--) {
                                if (board[i1][j1].color == currentPlayer)
                                    break;
                                if (board[i1][j1].color != currentPlayer &&
                                    board[i1 - 1][j1 - 1] == null)
                                    throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}

                        try {
                            for (int i1 = i - 1, j1 = j + 1; i1 >= 0 && j1 < COLUMNS; i1--, j1++) {
                                if (board[i1][j1].color == currentPlayer)
                                    break;
                                if (board[i1][j1].color != currentPlayer &&
                                    board[i1 - 1][j1 + 1] == null)
                                    throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}

                        try {
                            for (int i1 = i + 1, j1 = j - 1; i1 < ROWS && j1 >= 0; i1++, j1--) {
                                if (board[i1][j1].color == currentPlayer)
                                    break;
                                if (board[i1][j1].color != currentPlayer &&
                                    board[i1 + 1][j1 - 1] == null)
                                    throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}

                        try {
                            for (int i1 = i + 1, j1 = j + 1; i1 < ROWS && j1 < COLUMNS; i1++, j1++) {
                                if (board[i1][j1].color == currentPlayer)
                                    break;
                                if (board[i1][j1].color != currentPlayer &&
                                    board[i1 + 1][j1 + 1] == null)
                                    throw new InvalidMove("A capture can and must be made: " + moveSequence[0] + "-" + moveSequence[1]);
                            }
                        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {}
                    }
    }


    private void makeCapture(String[] capturesSequence) throws Error, WhiteCell, BusyCell {
        int startRow = getRow(capturesSequence[0]);
        int startColumn = getColumn(capturesSequence[0]);
        int endRow = 0, endColumn = 0;

        if (!capturesSequence[0].substring(3).equals(board[startRow][startColumn].toString()))
            throw new Error("Invalid move format (board piece " + board[startRow][startColumn].toString() + " does not match move piece): " + capturesSequence[0] + "-" + capturesSequence[1]);

        // Comprobar que sea el turno del jugador de esa ficha
        if (currentPlayer == null) {
            currentPlayer = (board[startRow][startColumn].color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        } else {
            if (board[startRow][startColumn].color == currentPlayer) {
                currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
            } else
                throw new Error("It's the turn of the " + currentPlayer + " player.");
        }

        for (int s = 1; s < capturesSequence.length; s++) {
            endRow = getRow(capturesSequence[s]);
            endColumn = getColumn(capturesSequence[s]);

            checkCapture(startRow, startColumn, endRow, endColumn);
            //checkNotPossibleCapture(capturesSequence);

            int capturedRow = 0, capturedColumn = 0;
            if (board[startRow][startColumn].status == Status.NORMAL) {
                capturedRow = (startRow + endRow) / 2;
                capturedColumn = (startColumn + endColumn) / 2;
            } else {
                int r = Integer.signum(endRow - startRow);
                int c = Integer.signum(endColumn - startColumn);

                boolean found = false;
                for (int i = startRow + r; i < endRow && !found; i += r) {
                    for (int j = startColumn + c; j < endColumn && !found; j += c) {
                        if (board[i][j] != null) {
                            capturedRow = i;
                            capturedColumn = j;
                            found = true;
                        }
                    }
                }
            }

            Piece capturedPiece = board[startRow][startColumn].capturePiece(board[capturedRow][capturedColumn]);
            board[endRow][endColumn] = board[startRow][startColumn];
            board[startRow][startColumn] = null;
            board[capturedRow][capturedColumn] = capturedPiece;

            startRow = endRow;
            startColumn = endColumn;
        }
    }


    private void checkCapture(int startRow, int startColumn, int endRow, int endColumn) throws BusyCell, WhiteCell, Error {
        if (board[endRow][endColumn] != null)
            throw new BusyCell("Target cell is occupied");

        // Comprobar que la celda destino no sea blanca
        if (endColumn % 2 == 0) {
            if (endRow % 2 != 0)
                throw new WhiteCell("Target cell is white");
        } else {
            if (endRow % 2 == 0)
                throw new WhiteCell("Target cell is white");
        }

        // Comprobar que la ficha sea de otro color
        // Comprobar que la captura sea válida:
        // Si no es un rey, la ficha capturada tiene que estar vecino
        // Si es un rey, la ficha capturada tiene que estar en una diagonal sin fichas intermedias
        if (board[startRow][startColumn].status == Status.NORMAL) {
            if ( (endRow != startRow + 2) && (endRow != startRow - 2) &&
                    (endColumn != startColumn + 2) && (endColumn != startColumn - 2) )
                throw new Error("Invalid target cell for a normal piece");
            if (board[(startRow + endRow) / 2][(startColumn + endColumn) / 2] == null)
                throw new Error("Incorrect capture: empty cell");
        } else {
            if ( Math.abs(endColumn - startColumn) != Math.abs(endRow - startRow) )
                throw new Error("Invalid target cell for a king piece");
        }

    }

    public void drawBoard() {
        StringBuilder board = new StringBuilder("--a---b---c---d---e---f---g---h--\n");

        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLUMNS; j++) {
                Piece piece = this.board[i][j];
                String cell = "";

                if (piece == null)
                    cell = "|   ";
                else if (piece.isTower()) {
                    if (piece.color == Color.WHITE) {
                        cell = (piece.status == Status.NORMAL) ? "||o|" : "||O|";
                    } else {
                        cell = (piece.status == Status.NORMAL) ? "||·|" : "||*|";
                    }
                } else if (piece.color == Color.WHITE) {
                    cell = (piece.status == Status.NORMAL) ? "| o " : "| O ";
                } else {
                    cell = (piece.status == Status.NORMAL) ? "| · " : "| * ";
                }

                board.append(cell);
            }

            board.append("|\n");
        }

        board.append("--a---b---c---d---e---f---g---h--");
        System.out.println(board.toString());
    }


    public String toString() {
        StringBuilder boardString = new StringBuilder("");

        for (int c = 0; c < 2; c++)
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    Color color = (c == 0) ? Color.WHITE : Color.BLACK;
                    if (board[i][j] != null && board[i][j].color == color) {
                        if (board[i][j].status == Status.KING)
                            boardString.append((char) ('A' + j));
                        else
                            boardString.append((char) ('a' + j));
                        boardString.append(i + 1);
                        boardString.append("_");
                        boardString.append(board[i][j].toString());
                        boardString.append(" ");
                    }
                }
            }

        boardString.append("\n");
        return boardString.toString();
    }

}
