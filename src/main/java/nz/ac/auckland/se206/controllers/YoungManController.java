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
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class YoungManController {
  @FXML private Label lblTime;
  @FXML private Rectangle rectCrimeScene;
  @FXML private Rectangle rectOldMan;
  @FXML private Rectangle rectWoman;

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
    chat.setProfession("youngMan");
  }

  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  /** This switches the scene to the crime scene. */
  @FXML
  private void crimeScene(MouseEvent event) {
    try {
      Rectangle rect = (Rectangle) event.getSource();
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
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.OLDMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading oldManRoom.fxml");
      System.exit(0);
    }
  }

  /** This switches to the woman room. */
  @FXML
  private void woman(MouseEvent event) {
    try {
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.WOMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading womanRoom.fxml");
      System.exit(0);
    }
  }

  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isYoungManClicked = true;
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

  public void setSceneMenu() {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }

  /**
   * This method sets the scene to the guessing room
   */
  public void setSceneGuess() {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.GUESSROOM));
  }
}
