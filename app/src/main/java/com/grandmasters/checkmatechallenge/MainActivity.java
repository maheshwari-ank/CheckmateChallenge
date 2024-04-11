package com.grandmasters.checkmatechallenge;

import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ChessView chessView;
    private databaseHelper dbHelper;
    //    private Set<ChessPiece> pieces = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new databaseHelper(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_main);
        chessView = findViewById(R.id.chess_view);
        // Initialize and set the desired ChessLevel
        ChessLevel level = createLevel();
//        level.setPiecesBoxOriginalState(pieces);
        // Add pieces to the level as needed

        if (level != null) {
            // Your code that uses 'level'

            chessView.setChessDelegate(level);
        }
        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level.resetLevel();
                chessView.invalidate();
            }
        });

        findViewById(R.id.undoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level.undoLastMove();
                chessView.invalidate();
            }
        });

        databaseHelper dbHelper = new databaseHelper(this);
        int levelId = 1;
        ArrayList<ChessModel> chessModels = dbHelper.getLevelDetails(levelId);

        for (ChessModel chessModel : chessModels) {
            Log.d("MainActivity", "Level ID: " + chessModel.getLevelId());
            Log.d("MainActivity", "Level Name: " + chessModel.getLevelName());
            Log.d("MainActivity", "Is Solved: " + chessModel.getIsSolved());
            Log.d("MainActivity", "Layout ID: " + chessModel.getLayoutId());
            Log.d("MainActivity", "Rows: " + chessModel.getRows());
            Log.d("MainActivity", "Columns: " + chessModel.getCols());

            Log.d("MainActivity", "Chess Pieces:");
            ArrayList<ChessPiece> chessPieces = chessModel.getChessPieces();
            for (ChessPiece piece : chessPieces) {
                Log.d("MainActivity", "Row: " + piece.getRow());
                Log.d("MainActivity", "Column: " + piece.getCol());
                Log.d("MainActivity", "Piece Type: " + piece.getPieceType());
            }
        }
    }




//
//        long levelId = dbHelper.insertLevel("Easy Chess", 0);
//
//        // Insert level layout
//        dbHelper.insertLevelLayout(levelId, 3, 4);
//
//        // Insert chess pieces
//        dbHelper.insertChessPiece(levelId, 1, 2, "White", "Bishop");
//        dbHelper.insertChessPiece(levelId, 3, 1, "White", "Queen");
//        dbHelper.insertChessPiece(levelId, 1, 3, "Black", "King");

    private ChessLevel createLevel() {
        // Implement this method to create the desired level with specific rows, columns, and pieces
        int rows = 4;
        int columns = 3;
        Set<ChessPiece> pieces = new HashSet<>();
        // Add pieces to the set according to the level requirements
        pieces.add(new ChessPiece(0, 1, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.bishop_white));
        pieces.add(new ChessPiece(2, 0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white));
        pieces.add(new ChessPiece(3, 2, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black));

        databaseHelper dbHelper = new databaseHelper(this);
        long levelId = insertLevelBoardIntoDatabase(dbHelper, "Level 1", rows, columns, pieces);

        if (levelId == -1) {
            // Failed to insert level
            return null;
        }

        return new ChessLevel(rows, columns, pieces);
    }
    private long insertLevelBoardIntoDatabase(databaseHelper dbHelper, String levelName, int rows, int columns, Set<ChessPiece> pieces) {
        long levelId = dbHelper.insertLevel(levelName, 0); // Assuming initial level is not solved
        if (levelId == -1) {
            // Failed to insert level
            return -1;
        }

        if (dbHelper.insertLevelLayout(levelId, rows, columns) == -1) {
            // Failed to insert level layout
            return -1;
        }

        for (ChessPiece piece : pieces) {
            if (dbHelper.insertChessPiece(levelId, piece.getRow(),piece.getCol(),piece.getPieceType().toString()) == -1) {
                // Failed to insert chess piece   , Integer.toString(piece.getResId())
                return -1;
            }
        }

        return levelId;
    }

}

//    private void insertLevelBoardIntoDatabase(int rows, int columns, Set<ChessPiece> pieces) {
//        databaseHelper dbHelper = new databaseHelper(this);
//        long levelId = dbHelper.insertLevel("Level 1", 0);
//        dbHelper.insertLevelLayout(levelId, rows, columns);
//        for (ChessPiece piece : pieces) {
//            dbHelper.insertChessPiece(levelId, piece.getRow(), piece.getCol(), piece.getPieceType().toString(),Integer.toString(piece.getResId()));
//        }

// Update isSolved status if the level is solved
// dbHelper.updateIsSolved(levelId, true); // Example of how to update isSolved status


//, piece.getColor().toString()
// Update isSolved status if the level is solved
// dbHelper.updateIsSolved(levelId, true); // Example of how to update isSolved status



//    private ChessLevel createLevel() {
//        // Implement this method to create the desired level with specific rows, columns, and pieces
//        int rows = 4;
//        int columns = 3;
//
//        // Add pieces to the set according to the level requirements
//        pieces.add(new ChessPiece(0, 1, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.bishop_white));
//        pieces.add(new ChessPiece(2, 0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white));
//        pieces.add(new ChessPiece(3, 2, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black));
//
//        return new ChessLevel(rows, columns, pieces);
//    }



// Insert a level

// Update isSolved status if the level is solved
// dbHelper.updateIsSolved(levelId, true); // Example of how to update isSolved status



