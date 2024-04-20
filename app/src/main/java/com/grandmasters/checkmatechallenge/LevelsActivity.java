package com.grandmasters.checkmatechallenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class LevelsActivity extends AppCompatActivity {
    private MiniChessView miniChessView;
    private GridLayout levelsGrid;
    private DataManager dataManager;
    private ChessLevel currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
//        dataManager = new DataManager(getApplicationContext());
        dataManager = DataManagerSingleton.getInstance(getApplicationContext());
        levelsGrid = findViewById(R.id.levels_grid);

        // Fetch levels from the database, assuming you have a method to do so
        List<ChessLevel> levels = dataManager.getAllLevels();

        // Iterate over the levels and create a CardView for each
        int columnCount = 0;
        for (ChessLevel level : levels) {
            if (columnCount == 3) {
                columnCount = 0;
            }
            Set<ChessPiece> originalPieces = level.getPiecesBox();
            Set<ChessPiece> copiedPieces = level.deepCopySet(originalPieces);
            level.setPiecesBoxOriginalState(copiedPieces);

            CardView cardView = createCardView(level);
            levelsGrid.addView(cardView);
            columnCount++;
        }
    }


    private CardView createCardView(ChessLevel level) {
        CardView cardView = new CardView(this);
//        int cardWidth = getResources().getDisplayMetrics().widthPixels / 3;
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = 120; // Set to 0 to distribute equally in GridLayout
        layoutParams.height = 300;
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Equal width for each column
        layoutParams.setMargins(8, 8, 8, 8); // Set margins as needed
        cardView.setLayoutParams(layoutParams);
        cardView.setCardElevation(12);
        cardView.setCardBackgroundColor(Color.parseColor("#2b755b"));
        TextView textView = new TextView(getApplicationContext(), null);
        textView.setText(String.valueOf(level.getLevelId()));
        textView.setTextColor(Color.parseColor("White"));

        ImageView tickIcon = new ImageView(this);
        tickIcon.setImageResource(R.drawable.icons8_lock);
//        textView.style
        int firstUnsolvedId = dataManager.getUnsolvedLevelId();
        if(level.isSolved() || level.getLevelId() == firstUnsolvedId) {
            // Add ChessView to the CardView
            MiniChessView miniChessView = new MiniChessView(this, null);
            miniChessView.setChessDelegate(level);
            if(level.isSolved()) {
                // Create an ImageView for the check icon
                ImageView checkIcon = new ImageView(this);
                checkIcon.setImageResource(R.drawable.icons8_checkmark); // Replace R.drawable.check_icon with the actual resource ID of your check icon
                // Set layout parameters for the check icon
                CardView.LayoutParams checkParams = new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT);
                checkParams.gravity = Gravity.TOP | Gravity.END; // Position the icon at the top right corner
                checkIcon.setLayoutParams(checkParams);
                // Add the check icon to the CardView
                cardView.addView(checkIcon);
            }
            cardView.addView(miniChessView);
            cardView.addView(textView);
        }
        else {
            // Create an ImageView for the lock icon
            ImageView lockIcon = new ImageView(this);
            lockIcon.setImageResource(R.drawable.icons8_lock); // Replace R.drawable.lock_icon with the actual resource ID of your lock icon
            // Set layout parameters for the lock icon
            CardView.LayoutParams lockParams = new CardView.LayoutParams(CardView.LayoutParams.WRAP_CONTENT, CardView.LayoutParams.WRAP_CONTENT);
            lockParams.gravity = Gravity.CENTER;
            lockIcon.setLayoutParams(lockParams);
            // Add the lock icon to the CardView
            cardView.addView(lockIcon);
            cardView.addView(textView);
        }

        // Set click listener for the card
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openMainActivityWithLevel(level);
//            }
//        });

        if (level.isSolved() || level.getLevelId() == dataManager.getUnsolvedLevelId()) {
            // Level is solved or the first unsolved level, allow opening it
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMainActivityWithLevel(level);
                }
            });
        } else {
            // Level is locked, disable click listener
            cardView.setClickable(false);
        }
        return cardView;
    }

    private void openMainActivityWithLevel(ChessLevel level) {
        // Start MainActivity with the selected level
        Intent intent = new Intent(LevelsActivity.this, MainActivity.class);
        intent.putExtra("SELECTED_LEVEL", level);
        startActivity(intent);
        finish();
    }


}
