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

public class GuessingRoomController {
  @FXML private Label lblTime;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnMenu;

  @FXML private Label title;
  @FXML private Label selection;

  @FXML private Rectangle rectOldMan;
  @FXML private Rectangle rectYoungMan;
  @FXML private Rectangle rectWoman;

  private ChatController chat;
  private boolean guessStatus = false;
  private boolean hasSelectedSuspect = false;

  public void initialize() {
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    btnSend.setDisable(true);
    txtaChat.setVisible(false);
  }

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

  @FXML
  private void oldMan(MouseEvent event) {
    if (!chat.isLoading()) {
      rectOldMan.setDisable(true);
      rectYoungMan.setDisable(false);
      rectWoman.setDisable(false);
      enterReasoning("Edgar Thompson");
      hasSelectedSuspect = true;
    }
  }

  @FXML
  private void youngMan(MouseEvent event) {
    if (!chat.isLoading()) {
      rectOldMan.setDisable(false);
      rectYoungMan.setDisable(true);
      rectWoman.setDisable(false);
      enterReasoning("Alex Carter");
      hasSelectedSuspect = true;
    }
  }

  @FXML
  private void woman(MouseEvent event) {
    if (!chat.isLoading()) {
      rectOldMan.setDisable(false);
      rectYoungMan.setDisable(false);
      rectWoman.setDisable(true);
      enterReasoning("Lena Stone");
      hasSelectedSuspect = true;
    }
  }

  @FXML
  public void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isGuessTimerActive = false;

    // this method handles sending messages to the chatgpt
    title.setVisible(false);
    selection.setVisible(false);
    rectOldMan.setDisable(true);
    rectYoungMan.setDisable(true);
    rectWoman.setDisable(true);
    chat.onSendMessage(event);
    guessStatus = true;
  }

  @FXML
  private void hoverOn(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0.2);
  }

  @FXML
  private void hoverOff(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0);
  }

  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  public void setSceneMenu() {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }

  public boolean isHasSelectedSuspect() {
    return hasSelectedSuspect;
  }

  @FXML
  private void backToMenu(MouseEvent event) {
    if (!guessStatus) {
      MenuController.playMedia("/sounds/sound18.mp3");
      return;
    }
    setSceneMenu();
    SceneManager.getMainController().resetBooleans();
  }
}
