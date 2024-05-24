package tictactoe.mettechinno;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TicTacToe extends Application {

    @FXML
    private GridPane boardGrid;
    @FXML
    private Label statusLabel;
    @FXML
    private Label playerXScoreLabel;
    @FXML
    private Label playerOScoreLabel;
    @FXML
    private Label timerLabel;

    private TicTacToeController game;
    private int playerXScore;
    private int playerOScore;
    private long startTime;
    private String playerXName;
    private String playerOName;
    private Color playerXColor;
    private Color playerOColor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Scene menuScene = new Scene(loader.load());
        MenuController menuController = loader.getController();
        menuController.setPrimaryStage(primaryStage);
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();
    }

    public void initializeGame(String playerXName, String playerOName, Color playerXColor, Color playerOColor) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.playerXColor = playerXColor;
        this.playerOColor = playerOColor;

        game = new TicTacToeController();
        playerXScore = 0;
        playerOScore = 0;
        startTime = System.currentTimeMillis();

        initializeBoard();
        updateStatus();
        updateScores();
        startTimer();
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(100, 100);
                final int row = i;
                final int col = j;
                button.setOnAction(event -> handleMove(row, col, button));
                boardGrid.add(button, j, i);
            }
        }
    }

    private void handleMove(int row, int col, Button button) {
        if (game.placeMarker(row, col)) {
            button.setText(String.valueOf(game.getMarker(row, col)));
            button.setTextFill(game.getCurrentPlayer() == 'X' ? playerXColor : playerOColor);
            if (game.checkForWin()) {
                handleWin();
            } else if (game.isBoardFull()) {
                handleDraw();
            } else {
                game.changePlayer();
                updateStatus();
            }
        }
    }

    private void handleWin() {
        String winner = game.getCurrentPlayer() == 'X' ? playerXName : playerOName;
        statusLabel.setText("Player " + winner + " wins!");

        if (game.getCurrentPlayer() == 'X') {
            playerXScore++;
        } else {
            playerOScore++;
        }
        updateScores();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Result");
        alert.setHeaderText(null);
        alert.setContentText("The winner is: " + winner);
        alert.showAndWait();
        resetBoard();
    }

    private void handleDraw() {
        statusLabel.setText("The game is a draw!");
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Result");
        alert.setHeaderText(null);
        alert.setContentText("The game is a draw!");
        alert.showAndWait();
        resetBoard();
    }

    private void resetBoard() {
        game.initializeBoard();
        boardGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                ((Button) node).setText("");
            }
        });
        game.changePlayer();
        updateStatus();
        startTime = System.currentTimeMillis();
    }

    private void updateStatus() {
        statusLabel.setText("Current player: " + (game.getCurrentPlayer() == 'X' ? playerXName : playerOName));
    }

    private void updateScores() {
        playerXScoreLabel.setText(playerXName + ": " + playerXScore);
        playerOScoreLabel.setText(playerOName + ": " + playerOScore);
    }

    private void startTimer() {
        Thread timerThread = new Thread(() -> {
            while (true) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
                String timerText = String.format("%02d:%02d", minutes, seconds);
                javafx.application.Platform.runLater(() -> timerLabel.setText(timerText));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }
}
