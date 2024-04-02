package com.grandmasters.checkmatechallenge;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ChessView chessView;
    private List<ChessLevel> gameLevels;
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
        ChessLevel level2 = createLevel(4,2);

        // Add pieces to the level as needed
        chessView.setChessDelegate(level2);

        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level2.resetLevel();
                chessView.invalidate();
            }
        });

        findViewById(R.id.undoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                level2.undoLastMove();
                chessView.invalidate();
            }
        });
    }

    private ChessLevel createLevel(int rows, int cols) {
        // Implement this method to create the desired level with specific rows, columns, and pieces
        // Add pieces to the set according to the level requirements
        pieces.add(new ChessPiece(0, 0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.queen_white));
        pieces.add(new ChessPiece(1, 1, ChessPlayer.WHITE, ChessPieceType.KNIGHT, R.drawable.knight_white));
        pieces.add(new ChessPiece(3, 1, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.king_black));

        return new ChessLevel(rows, cols, pieces);
    }
}
