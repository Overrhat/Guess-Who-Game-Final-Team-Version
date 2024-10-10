package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * Controller class for the young man. Handles all the user interaction with the young man scene.
 */
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

  /** This method sets up the chatbot for the young man. */
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

    txtInput.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            try {
              onSendMessage(new ActionEvent()); // Trigger send message
            } catch (ApiProxyException | IOException e) {
              e.printStackTrace();
            }
            event.consume(); // Consume the event so it doesn't propagate further
          }
        });
  }

  /**
   * This is the setter method for the young man timer.
   *
   * @param time the value to set the time
   */
  public void setLblTime(String time) {
    lblTime.setText(time);
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
   * This switches the scene to the guessing scene when guess button is clicked.
   *
   * @param event the mouse event that is triggered by clicking on the button
   */
  @FXML
  private void handleGuessButtonClick(MouseEvent event) {
    // Checking if all necessary conditions are met (clue found, chats with all key characters)
    if (!(MainRoomController.isClueFound
        && MainRoomController.isOldManClicked
        && MainRoomController.isYoungManClicked
        && MainRoomController.isWomanClicked)) {
      // Play a sound indicating the player cannot proceed to the guessing stage yet
      MenuController.playMedia("/sounds/sound17.mp3");
      return;
    }

    // Use the MainRoomController's transitionToGuessStage method to handle the transition
    MainRoomController.guessClicked = true;
    MenuController.playMedia("/sounds/sound19.mp3");
    SceneManager.getMainController().transitionToGuessStage();
  }

  /**
   * This method sets the scene to any scene.
   *
   * @param rootUi the scene to set it to
   */
  public void setSceneAny(AppUi rootUi) {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(rootUi));
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

  /**
   * This method handles switching the scene depending on what button was pressed.
   *
   * @param event the mouse event triggered by clicking on the switch scene button
   */
  @FXML
  private void handleSwitchScene(MouseEvent event) {
    Circle clickedCircle = (Circle) event.getSource();
    String circleId = clickedCircle.getId();

    switch (circleId) {
      case "circleCrimeScene":
        switchScene(event, AppUi.MAINROOM, "mainRoom");
        break;
      case "circleOldMan":
        switchScene(event, AppUi.OLDMANROOM, "oldManRoom");
        break;
      case "circleYoungMan":
        switchScene(event, AppUi.YOUNGMANROOM, "youngManRoom");
        break;
      case "circleWoman":
        switchScene(event, AppUi.WOMANROOM, "womanRoom");
        break;
    }
  }
}
