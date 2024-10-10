package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the guessing room. Handles user interactions with scene. */
public class GuessingRoomController {
  @FXML private Label lblTime;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnMenu;

  @FXML private Label title;
  @FXML private Label selection;

  @FXML private ImageView imagePopupBackground;
  @FXML private Label lblPopup1;
  @FXML private Label lblPopup2;

  @FXML private Rectangle rectOldMan;
  @FXML private Rectangle rectYoungMan;
  @FXML private Rectangle rectWoman;

  private ChatController chat;
  private boolean guessStatus = false;
  private boolean hasSelectedSuspect = false;

  /** This method initializes the chat AI. */
  public void initialize() {
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    btnSend.setDisable(true);
    txtaChat.setVisible(false);

    // Adding event filter for Enter key
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
   * This method prompts the user to send their reasoning of accusing a suspect.
   *
   * @param guess this is the name of the suspect
   */
  private void enterReasoning(String guess) {
    // this method handles the logic behind sending the reasoning
    chat.setGuess(guess);
    chat.setProfession("guess");
    btnSend.setDisable(false);
    selection.setText("Curent selection: " + guess);
    title.setText("Please enter your reasoning for your selection in the chat box.");
    txtaChat.clear();
    txtaChat.setVisible(true);
  }

  /**
   * This method selects the old man to be the suspect we are accusing.
   *
   * @param event the mouse event that is triggered by clicking on the old man
   */
  @FXML
  private void selectOldMan(MouseEvent event) {
    // This handles when the user selects the old man
    if (!chat.isLoading()) {
      rectOldMan.setDisable(true);
      rectYoungMan.setDisable(false);
      rectWoman.setDisable(false);
      enterReasoning("Edgar Thompson");
      hasSelectedSuspect = true;
    }
  }

  /**
   * This method selects the young man to be the suspect we are accusing.
   *
   * @param event the mouse event that is triggered by clicking on the young man
   */
  @FXML
  private void selectYoungMan(MouseEvent event) {
    // This handles when the user selects the young man
    if (!chat.isLoading()) {
      rectOldMan.setDisable(false);
      rectYoungMan.setDisable(true);
      rectWoman.setDisable(false);
      enterReasoning("Alex Carter");
      hasSelectedSuspect = true;
    }
  }

  /**
   * This method selects the woman to be the suspect we are accusing.
   *
   * @param event the mouse event that is triggered by clicking on the woman
   */
  @FXML
  private void selectWoman(MouseEvent event) {
    // This handles when the user selects the woman
    if (!chat.isLoading()) {
      rectOldMan.setDisable(false);
      rectYoungMan.setDisable(false);
      rectWoman.setDisable(true);
      enterReasoning("Lena Stone");
      hasSelectedSuspect = true;
    }
  }

  /**
   * This method handles sending messages to the AI.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    if (!hasSelectedSuspect) {
      MenuController.playMedia("/sounds/sound24.mp3");
      return;
    }

    // stop the timer
    MainRoomController.isGuessTimerActive = false;

    // this method handles sending messages to the chatgpt
    title.setVisible(false);
    selection.setVisible(false);
    rectOldMan.setDisable(true);
    rectYoungMan.setDisable(true);
    rectWoman.setDisable(true);
    chat.sendMessage(event);
    guessStatus = true;
  }

  /**
   * This method makes it so we can use the private FXML function in other classes.
   *
   * @param event the action event generated by sending a message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  public void sendMessagePublic(ActionEvent event) throws ApiProxyException, IOException {
    onSendMessage(event);
  }

  /**
   * This method handles the hover effects turning on.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOn(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0.2);
  }

  /**
   * This method handles the hover effects turning off.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOff(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0);
  }

  /**
   * This is a setter method for the LblTime.
   *
   * @param time the time to be set
   */
  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  /** This method sets the scene to be the menu. */
  public void setSceneMenu() {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }

  /**
   * This is a getter method for the hasSelectedSuspect boolean.
   *
   * @return the boolean hasSelectedSuspect
   */
  public boolean isHasSelectedSuspect() {
    return hasSelectedSuspect;
  }

  /**
   * This method handles the logic behind returning back to the menu.
   *
   * @param event the mouse event triggered by clicking the button
   */
  @FXML
  private void backToMenu(MouseEvent event) {
    if (!guessStatus) {
      MenuController.playMedia("/sounds/sound18.mp3");
      return;
    }
    setSceneMenu();
    SceneManager.getMainController().resetBooleans();
  }

  /**
   * This method enables / disables the popup depending on status.
   *
   * @param status a boolean to enable/disable the popup
   */
  public void enablePopup(boolean status) {
    imagePopupBackground.setVisible(status);
    lblPopup1.setVisible(status);
    lblPopup2.setVisible(status);
  }
}
