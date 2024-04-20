package com.grandmasters.checkmatechallenge;

import java.util.Set;

public interface ChessDelegate {
    /**
     * Retrieves the chess piece at the specified square.
     *
     * @param square The square to check.
     * @return The chess piece at the specified square, or null if no piece is present.
     */
    ChessPiece pieceAt(Square square);

    /**
     * Moves a chess piece from one square to another.
     *
     * @param fromSquare The square from which to move the piece.
     * @param toSquare   The square to which the piece should be moved.
     */
    void movePiece(Square fromSquare, Square toSquare);

    /**
     * Checks if the king of the specified type is in check.
     *
     * @param king The chess piece of  type king.
     * @return True if the specified king is in check, otherwise false.
     */
    boolean isKingInCheck(ChessPiece king);

    /**
     * Checks if a knight can move from one square to another.
     *
     * @param fromSquare The square from which the knight is moving.
     * @param toSquare   The square to which the knight is trying to move.
     * @return True if the knight can legally move to the specified square, otherwise false.
     */
    boolean canKnightMove(Square fromSquare, Square toSquare);

    /**
     * Checks if a piece can move from one square to another.
     *
     * @param fromSquare The square from which the piece is moving.
     * @param toSquare   The square to which the piece is trying to move.
     * @return True if the piece can legally move to the specified square, otherwise false.
     */
    boolean canPieceMove(Square fromSquare, Square toSquare);

    /**
     * Checks if the king can move from one square to another.
     *
     * @param fromSquare The square from which the king is moving.
     * @param toSquare   The square to which the king is trying to move.
     * @return True if the king can legally move to the specified square, otherwise false.
     */
    boolean canKingMove(Square fromSquare, Square toSquare);

    /**
     * Retrieves the set of chess pieces on the board.
     *
     * @return The set of chess pieces currently on the board.
     */
    Set<ChessPiece> getPiecesBox();

    /**
     * Retrieves the graph representing the board.
     *
     * @return The graph representing the board.
     */
    Graph getBoardGraph();

    /**
     * Retrieves the number of rows on the chessboard.
     *
     * @return The number of rows on the chessboard.
     */
    int getRows();

    /**
     * Retrieves the number of columns on the chessboard.
     *
     * @return The number of columns on the chessboard.
     */
    int getColumns();

    /**
     * Sets the original state of the pieces box.
     *
     * @param piecesBoxOriginalState The original state of the pieces box to set.
     */
    void setPiecesBoxOriginalState(Set<ChessPiece> piecesBoxOriginalState);
}
