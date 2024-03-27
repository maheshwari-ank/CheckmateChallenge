package com.grandmasters.checkmatechallenge;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Color;

import androidx.annotation.NonNull;

import java.util.Random;

public class ChessBoardGenerator {
    private static final int MAX_ROWS = 6;
    private static final int MAX_COLS = 5;
    private static Paint paint = new Paint();
    public static void generateChessboardConfig(Canvas canvas, int cols, int rows) {
        if (cols > MAX_COLS || rows > MAX_ROWS) {
            System.out.println("Number of columns or rows exceeds maximum limit.");
            return;
        }

        // Calculate the cell size based on the maximum number of columns and rows
        float cellSize = Math.min((float) canvas.getWidth() / MAX_COLS, (float) canvas.getHeight() / MAX_ROWS);

        // Calculate the size of the chessboard
        float boardWidth = cellSize * cols;
        float boardHeight = cellSize * rows;

        // Define the margin
        float margin = 20; // Adjust this value as needed

        // Calculate the position of the chessboard to center it on the screen with margins
        float boardLeft = (canvas.getWidth() - boardWidth) / 2 + margin;
        float boardTop = (canvas.getHeight() - boardHeight) / 2 + margin;

        // Draw the chessboard
        drawCustomChessBoard(canvas, cols, rows, boardLeft, boardTop, cellSize);
    }

    private static void drawCustomChessBoard(Canvas canvas, int cols, int rows, float boardLeft, float boardTop, float cellSize) {
        // Draw the chessboard
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if ((i + j) % 2 == 0) {
                    paint.setColor(Color.LTGRAY);
                } else {
                    paint.setColor(Color.DKGRAY);
                }
                canvas.drawRect(boardLeft + i * cellSize, boardTop + j * cellSize, boardLeft + (i + 1) * cellSize, boardTop + (j + 1) * cellSize, paint);
            }
        }
    }

//    private void drawCustomChessBoard(@NonNull Canvas canvas, int cols, int rows) {
//        // Define the maximum number of columns and rows
//        int maxCols = 5;
//        int maxRows = 6;
//
//        // Define the margin
//        float margin = 20; // Adjust this value as needed
//        paint.setShadowLayer(15, 0, 0, Color.GRAY);
//
//        // Important for certain APIs
//        setLayerType(LAYER_TYPE_SOFTWARE, paint);
//        // Calculate the available width and height for the chessboard after considering the margin
//        availableWidth = canvas.getWidth() - 2 * margin;
//        availableHeight = canvas.getHeight() - 2 * margin;
//
//        // Calculate the cell size based on the maximum number of columns and rows and the available width and height
//        cellSize = Math.min(availableWidth / maxCols, availableHeight / maxRows);
//
//        // Calculate the size of the chessboard
//        boardWidth = cellSize * cols;
//        boardHeight = cellSize * rows;
//
//        // Calculate the position of the chessboard to center it on the screen with margins
//        boardLeft = (canvas.getWidth() - boardWidth) / 2;
//        boardTop = (canvas.getHeight() - boardHeight) / 2;
//
//        // Draw the chessboard
//        for (int i = 0; i < cols; i++) {
//            for (int j = 0; j <= rows; j++) {
//                if(j != rows) {
//                    if ((i + j) % 2 == 0) {
//                        paint.setColor(Color.LTGRAY);
//                    } else {
//                        paint.setColor(Color.DKGRAY);
//                    }
//                    canvas.drawRect(boardLeft + i * cellSize, boardTop + j * cellSize, boardLeft + (i + 1) * cellSize, boardTop + (j + 1) * cellSize, paint);
//                }
//                else {
//                    if ((i + j) % 2 == 0) {
//                        paint.setColor(Color.DKGRAY);
//                    } else {
//                        paint.setColor(Color.GRAY);
//                    }
//                    canvas.drawRect(boardLeft + i * cellSize, boardTop + j * cellSize, boardLeft + (i + 1) * (cellSize), boardTop + (j+1) * (cellSize - 25), paint);
//                }
//            }
//        }
//    }

}
