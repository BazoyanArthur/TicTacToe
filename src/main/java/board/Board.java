package board;

import static board.Mark.*;

public class Board {

    private final Mark[][] board;
    private Mark winningMark;
    private final int BOARD_WIDTH = 3;
    private boolean crossTurn, gameOver;
    private int availableMoves = BOARD_WIDTH * BOARD_WIDTH;

    public Board() {
        board = new Mark[BOARD_WIDTH][BOARD_WIDTH];
        crossTurn = true;
        gameOver = false;
        winningMark = BLANK;
        initialiseBoard();
    }

    private void initialiseBoard() {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = BLANK;
            }
        }
    }

    public boolean placeMark(int row, int col) {
        if (row < 0 || row >= BOARD_WIDTH || col < 0 || col >= BOARD_WIDTH
                || isTileMarked(row, col) || gameOver) {
            return false;
        }
        availableMoves--;
        board[row][col] = crossTurn ? X : O;
        togglePlayer();
        checkWin(row, col);
        return true;
    }

    private int getRowValue(int row){
        int rowSum = 0;
        for (int c = 0; c < BOARD_WIDTH; c++) {
            rowSum += getMarkAt(row, c).getMark();
        }
        return rowSum;
    }

    private int getColValue(int col){
        int rowSum = 0;
        for (int r = 0; r < BOARD_WIDTH; r++) {
            rowSum += getMarkAt(r, col).getMark();
        }
        return rowSum;
    }

    private int getTopLeftToBottomRightDiag(){
        int rowSum = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            rowSum += getMarkAt(i, i).getMark();
        }
        return rowSum;
    }

    private int getTopRightToBottomLeftDiag(){
        int rowSum = 0;
        int indexMax = BOARD_WIDTH - 1;
        for (int i = 0; i <= indexMax; i++) {
            rowSum += getMarkAt(i, indexMax - i).getMark();
        }
        return rowSum;
    }

    private void checkWin(int row, int col) {
        int rowSum = getRowValue(row);
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on row " + row);
            return;
        }

        // Check column for winner.
        rowSum = getColValue(col);
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on column " + col);
            return;
        }

        // Top-left to bottom-right diagonal.
        rowSum = getTopLeftToBottomRightDiag();
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on the top-left to "
                    + "bottom-right diagonal");
            return;
        }

        // Top-right to bottom-left diagonal.
        rowSum = getTopRightToBottomLeftDiag();
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on the top-right to "
                    + "bottom-left diagonal.");
            return;
        }

        if (!anyMovesAvailable()) {
            gameOver = true;
            System.out.println("Tie!");
        }
    }

    public int evaluateBoard() {
        int rowSum = 0;
        int bWidth = BOARD_WIDTH;
        int Xwin = X.getMark() * bWidth;
        int Xfork = X.getMark() * 4 + (2 * BLANK.getMark());
        int Owin = O.getMark() * bWidth;
        int Ofork = O.getMark() * 4 + (2 * BLANK.getMark());
        int XwinInNext = X.getMark() * 2 + BLANK.getMark();
        int OwinInNext = O.getMark() * 2 + BLANK.getMark();

        // Check rows for winner.
        for (int row = 0; row < bWidth; row++) {
            rowSum = getRowValue(row);
            if (rowSum == Xwin) {
                return 10;
            } else if (rowSum == Owin) {
                return -10;
            }
            rowSum = 0;
        }

        // Check columns for winner.
        rowSum = 0;
        for (int col = 0; col < bWidth; col++) {
            rowSum = getColValue(col);
            if (rowSum == Xwin) {
                return 10;
            } else if (rowSum == Owin) {
                return -10;
            }
            rowSum = 0;
        }

        // check rows and columns for forks
        rowSum = 0;
        for(int row = 0; row < bWidth; row++){
            for(int col = 0; col < bWidth; col++){
                rowSum = getRowValue(row) + getColValue(col);
                if (rowSum == Xfork) {
                    return 6;
                } else if (rowSum == Ofork) {
                    return -6;
                }
            }
        }


        // Check diagonals for winner.
        // Top-left to bottom-right diagonal.
        rowSum = getTopLeftToBottomRightDiag();
        if (rowSum == Xwin) {
            return 10;
        } else if (rowSum == Owin) {
            return -10;
        }

        // Top-right to bottom-left diagonal.
        rowSum = getTopRightToBottomLeftDiag();
        if (rowSum == Xwin) {
            return 10;
        } else if (rowSum == Owin) {
            return -10;
        }

        // Check diagonals for fork
        rowSum = getTopRightToBottomLeftDiag() + getTopLeftToBottomRightDiag();
        if (rowSum == Xfork) {
            return 6;
        } else if (rowSum == Ofork) {
            return -6;
        }

        // check rows for 2 marks in a row
        rowSum = 0;
        for (int row = 0; row < bWidth; row++) {
            rowSum = getRowValue(row);
            if (rowSum == XwinInNext) {
                return 2;
            } else if (rowSum == OwinInNext) {
                return -2;
            }
            rowSum = 0;
        }

        // check columns for 2 marks in a row
        rowSum = 0;
        for (int col = 0; col < bWidth; col++) {
            rowSum = getColValue(col);
            if (rowSum == XwinInNext) {
                return 2;
            } else if (rowSum == OwinInNext) {
                return -2;
            }
            rowSum = 0;
        }

        // Check diagonals for winner.
        // Top-left to bottom-right diagonal.
        rowSum = getTopLeftToBottomRightDiag();
        if (rowSum == XwinInNext) {
            return 2;
        } else if (rowSum == OwinInNext) {
            return -2;
        }

        // Top-right to bottom-left diagonal.
        rowSum = getTopRightToBottomLeftDiag();
        if (rowSum == XwinInNext) {
            return 2;
        } else if (rowSum == OwinInNext) {
            return -2;
        }

        return 0;
    }

    private Mark calcWinner(int rowSum) {
        int Xwin = X.getMark() * BOARD_WIDTH;
        int Owin = O.getMark() * BOARD_WIDTH;
        if (rowSum == Xwin) {
            gameOver = true;
            winningMark = X;
            return X;
        } else if (rowSum == Owin) {
            gameOver = true;
            winningMark = O;
            return O;
        }
        return BLANK;
    }

    private void togglePlayer() {
        crossTurn = !crossTurn;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean anyMovesAvailable() {
        return availableMoves > 0;
    }

    public Mark getMarkAt(int row, int column) {
        return board[row][column];
    }

    public boolean isTileMarked(int row, int column) {
        return board[row][column].isMarked();
    }

    public void setMarkAt(int row, int column, Mark newMark) {
        if(newMark == BLANK){
            availableMoves++;
        }
        else{
            availableMoves--;
        }
        board[row][column] = newMark;
    }

    public boolean isCrossTurn() {
        return crossTurn;
    }

    public int getWidth() {
        return BOARD_WIDTH;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Mark getWinningMark() {
        return winningMark;
    }

    public void printBoard(){
        for(int i = 0; i < BOARD_WIDTH; i++){
            for(int j = 0; j < BOARD_WIDTH; j++) {
                System.out.print("|" + getMarkAt(i, j) + "|");
            }
            System.out.println();
            System.out.println("_________");
        }
        System.out.println("Board utility is: " + evaluateBoard());
    }

    public Board reset() {
        return new Board();
    }
}