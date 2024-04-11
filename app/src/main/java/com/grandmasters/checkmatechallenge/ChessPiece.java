package com.grandmasters.checkmatechallenge;


public class ChessPiece {
    private int row;
    private int col;
    private ChessPlayer player;
    private ChessPieceType pieceType;

    public int resId;

    public ChessPlayer getPlayer() {
        return player;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ChessPieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(ChessPieceType pieceType) {
        this.pieceType = pieceType;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public ChessPiece(int row, int col, ChessPlayer player, ChessPieceType pieceType, int resId) {
        this.row = row;
        this.col = col;
        this.player = player;
        this.pieceType = pieceType;
        this.resId = resId;
    }

    public ChessPiece createPiece(int row, int col, ChessPlayer player, ChessPieceType pieceType, int resId) {
        return new ChessPiece(row, col, player, pieceType, resId);
    }

    // Copy constructor
    public ChessPiece(ChessPiece other) {
        this.row = other.row;
        this.col = other.col;
        this.player = other.player;
        this.pieceType = other.pieceType;
        this.resId = other.resId;
    }

    public ChessPiece copy() {
        return new ChessPiece(this);
    }
}
