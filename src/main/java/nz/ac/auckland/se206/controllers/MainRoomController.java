package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * Controller class for the main room. Handles all the timer stuff and user interaction for the main
 * room.
 */
public class MainRoomController {
  // static fields
  public static boolean isClueFound = false; // whether the clue has been found
  public static boolean isOldManClicked = false; // whether the user has chated with the old man
  public static boolean isYoungManClicked = false; // whether the user has chated with the young man
  public static boolean isWomanClicked = false; // whether the user has chated with the woman
  public static boolean guessClicked = false; // where the user has press the guess button or not
  public static boolean isGuessTimerActive = true; // Default to true
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
  private boolean isMainTimerActive = true; // Default to true

  /** starts the timer on the main room once we switch to the main room. */
  @FXML
  public void initialize() {
    if (isFirstTimeInit) {
      // Start the timer task
      startMainTimer();

      // Initialize UI elements
      circleWoman.setOpacity(0);
      circleYoungMan.setOpacity(0);
      circleOldMan.setOpacity(0);

      isFirstTimeInit = false;
    }
  }

  private void startMainTimer() {
    Task<Void> countingTask =
        new Task<Void>() {
          @Override
          protected Void call() {
            // Set timer for 5 minutes (adjust as needed for testing)
            Platform.runLater(() -> lblTime.setText("5:00"));
            for (int i = 4; i >= 0; i--) {
              int minute = i;
              for (int j = 59; j >= 0; j--) {
                if (!isMainTimerActive) {
                  break; // Break the inner loop if the main timer is no longer active
                }
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                int second = j;
                updateTimerDisplay(minute, second);
              }
              if (!isMainTimerActive) {
                break; // Break the outer loop if the main timer is no longer active
              }
            }
            if (isMainTimerActive) {
              // After 5 minutes, check game conditions only if the timer wasn't stopped
              handleTimeUp();
            }
            return null;
          }
        };

    Thread backgroundThread = new Thread(countingTask);
    backgroundThread.setDaemon(true);
    backgroundThread.start();
  }

  private void updateTimerDisplay(int minute, int second) {
    if (!isMainTimerActive) return; // Don't update if main timer is stopped
    String time = minute + ":" + (second < 10 ? "0" + second : second);
    Platform.runLater(
        () -> {
          lblTime.setText(time);
          updateAllControllersTime(time);
        });
  }

  private void updateAllControllersTime(String time) {
    SceneManager.getOldManController().setLblTime(time);
    SceneManager.getYoungManController().setLblTime(time);
    SceneManager.getWomanController().setLblTime(time);
    SceneManager.getGuessController().setLblTime(time);
  }

  private void handleTimeUp() {
    if (!(isClueFound && isOldManClicked && isYoungManClicked && isWomanClicked)) {
      handleAutoLose();
    } else if (!guessClicked) {
      MenuController.playMedia("/sounds/sound06.mp3");
      transitionToGuessStage();
    }
  }

  private void handleAutoLose() {
    Platform.runLater(
        () -> {
          MenuController.playMedia("/sounds/sound15.mp3");
          resetBooleans();
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
        });
  }

  public void transitionToGuessStage() {
    isMainTimerActive = false; // Stop main timer updates

    // Stop updating the main timer here (by setting a flag or stopping the task)
    Platform.runLater(
        () -> {
          updateAllControllersTime("1:00"); // Set the guessing time (1 minute)
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
          startGuessTimer(); // Start the guessing timer
        });
  }

  private void startGuessTimer() {
    isGuessTimerActive = true; // Activate the guess timer
    Task<Void> guessTimerTask =
        new Task<Void>() {
          @Override
          protected Void call() {
            for (int i = 59; i >= 0; i--) {
              if (!isGuessTimerActive) {
                break; // Stop the timer if the answer is sent
              }
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              final int second = i;
              Platform.runLater(
                  () -> {
                    String time = "0:" + (second < 10 ? "0" + second : second);
                    lblTime.setText(time);
                    updateAllControllersTime(time);
                  });
            }
            if (isGuessTimerActive) {
              handleGuessTimeUp(); // Only call this if the timer runs out
            }
            return null;
          }
        };

    Thread guessThread = new Thread(guessTimerTask);
    guessThread.setDaemon(true);
    guessThread.start();
  }

