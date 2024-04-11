package com.grandmasters.checkmatechallenge;
import java.util.ArrayList;


public class ChessModel {
    private long levelId;
    private String levelName;
    private int isSolved;
    private int layoutId;
    private int rows;
    private int cols;
    private ArrayList<ChessPiece> chessPieces;

    // Constructor
    public ChessModel(long levelId, String levelName, int isSolved, int layoutId, int rows, int cols) {
        this.levelId = levelId;
        this.levelName = levelName;
        this.isSolved = isSolved;
        this.layoutId = layoutId;
        this.rows = rows;
        this.cols = cols;
        this.chessPieces = new ArrayList<>();
    }

    // Getters
    public long getLevelId() {
        return levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getIsSolved() {
        return isSolved;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public ArrayList<ChessPiece> getChessPieces() {
        return chessPieces;
    }


    public void addChessPiece(ChessPiece chessPiece) {
        chessPieces.add(chessPiece);
    }

}




