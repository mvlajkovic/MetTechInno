package tictactoe.mettechinno;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class MenuController {
    @FXML
    private TextField playerXNameField;
    @FXML
    private TextField playerONameField;
    @FXML
    private ColorPicker playerXColorPicker;
    @FXML
    private ColorPicker playerOColorPicker;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void startGame() {
        String playerXName = playerXNameField.getText();
        String playerOName = playerONameField.getText();
        Color playerXColor = playerXColorPicker.getValue();
        Color playerOColor = playerOColorPicker.getValue();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoe.fxml"));
            Scene gameScene = new Scene(loader.load());
            TicTacToe controller = loader.getController();
            controller.initializeGame(playerXName, playerOName, playerXColor, playerOColor);
            primaryStage.setScene(gameScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
