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

  @FXML private Label title;
  @FXML private Label selection;
  
  @FXML private Rectangle rectOldMan;
  @FXML private Rectangle rectYoungMan;
  @FXML private Rectangle rectWoman;

  private String guess;
  
  private ChatController chat;

  public void initialize() {
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    btnSend.setDisable(true);
    txtaChat.setVisible(false);
  }

  private void enterReasoning(String guess) {
    this.guess = guess;
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
    rectOldMan.setDisable(true);
    rectYoungMan.setDisable(false);
    rectWoman.setDisable(false);
    enterReasoning("Edgar Thompson");
  }

  @FXML
  private void youngMan(MouseEvent event) {
    rectOldMan.setDisable(false);
    rectYoungMan.setDisable(true);
    rectWoman.setDisable(false);
    enterReasoning("Alex Carter");
  }

  @FXML
  private void woman(MouseEvent event) {
    rectOldMan.setDisable(false);
    rectYoungMan.setDisable(false);
    rectWoman.setDisable(true);
    enterReasoning("Lena Stone");
  }





  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    title.setVisible(false);
    selection.setVisible(false);
    rectOldMan.setDisable(true);
    rectYoungMan.setDisable(true);
    rectWoman.setDisable(true);
    chat.onSendMessage(event);
  }

}
