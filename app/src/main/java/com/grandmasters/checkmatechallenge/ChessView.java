package com.grandmasters.checkmatechallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChessView extends View {
    private float margin;
    private Canvas canvas;
    private float cellSize;
    private float boardTop;
    private float boardLeft;
    private float boardWidth;
    private float boardHeight;
    private static final String TAG = "ChessView";
    private final Set<Integer> imgResIds = new HashSet<>();
    private final Map<Integer, Bitmap> bitmaps = new HashMap<>();
    private final Paint paint = new Paint();
    private float availableWidth;
    private float availableHeight;
    private ChessDelegate chessDelegate;

    private int selectedCol;
    private int selectedRow;
    private Boolean pieceSelected;
    public void setChessDelegate(ChessDelegate chessDelegate) {
        this.chessDelegate = chessDelegate;
    }

    public ChessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getImgResIds();
        loadBitmaps();
        this.selectedCol = -1;
        this.selectedRow = -1;
    }

    // Method Get Chess Pieces Resource Ids and save them in a Hash set
    private void getImgResIds() {
        imgResIds.add(R.drawable.queen_white);
        imgResIds.add(R.drawable.queen_black);
        imgResIds.add(R.drawable.bishop_white);
        imgResIds.add(R.drawable.bishop_black);
        imgResIds.add(R.drawable.rook_white);
        imgResIds.add(R.drawable.rook_black);
        imgResIds.add(R.drawable.king_white);
        imgResIds.add(R.drawable.king_black);
        imgResIds.add(R.drawable.knight_black);
        imgResIds.add(R.drawable.knight_white);
        imgResIds.add(R.drawable.pawn_white);
        imgResIds.add(R.drawable.pawn_black);
    }

    // Method to load Chess Pieces Resource Ids as key and and their bitmaps as a value in Hash map.
    private void loadBitmaps() {
        imgResIds.forEach(it -> bitmaps.put(it, BitmapFactory.decodeResource(getResources(), it)));
    }

    // Method to define board size as per the canvas dimensions
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvas = new Canvas(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888));
        margin = 30;
        availableWidth = canvas.getWidth() - 2 * margin;
        availableHeight = canvas.getHeight() - 2 * margin;

        // Calculate the cell size based on the maximum number of columns and rows and the available width and height
        cellSize = Math.min(availableWidth / 8, availableHeight / 8);

        // Calculate the size of the chessboard
        boardWidth = cellSize * 8;
        boardHeight = cellSize * 8;

        // Calculate the position of the chessboard to center it on the screen with margins
        boardLeft = (canvas.getWidth() - boardWidth) / 2;
        boardTop = (canvas.getHeight() - boardHeight) / 2;
    }
    // Method to draw chess pieces
    private void drawPieces(Canvas canvas) {
        for (int col=0; col<=7; col++) {
            for (int row=0; row<=7; row++) {
                ChessPiece piece = chessDelegate.pieceAt(col, row);
                if (piece != null) {
                    drawPieceAt(canvas, col, row, piece.resId);
                }
            }
        }
    }

    // Method to provide position for the chess piece to be drawn
    private void drawPieceAt(Canvas canvas, int col, int row, int resId) {
        Bitmap bitmap = bitmaps.get(resId);
        canvas.drawBitmap(bitmap, null, new RectF(boardLeft + col * cellSize, boardTop + (7 -row) * cellSize, boardLeft + (col + 1) * cellSize, boardTop + ((7 -row) + 1) * cellSize) , paint);
    }

    // Method to draw on canvas
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        drawChessBoard(canvas);
        drawPieces(canvas);
        // Draw selection border if a piece is selected
        if (selectedCol != -1 && selectedRow != -1) {
            drawSelectionBorder(canvas, selectedRow, selectedCol);
        }
    }

    private void drawSelectionBorder(Canvas canvas, int row, int col) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        // Calculate the coordinates of the selected cell
        float left = boardLeft + col * cellSize;
        float top = boardTop + (7 - row) * cellSize;
        float right = left + cellSize;
        float bottom = top + cellSize;

        // Draw the border around the selected cell
        canvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int clickedCol = (int) ((event.getX() - boardLeft) / cellSize);
                int clickedRow = 7 - (int) ((event.getY() - boardTop) / cellSize);

                if (selectedCol == clickedCol && selectedRow == clickedRow) {
                    // If the same cell is clicked again, deselect it
                    selectedCol = -1;
                    selectedRow = -1;
                    invalidate(); // Redraw the view to remove the selection
                } else {
                    if (selectedCol != -1 && selectedRow != -1) {
                        // If a piece is already selected and clicked on a non-empty cell, move the piece
                        int destinationCol = clickedCol;
                        int destinationRow = clickedRow;
                        chessDelegate.movePiece(selectedCol, selectedRow, destinationCol, destinationRow);
                        selectedCol = -1;
                        selectedRow = -1;
                        invalidate(); // Redraw the view after moving
                    } else {
                        // If the clicked cell is not empty, select it
                        if (chessDelegate.pieceAt(clickedCol, clickedRow) != null) {
                            selectedCol = clickedCol;
                            selectedRow = clickedRow;
                            invalidate(); // Redraw the view to show selection
                        }
                    }
                }
                break;
        }
        return true;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // Check if there's a piece at the clicked position
