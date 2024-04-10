package com.grandmasters.checkmatechallenge;

import java.util.Set;

public interface ChessDelegate {
    ChessPiece pieceAt(Square square);

    void movePiece(Square fromSquare, Square toSquare);
    boolean isKingInCheck(ChessPlayer player);
    boolean canKnightMove(Square fromSquare, Square toSquare);
    boolean canPieceMove(Square fromSquare, Square toSquare);
    Set<ChessPiece> getPiecesBox();

    Graph getBoardGraph();
    int getRows();

    int getColumns();

    void setPiecesBoxOriginalState(Set<ChessPiece> piecesBoxOriginalState);
}
