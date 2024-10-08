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

public class YoungManController {
  @FXML private Label lblTime;
  @FXML private Circle circleCrimeScene;
  @FXML private Circle circleOldMan;
  @FXML private Circle circleWoman;

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnGuess;

  private ChatController chat;

  /** This method sets up the chatbot for the young man */
  public void initialize() {
    // Initialize the controller
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    chat.setProfession("youngMan");

    circleCrimeScene.setOpacity(0);
    circleWoman.setOpacity(0);
    circleOldMan.setOpacity(0);
  }

  /**
   * This is the setter method for the young man timer
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
    // Switch to crime scene by using the switchScene method
    switchScene(event, AppUi.MAINROOM, "crimeScene");
  }

  /**
   * This switches the scene to the old man.
   *
   * @param event the mouse event that is triggered by clicking on the button
   */
  @FXML
  private void switchToOldMan(MouseEvent event) {
    // Switch to oldman room by using the switchScene method
    switchScene(event, AppUi.OLDMANROOM, "oldManRoom");
  }

  /**
   * This switches the scene to the woman.
   *
   * @param event the mouse event that is triggered by clicking on the button
   */
  @FXML
  private void switchToWoman(MouseEvent event) {
    // Switch to woman room by using the switchScene method
    switchScene(event, AppUi.WOMANROOM, "womanRoom");
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
    // Send the message to the chat with youngman prompt
    MainRoomController.isYoungManClicked = true;
    chat.sendMessage(event);
  }

  /**
   * This switches the scene to the guessing scene when guess button is clicked
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
      setSceneGuess();
    } catch (Exception e) {
      System.out.println("Error loading guessingRoom.fxml");
      System.exit(0);
    }
  }

  /** This method sets the scene to the menu */
  public void setSceneMenu() {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }

  /** This method sets the scene to the guessing room */
  public void setSceneGuess() {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.GUESSROOM));
  }

  /**
   * This method handles the hover effects turning on
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOn(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(1);
  }

   /**
   * This method handles the hover effects turning off
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOff(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(0);
  }

  /**
   * This method switches the scene to whatever is clicked
   *
   * @param event the mouse event that is triggered by clicking on the button
   * @param root the UI root of the scene to be switched to
   * @param name the name of the fxml file for the respective scene
   */
  private void switchScene(MouseEvent event, AppUi root, String name) {
    // Switch to scene to the inputed scene
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
