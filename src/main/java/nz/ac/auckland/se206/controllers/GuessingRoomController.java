package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;

public class GuessingRoomController {
  @FXML private Label lblTime;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtaInput;
  @FXML private Button btnGuess;
  @FXML private Button btnMenu;
  @FXML private Rectangle rectOldMan;
  @FXML private Rectangle rectYoungMan;
  @FXML private Rectangle rectWoman;

  private static GameStateContext context;

  @FXML
  public void initialize() {
    context = MenuController.getContext();
  }
}