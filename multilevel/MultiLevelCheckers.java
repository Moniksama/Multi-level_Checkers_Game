package checkers.multilevel;

import java.util.Arrays;
import java.util.Scanner;


public class MultiLevelCheckers {

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.print("White pieces: ");
        String[] whitePieces = console.nextLine().split(" ");
        System.out.print("Black pieces: ");
        String[] blackPieces = console.nextLine().split(" ");
        System.out.print("Moves: ");
        String[] moves = console.nextLine().split(" ");

        String[] boardPieces = Arrays.copyOf(whitePieces, whitePieces.length + blackPieces.length);
        System.arraycopy(blackPieces, 0, boardPieces, whitePieces.length, blackPieces.length);

        Board board = null;
        try {
            board = new Board(boardPieces);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        //System.out.print("Board before moves: ");
        //System.out.println(board);
        //board.drawBoard();   // Dibuja el tablero

        try {
            board.movePieces(moves);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.exit(1);
        }
        //System.out.print("Board after moves: ");
        System.out.println(board);
        //board.drawBoard();   // Dibuja el tablero
    }

}


/*
        // a1_w c1_w e1_w f2_ww h2_w g5_wbb
        // a3_b e3_b a5_bww c5_bwww e7_b g7_b b8_b d8_b f8_b h8_b
        // f2_ww:d4_wwb:b6_wwbb g7_b-f6_b h2_w-g3_w f6_d:h4_bw:f2_bww e1_w:g3_wb g5_bb-h4_bb

        // Several tests
        System.out.println("Piezas:");
        Piece piece1 = new Piece("bbBbw");
        System.out.println("p1 " + piece1);

        Piece piece2 = new Piece("bbBBBb");
        System.out.println("p2 " + piece2);

        Piece piece3 = new Piece("wWwWwWbb");
        System.out.println("p3 " + piece3);

        Piece piece4 = new Piece("B");
        System.out.println("p4 " + piece4);

        Piece piece5 = new Piece("W");
        System.out.println("p5 " + piece5);

        System.out.println("Capturas:");
        piece2 = piece1.capturePiece(piece2);
        System.out.println("p1 " + piece1);
        System.out.println("p2 " + piece2);
        System.out.println("---");

        piece4 = piece3.capturePiece(piece4);
        System.out.println("p3 " + piece3);
        System.out.println("p4 " + piece4);
        System.out.println("---");

        piece2 = piece3.capturePiece(piece2);
        System.out.println("p3 " + piece3);
        System.out.println("p2 " + piece2);
        System.out.println("---");

        piece1 = piece2.capturePiece(piece1);
        System.out.println("p2 " + piece2);
        System.out.println("p1 " + piece1);
        System.out.println("---");

        piece5 = piece2.capturePiece(piece5);
        System.out.println("p2 " + piece2);
        System.out.println("p5 " + piece5);
        System.out.println("---");

        // Éste debe fallar, porque piece4 ha sido capturada
        piece4 = piece2.capturePiece(piece4);
        System.out.println("p2 " + piece2);
        System.out.println("p4 " + piece4);
        System.out.println("---");

        String[] positions = { "a1_W", "c1_w", "e1_w", "f2_ww", "h2_w", "g5_wbb", "a3_w", "e3_b",
                "a5_bww", "c5_bwww", "e7_b", "g7_b", "b8_b", "d8_b", "f8_b", "h8_b"};
        Board board = new Board(positions);
        System.out.println(board);

        try {
            board.movePiece(moves);
        } catch (Exception e) {
            e.printStackTrace();
        }

    // a1_w c1_w e1_w f2_ww h2_w g5_wbb
    // a3_b e3_b a5_bww c5_bwww e7_b g7_b b8_b d8_b f8_b h8_b

    // f2_ww:d4_wwb:b6_wwbb g7_b-f6_b h2_w-g3_w f6_d:h4_bw:f2_bww e1_w:g3_wb g5_bb-h4_bb
 */
