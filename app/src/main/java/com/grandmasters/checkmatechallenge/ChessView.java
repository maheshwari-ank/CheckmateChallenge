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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
public class ChessView extends View{
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
    private Graph boardGraph;
    private boolean bidirectional;
    private Square newSource;
    public boolean userTurn = true;
    public boolean opponentTurn = false;
    private ChessLevel currentLevel;
    private final int maxDepth = 4;

    public ChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getImgResIds();
        loadBitmaps();
        paint = new Paint();
        this.selectedCol = -1;
        this.selectedRow = -1;

    }

    // Define an interface for checkmate callback
    public interface OnCheckmateListener {
        void onCheckmate();
    }

    // Declare a member variable to hold the listener
    private OnCheckmateListener checkmateListener;

    // Setter method for the listener
    public void setOnCheckmateListener(OnCheckmateListener listener) {
        this.checkmateListener = listener;
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
        this.boardGraph = chessDelegate.getBoardGraph();
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
                drawSelectionBorder(canvas, new Square(selectedCol, selectedRow));
            }
            if (selectedCol != -1 && selectedRow != -1) {
                highlightPossibleMoves(canvas, selectedCol, selectedRow);
            }
//            if (newSource != null && kingInCheck(newSource)) {
//                drawHighlight(canvas,getKingPosition());
//            }

        }
    }

    // Method to draw chessboard on canvas
    private void drawChessBoard(@NonNull Canvas canvas) {
        for (int col=0; col<=cols-1; col++) {
            for (int row=0; row<=rows; row++) {
                drawSquareAt(canvas, new Square(col, row), (col + row) % 2 == 0);
            }
        }
    }

    // Method to draw squares at given positions and given color
