package com.grandmasters.checkmatechallenge;

import java.util.Set;

public interface ChessDelegate {
    ChessPiece pieceAt(Square square);

    void movePiece(Square fromSquare, Square toSquare);

    Set<ChessPiece> getPiecesBox();

    int getRows();

    int getColumns();

    void setPiecesBoxOriginalState(Set<ChessPiece> piecesBoxOriginalState);
}
