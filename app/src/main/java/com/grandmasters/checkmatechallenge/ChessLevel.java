package com.grandmasters.checkmatechallenge;

import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

public class ChessLevel implements ChessDelegate {

    private static final int MAX_ROWS = 6;
    private static final int MAX_COLS = 5;
    private static final String TAG = "ChessLevel";
    private String levelId;
    private int rows;
    private int columns;
    private Set<ChessPiece> piecesBox;
    private Set<ChessPiece> piecesBoxOriginalState;
    private Graph boardGraph;
    public Set<ChessPiece> getPiecesBoxOriginalState() {
        return piecesBoxOriginalState;
    }

    public void setPiecesBoxOriginalState(Set<ChessPiece> piecesBoxOriginalState) {
        this.piecesBoxOriginalState = piecesBoxOriginalState;
    }
    Stack<ChessMove> moves = new Stack<>();

    public Graph getBoardGraph() {
        return boardGraph;
    }

    public void setBoardGraph(Graph boardGraph) {
        this.boardGraph = boardGraph;
    }

    public ChessLevel(String levelId, int rows, int columns){
        if (columns > MAX_COLS || rows > MAX_ROWS) {
            throw new IllegalArgumentException("Number of columns or rows exceeds maximum limit.");
        }
        this.levelId = levelId;
        this.rows = rows;
        this.columns = columns;
//        this.piecesBox = piecesBox;
        this.piecesBox = new HashSet<>();
        this.piecesBoxOriginalState = deepCopySet(piecesBox);


        initializeGraph();
    }

    private void initializeGraph() {
        boardGraph = new Graph();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                boardGraph.addVertex(new Square(col, row));
            }
        }
    }
    public void resetLevel() {
        piecesBox.clear();
        moves.removeAllElements();
//        piecesBox.addAll(deepCopySet(piecesBoxOriginalState));
        for (ChessPiece originalPiece : piecesBoxOriginalState) {
            piecesBox.add(new ChessPiece(originalPiece));
        }
    }

    public void addPiece(ChessPiece piece) {
        piecesBox.add(piece);
//        piecesBoxOriginalState.add(piece);
    }

    private boolean isValidPosition(Square square) {
        int column = square.getCol();
        int rows = square.getRow();
        return column >= 0 && column < this.columns && rows >= 0 && rows < this.rows;
    }
//    public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {


    public void movePiece(Square fromSquare, Square toSquare) {
        if (canPieceMove(fromSquare, toSquare)) {
            // Check if the move is within the bounds of the chessboard
            if (isValidPosition(fromSquare) && isValidPosition(toSquare)) {
                ChessPiece movingPiece = pieceAt(fromSquare);
                ChessPiece pieceAtDestination = pieceAt(toSquare);

                // Check if move is valid
                if (movingPiece != null && (pieceAtDestination == null || pieceAtDestination.getPlayer() != movingPiece.getPlayer())) {
                    ChessMove move = new ChessMove(fromSquare.getCol(), fromSquare.getRow(), toSquare.getCol(), toSquare.getRow(), pieceAtDestination);

                    // Push the move onto the stack
                    moves.push(move);

                    // Check if a piece is killed during the move
                    if (pieceAtDestination != null) {
                        piecesBox.remove(pieceAtDestination);
                    }

                    // Move the piece to the new position
                    movingPiece.setCol(toSquare.getCol());
                    movingPiece.setRow(toSquare.getRow());
                }
            } else {
                Log.e(TAG, "Invalid move: Out of bounds");
            }
        }

    }

    public void undoLastMove() {
        if (!moves.isEmpty()) {
            ChessMove lastMove = moves.pop();
            int fromCol = lastMove.getFromCol();
            int fromRow = lastMove.getFromRow();
            int toCol = lastMove.getToCol();
            int toRow = lastMove.getToRow();
            ChessPiece pieceMoved = pieceAt(new Square(toCol, toRow));
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
    public ChessPiece pieceAt(Square square) {
        for(ChessPiece piece : piecesBox) {
            if(square.getCol() == piece.getCol() && square.getRow() == piece.getRow()) {
                return piece;
            }
        }
        return null;
    }

    public boolean isClearHorizontal(Square fromSquare, Square toSquare){
        if(fromSquare.getRow() != toSquare.getRow())    return false;
        int gap = Math.abs(fromSquare.getRow() - toSquare.getRow()) -1;
        if(gap == 0)    return true;
        for(int i=1;i<=gap;i++){
            int nextCol = toSquare.getCol() > fromSquare.getCol() ? fromSquare.getCol() + i :
                    fromSquare.getCol() - i;
            if(pieceAt(new Square(nextCol, fromSquare.getRow())) != null){
                return false;
            }
        }
        return true;
    }

    public boolean isClearVertical(Square fromSquare, Square toSquare){
        if(fromSquare.getCol() != toSquare.getCol())    return false;
        int gap = Math.abs(fromSquare.getRow() - toSquare.getRow()) -1;
        if(gap == 0)    return true;
        for(int i=1;i<=gap;i++){
            int nextRow = toSquare.getRow() > fromSquare.getRow() ? fromSquare.getRow() + i :
                    fromSquare.getRow() - i;
            if(pieceAt(new Square(nextRow, fromSquare.getCol())) != null){
                return false;
            }
        }
        return true;
    }

    public boolean canKnightMove(Square fromSquare, Square toSquare) {
        return Math.abs(fromSquare.getCol() - toSquare.getCol()) == 2 && Math.abs(fromSquare.getRow() - toSquare.getRow()) == 1 ||
                Math.abs(fromSquare.getCol() - toSquare.getCol()) == 1 && Math.abs(fromSquare.getRow() - toSquare.getRow()) == 2;
    }
    
    public boolean canRookMove(Square fromSquare, Square toSquare){
        return (fromSquare.getCol() == toSquare.getCol() && isClearHorizontal(fromSquare, toSquare))
                || (fromSquare.getRow() == toSquare.getRow() && isClearVertical(fromSquare, toSquare));
    }
    


    public boolean canPieceMove(Square fromSquare, Square toSquare) {
        if(fromSquare.getCol() == toSquare.getCol() && fromSquare.getRow() == toSquare.getRow()){
            return false;
        }
        ChessPiece movingPiece = pieceAt(fromSquare);
        switch (movingPiece.getPieceType()) {
            case KNIGHT:
                return canKnightMove(fromSquare, toSquare);
            case ROOK:
                return canRookMove(fromSquare, toSquare);
        }
        return false;
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

