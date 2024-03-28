package com.grandmasters.checkmatechallenge;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ChessLevel implements ChessDelegate {

    private static final int MAX_ROWS = 6;
    private static final int MAX_COLS = 5;
    private static final String TAG = "ChessLevel";
    private int rows;
    private int columns;
    private Set<ChessPiece> piecesBox;
    private Set<ChessPiece> piecesBoxOriginalState;

    public Set<ChessPiece> getPiecesBoxOriginalState() {
        return piecesBoxOriginalState;
    }

    public void setPiecesBoxOriginalState(Set<ChessPiece> piecesBoxOriginalState) {
        this.piecesBoxOriginalState = piecesBoxOriginalState;
    }

    Stack<ChessMove> moves = new Stack<>();

    public ChessLevel(int rows, int columns, Set<ChessPiece> piecesBox){
        if (columns > MAX_COLS || rows > MAX_ROWS) {
            Log.d(TAG,"Number of columns or rows exceeds maximum limit.");
            return;
        }
        this.rows = rows;
        this.columns = columns;
        this.piecesBox = piecesBox;
        this.piecesBoxOriginalState = deepCopySet(piecesBox);
    }

    public void resetLevel() {
        piecesBox.clear();
        moves.removeAllElements();
        piecesBox.addAll(piecesBoxOriginalState);
    }

    public void addPiece(ChessPiece piece) {
        piecesBox.add(piece);
    }

    private boolean isValidPosition(int col, int row) {
        return col >= 0 && col < this.columns && row >= 0 && row < this.rows;
    }
    public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        // Check if the move is within the bounds of the chessboard
        if (isValidPosition(fromCol, fromRow) && isValidPosition(toCol, toRow)) {
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
        } else {
            Log.e(TAG, "Invalid move: Out of bounds");
        }
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

    @Override
    public ChessPiece pieceAt(int col, int row) {
        for(ChessPiece piece : piecesBox) {
            if(col == piece.getCol() && row == piece.getRow()) {
                return piece;
            }
        }
        return null;
    }

    @Override
    public Set<ChessPiece> getPiecesBox() {
        return piecesBox;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    private Set<ChessPiece> deepCopySet(Set<ChessPiece> originalSet) {
        Set<ChessPiece> copySet = new HashSet<>();
        for (ChessPiece piece : originalSet) {
            copySet.add(new ChessPiece(piece));
        }
        return copySet;
    }

}

