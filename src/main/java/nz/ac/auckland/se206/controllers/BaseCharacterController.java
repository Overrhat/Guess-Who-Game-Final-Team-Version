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

  public void initialize(String profession) {
    chat = new ChatController();
    chat.setTxtaChat(txtaChat);
    chat.setTxtInput(txtInput);
    chat.setBtnSend(btnSend);
    chat.setProfession(profession);

    circleCrimeScene.setOpacity(0);
    if (circleOldMan != null) circleOldMan.setOpacity(0);
    if (circleYoungMan != null) circleYoungMan.setOpacity(0);
    if (circleWoman != null) circleWoman.setOpacity(0);

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

  public void setLblTime(String time) {
    lblTime.setText(time);
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
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    chat.sendMessage(event);
  }

  @FXML
  private void handleGuessButtonClick(MouseEvent event) {
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

  public void setSceneAny(AppUi rootUi) {
    Scene scene = lblTime.getScene();
    scene.setRoot(SceneManager.getUiRoot(rootUi));
  }

  private void switchScene(MouseEvent event, AppUi root, String name) {
    try {
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(root));
    } catch (Exception e) {
      System.out.println("Error loading " + name + ".fxml");
      System.exit(0);
    }
  }

  @FXML
  protected void handleSwitchScene(MouseEvent event) {
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
