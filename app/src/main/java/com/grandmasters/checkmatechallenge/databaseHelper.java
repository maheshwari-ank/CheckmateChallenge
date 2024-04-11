package com.grandmasters.checkmatechallenge;


import android.content.Context;
import android.util.Log;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import android.database.Cursor;
import java.util.List;




import androidx.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "checkmate_challenge.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase database;

    public databaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the Levels table
        db.execSQL("CREATE TABLE Levels (" +
                "level_id INTEGER PRIMARY KEY," +
                "level_name TEXT," +
                "is_solved INTEGER)");

        // Create the LevelLayout table
        db.execSQL("CREATE TABLE LevelLayout (" +
                "layout_id INTEGER PRIMARY KEY," +
                "level_id INTEGER," +
                "rows INTEGER," +
                "cols INTEGER," +
                "FOREIGN KEY(level_id) REFERENCES Levels(level_id))");

        // Create the ChessPieces table
        db.execSQL("CREATE TABLE ChessPieces (" +
                "piece_id INTEGER PRIMARY KEY," +
                "level_id INTEGER," +
                "`row` INTEGER," +
                "col INTEGER," +
                "piece_type TEXT," +
                "FOREIGN KEY(level_id) REFERENCES Levels(level_id))");
// "count INTEGER," +

//    SQLiteDatabase database = this.getWritableDatabase();

        database.close();
    }

    public long insertLevel(String levelName, int isSolved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("level_name", levelName);
        values.put("is_solved", isSolved);
        long result = db.insert("Levels", null, values);
        db.close();
        return result;
    }

    public long insertLevelLayout(long levelId, int rows, int cols) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("level_id", levelId);
        values.put("rows", rows);
        values.put("cols", cols);
        long result = db.insert("LevelLayout", null, values);
        db.close();
        return result;
    }

    public long insertChessPiece(long levelId, int row, int col, String pieceType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("level_id", levelId);
        values.put("row", row);
        values.put("col", col);

        values.put("piece_type", pieceType);
        long result = db.insert("ChessPieces", null, values);
        db.close();
        return result;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database needs to be upgraded.
        // You should handle any schema changes or data migrations here.
        // For now, you can just drop and recreate the tables:

        db.execSQL("DROP TABLE IF EXISTS Levels");
        db.execSQL("DROP TABLE IF EXISTS LevelLayout");
        db.execSQL("DROP TABLE IF EXISTS ChessPieces");
        onCreate(db); // Recreate the tables with the new schema
    }

//    public List<ChessPiece> getChessPieces() {
//        List<ChessPiece> chessPieces = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM ChessPieces";
//        Cursor cursor = db.rawQuery(query, null);
//
//        if (cursor != null) {
//            String[] columnNames = cursor.getColumnNames();
//            for (String columnName : columnNames) {
//                Log.d("Column Name", columnName);
//            }
//
//            if (cursor.moveToFirst()) {
//                do {
//                    String rowValue = cursor.getString(cursor.getColumnIndex("row"));
//                    String colValue = cursor.getString(cursor.getColumnIndex("col"));
//                    String colorValue = cursor.getString(cursor.getColumnIndex("color"));
//                    String pieceTypeValue = cursor.getString(cursor.getColumnIndex("piece_type"));
//
//                    Log.d("CursorDebug", "Row: " + rowValue + ", Col: " + colValue + ", Color: " + colorValue + ", Piece Type: " + pieceTypeValue);
//
//                    ChessPiece piece = new ChessPiece(Integer.parseInt(rowValue), Integer.parseInt(colValue), colorValue, pieceTypeValue);
//                    chessPieces.add(piece);
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close();
//        }
//        db.close();
//        return chessPieces;
//    }


    public ArrayList<ChessModel> getLevelDetails(long levelId) {
        SQLiteDatabase db = this.getReadableDatabase();



        String query = "SELECT Levels.level_id, level_name, is_solved, layout_id, rows, cols, `row`, col, color, piece_type " +
                "FROM Levels " +
                "JOIN LevelLayout ON Levels.level_id = LevelLayout.level_id " +
                "JOIN ChessPieces ON Levels.level_id = ChessPieces.level_id " +
                "WHERE Levels.level_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(levelId)});

        ChessModel chessModel = null;
        ArrayList<ChessModel> chessModels = new ArrayList<>();
        ArrayList<ChessPiece> chessPieces = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do{
                String levelName = cursor.getString(0);
                int isSolved = cursor.getInt(1);
                int layoutId = cursor.getInt(2);
                int rows = cursor.getInt(3);
                int cols = cursor.getInt(4);
                chessModel = new ChessModel(levelId, levelName, isSolved, layoutId, rows, cols);

                do {
                    int row = cursor.getInt(5);
                    int col = cursor.getInt(6);
                    ChessPieceType pieceType = ChessPieceType.valueOf(cursor.getString(7));
                    ChessPiece piece = new ChessPiece(row, col, pieceType); // Assuming ChessPiece constructor
                    chessPieces.add(piece);
                    chessModel.addChessPiece(piece);
                } while (cursor.moveToNext());

//            chessModel = new ChessModel(levelId, levelName, isSolved, layoutId, rows, cols, chessPieces);
//            ChessModel chessModel = new ChessModel(levelId, levelName, isSolved, layoutId, rows, cols);
                chessModels.add(chessModel);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return chessModels;
    }



}