//    private void drawSquareAt(Canvas canvas, int col, int row, boolean isDark) {
    private void drawSquareAt(Canvas canvas, Square square, boolean isDark) {
        int row = square.getRow();
        int col = square.getCol();
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
                ChessPiece piece = chessDelegate.pieceAt(new Square(col, row));
                if (piece != null) {
                    drawPiece(canvas, new Square(col, row), piece.resId);
                }
            }
        }
    }

    private void drawPiece(Canvas canvas, Square square, int resId) {
        int col = square.getCol();
        int row = square.getRow();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        canvas.drawBitmap(bitmap, null, new RectF(boardLeft + col * cellSize, boardTop + (rows - 1 - row) * cellSize, boardLeft + (col + 1) * cellSize, boardTop + ((rows - 1 - row) + 1) * cellSize) , paint);
    }

    private void drawSelectionBorder(Canvas canvas, Square square) {
        int row = square.getRow();
        int col = square.getCol();
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

    public Square getKingPosition(){
        for(Square square : boardGraph.getVertices()) {
            if (chessDelegate.pieceAt(square) != null && chessDelegate.pieceAt(square).getPieceType() == ChessPieceType.KING && chessDelegate.pieceAt(square).getPlayer() == ChessPlayer.BLACK) {
                return square;
            }
        }
        return null;
    }

    public boolean isCheckMate(Square kingPos){
        boolean checkFlag = false;
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
//                ChessPiece piece = chessDelegate.pieceAt(new Square(col, row));
                if (chessDelegate.canKingMove(kingPos, new Square(col, row))) {
                    checkFlag = true;
                    continue;
                }
            }
        }
        return !checkFlag;
    }

    public boolean kingInCheck(Square toSquare) {
        for (Square vertex : boardGraph.getVertices()) {
            boardGraph.removeEdgesFromVertex(vertex);
        }
        // Add edges to the graph based on possible moves
        for (int c = 0; c < chessDelegate.getColumns(); c++) {
            for (int r = 0; r < chessDelegate.getRows(); r++) {
                if (chessDelegate.canPieceMove(new Square(toSquare.getCol(), toSquare.getRow()), new Square(c, r))) {
                    // Add an edge to the graph
                    boardGraph.addEdge(new Square(toSquare.getCol(), toSquare.getRow()), new Square(c, r), bidirectional);
                }
            }
        }

        List<Square> list = boardGraph.getAdjacentVertices(new Square(toSquare.getCol(), toSquare.getRow()));
        for(Square square: list) {
            if(chessDelegate.pieceAt(square) != null && chessDelegate.pieceAt(square).getPieceType().equals(ChessPieceType.KING)) {
               return true;
            }
        }
        return false;
    }

    public boolean checkMate(Square newSource) {
        for (Square vertex : boardGraph.getVertices()) {
            boardGraph.removeEdgesFromVertex(vertex);
        }
        // Add edges to the graph based on possible moves
        for (int c = 0; c < chessDelegate.getColumns(); c++) {
            for (int r = 0; r < chessDelegate.getRows(); r++) {
                Square kingPosition = getKingPosition();
                if (chessDelegate.canPieceMove(kingPosition, new Square(c, r))) {
                    // Add an edge to the graph
                    boardGraph.addEdge(getKingPosition(), new Square(c, r), bidirectional);
                }
            }
        }
        boolean kingCanMove = true;
        List<Square> kingList = boardGraph.getAdjacentVertices(getKingPosition());
        if (kingList.stream().count() == 0) {
            kingCanMove = false;
        }

        if (kingCanMove) {
            for (Square square : kingList) {
                if (chessDelegate.canPieceMove(getKingPosition(), square)) {
                    return false;
                }
            }
        }

        Set<ChessPiece> pieces = chessDelegate.getPiecesBox();
        ChessPlayer kingColor = null;
        for (ChessPiece piece : pieces) {
            if (piece.getPieceType() == ChessPieceType.KING) {
                kingColor = piece.getPlayer();
            }
        }
        if (!kingCanMove) {
            Set<ChessPiece> kingSidePieces = new HashSet<ChessPiece>();

            for (ChessPiece piece : pieces) {
                if (piece.getPlayer() == kingColor && piece.getPieceType() != ChessPieceType.KING) {
                    kingSidePieces.add(piece);
                }
            }

            for (ChessPiece kingSidePiece : kingSidePieces) {
                for (int c = 0; c < chessDelegate.getColumns(); c++) {
                    for (int r = 0; r < chessDelegate.getRows(); r++) {
                        if (chessDelegate.canPieceMove(new Square(kingSidePiece.getCol(), kingSidePiece.getRow()), new Square(c, r))) {
                            // Add an edge to the graph
                            boardGraph.addEdge(new Square(kingSidePiece.getCol(), kingSidePiece.getRow()), new Square(c, r), bidirectional);
                        }
                    }
                }
            }

//            List<Square> piecelist = boardGraph.getAdjacentVertices(new Square(piece.getCol(), piece.getRow()));
//            ChessPiece checkingPiece = chessDelegate.pieceAt(newSource);
//            ChessPiece king = chessDelegate.pieceAt(getKingPosition());
            for (ChessPiece kingSidePiece : kingSidePieces) {
                Square kingSidePieceSquare = new Square(kingSidePiece.getCol(), kingSidePiece.getRow());
                List<Square> kingSidePieceList = boardGraph.getAdjacentVertices(kingSidePieceSquare);
                if(kingSidePieceList.stream().count() == 0) {
                    continue;
                }
                for (Square square : kingSidePieceList) {
                        if (chessDelegate.canPieceMove(kingSidePieceSquare, square)) {
                            ChessPiece destinationPiece = chessDelegate.pieceAt(square);
                            kingSidePiece.setCol(square.getCol());
                            kingSidePiece.setRow(square.getRow());

                            // Simulate the move
                            if (destinationPiece != null) {
                                // Restore the destination piece if it was captured
                                chessDelegate.getPiecesBox().remove(destinationPiece);
                            }
                            ChessPiece king = chessDelegate.pieceAt(getKingPosition());
                            boolean kingInCheck = chessDelegate.isKingInCheck(king);

                            kingSidePiece.setCol(kingSidePieceSquare.getCol());
                            kingSidePiece.setRow(kingSidePieceSquare.getRow());
                            if (destinationPiece != null) {
                                // Restore the destination piece if it was captured
                                chessDelegate.getPiecesBox().add(destinationPiece);
                            }

                            // Return true if the king is not in check after the move
                            if (!kingInCheck) {
                                return false;
                            }
                        }
                }
            }
        }
        return true;
    }

//                    if(chessDelegate.pieceAt(square) == chessDelegate.pieceAt(newSource)) {
//                        // Check if the kingSidePiece can move to the square without capturing the opponent's piece
//                        if(chessDelegate.canPieceMove(kingSidePieceSquare, square) && chessDelegate.pieceAt(square) == null) {
//                            canBlockCheck = true;
//                            break;
//                        }
//                        else if(chessDelegate.pieceAt(square) != null) {
//
//                        }
//                    }
//                }
//                if(canBlockCheck) {
//                    // If a kingSidePiece can block the check without capturing the opponent's piece, return false
//                    return false;
//                }
//            }
//                    for(Square square: piecelist) {
//                        if(chessDelegate.pieceAt(square) == chessDelegate.pieceAt(newSource)) {
//                            return false;
//                        }
//                    }
//                    return true;


//            return true;

//        return false;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                int clickedCol = (int) ((event.getX() - boardLeft) / cellSize);
                int clickedRow = rows - 1 - (int) ((event.getY() - boardTop) / cellSize);
                if ((event.getY() - boardTop) < 0) {
                    clickedRow = rows;
                }
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
                        Square fromSquare = new Square(selectedCol, selectedRow);
                        Square toSquare = new Square(destinationCol, destinationRow);
                        newSource = toSquare;
                        chessDelegate.movePiece(fromSquare, toSquare);
                        if(kingInCheck(newSource)) {
                            Log.d(TAG, "King in Check!");
                            if(checkMate(newSource)){
                                Log.d(TAG, "Checkmate!");
                                checkmateListener.onCheckmate();
                                userTurn = false;
                                opponentTurn = false;
                            }
                            else {
                                opponentTurn = true;
                            }
                        }
                        else {
                            opponentTurn = true;
                        }

