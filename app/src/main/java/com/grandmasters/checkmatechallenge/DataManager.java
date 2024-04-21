package com.grandmasters.checkmatechallenge;

import android.content.Context;
import java.util.HashSet;
import java.util.Set;

public class DataManager {
    private static final String TAG = "DataManager";
    private HashMap<Integer, ChessLevel> dataMap = new HashMap<>();
    private ChessDelegate chessDelegate;
    private Set<ChessPiece> pieces = new HashSet<>();
    private DatabaseHelper dbHelper;
    private Context context;
    private int unsolvedLevelId;
    private ChessLevel currentLevel;

    public HashMap<Integer, ChessLevel> getDataMap() {
        return dataMap;
    }

    public void setDataMap(HashMap<Integer, ChessLevel> dataMap) {
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
        dbHelper = DbHelperSingleton.getInstance(context);
        insertAllLevels();
        loadLevelsFromDatabase();
        setOriginalStateForAllLevels();
        this.unsolvedLevelId = dbHelper.getFirstUnsolvedLevelId();
    }

    private void insertAllLevels() {
        if(dbHelper.insertLevel(1,4,2, false, ChessPlayer.WHITE, 1)!=-1 ) {
            dbHelper.insertChessPiece(1,0,0,"WHITE","QUEEN",R.drawable.queen_white);
            dbHelper.insertChessPiece(1,3,0,"BLACK","ROOK",R.drawable.rook_black);
            dbHelper.insertChessPiece(1,3,1,"BLACK","KING",R.drawable.king_black);
        }

        if(dbHelper.insertLevel(2,4,2, false, ChessPlayer.WHITE, 1)!=-1) {
            dbHelper.insertChessPiece(2, 1, 0, "WHITE", "BISHOP", R.drawable.bishop_white);
            dbHelper.insertChessPiece(2, 0, 1, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(2, 3, 0, "BLACK", "KING", R.drawable.king_black);
        }

        if(dbHelper.insertLevel(3,4,3, false, ChessPlayer.WHITE, 1)!=-1) {
            dbHelper.insertChessPiece(3, 0, 1, "WHITE", "BISHOP", R.drawable.bishop_white);
            dbHelper.insertChessPiece(3, 2, 0, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(3, 3, 2, "BLACK", "KING", R.drawable.king_black);
        }
        if(dbHelper.insertLevel(4,4,3, false, ChessPlayer.WHITE, 1)!=-1) {
            dbHelper.insertChessPiece(4, 2, 1, "BLACK", "PAWN", R.drawable.pawn_black);
            dbHelper.insertChessPiece(4, 2, 2, "BLACK", "PAWN", R.drawable.pawn_black);
            dbHelper.insertChessPiece(4, 0, 0, "WHITE", "ROOK", R.drawable.rook_white);
            dbHelper.insertChessPiece(4, 3, 2, "BLACK", "KING", R.drawable.king_black);
        }
        if(dbHelper.insertLevel(5,4,3, false, ChessPlayer.WHITE, 1)!=-1) {
            dbHelper.insertChessPiece(5, 2, 0, "BLACK", "PAWN", R.drawable.pawn_black);
            dbHelper.insertChessPiece(5, 3, 1, "BLACK", "ROOK", R.drawable.rook_black);
            dbHelper.insertChessPiece(5, 1, 0, "WHITE", "BISHOP", R.drawable.bishop_white);
            dbHelper.insertChessPiece(5, 3, 2, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(5, 3, 0, "BLACK", "KING", R.drawable.king_black);
        }
        if(dbHelper.insertLevel(6,4,3, false, ChessPlayer.WHITE,2)!=-1) {
            dbHelper.insertChessPiece(6, 0, 0, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(6, 1, 0, "WHITE", "ROOK", R.drawable.rook_white);
            dbHelper.insertChessPiece(6, 3, 1, "BLACK", "KING", R.drawable.king_black);
        }
        if(dbHelper.insertLevel(7,4,3, false, ChessPlayer.WHITE,2)!=-1) {
            dbHelper.insertChessPiece(7, 0, 2, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(7, 0, 1, "WHITE", "KNIGHT", R.drawable.knight_white);
            dbHelper.insertChessPiece(7, 3, 1, "BLACK", "KING", R.drawable.king_black);
        }
        if(dbHelper.insertLevel(8,4,3, false, ChessPlayer.WHITE,2)!=-1) {
            dbHelper.insertChessPiece(8, 0, 0, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(8, 1, 2, "WHITE", "ROOK", R.drawable.rook_white);
            dbHelper.insertChessPiece(8, 3, 1, "BLACK", "KING", R.drawable.king_black);
        }
        if(dbHelper.insertLevel(9,4,3, false, ChessPlayer.WHITE,2)!=-1) {
            dbHelper.insertChessPiece(9, 0, 0, "WHITE", "KING", R.drawable.king_white);
            dbHelper.insertChessPiece(9, 0, 2, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(9, 3, 1, "BLACK", "KING", R.drawable.king_black);
            dbHelper.insertChessPiece(9, 1, 0, "WHITE", "PAWN", R.drawable.pawn_white);
            dbHelper.insertChessPiece(9, 1, 1, "WHITE", "PAWN", R.drawable.pawn_white);
        }
        if(dbHelper.insertLevel(10,4,3, false, ChessPlayer.WHITE,2)!=-1) {
            dbHelper.insertChessPiece(10, 0, 1, "WHITE", "QUEEN", R.drawable.queen_white);
            dbHelper.insertChessPiece(10, 2, 0, "BLACK", "PAWN", R.drawable.pawn_black);
            dbHelper.insertChessPiece(10, 3, 0, "BLACK", "KING", R.drawable.king_black);
            dbHelper.insertChessPiece(10, 1, 0, "WHITE", "PAWN", R.drawable.pawn_white);
            dbHelper.insertChessPiece(10, 1, 1, "BLACK", "BISHOP", R.drawable.bishop_black);
        }
    }
    private void loadLevelsFromDatabase() {
        // Load levels from the database
        CustomList<ChessLevel> levelDataList = dbHelper.getAllLevels();
        int levelDataListSize = levelDataList.size();
        for (int i = 0; i < levelDataListSize; i++) {
            ChessLevel levelData = levelDataList.get(i);
            ChessLevel chessLevel = new ChessLevel(levelData.getLevelId(), levelData.getRows(), levelData.getColumns(), levelData.isSolved(), levelData.getUserPlayer());
            dataMap.put(levelData.getLevelId(), chessLevel);

            // Load pieces for each level
            CustomList<ChessPiece> pieceDataList = dbHelper.getPiecesForLevel(levelData.getLevelId());
            int pieceDataListSize = pieceDataList.size();
            for (int j = 0; j < pieceDataListSize; j++) {
                ChessPiece pieceData = pieceDataList.get(j);
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
    public void setOriginalStateForAllLevels() {
        ChessLevel level = null;
        for (int i = 1; i <= dataMap.size(); i++) {
            level = dataMap.get(i);
            Set<ChessPiece> originalPieces = level.getPiecesBox();
            Set<ChessPiece> copiedPieces = deepCopySet(originalPieces);
            level.setPiecesBoxOriginalState(copiedPieces);
        }
    }

    public void createChessLevel(int levelId, int rows, int columns, boolean isSolved, ChessPlayer userPlayer) {
        ChessLevel chessLevel = new ChessLevel(levelId, rows, columns, isSolved, userPlayer);
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

//    public CustomList<ChessLevel> getAllLevels() {
//        return new ArrayList<>(dataMap.values());
//    }

    public CustomList<ChessLevel> getAllLevels() {
        ArrayList<ChessLevel> levelsList = new ArrayList<>(dataMap.size()); // Create an ArrayList with initial capacity
        ChessLevel level = null;
        for (int i = 1; i <= dataMap.size(); i++) {
            level = dataMap.get(i);
            levelsList.add(level); // Add each ChessLevel from dataMap.values() to levelsList
        }
        return levelsList; // Return the CustomList
    }

    public void updateCurrentLevel(ChessLevel currentLevel) {
        this.currentLevel = currentLevel;
    }
}
