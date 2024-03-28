package com.grandmasters.checkmatechallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
public class ChessView extends View {
    private ChessDelegate chessDelegate;
    private Paint paint;
    private int rows;
    private int cols;
    private float cellSize;
    private int selectedCol;
    private int selectedRow;
    private Boolean pieceSelected;
    private float margin;
    private Canvas canvas;
    private float boardTop;
    private float boardLeft;
    private float boardWidth;
    private float boardHeight;
    private static final String TAG = "ChessView";
    private final Set<Integer> imgResIds = new HashSet<>();
    private final Map<Integer, Bitmap> bitmaps = new HashMap<>();
    private float availableWidth;
    private float availableHeight;
    public ChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getImgResIds();
        loadBitmaps();
        paint = new Paint();

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
    public void setChessDelegate(ChessDelegate chessDelegate) {
        this.chessDelegate = chessDelegate;
        this.rows = chessDelegate.getRows();
        this.cols = chessDelegate.getColumns();
        invalidate(); // Redraw the view when the delegate is set
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvas = new Canvas(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888));
        margin = 20;
        availableWidth = canvas.getWidth() - 2 * margin;
        availableHeight = canvas.getHeight() - 2 * margin;
        // Calculate the cell size based on the maximum number of columns and rows and the available width and height
        cellSize = Math.min(availableWidth / 5, availableHeight / 6);
        // Calculate the size of the chessboard
        boardWidth = cellSize * cols;
        boardHeight = cellSize * rows;

        // Calculate the position of the chessboard to center it on the screen with margins
        boardLeft = (canvas.getWidth() - boardWidth) / 2;
        boardTop = (canvas.getHeight() - boardHeight) / 2;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (chessDelegate != null) {
            drawChessBoard(canvas);
            drawPieces(canvas);
            if (selectedCol != -1 && selectedRow != -1) {
                drawSelectionBorder(canvas, selectedRow, selectedCol);
            }
        }
    }

    // Method to draw chessboard on canvas
    private void drawChessBoard(@NonNull Canvas canvas) {
        for (int col=0; col<=cols-1; col++) {
            for (int row=0; row<=rows; row++) {
                drawSquareAt(canvas, col, row, (col + row) % 2 == 0);
            }
        }
    }

    // Method to draw squares at given positions and given color
    private void drawSquareAt(Canvas canvas, int col, int row, boolean isDark) {
        paint.setShadowLayer(20, 0, 0, Color.GRAY);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
        if(row != chessDelegate.getRows()) {
            if (isDark) {
                paint.setColor(Color.parseColor("#d2ba7f"));
            } else {
                paint.setColor(Color.parseColor("#fef0bf"));
            }
            canvas.drawRect(boardLeft + col * cellSize, boardTop + (rows - row - 1) * cellSize, boardLeft + (col + 1) * cellSize, boardTop + ((rows - row - 1) + 1) * cellSize, paint);
        }
        else {
            if (isDark) {
                paint.setColor(Color.parseColor("#ad9a68"));
            } else {
                paint.setColor(Color.parseColor("#c2b78f"));
            }
            canvas.drawRect(boardLeft + col * cellSize, boardTop + row * cellSize, boardLeft + (col + 1) * (cellSize), boardTop + (row + 1) * (cellSize - 37), paint);
        }
    }

    // Method to draw chess pieces
    private void drawPieces(Canvas canvas) {
        for (int col=0; col<=cols-1; col++) {
            for (int row=0; row<=rows-1; row++) {
                ChessPiece piece = chessDelegate.pieceAt(col, row);
                if (piece != null) {
                    drawPiece(canvas, col, row, piece.resId);
                }
            }
        }
    }

    private void drawPiece(Canvas canvas, int col, int row, int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        canvas.drawBitmap(bitmap, null, new RectF(boardLeft + col * cellSize, boardTop + (rows - 1 - row) * cellSize, boardLeft + (col + 1) * cellSize, boardTop + ((rows - 1 - row) + 1) * cellSize) , paint);
    }

    private void drawSelectionBorder(Canvas canvas, int row, int col) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        // Calculate the coordinates of the selected cell
        float left = boardLeft + col * cellSize;
        float top = boardTop + (rows - 1 - row) * cellSize;
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
                int clickedRow = rows - 1 - (int) ((event.getY() - boardTop) / cellSize);
                if ((event.getY() - boardTop) < 0) {
                    clickedRow = rows;
                }
                Log.d(TAG, String.valueOf(clickedRow));
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
}

