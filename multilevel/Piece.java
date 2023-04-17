package checkers.multilevel;


public class Piece {

    public Color color;
    public Status status;

    private Tower tower;


    Piece(Color color) throws Error {
        this.color = color;
        this.status = Status.NORMAL;
        this.tower = new Tower();
    }


    Piece(Color color, Status status) throws Error {
        this.color = color;
        this.status = status;
        this.tower = new Tower();
    }


    Piece(String pieceDescription) throws Error {
        if (pieceDescription.equals(""))
            throw new Error("Cannot create a blank piece");

        char pieceCode = pieceDescription.charAt(0);
        if (pieceCode == PieceType.w.code()) {
            this.color = Color.WHITE;
            this.status = status.NORMAL;
        } else if (pieceCode == PieceType.W.code()) {
            this.color = Color.WHITE;
            this.status = status.KING;
        } else if (pieceCode == PieceType.b.code()) {
            this.color = Color.BLACK;
            this.status = status.NORMAL;
        } else if (pieceCode == PieceType.B.code()) {
            this.color = Color.BLACK;
            this.status = status.KING;
        } else
            throw new Error("Invalid piece: " + pieceDescription.charAt(0) + " in " + pieceDescription);

        this.tower = new Tower(pieceDescription.substring(1));
    }


    public boolean isTower() {
        return this.tower.size() > 0;
    }
    
    
    public Piece capturePiece(Piece piece) throws Error {
        if (piece == null)
            throw new Error("Cannot capture a non existing piece");

        Tower tower = piece.tower;

        piece.tower = new Tower();
        this.tower.addPiece(piece);

        piece = tower.promoteTopPiece();
        if (piece != null)
            piece.tower = tower;

        return piece;
    }


    private PieceType pieceType() {
        PieceType pieceType;

        if (color == Color.WHITE) {
            pieceType = (status == Status.NORMAL) ? PieceType.w : PieceType.W;
        } else {
            pieceType = (status == Status.NORMAL) ? PieceType.b : PieceType.B;
        }

        return pieceType;
    }


    public String toString() {
        StringBuilder type = new StringBuilder();

        type.append(this.pieceType().code());
        for (Piece piece : this.tower.getPieces()) {
            type.append(piece.pieceType().code());
        }

        return type.toString();
    }

}
