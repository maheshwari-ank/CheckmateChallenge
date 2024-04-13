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

public class LevelsActivity extends AppCompatActivity {
    private MiniChessView miniChessView;
    private GridLayout levelsGrid;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        dataManager = new DataManager(getApplicationContext());
        levelsGrid = findViewById(R.id.levels_grid);

        // Fetch levels from the database, assuming you have a method to do so
        List<ChessLevel> levels = dataManager.getAllLevels();

        // Iterate over the levels and create a CardView for each
        int columnCount = 0;
        for (ChessLevel level : levels) {
            if (columnCount == 3) {
                columnCount = 0;
            }
            CardView cardView = createCardView(level);
            levelsGrid.addView(cardView);
            columnCount++;
        }
    }

//    private CardView createCardView(ChessLevel level) {
//
//        // Inflate the custom card layout directly into a View
//        View cardViewLayout = LayoutInflater.from(this).inflate(R.layout.custom_card_layout, null);
//
//        // Find the CardView inside the inflated layout
//        CardView cardView = cardViewLayout.findViewById(R.id.cardView);
//
//        // Find views inside the custom layout
//        ChessView chessView = cardViewLayout.findViewById(R.id.chess_view);
//
//        // Inflate the custom card layout
////        View cardView = LayoutInflater.from(this).inflate(R.layout.custom_card_layout, null);
//
//        // Inflate the custom card layout directly into a View
////        View cardViewLayout = LayoutInflater.from(this).inflate(R.layout.custom_card_layout, null);
//
//        // Find the CardView inside the inflated layout
////        CardView cardView = cardViewLayout.findViewById(R.id.cardView);
//
//        // Find views inside the custom layout
////        ChessView chessView = cardView.findViewById(R.id.chess_view);
//        // Add other views here
//
//        // Setup ChessView (if needed)
//        chessView.setChessDelegate(level);
//        CardView card = (CardView) cardViewLayout;
//        // Create a CardView and set the custom layout as its content
////        CardView card = new CardView(this);
//        card.setLayoutParams(new CardView.LayoutParams(
//                CardView.LayoutParams.MATCH_PARENT,
//                CardView.LayoutParams.WRAP_CONTENT)); // Set the CardView's layout parameters
//        card.addView(cardView);
////        card.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT));
////        card.addView(cardView);
//
//        return card;
//    }

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
//        textView.style
        int firstUnsolvedId = dataManager.getUnsolvedLevelId();
        if(level.isSolved() || level.getLevelId() == firstUnsolvedId) {
            // Add ChessView to the CardView
            MiniChessView miniChessView = new MiniChessView(this, null);
            miniChessView.setChessDelegate(level);
            if(level.isSolved()) {

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
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openMainActivityWithLevel(level);
                finish();
            }
        });

        return cardView;
    }

    private void openMainActivityWithLevel(ChessLevel level) {
        // Start MainActivity with the selected level
        Intent intent = new Intent(LevelsActivity.this, MainActivity.class);
        intent.putExtra("SELECTED_LEVEL", level);
        LevelsActivity.this.startActivity(intent);
    }
}