//                        Log.d(TAG, String.valueOf(boardGraph.getAdjacentVertices(getKingPosition()).stream().count()));
                        if(opponentTurn) {
//                            makeRandomMove();
                            makeAIMove();
                        }

                        selectedCol = -1;
                        selectedRow = -1;
                        invalidate(); // Redraw the view after moving

                    } else {
                        // If the clicked cell is not empty, select it
                        if (chessDelegate.pieceAt(new Square(clickedCol, clickedRow)) != null) {
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

    private void makeRandomMove() {
        List<ChessMove> possibleBlackMoves = currentLevel.getAllPossibleMoves(ChessPlayer.BLACK);
        if (!possibleBlackMoves.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(possibleBlackMoves.size());
            ChessMove randomMove = possibleBlackMoves.get(randomIndex);

            Square fromSquare = new Square(randomMove.getFromCol(), randomMove.getFromRow());
            Square toSquare = new Square(randomMove.getToCol(), randomMove.getToRow());
            chessDelegate.movePiece(fromSquare, toSquare);

//            // Check for checkmate
//            if (kingInCheck(toSquare)) {
//                Log.d(TAG, "King in Check!");
//                if (checkMate(toSquare)) {
//                    Log.d(TAG, "Checkmate!");
//                    checkmateListener.onCheckmate();
//                }
//            }

            // Update turn flags
            userTurn = true;
            opponentTurn = false;
            // Perform the random move
            // For example: yourChessBoard.performMove(randomMove);
        } else {
            // Handle the case where there are no possible moves
            System.out.println("No possible moves for black.");
        }
    }

    private void makeAIMove() {


        // Call findBestMove method to get the best move for the AI player
        ChessMove bestMove = currentLevel.findBestMove(maxDepth, true); // You may need to define maxDepth

        // Apply the best move on the chessboard
        Square fromSquare = new Square(bestMove.getFromCol(), bestMove.getFromRow());
        Square toSquare = new Square(bestMove.getToCol(), bestMove.getToRow());
        chessDelegate.movePiece(fromSquare, toSquare);

        // Check for checkmate
        if (kingInCheck(toSquare)) {
            Log.d(TAG, "King in Check!");
            if (checkMate(toSquare)) {
                Log.d(TAG, "Checkmate!");
                checkmateListener.onCheckmate();
            }
        }

        // Update turn flags
        userTurn = true;
        opponentTurn = false;

        invalidate(); // Redraw the view after AI move
    }


    private void highlightPossibleMoves(Canvas canvas, int col, int row) {
        ChessPiece selectedPiece = chessDelegate.pieceAt(new Square(col, row));

        if (selectedPiece != null) {
            // Clear existing highlights
            clearHighlights();
            if (selectedPiece.getPieceType() == ChessPieceType.PAWN) {
                bidirectional = false;
            }
            else {
                bidirectional = true;
            }
            // Add edges to the graph based on possible moves
            for (int c = 0; c < chessDelegate.getColumns(); c++) {
                for (int r = 0; r < chessDelegate.getRows(); r++) {
                    if (chessDelegate.canPieceMove(new Square(col, row), new Square(c, r))) {
                        // Add an edge to the graph
                        boardGraph.addEdge(new Square(col, row), new Square(c, r), bidirectional);
                    }
                }
            }
//            Log.d(TAG, String.valueOf(boardGraph.getAdjacentVertices(new Square(col, row))));
            // Highlight cells based on the graph
            Set<Square> adjacentSquares = new HashSet<>(boardGraph.getAdjacentVertices(new Square(col, row)));
            for (Square square : adjacentSquares) {
                drawHighlight(canvas, square);
            }
        }
    }

    private void drawHighlight(Canvas canvas, Square square) {
        int row = square.getRow();
        int col = square.getCol();

        // Calculate cell position
        float left = boardLeft + col * cellSize;
        float top = boardTop + (rows - 1 - row) * cellSize;
        float right = left + cellSize;
        float bottom = top + cellSize;

        // Draw highlight
        Paint highlightPaint = new Paint();
        highlightPaint.setColor(Color.argb(60, 250, 250, 0));
        canvas.drawRect(left, top, right, bottom, highlightPaint);
    }


    // Method to clear existing highlights
    private void clearHighlights() {
        // Remove all edges from the graph
        for (Square vertex : boardGraph.getVertices()) {
            boardGraph.removeEdgesFromVertex(vertex);
        }
    }

    public void setCurrentLevel(ChessLevel level) {
        this.currentLevel = level;
    }
}

