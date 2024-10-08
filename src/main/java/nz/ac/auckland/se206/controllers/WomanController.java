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
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the woman. Handles all the user interaction with the woman scene. */
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

  /** This method sets up the chatbot for the woman. */
  public void initialize() {
    // this initializes the chat controller
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    chat.setProfession("woman");

    // this initializes the hover effects
    circleCrimeScene.setOpacity(0);
    circleYoungMan.setOpacity(0);
    circleOldMan.setOpacity(0);
  }

  /**
   * This is the setter method for the woman timer.
   *
   * @param time the value to set the time
   */
  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  /**
   * This switches the scene to the crime scene.
   *
   * @param event the mouse event that is triggered by clicking on the button
   */
  @FXML
  private void switchToCrimeScene(MouseEvent event) {
    // this switches the scene to the crime scene
    switchScene(event, AppUi.MAINROOM, "mainRoom");
  }

  /**
   * This switches the scene to the old man.
   *
   * @param event the mouse event that is triggered by clicking on the button
   */
  @FXML
  private void switchToOldMan(MouseEvent event) {
    // this switches the scene to the old man
    switchScene(event, AppUi.OLDMANROOM, "oldManRoom");
  }

  /**
   * This switches the scene to the young man.
   *
   * @param event the mouse event that is triggered by clicking on the button
   */
  @FXML
  private void switchToYoungMan(MouseEvent event) {
    // this switches the scene to young man
    switchScene(event, AppUi.YOUNGMANROOM, "youngManRoom");
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isWomanClicked = true;
    chat.sendMessage(event);
  }

  /**
   * This switches the scene to the guessing scene when guess button is clicked.
   *
   * @param event the mouse event that is triggered by clicking on the button
   */
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
      // Switch to the GUESSROOM scene
      setSceneAny(AppUi.GUESSROOM);
    } catch (Exception e) {
      System.out.println("Error loading guessingRoom.fxml");
      System.exit(0);
    }
  }

  /**
   * This method sets the scene to any scene.
   *
   * @param uiRoot the scene to set it to
   */
  public void setSceneAny(AppUi uiRoot) {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(uiRoot));
  }

  /**
   * This method handles the hover effects turning on.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOn(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(1);
  }

  /**
   * This method handles the hover effects turning off.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOff(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(0);
  }

  /**
   * This method switches the scene to whatever is clicked.
   *
   * @param event the mouse event that is triggered by clicking on the button
   * @param root the UI root of the scene to be switched to
   * @param name the name of the fxml file for the respective scene
   */
  private void switchScene(MouseEvent event, AppUi root, String name) {
    // this switches the scene to the specified root
    try {
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(root));
    } catch (Exception e) {
      System.out.println("Error loading " + name + ".fxml");
      System.exit(0);
    }
  }
}
