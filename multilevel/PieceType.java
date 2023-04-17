package checkers.multilevel;

public enum PieceType {
    W, w, B, b;

    public char code() {
        return name().charAt(0);
    }

}
