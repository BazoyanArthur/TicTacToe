import board.Board;
import board.Mark;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import minimax.MiniMaxAlphaBeta;
import search.Minimax;

import java.util.Scanner;


public class Main extends Application {

    private static Board board;
    private AnimationTimer gameTimer;

public boolean handleKeyPressed(char ch) {
        if(ch == 'q'){
            return board.placeMark(0, 0);
        }

        if(ch == 'w'){
            return board.placeMark(0, 1);
        }

        if(ch == 'e'){
            return board.placeMark(0, 2);
        }

        if(ch == 'a'){
            return board.placeMark(1, 0);
        }

        if(ch == 's'){
            return board.placeMark(1, 1);
        }

        if(ch == 'd'){
            return board.placeMark(1, 2);
        }

        if(ch == 'z'){
            return board.placeMark(2, 0);
        }

        if(ch == 'x'){
            return board.placeMark(2, 1);
        }

        if(ch == 'c'){
            return board.placeMark(2, 2);
        }

        if(ch == 'r'){
            board = new Board();
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        board = new Board();
        Scanner sc = new Scanner(System.in);
        board.printBoard();


        while(true) {
            if(board.isCrossTurn()){
                // Character input
                char c = sc.next().charAt(0);
                handleKeyPressed(c);
            }else{
                playAI();
            }
            board.printBoard();
            if(board.isGameOver()){
                endGame();
                System.out.println("Press R to restart the game.");
                char c = sc.next().charAt(0);
                if(!handleKeyPressed(c)){
                    break;
                }
                board.printBoard();
            }
        }

    }

    private static void playAI() {
        int[] move = Minimax.getBestMoveForO(board);
        int row = move[0];
        int col = move[1];
        board.placeMark(row, col);
    }

    private void endGame() {
        Mark winner = board.getWinningMark();
        if (winner == Mark.BLANK) {
            System.out.println("It is a Draw!");
        } else {
            System.out.println(winner + " wins!");
        }
        board = board.reset();
    }


}