  private void handleGuessTimeUp() {
    if (!SceneManager.getGuessController().isHasSelectedSuspect()) {
      Platform.runLater(
          () -> {
            MenuController.playMedia("/sounds/sound16.mp3");
            resetBooleans();
            SceneManager.getGuessController().setSceneMenu();
          });
    } else {
      try {
        SceneManager.getGuessController().onSendMessage(null);
      } catch (Exception e) {
        System.out.println("Failed to send message");
      }
    }
  }

  /**
   * This switches the scene to the old man.
   *
   * @param event the mouse event that is triggered by clicking on the old man
   */
  @FXML
  private void switchToOldMan(MouseEvent event) {
    // Use the switchScene method to switch
    switchScene(event, AppUi.OLDMANROOM, "oldManRoom");
  }

  /**
   * This switches the scene to the young man.
   *
   * @param event the mouse event that is triggered by clicking on the young man
   */
  @FXML
  private void switchToYoungMan(MouseEvent event) {
    // Use the switchScene method to switch
    switchScene(event, AppUi.YOUNGMANROOM, "youngManRoom");
  }

  /**
   * This switches the scene to the woman.
   *
   * @param event the mouse event that is triggered by clicking on the woman
   */
  @FXML
  private void switchToWoman(MouseEvent event) {
    // Use the switchScene method to switch
    switchScene(event, AppUi.WOMANROOM, "womanRoom");
  }

  /**
   * This switches the scene to the guessing scene when guess button is clicked.
   *
   * @param event the mouse event that is triggered by clicking on the guess button
   */
  @FXML
  private void handleGuessButtonClick(MouseEvent event) {
    if (isClueFound && isOldManClicked && isYoungManClicked && isWomanClicked) {
      guessClicked = true;
      MenuController.playMedia("/sounds/sound19.mp3");
      transitionToGuessStage();
    } else {
      MenuController.playMedia("/sounds/sound17.mp3");
    }
  }

  /**
   * Handles the click event on the footprint rectangle.
   *
   * @param event the mouse event that is triggered by clicking on the footprint
   */
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

  /**
   * Handles the click event on the case rectangle.
   *
   * @param event the mouse event that is triggered by clicking on the case
   */
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

  /**
   * Handles the click event on the piano rectangle.
   *
   * @param event the mouse event that is triggered by clicking on the piano
   */
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

  /**
   * Handles the click event for the send button.
   *
   * @param event the mouse event that is triggered by clicking on the send button
   */
  @FXML
  private void handleSendButtonClick(MouseEvent event) {
    messageHandler();
  }

  /** This method resets all of the games booleans. */
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
    isMainTimerActive = true;
    isGuessTimerActive = true;
  }

  /**
   * This method handles the hover effects turning on.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOn(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(1);
  }

  /**
   * This method handles the hover effects turning off.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOff(MouseEvent event) {
    Circle circle = (Circle) event.getSource();
    circle.setOpacity(0);
  }

  /**
   * This method handles the hover effects turning on for the clues.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void clueHoverOn(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0.2);
  }

  /**
   * This method handles the hover effects turning off for the clues.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void clueHoverOff(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0);
  }

  @FXML
  private void handleEnterKeyPress(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      messageHandler();
    }
  }

  /**
   * This method switches the scene to whatever is clicked.
   *
   * @param event the mouse event that is triggered by clicking on the button
   * @param root the UI root of the scene to be switched to
   * @param name the name of the fxml file for the respective scene
   */
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

  private void messageHandler() {
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
}
