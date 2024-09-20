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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MainRoomController {
  @FXML private Label lblTime;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtaInput;
  @FXML private Button btnGuess;
  @FXML private Button btnSend;
  @FXML private Circle circleOldMan;
  @FXML private Circle circleYoungMan;
  @FXML private Circle circleWoman;
  @FXML private Rectangle rectCrimeScene;
  @FXML private Rectangle rectPiano;
  @FXML private Rectangle rectCase;
  @FXML private Rectangle rectFootprint;

  private static boolean isFirstTimeInit = true;
  private int footprintNum = 0; // number of times the footprint has been clicked
  private boolean isCaseClicked = false; // whether the case has been clicked
  private boolean isPianoClicked = false; // whether the piano has been clicked

  /** starts the timer on the main room once we switch to the main room */
  @FXML
  public void initialize() {
    if (isFirstTimeInit) {
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
                    Platform.runLater(() -> {lblTime.setText(time);
                      SceneManager.getOldManController().setLblTime(time);
                      SceneManager.getYoungManController().setLblTime(time);
                      SceneManager.getWomanController().setLblTime(time);
                    });
                    continue;
                  }
                  String time = minute + ":" + second;
                  Platform.runLater(() -> {lblTime.setText(time);
                  SceneManager.getOldManController().setLblTime(time);
                  SceneManager.getYoungManController().setLblTime(time);
                  SceneManager.getWomanController().setLblTime(time);
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

      circleWoman.setOpacity(0);
      circleYoungMan.setOpacity(0);
      circleOldMan.setOpacity(0);

    }
  }

  /** This switches the scene to the old man */
  @FXML
  private void oldMan(MouseEvent event) {
    try {
      Circle rect = (Circle) event.getSource();
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
      Circle rect = (Circle) event.getSource();
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
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.WOMANROOM));
    } catch (Exception e) {
      System.out.println("Error loading womanRoom.fxml");
      System.exit(0);
    }
  }

  /** This switches the scene to the guessing scene when guess button is clicked */
  @FXML
  private void handleGuessButtonClick(MouseEvent event) {
    try {
      // Get the current scene
      Scene scene = btnGuess.getScene();
      // Switch to the GUESSROOM scene
      scene.setRoot(SceneManager.getUiRoot(AppUi.GUESSROOM));
    } catch (Exception e) {
      System.out.println("Error loading guessingRoom.fxml");
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

    // set case and piano clicked to false
    isCaseClicked = false;
    isPianoClicked = false;

    // clear the text field
    txtaChat.clear();
    txtaInput.clear();
  }

  /** Handles the click event on the case rectangle. */
  @FXML
  private void handleCaseClick(MouseEvent event) {
    MenuController.playMedia("/sounds/sound13.mp3");

    // Put the text on the text area
    txtaChat.clear();
    txtaChat.appendText("Type: (Yes) or (No) to open the case\n\n");

    // Set the case clicked to true
    isCaseClicked = true;
    isPianoClicked = false;
  }

  /** Handles the click event on the piano rectangle. */
  @FXML
  private void handlePianoClick(MouseEvent event) {
    MenuController.playMedia("/sounds/sound11.mp3");

    // Put the text on the text area
    txtaChat.clear();
    txtaChat.appendText("Type: C or E to guess the note\n\n");

    // Set the piano clicked to true
    isPianoClicked = true;
    isCaseClicked = false;
  }

  /** Handles the click event for the send button. */
  @FXML
  private void handleSendButtonClick(MouseEvent event) {
    String userInput = txtaInput.getText().trim();
    if (!isCaseClicked && !isPianoClicked) {
      txtaChat.clear();
      // clear the text field
      txtaInput.clear();
      return;
    } else if (isPianoClicked) {
      if (userInput.equalsIgnoreCase("C")) {
        txtaChat.clear();
        txtaChat.appendText("Correct! The note is C\n\n");
        MenuController.playMedia("/sounds/sound12.mp3");

        // set case and piano clicked to false
        isCaseClicked = false;
        isPianoClicked = false;

      } else if (userInput.equalsIgnoreCase("E")) {
        txtaChat.clear();
        txtaChat.appendText("Incorrect! Try again!\n\n");
      } else {
        txtaChat.clear();
        txtaChat.appendText("Invalid input! Please type C or E\n\n");
      }
    } else if (isCaseClicked) {
      if (userInput.equalsIgnoreCase("Yes") || userInput.equalsIgnoreCase("Y")) {
        txtaChat.clear();
        txtaChat.appendText("Opened the case.\n\n");
        MenuController.playMedia("/sounds/sound14.mp3");

        // set case and piano clicked to false
        isCaseClicked = false;
        isPianoClicked = false;

      } else if (userInput.equalsIgnoreCase("No") || userInput.equalsIgnoreCase("N")) {
        txtaChat.clear();
        txtaChat.appendText("Didn't open the case.\n\n");
      } else {
        txtaChat.clear();
        txtaChat.appendText("Invalid input!\n\n");
      }
    }

    // clear the text field
    txtaInput.clear();
  }

  @FXML private void hoverOn(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(1);
  }

  @FXML private void hoverOff(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(0);
  }
}
