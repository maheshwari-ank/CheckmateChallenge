package com.grandmasters.checkmatechallenge;

public class ChessMove {
    private int fromCol;
    private int fromRow;
    private int toCol;
    private int toRow;
    private ChessPiece pieceKilled;

    public ChessMove(int fromCol, int fromRow, int toCol, int toRow, ChessPiece pieceKilled) {
        this.fromCol = fromCol;
        this.fromRow = fromRow;
        this.toCol = toCol;
        this.toRow = toRow;
        this.pieceKilled = pieceKilled;
    }

    public int getFromCol() {
        return fromCol;
    }

    public void setFromCol(int fromCol) {
        this.fromCol = fromCol;
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToCol() {
        return toCol;
    }

    public void setToCol(int toCol) {
        this.toCol = toCol;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public ChessPiece getPieceKilled() {
        return pieceKilled;
    }

    public void setPieceKilled(ChessPiece pieceKilled) {
        this.pieceKilled = pieceKilled;
    }
}
