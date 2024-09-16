package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.GameStateContext;

public class MainRoomController {
  @FXML private Label lblTime;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtaInput;
  @FXML private Button btnGuess;
  @FXML private Button btnSend;
  @FXML private Rectangle rectOldMan;
  @FXML private Rectangle rectYoungMan;
  @FXML private Rectangle rectWoman;
  @FXML private Rectangle rectCrimeScene;
  @FXML private Rectangle rectPiano;
  @FXML private Rectangle rectCase;
  @FXML private Rectangle rectFootprint;

  private static boolean isFirstTimeInit = true;
  private static GameStateContext context;

  /** 
   * starts the timer on the main room once we switch to the main room
   */
  @FXML
  public void initialize() {
    if (isFirstTimeInit) {
      context = MenuController.getContext();
      Task<Void> countingTask =
          new Task<Void>() {
            @Override
            protected Void call() {
              // start timer for 5 x 60 seconds
              lblTime.setText("5:00");
              for (int i = 4; i >= 0; i--) {
                int minute = i;
                for (int j = 59; j >= 0; j--) {
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  int second = j;
                  if (j < 10) {
                    Platform.runLater(() -> lblTime.setText(minute + ":" + "0" + second));
                    continue;
                  }
                  Platform.runLater(() -> lblTime.setText(minute + ":" + second));
                }
              }
              return null;
            }
          };
      isFirstTimeInit = false;
      Thread backgroundThread = new Thread(countingTask);
      backgroundThread.setDaemon(true);
      backgroundThread.start();
    }
  }
}
