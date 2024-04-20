package com.grandmasters.checkmatechallenge;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "checkmate_challenge.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHelper";
    private static SQLiteDatabase database;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the Levels table
        db.execSQL("CREATE TABLE Levels (" +
                "level_id INTEGER PRIMARY KEY," +
                "rows INTEGER," +
                "cols INTEGER," +
                "is_solved Boolean," +
                "user_player TEXT," +
                "mate_in INTEGER)");

        // Create the ChessPieces table
        db.execSQL("CREATE TABLE ChessPieces (" +
                "piece_id INTEGER PRIMARY KEY," +
                "level_id," +
                "`row` INTEGER," +
                "col INTEGER," +
                "piece_type TEXT," +
                "piece_player TEXT," +
                "image_resource_id INTEGER," +
                "FOREIGN KEY(level_id) REFERENCES Levels(level_id))");

//        db.close();
    }

    @SuppressLint("Range")
    public int getFirstUnsolvedLevelId() {
        int levelId = -1; // Default value if no unsolved level is found
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT level_id FROM Levels WHERE is_solved = 0 LIMIT 1", null);
        if (cursor.moveToFirst()) {
            levelId = cursor.getInt(cursor.getColumnIndex("level_id"));
        }
        cursor.close();
        return levelId;
    }

    @SuppressLint("Range")
    public int getMateInForLevel(int levelId) {
        int mateIn = -1; // Default value if level is not found or is solved
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT mate_in FROM Levels WHERE level_id = " + String.valueOf(levelId) + " ";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            mateIn = cursor.getInt(cursor.getColumnIndex("mate_in"));
        }
        cursor.close();
        return mateIn;
    }

    public void updateLevelSolvedStatus(int levelId, boolean isSolved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_solved", isSolved ? 1 : 0);
        String whereClause = "level_id = ?";
        String[] whereArgs = {String.valueOf(levelId)};
        int rowsAffected = db.update("Levels", values, whereClause, whereArgs);
        db.close();
    }

    public long insertLevel(int levelId, int rows, int cols, boolean isSolved, ChessPlayer userPlayer, int mateIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("level_id", levelId);
        values.put("rows", rows);
        values.put("cols", cols);
        values.put("is_solved", isSolved);
        values.put("user_player", userPlayer.name());
        values.put("mate_in", mateIn);
        long result = db.insert("Levels", null, values);
        db.close();
        return result;
    }
    public long insertChessPiece(long levelId, int row, int col, String piecePlayer, String pieceType, int imageResourceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("level_id", levelId);
        values.put("row", row);
        values.put("col", col);
        values.put("piece_player", piecePlayer);
        values.put("piece_type", pieceType);
        values.put("image_resource_id", imageResourceId);
        long result = db.insert("ChessPieces", null, values);
        db.close();
        return result;
    }

    @SuppressLint("Range")
    public List<ChessLevel> getAllLevels() {
        List<ChessLevel> levelDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Levels", null);
        if (cursor.moveToFirst()) {
            do {
                ChessLevel levelData = new ChessLevel();
                levelData.setLevelId(cursor.getInt(cursor.getColumnIndex("level_id")));
                levelData.setRows(cursor.getInt(cursor.getColumnIndex("rows")));
                levelData.setColumns(cursor.getInt(cursor.getColumnIndex("cols")));
                levelData.setSolved(cursor.getInt(cursor.getColumnIndex("is_solved")) == 1);
                levelData.setUserPlayer(ChessPlayer.valueOf(cursor.getString(cursor.getColumnIndex("user_player"))));
                levelDataList.add(levelData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return levelDataList;
    }

    @SuppressLint("Range")
    public List<ChessPiece> getPiecesForLevel(int levelId) {
        List<ChessPiece> pieceDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {String.valueOf(levelId)};
        // Assuming db is your SQLiteDatabase instance
        // Define the SQL query with a placeholder for the parameter
        String query = "SELECT * FROM ChessPieces WHERE level_id = " + String.valueOf(levelId) + " ";

        // Execute the query and get the cursor
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ChessPiece pieceData = new ChessPiece();
                pieceData.setPieceId(cursor.getInt(cursor.getColumnIndex("piece_id")));
                pieceData.setLevelId(cursor.getInt(cursor.getColumnIndex("level_id")));
                pieceData.setRow(cursor.getInt(cursor.getColumnIndex("row")));
                pieceData.setCol(cursor.getInt(cursor.getColumnIndex("col")));
                pieceData.setPlayer(ChessPlayer.valueOf(cursor.getString(cursor.getColumnIndex("piece_player"))));
                pieceData.setPieceType(ChessPieceType.valueOf(cursor.getString(cursor.getColumnIndex("piece_type"))));
                pieceData.setResId(cursor.getInt(cursor.getColumnIndex("image_resource_id")));
                pieceDataList.add(pieceData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pieceDataList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database needs to be upgraded.
        db.execSQL("DROP TABLE IF EXISTS Levels");
        db.execSQL("DROP TABLE IF EXISTS LevelLayout");
        db.execSQL("DROP TABLE IF EXISTS ChessPieces");
        onCreate(db); // Recreate the tables with the new schema
    }
}

