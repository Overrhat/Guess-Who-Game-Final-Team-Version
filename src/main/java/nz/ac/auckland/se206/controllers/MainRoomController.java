package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
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
  private int footprintNum = 0; // number of times the footprint has been clicked

  /** starts the timer on the main room once we switch to the main room */
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
                    String time = minute + ":" + "0" + second;
                    Platform.runLater(
                        () -> {
                          lblTime.setText(time);
                          SceneManager.getOldManController().setLblTime(time);
                        });
                    continue;
                  }
                  String time = minute + ":" + second;
                  Platform.runLater(
                      () -> {
                        lblTime.setText(time);
                        SceneManager.getOldManController().setLblTime(time);
                      });
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

  /** This switches the scene to the old man */
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

  /** This switches the scene to the young man */
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

  /** This switches the scene to the woman */
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

  /** Handles the click event on the footprint rectangle. */
  @FXML
  private void handleFootprintClick(MouseEvent event) {
    if (footprintNum == 0) {
      txtaChat.clear();
      MenuController.playMedia("/sounds/sound09.mp3");
      footprintNum++;
    } else if (footprintNum == 6) {
      MenuController.playMedia("/sounds/sound10.mp3");
    } else {
      footprintNum++;
    }
  }

  /** Handles the click event on the case rectangle. */
  @FXML
  private void handleCaseClick(MouseEvent event) {
    MenuController.playMedia("/sounds/sound13.mp3");

    // Put the text on the text area
    txtaChat.clear();
    txtaChat.appendText("Type: (Yes) or (No) to open the case\n\n");
  }

  /** Handles the click event on the piano rectangle. */
  @FXML
  private void handlePianoClick(MouseEvent event) {
    MenuController.playMedia("/sounds/sound11.mp3");

    // Put the text on the text area
    txtaChat.clear();
    txtaChat.appendText("Type: C or E to guess the note\n\n");
  }
}
