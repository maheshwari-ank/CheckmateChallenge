package com.grandmasters.checkmatechallenge;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ChessView chessView;
    private Set<ChessPiece> pieces = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        chessView.setChessDelegate(level);

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
    }

    private ChessLevel createLevel() {
        // Implement this method to create the desired level with specific rows, columns, and pieces
        int rows = 4;
        int columns = 3;

        // Add pieces to the set according to the level requirements
        pieces.add(new ChessPiece(0, 1, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.bishop_white));
        pieces.add(new ChessPiece(2, 0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white));
        pieces.add(new ChessPiece(3, 2, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black));

        return new ChessLevel(rows, columns, pieces);
    }
}
