//package com.grandmasters.checkmatechallenge;
//
//import android.os.Bundle;
//import android.view.View;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.ArrayList;
//
//public class MainActivity extends AppCompatActivity {
//    private ChessView chessView;
//    private databaseHelper dbHelper;
//    //    private Set<ChessPiece> pieces = new HashSet<>();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        dbHelper = new databaseHelper(this);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//
//        setContentView(R.layout.activity_main);
//        chessView = findViewById(R.id.chess_view);
//        // Initialize and set the desired ChessLevel
//        ChessLevel level = createLevel();
////        level.setPiecesBoxOriginalState(pieces);
//        // Add pieces to the level as needed
//
//        if (level != null) {
//            // Your code that uses 'level'
//
//            chessView.setChessDelegate(level);
//        }
//        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                level.resetLevel();
//                chessView.invalidate();
//            }
//        });
//
//        findViewById(R.id.undoButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                level.undoLastMove();
//                chessView.invalidate();
//            }
//        });
//
//        databaseHelper dbHelper = new databaseHelper(this);
//        int levelId = 1;
//        ArrayList<ChessModel> chessModels = dbHelper.getLevelDetails(levelId);
//
//        for (ChessModel chessModel : chessModels) {
//            Log.d("MainActivity", "Level ID: " + chessModel.getLevelId());
//            Log.d("MainActivity", "Level Name: " + chessModel.getLevelName());
//            Log.d("MainActivity", "Is Solved: " + chessModel.getIsSolved());
//            Log.d("MainActivity", "Layout ID: " + chessModel.getLayoutId());
//            Log.d("MainActivity", "Rows: " + chessModel.getRows());
//            Log.d("MainActivity", "Columns: " + chessModel.getCols());
//
//            Log.d("MainActivity", "Chess Pieces:");
//            ArrayList<ChessPiece> chessPieces = chessModel.getChessPieces();
//            for (ChessPiece piece : chessPieces) {
//                Log.d("MainActivity", "Row: " + piece.getRow());
//                Log.d("MainActivity", "Column: " + piece.getCol());
//                Log.d("MainActivity", "Piece Type: " + piece.getPieceType());
//            }
//        }
//    }
//
//
//
//
////
////        long levelId = dbHelper.insertLevel("Easy Chess", 0);
////
////        // Insert level layout
////        dbHelper.insertLevelLayout(levelId, 3, 4);
////
////        // Insert chess pieces
////        dbHelper.insertChessPiece(levelId, 1, 2, "White", "Bishop");
////        dbHelper.insertChessPiece(levelId, 3, 1, "White", "Queen");
////        dbHelper.insertChessPiece(levelId, 1, 3, "Black", "King");
//
//    private ChessLevel createLevel() {
//        // Implement this method to create the desired level with specific rows, columns, and pieces
//        int rows = 4;
//        int columns = 3;
//        Set<ChessPiece> pieces = new HashSet<>();
//        // Add pieces to the set according to the level requirements
//        pieces.add(new ChessPiece(0, 1, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.bishop_white));
//        pieces.add(new ChessPiece(2, 0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white));
//        pieces.add(new ChessPiece(3, 2, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black));
//
//        databaseHelper dbHelper = new databaseHelper(this);
//        long levelId = insertLevelBoardIntoDatabase(dbHelper, "Level 1", rows, columns, pieces);
//
//        if (levelId == -1) {
//            // Failed to insert level
//            return null;
//        }
//
//        return new ChessLevel(rows, columns, pieces);
//    }
//    private long insertLevelBoardIntoDatabase(databaseHelper dbHelper, String levelName, int rows, int columns, Set<ChessPiece> pieces) {
//        long levelId = dbHelper.insertLevel(levelName, 0); // Assuming initial level is not solved
//        if (levelId == -1) {
//            // Failed to insert level
//            return -1;
//        }
//
//        if (dbHelper.insertLevelLayout(levelId, rows, columns) == -1) {
//            // Failed to insert level layout
//            return -1;
//        }
//
//        for (ChessPiece piece : pieces) {
//            if (dbHelper.insertChessPiece(levelId, piece.getRow(),piece.getCol(),piece.getPieceType().toString()) == -1) {
//                // Failed to insert chess piece   , Integer.toString(piece.getResId())
//                return -1;
//            }
//        }
//
//        return levelId;
//    }
//
//}
//
////    private void insertLevelBoardIntoDatabase(int rows, int columns, Set<ChessPiece> pieces) {
////        databaseHelper dbHelper = new databaseHelper(this);
////        long levelId = dbHelper.insertLevel("Level 1", 0);
////        dbHelper.insertLevelLayout(levelId, rows, columns);
////        for (ChessPiece piece : pieces) {
////            dbHelper.insertChessPiece(levelId, piece.getRow(), piece.getCol(), piece.getPieceType().toString(),Integer.toString(piece.getResId()));
////        }
//
//// Update isSolved status if the level is solved
//// dbHelper.updateIsSolved(levelId, true); // Example of how to update isSolved status
//
//
////, piece.getColor().toString()
//// Update isSolved status if the level is solved
//// dbHelper.updateIsSolved(levelId, true); // Example of how to update isSolved status
//
//
//
////    private ChessLevel createLevel() {
////        // Implement this method to create the desired level with specific rows, columns, and pieces
////        int rows = 4;
////        int columns = 3;
////
////        // Add pieces to the set according to the level requirements
////        pieces.add(new ChessPiece(0, 1, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.bishop_white));
////        pieces.add(new ChessPiece(2, 0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white));
////        pieces.add(new ChessPiece(3, 2, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black));
////
////        return new ChessLevel(rows, columns, pieces);
////    }
//
//
//
//// Insert a level
//
//// Update isSolved status if the level is solved
//// dbHelper.updateIsSolved(levelId, true); // Example of how to update isSolved status
//
//
//


