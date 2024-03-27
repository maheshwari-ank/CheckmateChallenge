package com.grandmasters.checkmatechallenge;

import java.util.Set;

public interface ChessDelegate {
    ChessPiece pieceAt(int col, int row);

    void movePiece(int fromCol, int fromRow, int toCol, int toRow);

    Set<ChessPiece> getPiecesBox();
}
