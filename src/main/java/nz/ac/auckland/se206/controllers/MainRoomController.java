package nz.ac.auckland.se206.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
  @FXML private ImageView blurredBackground;
  @FXML private ImageView pianoKeys;
  @FXML private ImageView soundButtonImage;
  @FXML private Label matchText;
  @FXML private Label currentSelectionText;
  @FXML private Label againText;
  @FXML private ImageView backButtonImage;
  @FXML private ImageView imgDust;
  @FXML private Rectangle backButton;
  @FXML private Rectangle recC3;
  @FXML private Rectangle recDb3;
  @FXML private Rectangle recD3;
  @FXML private Rectangle recEb3;
  @FXML private Rectangle recE3;
  @FXML private Rectangle recF3;
  @FXML private Rectangle recGb3;
  @FXML private Rectangle recG3;
  @FXML private Rectangle recAb3;
  @FXML private Rectangle recA3;
  @FXML private Rectangle recBb3;
  @FXML private Rectangle recB3;
  @FXML private Rectangle recC4;
  @FXML private Rectangle recDb4;
  @FXML private Rectangle recD4;
  @FXML private Rectangle recEb4;
  @FXML private Rectangle recE4;
  @FXML private Rectangle recF4;
  @FXML private Rectangle recGb4;
  @FXML private Rectangle recG4;
  @FXML private Rectangle recAb4;
  @FXML private Rectangle recA4;
  @FXML private Rectangle recBb4;
  @FXML private Rectangle recB4;

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

    circleWoman.setOpacity(0);
    circleYoungMan.setOpacity(0);
    circleOldMan.setOpacity(0);

    // Disable all the piano keys
    setAllKeysDisabled();

    Thread backgroundThread = new Thread(countingTask);
    backgroundThread.setDaemon(true);
    backgroundThread.start();
  }

  /**
   * This method is called when the timer is updated. It will update the time for the main stage and
   * all the controllers.
   */
  private void updateTimerDisplay(int minute, int second) {
    // Update the time for the main stage
    if (!isMainTimerActive) {
      return; // Don't update if main timer is stopped
    }
    String time = minute + ":" + (second < 10 ? "0" + second : second);
    Platform.runLater(
        () -> {
          lblTime.setText(time);
          updateAllControllersTime(time);
        });
  }

  /**
   * This method is called when the time is up for the main stage. It will update the time for all
   * the controllers.
   */
  private void updateAllControllersTime(String time) {
    // Update the time for all the controllers
    SceneManager.getOldManController().setLblTime(time);
    SceneManager.getYoungManController().setLblTime(time);
    SceneManager.getWomanController().setLblTime(time);
    SceneManager.getGuessController().setLblTime(time);
  }

  /**
   * This method is called when the time is up for the main stage. It will check if the user has
   * chatted with all the characters and found the clue. If not then it will switch the scene back
   * to the menu.
   */
  private void handleTimeUp() {
    if (!(isClueFound && isOldManClicked && isYoungManClicked && isWomanClicked)) {
      handleAutoLose();
    } else if (!guessClicked) {
      MenuController.playMedia("/sounds/sound06.mp3");
      SceneManager.getGuessController().enablePopup(true); // Enable popup if auto transition
      transitionToGuessStage();
    }
  }

  /**
   * This method is called when the time is up for the main stage and the user did not chat with all
   * the characters or find any of the clue. It will switch the scene back to the menu.
   */
  private void handleAutoLose() {
    // Switch to the menu scene when the time is up
    Platform.runLater(
        () -> {
          // generate a sound that the user has lost
          MenuController.playMedia("/sounds/sound15.mp3");
          resetBooleans();
          SceneManager.AppUi currentRoom = SceneManager.getCurrentRoom();
          switch (currentRoom) {
            case OLDMANROOM:
              SceneManager.getOldManController().setSceneAny(AppUi.MENU);
              break;
            case YOUNGMANROOM:
              SceneManager.getYoungManController().setSceneAny(AppUi.MENU);
              break;
            case WOMANROOM:
              SceneManager.getWomanController().setSceneAny(AppUi.MENU);
              break;
            default:
              Scene scene = lblTime.getScene();
              // Switch to the menu scene
              scene.setRoot(SceneManager.getUiRoot(AppUi.MENU));
              break;
          }
        });
  }

  /**
   * Transitions the application from the main stage to the guessing stage.
   *
   * <p>This method is called when the main stage timer expires. It stops the main timer, updates
   * the time display to "1:00" for the guessing stage, switches the scene based on the current
   * room, and starts the guessing timer.
   */
  public void transitionToGuessStage() {
    isMainTimerActive = false; // Stop main timer updates

    // Stop updating the main timer here (by setting a flag or stopping the task)
    Platform.runLater(
        () -> {
          updateAllControllersTime("1:00"); // Set the guessing time (1 minute)
          SceneManager.AppUi currentRoom = SceneManager.getCurrentRoom();
          switch (currentRoom) {
            case OLDMANROOM:
              SceneManager.getOldManController().setSceneAny(AppUi.GUESSROOM);
              break;
            case YOUNGMANROOM:
              SceneManager.getYoungManController().setSceneAny(AppUi.GUESSROOM);
              break;
            case WOMANROOM:
              SceneManager.getWomanController().setSceneAny(AppUi.GUESSROOM);
              break;
            default:
              Scene scene = lblTime.getScene();
              scene.setRoot(SceneManager.getUiRoot(AppUi.GUESSROOM));
              break;
          }
          startGuessTimer(); // Start the guessing timer
        });
  }

  /**
   * This method is called when the timer of the guessing stage is started. It will start a new
   * thread that will update the time for the guessing stage.
   */
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
              if (second == 57) {
                SceneManager.getGuessController()
                    .enablePopup(false); // disable popup after 2 seconds
              }
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

  /**
   * This method is called when the time is up for the guessing stage. It will check if the user has
   * typed in a guess or not. If it has then it will send the message to the server. If not then it
   * will switch the scene to the menu.
   */
  private void handleGuessTimeUp() {
    // Check if the user has typed in a guess
    if (!SceneManager.getGuessController().isHasSelectedSuspect()) {
      Platform.runLater(
          () -> {
            MenuController.playMedia("/sounds/sound16.mp3");
            resetBooleans();
            SceneManager.getGuessController().setSceneMenu();
          });
    } else {
      try {
        SceneManager.getGuessController().sendMessagePublic(null);
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
    // Check if all the clues have been found and all the characters have been chatted with
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
    if (footprintNum == 0) {
      txtaChat.clear();
      MenuController.playMedia("/sounds/sound21.mp3");
      footprintNum++;
    } else if (footprintNum < 5) {
      MenuController.playMedia("/sounds/sound21.mp3");
      imgDust.setOpacity(imgDust.getOpacity() - 0.25); // Reduce opacity by 0.25 each click
      footprintNum++;
    } else {
      MenuController.playMedia("/sounds/sound22.mp3");
      // the user has found the clue
      isClueFound = true;
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

    MenuController.playMedia("/sounds/sound20.mp3");

    // Put the text on the text area
    txtaChat.clear();
    txtaChat.appendText("Type the 4-digit birthday (mm/dd) of the owner to opend the case.\n\n");

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

    // Initialize the piano state
    blurredBackground.setVisible(true);
    blurredBackground.setDisable(false);
    pianoKeys.setVisible(true);
    soundButtonImage.setVisible(true);
    matchText.setVisible(true);
    currentSelectionText.setVisible(true);
    againText.setVisible(true);
    backButton.setDisable(false);
    backButtonImage.setVisible(true);

    // Enable all the piano keys
    recC3.setDisable(false);
    recDb3.setDisable(false);
    recD3.setDisable(false);
    recEb3.setDisable(false);
    recE3.setDisable(false);
    recF3.setDisable(false);
    recGb3.setDisable(false);
    recG3.setDisable(false);
    recAb3.setDisable(false);
    recA3.setDisable(false);
    recBb3.setDisable(false);
    recB3.setDisable(false);
    recC4.setDisable(false);
    recDb4.setDisable(false);
    recD4.setDisable(false);
    recEb4.setDisable(false);
    recE4.setDisable(false);
    recF4.setDisable(false);
    recGb4.setDisable(false);
    recG4.setDisable(false);
    recAb4.setDisable(false);
    recA4.setDisable(false);
    recBb4.setDisable(false);
    recB4.setDisable(false);

    // Play the note E4
    MenuController.playMedia("/sounds/notes/E4.mp3");

    // Set the piano clicked to true
    isPianoClicked = true;
    isCaseClicked = false;
  }

  /**
   * Handles the click event when a note is played on the piano.
   *
   * @param event the mouse event that is triggered by clicking on the piano keys
   */
  @FXML
  private void playNote(MouseEvent event) {
    // Play the note
    Rectangle rect = (Rectangle) event.getSource();
    String note = rect.getId();
    MenuController.playMedia("/sounds/notes/" + note.substring(3) + ".mp3");
    currentSelectionText.setText("Current selection: " + note.substring(3));
    if (note.equals("recE4")) {
      handleBackButtonClick(event);
      MenuController.playMedia("/sounds/sound23.mp3");
    }
  }

  /**
   * Handles the click event when the correct note is clicked.
   *
   * @param event the mouse event that is triggered by clicking on the sound button
   */
  @FXML
  private void playCorrectNote(MouseEvent event) {
    MenuController.playMedia("/sounds/notes/E4.mp3");
  }

  /**
   * Handles the click event when hovering over the sound button.
   *
   * @param event the mouse event that is triggered by clicking on the back button
   */
  @FXML
  private void soundHoverOn(MouseEvent event) {
    soundButtonImage.setOpacity(1);
  }

  /**
   * Handles the click event when hovering off the sound button.
   *
   * @param event the mouse event that is triggered by clicking on the back button
   */
  @FXML
  private void soundHoverOff(MouseEvent event) {
    soundButtonImage.setOpacity(0.5);
  }

  /**
   * Handles the click event when hovering over the piano keys.
   *
   * @param event the mouse event that is triggered by clicking on the back button
   */
  @FXML
  private void pianoHoverOn(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(1);
  }

  /**
   * Handles the click event when hovering off the piano keys.
   *
   * @param event the mouse event that is triggered by clicking on the back button
   */
  @FXML
  private void pianoHoverOff(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    rect.setOpacity(0);
  }

  /**
   * Handles the click event when the back button is clicked.
   *
   * @param event the mouse event that is triggered by clicking on the back button
   */
  @FXML
  private void handleBackButtonClick(MouseEvent event) {

    // Set the piano state to initial state
    blurredBackground.setVisible(false);
    blurredBackground.setDisable(true);
    pianoKeys.setVisible(false);
    soundButtonImage.setVisible(false);
    matchText.setVisible(false);
    currentSelectionText.setVisible(false);
    againText.setVisible(false);
    backButton.setDisable(true);
    backButtonImage.setVisible(false);

    // Disable all the piano keys
    setAllKeysDisabled();
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

  /** This handles the enter key press. */
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

  /** This handles the message inputed by the user. */
  private void messageHandler() {
    String userInput = txtaInput.getText().trim();
    if (!isCaseClicked && !isPianoClicked) {
      txtaChat.clear();
      // clear the text field
      txtaInput.clear();
      return;
    } else if (isCaseClicked) {
      if (userInput.equalsIgnoreCase("1105") || userInput.equalsIgnoreCase("11/05")) {
        txtaChat.clear();
        txtaChat.appendText("Correct Pasword! Opened the case.\n\n");
        txtaChat.appendText("Dr. Watson: Oh! I see blonde hair inside the case.\n\n");
        MenuController.playMedia("/sounds/sound14.mp3");

        // set case and piano clicked to false
        isCaseClicked = false;
        isPianoClicked = false;

      } else {
        txtaChat.clear();
        txtaChat.appendText("Incorrect Pasword! Couldn't open the case.\n\n");
      }
    }

    // clear the text field
    txtaInput.clear();
  }

  /** This method disables all the piano keys. */
  private void setAllKeysDisabled() {
    // Disable all the piano keys
    recC3.setDisable(true);
    recDb3.setDisable(true);
    recD3.setDisable(true);
    recEb3.setDisable(true);
    recE3.setDisable(true);
    recF3.setDisable(true);
    recGb3.setDisable(true);
    // Harry is the goat
    recG3.setDisable(true);
    recAb3.setDisable(true);
    recA3.setDisable(true);
    recBb3.setDisable(true);
    recB3.setDisable(true);
    recC4.setDisable(true);
    recDb4.setDisable(true);
    recD4.setDisable(true);
    recEb4.setDisable(true);
    recE4.setDisable(true);
    // Harry is so smart
    recF4.setDisable(true);
    recGb4.setDisable(true);
    recG4.setDisable(true);
    recAb4.setDisable(true);
    recA4.setDisable(true);
    recBb4.setDisable(true);
    recB4.setDisable(true);
  }
}
