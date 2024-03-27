package com.grandmasters.checkmatechallenge;

import android.util.Log;

import androidx.annotation.NonNull;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

public class ChessModel {
    private static final String TAG = "ChessModel";
    Set<ChessPiece> piecesBox = new HashSet<>();
    ChessMove chessMove;
    Stack<ChessMove> moves = new Stack<>();
    boolean isFirstMove = true;
    public ChessModel() {
        reset();
    }

    public void undoLastMove() {
        if (!moves.isEmpty()) {
            ChessMove lastMove = moves.pop();
            int fromCol = lastMove.getFromCol();
            int fromRow = lastMove.getFromRow();
            int toCol = lastMove.getToCol();
            int toRow = lastMove.getToRow();
            ChessPiece pieceMoved = pieceAt(toCol, toRow);
            ChessPiece pieceKilled = lastMove.getPieceKilled();

            // Restore piece to original position
            if (pieceMoved != null) {
                pieceMoved.setCol(fromCol);
                pieceMoved.setRow(fromRow);
            }

            // If a piece was killed during the move, restore it
            if (pieceKilled != null) {
                piecesBox.add(pieceKilled);
            }
        }
    }

    public void reset() {
        piecesBox.removeAll(piecesBox);
        for (int i=0; i<=1; i++) {
            piecesBox.add(new ChessPiece(0, 0 + i*7, ChessPlayer.WHITE, ChessPieceType.ROOK, R.drawable.rook_white));
            piecesBox.add(new ChessPiece(7, 0 + i*7, ChessPlayer.BLACK, ChessPieceType.ROOK, R.drawable.rook_black));
            piecesBox.add(new ChessPiece(0, 1 + i*5, ChessPlayer.WHITE, ChessPieceType.KNIGHT, R.drawable.knight_white));
            piecesBox.add(new ChessPiece(7, 1 + i*5, ChessPlayer.BLACK, ChessPieceType.KNIGHT, R.drawable.knight_black));
            piecesBox.add(new ChessPiece(0, 2 + i*3, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.bishop_white));
            piecesBox.add(new ChessPiece(7, 2 + i*3, ChessPlayer.BLACK, ChessPieceType.BISHOP, R.drawable.bishop_black));
        }

        for (int i=0; i<=7; i++) {
            piecesBox.add(new ChessPiece(1,i,ChessPlayer.WHITE, ChessPieceType.PAWN, R.drawable.pawn_white));
            piecesBox.add(new ChessPiece(6,i,ChessPlayer.BLACK, ChessPieceType.PAWN, R.drawable.pawn_black));
        }

        piecesBox.add(new ChessPiece(0,4,ChessPlayer.WHITE, ChessPieceType.KING, R.drawable.king_white));
        piecesBox.add(new ChessPiece(7,4,ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black));
        piecesBox.add(new ChessPiece(0,3,ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white));
        piecesBox.add(new ChessPiece(7,3,ChessPlayer.BLACK, ChessPieceType.QUEEN, R.drawable.queen_black));
    }
    public ChessPiece pieceAt(int col, int row) {
        for(ChessPiece piece : piecesBox) {
            if(col == piece.getCol() && row == piece.getRow()) {
                return piece;
            }
        }
        return null;
    }

    public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        ChessPiece movingPiece = pieceAt(fromCol, fromRow);
        ChessPiece pieceAtDestination = pieceAt(toCol, toRow);

        // Check if move is valid
        if (movingPiece != null && (pieceAtDestination == null || pieceAtDestination.getPlayer() != movingPiece.getPlayer())) {
            ChessMove move = new ChessMove(fromCol, fromRow, toCol, toRow, pieceAtDestination);

            // Push the move onto the stack
            moves.push(move);

            // Check if a piece is killed during the move
            if (pieceAtDestination != null) {
                piecesBox.remove(pieceAtDestination);
            }

            // Move the piece to the new position
            movingPiece.setCol(toCol);
            movingPiece.setRow(toRow);
        }
    }





    public Set<ChessPiece> getPiecesBox() {
        return piecesBox;
    }

//    @NonNull
//    @Override
//    public String toString() {
//        String desc = "\n\n";
//        for(int row=7; row>=0; row--) {
//            desc += row + " ";
//            for(int col=0; col<8; col++) {
//                ChessPiece piece = pieceAt(row,col);
////                System.out.println(piece.toString());
//                if (piece == null) {
//                    desc += ".  ";
//                }
//                else {
//                    boolean white = piece.player == ChessPlayer.WHITE;
//                    switch (piece.pieceType) {
//                        case KING:
//                            if (white) {
//                                desc += "K  ";
//                            }
//                            else{
//                                desc += "k  ";
//                            }
//                            break;
//                        case QUEEN:
//                            if (white) {
//                                desc += "Q  ";
//                            }
//                            else{
//                                desc += "q  ";
//                            }
//                            break;
//                        case ROOK:
//                            if (white) {
//                                desc += "R  ";
//                            }
//                            else{
//                                desc += "r  ";
//                            }
//                            break;
//                        case KNIGHT:
//                            if (white) {
//                                desc += "N  ";
//                            }
//                            else{
//                                desc += "n  ";
//                            }
//                            break;
//                        case BISHOP:
//                            if (white) {
//                                desc += "B  ";
//                            }
//                            else{
//                                desc += "b  ";
//                            }
//                            break;
//                        case PAWN:
//                            if (white) {
//                                desc += "P  ";
//                            }
//                            else{
//                                desc += "p  ";
//                            }
//                            break;
//                    }
//                }
//
//
//            }
//            desc += "\n";
//        }
//        desc += "  0  1  2  3  4  5  6  7";
//        return desc;
//    }
}
