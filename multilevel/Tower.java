package checkers.multilevel;

import java.util.LinkedList;


public class Tower {

    private LinkedList<Piece> pieces;


    Tower() {
        this.pieces = new LinkedList<Piece>();
    }


    Tower(String towerString) throws Error {
        this.pieces = new LinkedList<Piece>();

        for (int i = 0; i < towerString.length(); i++) {
            Piece piece = new Piece(String.valueOf(towerString.charAt(i)));
            this.pieces.addLast(piece);
        }
    }


    public int size() {
        return this.pieces.size();
    }
    
    
    public LinkedList<Piece> getPieces() {
        return this.pieces;
    }


    public void addPiece(Piece piece) {
        this.pieces.addLast(piece);
    }


    public Piece promoteTopPiece() {
        return ( this.pieces.isEmpty() ? null : this.pieces.removeFirst() );
    }

}
