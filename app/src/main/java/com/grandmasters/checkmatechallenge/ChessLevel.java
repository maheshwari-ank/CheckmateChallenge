package com.grandmasters.checkmatechallenge;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.io.Serializable;

public class ChessLevel implements ChessDelegate, Serializable {

    // Piece values
    private static final int PAWN_VALUE = 1;
    private static final int KNIGHT_VALUE = 3;
    private static final int BISHOP_VALUE = 3;
    private static final int ROOK_VALUE = 5;
    private static final int QUEEN_VALUE = 9;

    private static final int MAX_ROWS = 6;
    private static final int MAX_COLS = 5;
    private static final String TAG = "ChessLevel";

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }

    //    private String levelId;
    private int levelId;
    private int rows;
    private int columns;
    private boolean isSolved;
    private ChessPlayer userPlayer;
    private Set<ChessPiece> piecesBox;
    private Set<ChessPiece> piecesBoxOriginalState;
    private Graph boardGraph;
    public Set<ChessPiece> getPiecesBoxOriginalState() {
        return piecesBoxOriginalState;
    }

    public void setPiecesBoxOriginalState(Set<ChessPiece> piecesBoxOriginalState) {
        this.piecesBoxOriginalState = piecesBoxOriginalState;
    }
    Stack<ChessMove> moves = new Stack<>();

    public Graph getBoardGraph() {
        return boardGraph;
    }

    public void setBoardGraph(Graph boardGraph) {
        this.boardGraph = boardGraph;
    }

    public ChessLevel() {

    }

    public ChessPlayer getUserPlayer() {
        return userPlayer;
    }

    public void setUserPlayer(ChessPlayer userPlayer) {
        this.userPlayer = userPlayer;
    }

    public ChessLevel(int levelId, int rows, int columns, boolean isSolved, ChessPlayer userPlayer){
        if (columns > MAX_COLS || rows > MAX_ROWS) {
            throw new IllegalArgumentException("Number of columns or rows exceeds maximum limit.");
        }
        this.levelId = levelId;
        this.rows = rows;
        this.columns = columns;
        this.piecesBox = new HashSet<>();
        this.piecesBoxOriginalState = deepCopySet(piecesBox);
        this.isSolved = isSolved;
        this.userPlayer = userPlayer;
        initializeGraph();
    }

    public void initializeGraph() {
        boardGraph = new Graph();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                boardGraph.addVertex(new Square(col, row));
            }
        }
    }
    public void resetLevel() {

        piecesBox.clear();
        moves.removeAllElements();
//        piecesBox.addAll(deepCopySet(piecesBoxOriginalState));
        for (ChessPiece originalPiece : piecesBoxOriginalState) {
            piecesBox.add(new ChessPiece(originalPiece));
        }
//        initializeGraph();
//        for (Square vertex : boardGraph.getVertices()) {
//            boardGraph.removeEdgesFromVertex(vertex);
//        }

    }

    public void addPiece(ChessPiece piece) {
        piecesBox.add(piece);
//        piecesBoxOriginalState.add(piece);
    }

    private boolean isValidPosition(Square square) {
        int column = square.getCol();
        int rows = square.getRow();
        return column >= 0 && column < this.columns && rows >= 0 && rows < this.rows;
    }

    public void movePiece(Square fromSquare, Square toSquare) {
        if (canPieceMove(fromSquare, toSquare)) {
            // Check if the move is within the bounds of the chessboard
            if (isValidPosition(fromSquare) && isValidPosition(toSquare)) {
                ChessPiece movingPiece = pieceAt(fromSquare);
                ChessPiece pieceAtDestination = pieceAt(toSquare);

                // Check if move is valid
                if (movingPiece != null && (pieceAtDestination == null || pieceAtDestination.getPlayer() != movingPiece.getPlayer())) {
                    ChessMove move = new ChessMove(fromSquare.getCol(), fromSquare.getRow(), toSquare.getCol(), toSquare.getRow(), pieceAtDestination);

                    // Push the move onto the stack
                    moves.push(move);

                    // Check if a piece is killed during the move
                    if (pieceAtDestination != null) {
                        piecesBox.remove(pieceAtDestination);
                    }

                    // Move the piece to the new position
                    movingPiece.setCol(toSquare.getCol());
                    movingPiece.setRow(toSquare.getRow());
                }
            } else {
                Log.e(TAG, "Invalid move: Out of bounds");
            }
        }

    }

    public void undoLastMove() {
        if (!moves.isEmpty()) {
            ChessMove lastMove = moves.pop();
            int fromCol = lastMove.getFromCol();
            int fromRow = lastMove.getFromRow();
            int toCol = lastMove.getToCol();
            int toRow = lastMove.getToRow();
            ChessPiece pieceMoved = pieceAt(new Square(toCol, toRow));
            ChessPiece pieceKilled = lastMove.getPieceKilled();

            // Restore piece to original position
            if (pieceMoved != null) {
                pieceMoved.setCol(fromCol);
                pieceMoved.setRow(fromRow);
            }

            // If a piece was killed during the move, restore it
            if (pieceKilled != null) {
                piecesBox.add(pieceKilled);
            }
        }
    }

    @Override
    public ChessPiece pieceAt(Square square) {
        for(ChessPiece piece : piecesBox) {
            if(square.getCol() == piece.getCol() && square.getRow() == piece.getRow()) {
                return piece;
            }
        }
        return null;
    }

    public boolean isClearHorizontal(Square fromSquare, Square toSquare) {
        if (fromSquare.getRow() != toSquare.getRow()) return false;
        int gap = Math.abs(fromSquare.getCol() - toSquare.getCol()) - 1;
        ChessPiece pieceAtDestination = pieceAt(toSquare);
        if (pieceAtDestination != null && pieceAtDestination.getPlayer().equals(pieceAt(fromSquare).getPlayer())) {
            return false;
        }
        for (int i = 1; i <= gap; i++) {
            int nextCol = toSquare.getCol() > fromSquare.getCol() ? fromSquare.getCol() + i :
                    fromSquare.getCol() - i;
            if (pieceAt(new Square(nextCol, fromSquare.getRow())) != null) {
                return false;
            }
        }
        return true;
    }

    public boolean isClearVertical(Square fromSquare, Square toSquare){
        if(fromSquare.getCol() != toSquare.getCol())    return false;
        int gap = Math.abs(fromSquare.getRow() - toSquare.getRow()) -1;
        ChessPiece pieceAtDestination = pieceAt(toSquare);
        if (pieceAtDestination != null && pieceAtDestination.getPlayer().equals(pieceAt(fromSquare).getPlayer())) {
            return false;
        }
        for(int i=1;i<=gap;i++){
            int nextRow = toSquare.getRow() > fromSquare.getRow() ? fromSquare.getRow() + i :
                    fromSquare.getRow() - i;
            if(pieceAt(new Square(fromSquare.getCol(), nextRow)) != null){
                return false;
            }
        }
        return true;
    }

    public boolean isClearDiagonal(Square fromSquare, Square toSquare){
        if(Math.abs(fromSquare.getRow() - toSquare.getRow()) != Math.abs(fromSquare.getCol() - toSquare.getCol())){
            return false;
        }
        int gap = Math.abs(fromSquare.getCol() - toSquare.getCol()) - 1;
        for(int i = 1;i<=gap;i++){
            int nextCol = toSquare.getCol() > fromSquare.getCol() ? fromSquare.getCol() + i :
                    fromSquare.getCol() - i;
            int nextRow = toSquare.getRow() > fromSquare.getRow() ? fromSquare.getRow() + i :
                    fromSquare.getRow() - i;
            if(pieceAt(new Square(nextCol, nextRow)) != null){
                return false;
            };
        }
        return true;
    }

    public boolean isClearFront(Square fromSquare, Square toSquare){
        int gap = Math.abs(fromSquare.getRow() - toSquare.getRow()) - 1;
        if(gap == 0){
            if (pieceAt(new Square(fromSquare.getCol(), toSquare.getRow())) == null){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    public boolean checkDiagonal(Square fromSquare, Square toSquare){
        ChessPiece piece = pieceAt(new Square(fromSquare.getCol(), fromSquare.getRow()));
        int gapRow = 0;
        if(piece.getPlayer().equals(ChessPlayer.WHITE)) {
             gapRow = toSquare.getRow() - fromSquare.getRow() - 1;
        }
        else{
            gapRow = fromSquare.getRow() - toSquare.getRow() - 1;
        }
        int gapCol = Math.abs(toSquare.getCol() - fromSquare.getCol()) - 1;

        if(gapRow == 0 && gapCol == 0
                && pieceAt(new Square(gapCol + toSquare.getCol(), gapRow + toSquare.getRow())) != null) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean canPawnMove(Square fromSquare, Square toSquare) {
        ChessPiece piece = pieceAt(new Square(fromSquare.getCol(), fromSquare.getRow()));
        if(piece.getPlayer().equals(ChessPlayer.WHITE)){
            if ((toSquare.getRow() - fromSquare.getRow() == 1 && toSquare.getCol() == fromSquare.getCol() &&
                    isClearFront(fromSquare, toSquare)) || checkDiagonal(fromSquare, toSquare)) {
                return true;
            }
        }
        else{
            if((fromSquare.getRow() - toSquare.getRow() == 1 && toSquare.getCol() == fromSquare.getCol()
                    && isClearFront(fromSquare, toSquare)) || checkDiagonal(fromSquare, toSquare)){
                return true;
            }
        }

        return false;
    }
    public boolean canQueenMove(Square fromSquare, Square toSquare) {
        return canRookMove(fromSquare, toSquare) || canBishopMove(fromSquare, toSquare);
    }

//    public boolean canKingMove(Square kingSquare) {
//        for (ChessPiece piece : piecesBox) {
//            if (!piece.getPlayer().equals(ChessPlayer.WHITE)) {
//                Square pieceSquare = new Square(piece.getCol(), piece.getRow());
//                List<Square> adjacentSquares = boardGraph.getAdjacentVertices(pieceSquare);
//
//                for (Square adjacentSquare : adjacentSquares) {
//                    ChessPiece adjacentPiece = pieceAt(adjacentSquare);
//                    if (adjacentPiece != null && adjacentPiece.getPlayer().equals(ChessPlayer.WHITE)) {
//                        // The king is in check
//                        return true;
//                    }
//                }
//            }
//        }
//        // King is not in check
//        return false;
//    }

    public boolean isCheckMate(Square fromSquare){
        return false;
    }

    public boolean canKingMove(Square fromSquare, Square toSquare) {
        // Check if the move is within one square horizontally or vertically
//        if(isCheckMate(fromSquare)){
//            return false;
//        }
        if((Math.abs(fromSquare.getRow() - toSquare.getRow()) == 1 && Math.abs(fromSquare.getCol() - toSquare.getCol()) == 1)
                || (Math.abs(fromSquare.getRow() - toSquare.getRow()) == 1 && Math.abs(fromSquare.getCol() - toSquare.getCol()) == 0)
                || (Math.abs(fromSquare.getRow() - toSquare.getRow()) == 0 && Math.abs(fromSquare.getCol() - toSquare.getCol()) == 1)) {
            // Simulate the move and check if the king would still be in check
            ChessPiece king = pieceAt(fromSquare);
            ChessPiece destinationPiece = pieceAt(toSquare);
            king.setRow(toSquare.getRow());
            king.setCol(toSquare.getCol());

            // Simulate the move
            if (destinationPiece != null) {
                // Restore the destination piece if it was captured
                piecesBox.remove(destinationPiece);
            }
            boolean kingInCheck = isKingInCheck(king);
            // Undo the move
            king.setRow(fromSquare.getRow());
            king.setCol(fromSquare.getCol());
            if (destinationPiece != null) {
                // Restore the destination piece if it was captured
                piecesBox.add(destinationPiece);
            }

            // Return true if the king is not in check after the move
            return !kingInCheck;
        }
        return false;
    }

//    public boolean canKingMove(Square fromSquare, Square toSquare){
//        if((Math.abs(fromSquare.getRow() - toSquare.getRow()) == 1 && Math.abs(fromSquare.getCol() - toSquare.getCol()) == 1)
//                || (Math.abs(fromSquare.getRow() - toSquare.getRow()) == 1 && Math.abs(fromSquare.getCol() - toSquare.getCol()) == 0)
//        || (Math.abs(fromSquare.getRow() - toSquare.getRow()) == 0 && Math.abs(fromSquare.getCol() - toSquare.getCol()) == 1)){
//
//            return true;
//        }
//        return false;
//    }

    public boolean isKingInCheck(ChessPiece king) {
        // Find the king's position
//        Square kingPosition = null;
//        for (int col = 0; col < columns; col++) {
//            for (int row = 0; row < rows; row++) {
//                ChessPiece piece = pieceAt(new Square(col, row));
//                if (piece != null && piece.getPieceType() == ChessPieceType.KING && piece.getPlayer() == king.getPlayer()) {
//                    kingPosition = new Square(col, row);
//                    break;
//                }
//            }
//            if (kingPosition != null) break;
//        }
//
//        if (kingPosition == null) return false; // King not found, should not happen in a valid chess game
        Set <ChessPiece> pieces = getPiecesBox();
        for (ChessPiece piece: pieces) {
            Square fromSquare = new Square(piece.getCol(), piece.getRow());
            Square toSquare = new Square(king.getCol(), king.getRow());
            if (canPieceMove(fromSquare, toSquare)) {
                return true;
            }
        }
        // Iterate through opponent's pieces and check if any of them can attack the king
//        for (int col = 0; col < columns; col++) {
//            for (int row = 0; row < rows; row++) {
//                ChessPiece piece = pieceAt(new Square(col, row));
//                if (piece != null && piece.getPlayer() != king.getPlayer()) {
//                    if (canPieceMove(new Square(col, row), new Square(king.getCol(), king.getRow()))) {
//                        return true;
//                    }
//                }
//            }
//        }

        return false;
    }
    public boolean canKnightMove(Square fromSquare, Square toSquare) {
        return Math.abs(fromSquare.getCol() - toSquare.getCol()) == 2 && Math.abs(fromSquare.getRow() - toSquare.getRow()) == 1 ||
                Math.abs(fromSquare.getCol() - toSquare.getCol()) == 1 && Math.abs(fromSquare.getRow() - toSquare.getRow()) == 2;
    }
    
    public boolean canRookMove(Square fromSquare, Square toSquare){
        return ((fromSquare.getRow() == toSquare.getRow() && isClearHorizontal(fromSquare, toSquare))
                || (fromSquare.getCol() == toSquare.getCol() && isClearVertical(fromSquare, toSquare)));
    }

    public boolean canBishopMove(Square fromSquare, Square toSquare) {
        if(Math.abs(fromSquare.getRow() - toSquare.getRow()) == Math.abs(fromSquare.getCol() - toSquare.getCol())){
            return isClearDiagonal(fromSquare, toSquare);
        }
        return false;
    }

    public boolean canPieceMove(Square fromSquare, Square toSquare) {
        ChessPiece piece1 = pieceAt(fromSquare);
        ChessPiece piece2 = pieceAt(toSquare);
        if (piece1 != null && piece2 != null && piece1.getPlayer().equals(piece2.getPlayer())) {
            return false;
        }
        if(fromSquare.getCol() == toSquare.getCol() && fromSquare.getRow() == toSquare.getRow()){
            return false;
        }
        ChessPiece movingPiece = pieceAt(fromSquare);
        switch (movingPiece.getPieceType()) {
            case KNIGHT:
                return canKnightMove(fromSquare, toSquare);
            case ROOK:
                return canRookMove(fromSquare, toSquare);
            case BISHOP:
                return canBishopMove(fromSquare, toSquare);
            case QUEEN:
                return canQueenMove(fromSquare, toSquare);
            case KING:
                return canKingMove(fromSquare, toSquare);
            case PAWN:
                return canPawnMove(fromSquare, toSquare);
        }
        return false;
    }

    @Override
    public Set<ChessPiece> getPiecesBox() {
        return piecesBox;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    public Set<ChessPiece> deepCopySet(Set<ChessPiece> originalSet) {
        Set<ChessPiece> copySet = new HashSet<>();
        for (ChessPiece piece : originalSet) {
            copySet.add(new ChessPiece(piece));
        }
        return copySet;
    }

    public List<ChessMove> getAllPossibleMoves(ChessPlayer playerColor) {
        List<ChessMove> possibleMoves = new ArrayList<>();
        Set<ChessPiece> playerPieces = getPiecesBox();

        // Iterate through all pieces belonging to the specified player
        for (ChessPiece piece : playerPieces) {
            // Check if the piece belongs to the specified player
            if (piece.getPlayer() == playerColor) {
                // Get the possible moves for the current piece
                List<Square> possibleDestinations = getPossibleDestinations(piece);

                // Filter out illegal moves
                for (Square destination : possibleDestinations) {
                    if (canPieceMove(new Square(piece.getCol(), piece.getRow()), destination)) {
                        possibleMoves.add(new ChessMove(piece.getCol(), piece.getRow(), destination.getCol(), destination.getRow(), pieceAt(destination)));
                    }
                }
            }
        }

        return possibleMoves;
    }


    private List<Square> getPossibleDestinations(ChessPiece piece) {
        List<Square> possibleDestinations = new ArrayList<>();
        Square currentSquare = new Square(piece.getCol(), piece.getRow());

        // Get adjacent squares from the graph
        List<Square> adjacentSquares = boardGraph.getAdjacentVertices(currentSquare);

        // Iterate through adjacent squares
        for (Square square : adjacentSquares) {
            possibleDestinations.add(square);
        }

        return possibleDestinations;
    }


    public ChessMove findBestMove(int depth, boolean maximizingPlayer) {
        int bestScore = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        ChessMove bestMove = null;

        // Get all possible moves for the opponent
        List<ChessMove> possibleMoves = getAllPossibleMoves(ChessPlayer.BLACK);

        // Loop through each possible move and evaluate its score using Minimax
        for (ChessMove move : possibleMoves) {
            // Make the move
            Square fromSquare = new Square(move.getFromCol(), move.getFromRow());
            Square toSquare = new Square(move.getToCol(), move.getToRow());
            movePiece(fromSquare, toSquare);
            // Evaluate the move using Minimax with alpha-beta pruning
            int score = minimax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !maximizingPlayer);
            // Undo the move
            undoLastMove();
            // Update the best move and score
            if ((maximizingPlayer && score > bestScore) || (!maximizingPlayer && score < bestScore)) {
                bestScore = score;
                bestMove = move;
            }
        }

        // Return the best move found
        return bestMove;
    }


    private int minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || isGameOver()) {
            // If depth is 0 or game is over, evaluate the board position
            return evaluateBoard();
        }

        if (maximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;
            // Get all possible moves for the opponent
            List<ChessMove> possibleMoves = getAllPossibleMoves(ChessPlayer.BLACK);

            for (ChessMove move : possibleMoves) {
                // Make the move
                Square fromSquare = new Square(move.getFromCol(), move.getFromRow());
                Square toSquare = new Square(move.getToCol(), move.getToRow());
                movePiece(fromSquare, toSquare);

                // Recursively call minimax for the next level
                int score = minimax(depth - 1, alpha, beta, false);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);

                // Undo the move
                undoLastMove();

                // Perform alpha-beta pruning
                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            // Get all possible moves for the player
            List<ChessMove> possibleMoves = getAllPossibleMoves(ChessPlayer.WHITE);

            for (ChessMove move : possibleMoves) {
                // Make the move
                Square fromSquare = new Square(move.getFromCol(), move.getFromRow());
                Square toSquare = new Square(move.getToCol(), move.getToRow());
                movePiece(fromSquare, toSquare);

                // Recursively call minimax for the next level
                int score = minimax(depth - 1, alpha, beta, true);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);

                // Undo the move
                undoLastMove();

                // Perform alpha-beta pruning
                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }

    private int evaluateBoard() {
        int whiteScore = 0;
        int blackScore = 0;

        // Iterate through all pieces on the board
        for (ChessPiece piece : piecesBox) {
            switch (piece.getPieceType()) {
                case PAWN:
                    if (piece.getPlayer() == ChessPlayer.WHITE) {
                        whiteScore += PAWN_VALUE;
                    } else {
                        blackScore += PAWN_VALUE;
                    }
                    break;
                case KNIGHT:
                    if (piece.getPlayer() == ChessPlayer.WHITE) {
                        whiteScore += KNIGHT_VALUE;
                    } else {
                        blackScore += KNIGHT_VALUE;
                    }
                    break;
                case BISHOP:
                    if (piece.getPlayer() == ChessPlayer.WHITE) {
                        whiteScore += BISHOP_VALUE;
                    } else {
                        blackScore += BISHOP_VALUE;
                    }
                    break;
                case ROOK:
                    if (piece.getPlayer() == ChessPlayer.WHITE) {
                        whiteScore += ROOK_VALUE;
                    } else {
                        blackScore += ROOK_VALUE;
                    }
                    break;
                case QUEEN:
                    if (piece.getPlayer() == ChessPlayer.WHITE) {
                        whiteScore += QUEEN_VALUE;
                    } else {
                        blackScore += QUEEN_VALUE;
                    }
                    break;
                // You may add the king value if necessary, but it's often not used in traditional evaluations
            }
        }

        // Return the difference between white and black scores
        return whiteScore - blackScore;
    }

    private boolean isGameOver() {
        // Implement the logic to check if the game is over
        // Return true if the game is over (e.g., checkmate, stalemate)
        // Return false otherwise
        return false; // Placeholder
    }
}

