package com.grandmasters.checkmatechallenge;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class GrandMastersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grandmaster);

    }



    private void openMainActivityWithLevel(ChessLevel level) {
        // Start MainActivity with the selected level
        Intent intent = new Intent(GrandMastersActivity.this, MainActivity.class);
        intent.putExtra("SELECTED_LEVEL", level);
        startActivity(intent);
        finish();
    }

}
