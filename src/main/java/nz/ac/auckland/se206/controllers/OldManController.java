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

public class OldManController {
  @FXML private Label lblTime;
  @FXML private Circle circleCrimeScene;
  @FXML private Circle circleYoungMan;
  @FXML private Circle circleWoman;

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnGuess;

  private ChatController chat;

  public void initialize() {
    // Initialize the chat.
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    chat.setProfession("oldMan");

    // Initialize the circles.
    circleCrimeScene.setOpacity(0);
    circleYoungMan.setOpacity(0);
    circleWoman.setOpacity(0);

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

  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  private void switchScene(MouseEvent event, AppUi root, String name) {
    try {
      // Find source of click.
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(root));
    } catch (Exception e) {
      // Print error.
      System.out.println("Error loading " + name + ".fxml");
      System.exit(0);
    }
  }

  /** This switches the scene to the crime scene. */
  @FXML
  private void crimeScene(MouseEvent event) {
    switchScene(event, AppUi.MAINROOM, "mainRoom");
  }

  /** This switches to the young man room. */
  @FXML
  private void youngMan(MouseEvent event) {
    switchScene(event, AppUi.YOUNGMANROOM, "youngManRoom");
  }

  /** This switches to the woman room. */
  @FXML
  private void woman(MouseEvent event) {
    switchScene(event, AppUi.WOMANROOM, "womanRoom");
  }

  @FXML
  private void hoverOn(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(1);
  }

  @FXML
  private void hoverOff(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(0);
  }

  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isOldManClicked = true;
    chat.onSendMessage(event);
  }

  /** This switches the scene to the guessing scene when guess button is clicked */
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
}
