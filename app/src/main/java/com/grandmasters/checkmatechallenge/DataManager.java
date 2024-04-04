package com.grandmasters.checkmatechallenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataManager {

    private Map<String, ChessLevel> dataMap = new HashMap<>();
    private ChessDelegate chessDelegate;
    private Set<ChessPiece> pieces = new HashSet<>();

    public DataManager() {
        createChessLevel("1", 4,4);
        addPieceToChessLevel("1",3,0,ChessPlayer.WHITE,ChessPieceType.KING,R.drawable.king_white);
        addPieceToChessLevel("1",2,0,ChessPlayer.WHITE,ChessPieceType.PAWN,R.drawable.pawn_white);
        addPieceToChessLevel("1",1,0,ChessPlayer.BLACK,ChessPieceType.KNIGHT,R.drawable.knight_black);
        addPieceToChessLevel("1",2,1,ChessPlayer.WHITE,ChessPieceType.PAWN,R.drawable.pawn_white);
        addPieceToChessLevel("1",3,2,ChessPlayer.WHITE,ChessPieceType.ROOK,R.drawable.rook_white);
        addPieceToChessLevel("1",1,3,ChessPlayer.BLACK,ChessPieceType.QUEEN,R.drawable.queen_black);

        Set<ChessPiece> originalPieces = getChessLevel("1").getPiecesBox();
        Set<ChessPiece> copiedPieces = deepCopySet(originalPieces);
        getChessLevel("1").setPiecesBoxOriginalState(copiedPieces);
    }

    private Set<ChessPiece> deepCopySet(Set<ChessPiece> originalSet) {
        Set<ChessPiece> copySet = new HashSet<>();
        for (ChessPiece piece : originalSet) {
            copySet.add(new ChessPiece(piece));
        }
        return copySet;
    }

    public void createChessLevel(String levelId, int rows, int columns) {
        ChessLevel chessLevel = new ChessLevel(levelId, rows, columns);
        dataMap.put(levelId, chessLevel);
    }

    public void addPieceToChessLevel(String levelId, int row, int col, ChessPlayer player, ChessPieceType type, int resId) {
        ChessLevel chessLevel = dataMap.get(levelId);
        if (chessLevel != null) {
            chessLevel.addPiece(new ChessPiece(row, col, player, type, resId));
        } else {
            // Handle if the chess level doesn't exist
            System.out.println("Chess level does not exist.");
        }
    }

    // Method to retrieve a chess level by its ID
    public ChessLevel getChessLevel(String levelId) {
        return dataMap.get(levelId);
    }
}
