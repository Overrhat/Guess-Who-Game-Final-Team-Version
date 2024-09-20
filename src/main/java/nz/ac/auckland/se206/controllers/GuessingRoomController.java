package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class GuessingRoomController {
  @FXML private Label lblTime;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtaInput;
  @FXML private Button btnSend;
  


  @FXML
  public void initialize() {
    
  }

  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  public void setSceneMenu() {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }
}
