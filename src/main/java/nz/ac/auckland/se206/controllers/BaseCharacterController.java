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
 * An abstract base controller class for character-related functionality. This class manages UI
 * elements for different character scenes, including sending messages and scene transitions.
 */
public abstract class BaseCharacterController {

  @FXML protected Label lblTime;
  @FXML protected Circle circleCrimeScene;
  @FXML protected Circle circleYoungMan;
  @FXML protected Circle circleOldMan;
  @FXML protected Circle circleWoman;

  @FXML protected TextArea txtaChat;
  @FXML protected TextField txtInput;
  @FXML protected Button btnSend;
  @FXML protected Button btnGuess;

  protected ChatController chat;

  /**
   * Initializes the controller with the specified profession. This method sets up the chat
   * functionality and manages the visibility of circles based on the profession.
   *
   * @param profession the profession to initialize the chat controller with
   */
  public void initialize(String profession) {
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    chat.setProfession(profession);

    // Set opacity to 0.
    circleCrimeScene.setOpacity(0);
    if (circleOldMan != null) {
      circleOldMan.setOpacity(0);
    }
    if (circleYoungMan != null) {
      circleYoungMan.setOpacity(0);
    }
    if (circleWoman != null) {
      circleWoman.setOpacity(0);
    }

    txtInput.addEventFilter(
        KeyEvent.KEY_PRESSED,
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            try {
              onSendMessage(new ActionEvent());
            } catch (ApiProxyException | IOException e) {
              e.printStackTrace();
            }
            event.consume();
          }
        });
  }

  /**
   * Updates the label to display the specified time.
   *
   * @param time the time to display
   */
  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  /**
   * Handles mouse hover event to show the circle.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverOn(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(1);
  }

  /**
   * Handles mouse exit event to hide the circle.
   *
   * @param event the mouse event
   */
  @FXML
  private void hoverOff(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(0);
  }

  /**
   * Sends a chat message when invoked.
   *
   * @param event the action event triggered by the send button or Enter key
   * @throws ApiProxyException if an API error occurs
   * @throws IOException if an I/O error occurs
   */
  @FXML
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    chat.sendMessage(event);
  }

  /**
   * Handles the click event for the guess button. If all clues have been found, it transitions to
   * the guess stage.
   *
   * @param event the mouse event triggered by the guess button
   */
  @FXML
  private void handleGuessButtonClick(MouseEvent event) {
    // Check if they have done the clues.
    if (!(MainRoomController.isClueFound
        && MainRoomController.isOldManClicked
        && MainRoomController.isYoungManClicked
        && MainRoomController.isWomanClicked)) {
      MenuController.playMedia("/sounds/sound17.mp3");
      return;
    }

    MainRoomController.guessClicked = true;
    MenuController.playMedia("/sounds/sound19.mp3");
    SceneManager.getMainController().transitionToGuessStage();
  }

  /**
   * Sets the scene to a specified UI root.
   *
   * @param rootUi the application UI to set as root
   */
  public void setSceneAny(AppUi rootUi) {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(rootUi));
  }

  /**
   * Switches the scene based on the clicked circle.
   *
   * @param event the mouse event triggered by the circle click
   * @param root the application UI to switch to
   * @param name the name of the FXML file being loaded (for error handling)
   */
  private void switchScene(MouseEvent event, AppUi root, String name) {
    // Get the scene button they clicked on.
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
   * Handles scene switching based on the clicked circle's ID.
   *
   * @param event the mouse event triggered by the circle click
   */
  @FXML
  protected void handleSwitchScene(MouseEvent event) {
    Circle clickedCircle = (Circle) event.getSource();
    String circleId = clickedCircle.getId();

    // Do the switch scene.
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
