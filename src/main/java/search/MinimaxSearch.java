package search;

import board.Board;

import static board.Mark.*;


public class MinimaxSearch {

    private static final int MAX_DEPTH = 12;


    public static int maxValue(Board board){
        int boardVal = board.evaluateBoard();
        if (Math.abs(boardVal) == 10 || !board.anyMovesAvailable()) {
            return boardVal;
        }

        int highestVal = Integer.MIN_VALUE;
        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.isTileMarked(row, col)) {
                    board.setMarkAt(row, col, X);
                    highestVal = Math.max(highestVal, minValue(board));
                    board.setMarkAt(row, col, BLANK);
                }
            }
        }
        return highestVal;
    }

    public static int minValue(Board board){
        int boardVal = board.evaluateBoard();
        if (Math.abs(boardVal) == 10 || !board.anyMovesAvailable()) {
            return boardVal;
        }

        int lowestVal = Integer.MAX_VALUE;
        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.isTileMarked(row, col)) {
                    board.setMarkAt(row, col, O);
                    lowestVal = Math.min(lowestVal, maxValue(board));
                    board.setMarkAt(row, col, BLANK);
                }
            }
        }
        return lowestVal;
    }


    public static int[] getBestMoveForO(Board board) {
        int[] bestMove = new int[]{-1, -1};
        int bestValue = Integer.MAX_VALUE;

        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (!board.isTileMarked(row, col)) {
                    board.setMarkAt(row, col, O);
                    int moveValue = maxValue(board);
                    board.setMarkAt(row, col, BLANK);
                    if (moveValue < bestValue) {
                        bestMove[0] = row;
                        bestMove[1] = col;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }


}
