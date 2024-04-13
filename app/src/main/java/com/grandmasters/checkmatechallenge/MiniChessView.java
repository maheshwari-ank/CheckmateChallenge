package com.grandmasters.checkmatechallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class MiniChessView extends View {
    private ChessDelegate chessDelegate;
    private Paint paint;
    private int rows;
    private int cols;
    private float cellSize;
    private float margin;
    private Canvas canvas;
    private float boardTop;
    private float boardLeft;
    private float availableWidth;
    private float availableHeight;
    private float boardWidth;
    private float boardHeight;

    public MiniChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
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
        }
    }

    private void drawChessBoard(@NonNull Canvas canvas) {
        for (int col=0; col<=cols-1; col++) {
            for (int row=0; row<=rows; row++) {
                drawSquareAt(canvas, new Square(col, row), (col + row) % 2 == 0);
            }
        }
    }

    private void drawSquareAt(Canvas canvas, Square square, boolean isDark) {
        int row = square.getRow();
        int col = square.getCol();
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
        if(row != chessDelegate.getRows()) {
            if (isDark) {
                paint.setColor(Color.parseColor("#d2ba7f"));
            } else {
                paint.setColor(Color.parseColor("#fef0bf"));
            }
            canvas.drawRect(boardLeft + col * cellSize, boardTop + (rows - row - 1) * cellSize, boardLeft + (col + 1) * cellSize, boardTop + ((rows - row - 1) + 1) * cellSize, paint);
        }
    }

    private void drawPieces(Canvas canvas) {
        for (int col=0; col<=cols-1; col++) {
            for (int row=0; row<=rows-1; row++) {
                ChessPiece piece = chessDelegate.pieceAt(new Square(col, row));
                if (piece != null) {
                    drawPiece(canvas, new Square(col, row), piece.getPlayer().equals(ChessPlayer.BLACK));
                }
            }
        }
    }

    private void drawPiece(Canvas canvas, Square square, boolean isBlack) {
        int col = square.getCol();
        int row = square.getRow();
        float squareSize = cellSize * 0.5f;
        float left = boardLeft + col * cellSize + (cellSize - squareSize) / 2;
        float top = boardTop + (rows - row - 1) * cellSize + (cellSize - squareSize) / 2;
        float right = left + squareSize;
        float bottom = top + squareSize;

        if (isBlack) {
            paint.setColor(Color.BLACK);
        } else {
            paint.setColor(Color.WHITE);
        }
        canvas.drawRect(left, top, right, bottom, paint);
    }



}