//                int clickedCol = (int) ((event.getX() - boardLeft) / cellSize);
//                int clickedRow = (int) ( 8 - ((event.getY() - boardTop) / cellSize));
//                // Check if the clicked cell is already selected, if yes, deselect it
//                if (selectedCol == clickedCol && selectedRow == clickedRow) {
//                    selectedCol = -1;
//                    selectedRow = -1;
//                    invalidate(); // Redraw the view to remove the selection
//                } else if (chessDelegate.pieceAt(clickedCol, clickedRow) != null) {
//                    selectedCol = clickedCol;
//                    selectedRow = clickedRow;
//                    invalidate(); // Redraw the view to show selection
//                } else if (selectedCol != -1 && selectedRow != -1) {
//
//                    // If a piece is selected and clicked on an empty cell, move the piece
//                    int destinationCol = clickedCol;
//                    int destinationRow = clickedRow;
//                    chessDelegate.movePiece(selectedCol, selectedRow, destinationCol, destinationRow);
//                    selectedCol = -1;
//                    selectedRow = -1;
//                }
//                break;
//        }
//        return true;
//    }

    // Method to draw chessboard on canvas
    private void drawChessBoard(@NonNull Canvas canvas) {
        for (int col=0; col<=7; col++) {
            for (int row=0; row<=8; row++) {
                drawSquareAt(canvas, col, row, (col + row) % 2 == 0);
            }
        }
    }

    // Method to draw squares at given positions and given color
    private void drawSquareAt(Canvas canvas, int col, int row, boolean isDark) {
        paint.setShadowLayer(20, 0, 0, Color.GRAY);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
        if(row != 8) {
            if (isDark) {
                paint.setColor(Color.parseColor("#d2ba7f"));
            } else {
                paint.setColor(Color.parseColor("#fef0bf"));
            }
            canvas.drawRect(boardLeft + col * cellSize, boardTop + (7 -row) * cellSize, boardLeft + (col + 1) * cellSize, boardTop + ((7 -row)  + 1) * cellSize, paint);
        }
        else {
            if (isDark) {
                paint.setColor(Color.parseColor("#ad9a68"));
            } else {
                paint.setColor(Color.parseColor("#c2b78f"));
            }
            canvas.drawRect(boardLeft + col * cellSize, boardTop + row * cellSize, boardLeft + (col + 1) * (cellSize), boardTop + (row+1) * (cellSize - 13), paint);
        }
    }

}
