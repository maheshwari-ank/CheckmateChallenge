package com.grandmasters.checkmatechallenge;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataManager {
    private static final String TAG = "DataManager";
    private Map<Integer, ChessLevel> dataMap = new HashMap<>();
    private ChessDelegate chessDelegate;
    private Set<ChessPiece> pieces = new HashSet<>();
    private DatabaseHelper dbHelper;
    private Context context;
    private int unsolvedLevelId;

    public Map<Integer, ChessLevel> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<Integer, ChessLevel> dataMap) {
        this.dataMap = dataMap;
    }

    public int getUnsolvedLevelId() {
        return unsolvedLevelId;
    }

    public void setUnsolvedLevelId(int unsolvedLevelId) {
        this.unsolvedLevelId = unsolvedLevelId;
    }

    public DataManager(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
        insertAllLevels();
//        dbHelper.insertLevel(4,3,false);
//        dbHelper.insertChessPiece(1,2,0,"WHITE","QUEEN",R.drawable.queen_white);
        loadLevelsFromDatabase();
        this.unsolvedLevelId = dbHelper.getFirstUnsolvedLevelId();
        if (unsolvedLevelId != -1) {
            ChessLevel unsolvedLevel = getChessLevel(unsolvedLevelId);
            if (unsolvedLevel != null) {
                Set<ChessPiece> originalPieces = unsolvedLevel.getPiecesBox();
                Set<ChessPiece> copiedPieces = deepCopySet(originalPieces);
                unsolvedLevel.setPiecesBoxOriginalState(copiedPieces);
            } else {
                // Handle if the unsolved level does not exist
                Log.d(TAG,"Unsolved level does not exist.");
            }
        } else {
            // Handle if no unsolved level is found
            Log.d(TAG,"No unsolved level found.");
        }
    }

    private void insertAllLevels() {
        if(dbHelper.insertLevel(1,4,2, false)!=-1) {
            dbHelper.insertChessPiece(1,0,0,"WHITE","QUEEN",R.drawable.queen_white);
            dbHelper.insertChessPiece(1,3,0,"BLACK","ROOK",R.drawable.rook_black);
            dbHelper.insertChessPiece(1,3,1,"BLACK","KING",R.drawable.king_black);
        }

        if(dbHelper.insertLevel(2,4,2, false)!=-1) {
            dbHelper.insertChessPiece(2, 1, 0, "WHITE", "BISHOP", R.drawable.bishop_white);
            dbHelper.insertChessPiece(2, 0, 1, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(2, 3, 0, "BLACK", "KING", R.drawable.king_black);
        }

        if(dbHelper.insertLevel(3,4,2, false)!=-1) {
            dbHelper.insertChessPiece(3, 1, 1, "WHITE", "KNIGHT", R.drawable.knight_white);
            dbHelper.insertChessPiece(3, 0, 0, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(3, 3, 1, "BLACK", "KING", R.drawable.king_black);
        }
    }
    private void loadLevelsFromDatabase() {
        // Load levels from the database
        List<ChessLevel> levelDataList = dbHelper.getAllLevels();
        for (ChessLevel levelData : levelDataList) {
            ChessLevel chessLevel = new ChessLevel(levelData.getLevelId(), levelData.getRows(), levelData.getColumns(), levelData.isSolved());
            dataMap.put(levelData.getLevelId(), chessLevel);

            // Load pieces for each level
            List<ChessPiece> pieceDataList = dbHelper.getPiecesForLevel(levelData.getLevelId());
            for (ChessPiece pieceData : pieceDataList) {
                ChessPiece chessPiece = new ChessPiece(
                        pieceData.getLevelId(),
                        pieceData.getPieceId(),
                        pieceData.getRow(),
                        pieceData.getCol(),
                        pieceData.getPlayer(),
                        pieceData.getPieceType(),
                        pieceData.getResId()
                );
                chessLevel.addPiece(chessPiece);
            }
        }
    }
    private Set<ChessPiece> deepCopySet(Set<ChessPiece> originalSet) {
        Set<ChessPiece> copySet = new HashSet<>();
        for (ChessPiece piece : originalSet) {
            copySet.add(new ChessPiece(piece));
        }
        return copySet;
    }

    public void createChessLevel(int levelId, int rows, int columns, boolean isSolved) {
        ChessLevel chessLevel = new ChessLevel(levelId, rows, columns, isSolved);
        dataMap.put(levelId, chessLevel);
    }

    public void addPieceToChessLevel(int levelId, int pieceId, int row, int col, ChessPlayer player, ChessPieceType type, int resId) {
        ChessLevel chessLevel = dataMap.get(levelId);
        if (chessLevel != null) {
            chessLevel.addPiece(new ChessPiece(levelId, pieceId, row, col, player, type, resId));
        } else {
            // Handle if the chess level doesn't exist
            System.out.println("Chess level does not exist.");
        }
    }

    // Method to retrieve a chess level by its ID
    public ChessLevel getChessLevel(int levelId) {
        return dataMap.get(levelId);
    }

    public List<ChessLevel> getAllLevels() {
        return new ArrayList<>(dataMap.values());
    }
}
