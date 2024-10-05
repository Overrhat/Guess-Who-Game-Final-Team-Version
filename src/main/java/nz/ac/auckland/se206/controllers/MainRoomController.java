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
  // static fields
  public static boolean isClueFound = false; // whether the clue has been found
  public static boolean isOldManClicked = false; // whether the user has chated with the old man
  public static boolean isYoungManClicked = false; // whether the user has chated with the young man
  public static boolean isWomanClicked = false; // whether the user has chated with the woman
  public static boolean guessClicked = false; // where the user has press the guess button or not
  private static boolean isFirstTimeInit = true;

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
              Platform.runLater(() -> lblTime.setText("5:00"));
              for (int i = 4; i >= 0; i--) { // TEMP CHANGE THIS BEFORE PUSH set to 4
                int minute = i;
                for (int j = 59; j >= 0; j--) { // temp change this before push set to 59
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  int second = j;
                  // special formatting for it time is less than 10 seconds
                  if (j < 10) {
                    String time = minute + ":" + "0" + second;
                    Platform.runLater(
                        () -> {
                          lblTime.setText(time);
                          SceneManager.getOldManController().setLblTime(time);
                          SceneManager.getYoungManController().setLblTime(time);
                          SceneManager.getWomanController().setLblTime(time);
                          SceneManager.getGuessController().setLblTime(time);
                        });
                    continue;
                  }
                  // normal formatting for greater than 10 seconds
                  String time = minute + ":" + second;
                  Platform.runLater(
                      () -> {
                        lblTime.setText(time);
                        SceneManager.getOldManController().setLblTime(time);
                        SceneManager.getYoungManController().setLblTime(time);
                        SceneManager.getWomanController().setLblTime(time);
                        SceneManager.getGuessController().setLblTime(time);
                      });
                }
              }
              // if we dont meet conditions to guess just make us lose automatically
              if (!(isClueFound && isOldManClicked && isYoungManClicked && isWomanClicked)) {
                MenuController.playMedia("/sounds/sound15.mp3");
                // WE CAN DECIDE WHAT TO DO AFTER THE GAME AUTO LOSES HERE
                // for now im just gonna kick them to the menu so they can play again
                resetBooleans();

                // WE CAN CHANGE THIS BUT THIS IS JUST AN EASY WAY TO CHANGE TO MENU ON ANY SCENE WE
                // ARE ON
                SceneManager.AppUi currentRoom = SceneManager.getCurrentRoom();
                switch (currentRoom) {
                  case OLDMANROOM:
                    SceneManager.getOldManController().setSceneMenu();
                    break;
                  case YOUNGMANROOM:
                    SceneManager.getYoungManController().setSceneMenu();
                    break;
                  case WOMANROOM:
                    SceneManager.getWomanController().setSceneMenu();
                    break;
                  default:
                    Scene scene = lblTime.getScene();
                    scene.setRoot(SceneManager.getUiRoot(AppUi.MENU));
                    break;
                }
                return null;
              }
              // after the timer ends, if we are not in the guessing room move us there and give
              // another minute
              else if (!guessClicked) {
                Platform.runLater(() -> SceneManager.getGuessController().setLblTime("1:00"));
                MenuController.playMedia("/sounds/sound06.mp3");
                SceneManager.AppUi currentRoom = SceneManager.getCurrentRoom();
                switch (currentRoom) {
                  case OLDMANROOM:
                    SceneManager.getOldManController().setSceneGuess();
                    break;
                  case YOUNGMANROOM:
                    SceneManager.getYoungManController().setSceneGuess();
                    break;
                  case WOMANROOM:
                    SceneManager.getWomanController().setSceneGuess();
                    break;
                  default:
                    Scene scene = lblTime.getScene();
                    scene.setRoot(SceneManager.getUiRoot(AppUi.GUESSROOM));
                    break;
                }
                for (int i = 59; i >= 0; i--) {
                  try {
                    Thread.sleep(1000);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  if (i < 10) {
                    String time = "0:" + "0" + i;
                    Platform.runLater(
                        () -> {
                          lblTime.setText(time);
                          SceneManager.getOldManController().setLblTime(time);
                          SceneManager.getYoungManController().setLblTime(time);
                          SceneManager.getWomanController().setLblTime(time);
                          SceneManager.getGuessController().setLblTime(time);
                        });
                    continue;
                  }
                  String time = "0:" + i;
                  Platform.runLater(
                      () -> {
                        lblTime.setText(time);
                        SceneManager.getOldManController().setLblTime(time);
                        SceneManager.getYoungManController().setLblTime(time);
                        SceneManager.getWomanController().setLblTime(time);
                        SceneManager.getGuessController().setLblTime(time);
                      });
                }
                MenuController.playMedia("/sounds/sound16.mp3");
                // PUT WHATEVER YOU WANT TO HAPPEN AFTER THE 60 SECONDS IN GUESSING IS UP HERE
                resetBooleans();
                SceneManager.getGuessController().setSceneMenu();
                return null;
              } else {
                MenuController.playMedia("/sounds/sound07.mp3");
                resetBooleans();
                SceneManager.getGuessController().setSceneMenu();
              }
              return null;
            }
          };
      isFirstTimeInit = false;
      // run the background task
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
  private void switchToOldMan(MouseEvent event) {
    // Use the switchScene method to switch
    switchScene(event, AppUi.OLDMANROOM, "oldManRoom");
  }

  /** This switches the scene to the young man */
  @FXML
  private void switchToYoungMan(MouseEvent event) {
    // Use the switchScene method to switch
    switchScene(event, AppUi.YOUNGMANROOM, "youngManRoom");
  }

  /** This switches the scene to the woman */
  @FXML
  private void switchToWoman(MouseEvent event) {
    // Use the switchScene method to switch
    switchScene(event, AppUi.WOMANROOM, "womanRoom");
  }

  /** This switches the scene to the guessing scene when guess button is clicked */
  @FXML
  private void handleGuessButtonClick(MouseEvent event) {
    // Checking the requirements to switch to the guessing scene
    if (!(isClueFound && isOldManClicked && isYoungManClicked && isWomanClicked)) {
      MenuController.playMedia("/sounds/sound17.mp3");
      return;
    }

    try {
      // Get the current scene
      Scene scene = btnGuess.getScene();
      // Enable guess clicked boolean
      guessClicked = true;
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
    // the user has found the clue
    isClueFound = true;

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
    // the user has found the clue
    isClueFound = true;

    MenuController.playMedia("/sounds/sound13.mp3");

    // Put the text on the text area
    txtaChat.clear();
    txtaChat.appendText("Type: Yes or No to open the case\n\n");

    // Set the case clicked to true
    isCaseClicked = true;
    isPianoClicked = false;
  }

  /** Handles the click event on the piano rectangle. */
  @FXML
  private void handlePianoClick(MouseEvent event) {
    // the user has found the clue
    isClueFound = true;

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

  public void resetBooleans() {
    // reset all the booleans to initial state
    isFirstTimeInit = true;
    footprintNum = 0;
    isCaseClicked = false;
    isPianoClicked = false;
    isClueFound = false;
    isOldManClicked = false;
    isYoungManClicked = false;
    isWomanClicked = false;
    guessClicked = false;
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
  private void clueHoverOn(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0.2);
  }

  @FXML
  private void clueHoverOff(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0);
  }

  private void switchScene(MouseEvent event, AppUi root, String name) {
    // Switch to scene to the inputed scene
    try {
      Circle rect = (Circle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(root));
    } catch (Exception e) {
      System.out.println("Error loading " + name + ".fxml");
      System.exit(0);
    }
  }
}