package com.grandmasters.checkmatechallenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ChessView.OnCheckmateListener {
    private ChessView chessView;
    private TextView levelName;
    private TextView mate;
    ChessLevel currentlevel;
    private Context context;
    private DatabaseHelper dbHelper;
//    private List<ChessLevel> gameLevels;
    private Set<ChessPiece> pieces = new HashSet<>();
    private DataManager dataManager;
    private Serializable key;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        chessView = findViewById(R.id.chess_view);
        chessView.setOnCheckmateListener(this);
        levelName = findViewById(R.id.level_name);
        mate = findViewById(R.id.mate);
//        dataManager = new DataManager(getApplicationContext());
        dataManager = DataManagerSingleton.getInstance(getApplicationContext());
        dbHelper = DbHelperSingleton.getInstance(context);
        // Retrieve chess level from DataManager and set the ChessDelegate
        currentlevel = dataManager.getChessLevel(dbHelper.getFirstUnsolvedLevelId());
        // Retrieve selected level from intent extras
        if (intent != null && intent.hasExtra("SELECTED_LEVEL")) {
            currentlevel = (ChessLevel) intent.getSerializableExtra("SELECTED_LEVEL");
            if (currentlevel != null) {
                // Set the selected level to the ChessView
                currentlevel.resetLevel();
                chessView.setChessDelegate(currentlevel);
                chessView.setCurrentLevel(currentlevel);
                levelName.setText("Level " + String.valueOf(currentlevel.getLevelId()));
                levelName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            }
        }
        else {
//            currentlevel = dataManager.getChessLevel(2);
//            chessView.setChessDelegate(currentlevel);
            levelName.setText("Level " + String.valueOf(currentlevel.getLevelId()));
            levelName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
//            mate.setText("Level " + String.valueOf(currentlevel.getLevelId()));
            mate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            currentlevel.resetLevel();
            chessView.setChessDelegate(currentlevel);
            chessView.setCurrentLevel(currentlevel);
        }
        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentlevel.resetLevel();
                chessView.setChessDelegate(currentlevel);
                chessView.invalidate();
            }
        });

        findViewById(R.id.undoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentlevel.undoLastMove();
                chessView.setChessDelegate(currentlevel);
                chessView.invalidate();
            }
        });

        findViewById(R.id.levels_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLevelsActivity();
            }
        });
    }

    private void openLevelsActivity() {
        // Start a new activity to display the levels
        Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
        startActivity(intent);
        finish();
    }

    // Implement the onCheckmate method of the OnCheckmateListener interface
    @Override
    public void onCheckmate() {
        // Display the "Checkmate" message in your TextView
        TextView mate = findViewById(R.id.mate);
        mate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        mate.setText("Checkmate!");
        dataManager.getChessLevel(currentlevel.getLevelId()).setSolved(true);
        findViewById(R.id.hintButton).setVisibility(View.GONE);
        findViewById(R.id.resetButton).setVisibility(View.GONE);
        findViewById(R.id.undoButton).setVisibility(View.GONE);

        // Add the "Next" button dynamically
        Button nextButton = new Button(this);
        nextButton.setText("Next");
        nextButton.setTextColor(Color.BLACK);
        nextButton.setBackgroundColor(Color.WHITE);
        nextButton.setBackgroundResource(R.drawable.rounded_button_background);
        nextButton.setWidth(500);
        nextButton.setHeight(200);
        nextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Set text size
        nextButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // Set layout parameters

        ((ViewGroup) findViewById(R.id.buttonsLayout)).addView(nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Next" button click event, e.g., move to the next level
                moveNextLevel();
            }
        });
    }
    private void moveNextLevel() {
        // Assuming you have a list of levels and you want to move to the next level in the list
        // You need to keep track of the current level index
        int nextLevelIndex = currentlevel.getLevelId() + 1; // Increment the current level index
        if (nextLevelIndex <= dataManager.getAllLevels().size()) {
            // If there are more levels available, start the next level
            ChessLevel nextLevel = dataManager.getChessLevel(nextLevelIndex);
            startNextLevel(nextLevel);
        } else {
            // If there are no more levels available, do something (e.g., display a message)
            // For now, let's just log a message
            Log.d(TAG, "No more levels available");
        }
    }

    private void startNextLevel(ChessLevel nextLevel) {
        // Start the next level
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("SELECTED_LEVEL", nextLevel);
        dataManager.updateCurrentLevel(nextLevel);
        startActivity(intent);
        finish();
    }
}