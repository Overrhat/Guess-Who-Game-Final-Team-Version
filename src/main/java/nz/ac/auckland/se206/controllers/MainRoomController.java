package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

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

  /**
   * This switches the scene to the old man
   */
  @FXML
  private void oldMan(MouseEvent event) {
    try {
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.OLDMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading oldManRoom.fxml");
      System.exit(0);
    }
  }

  /**
   * This switches the scene to the young man
   */
  @FXML
  private void youngMan(MouseEvent event) {
    try {
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.YOUNGMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading youngManRoom.fxml");
      System.exit(0);
    }
  }

  /**
   * This switches the scene to the woman
   */
  @FXML
  private void woman(MouseEvent event) {
    try {
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.WOMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading womanRoom.fxml");
      System.exit(0);
    }
  }

}
