package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class WomanController {
  @FXML private Label lblTime;
  @FXML private Circle circleCrimeScene;
  @FXML private Circle circleYoungMan;
  @FXML private Circle circleOldMan;

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnGuess;

  private ChatController chat;

  public void initialize() {
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    chat.setProfession("woman");

    circleCrimeScene.setOpacity(0);
    circleYoungMan.setOpacity(0);
    circleOldMan.setOpacity(0);
  }

  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  /** This switches the scene to the crime scene. */
  @FXML
  private void crimeScene(MouseEvent event) {
    try {
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.MAINROOM));
    } catch (Exception e) {
      System.out.println("Error loading mainRoom.fxml");
      System.exit(0);
    }
  }

  /** This switches to the old man room. */
  @FXML
  private void oldMan(MouseEvent event) {
    try {
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.OLDMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading oldManRoom.fxml");
      System.exit(0);
    }
  }

  /** This switches to the young man room. */
  @FXML
  private void youngMan(MouseEvent event) {
    try {
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.YOUNGMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading youngManRoom.fxml");
      System.exit(0);
    }
  }

  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isWomanClicked = true;
    chat.onSendMessage(event);

  }
  /** This switches the scene to the guessing scene when guess button is clicked */
  @FXML
  private void handleGuessButtonClick(MouseEvent event) {
    // Checking the requirements to switch to the guessing scene
    if (!(MainRoomController.isClueFound
        && MainRoomController.isOldManClicked
        && MainRoomController.isYoungManClicked
        && MainRoomController.isWomanClicked)) {
      MenuController.playMedia("/sounds/sound17.mp3");
      return;
    }

    try {
      // Get the current scene
      Scene scene = btnGuess.getScene();
      // Switch to the GUESSROOM scene
      scene.setRoot(SceneManager.getUiRoot(AppUi.GUESSROOM));
    } catch (Exception e) {
      System.out.println("Error loading guessingRoom.fxml");
      System.exit(0);
    }
  }
  
  @FXML private void hoverOn(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(1);
  }

  @FXML private void hoverOff(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(0);
  }
 
}
