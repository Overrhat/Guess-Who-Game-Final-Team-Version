package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

/** Controller class for the main menu. This class also contains the playMedia method. */
public class MenuController {

  // Static fields
  public static MediaPlayer mediaPlayer;
  private static boolean isFirstTimeInit = true;

  // Static methods
  /**
   * Plays media from the specified file path. If a media file is already playing, it stops the
   * current playback before starting the new one.
   *
   * @param filePath The path to the media file to be played. This should be a valid resource path.
   * @throws IllegalArgumentException if the file path is null or the media file cannot be found.
   * @throws MediaException if an error occurs while trying to play the media.
   *     <p>This method uses JavaFX's {@link Media} and {@link MediaPlayer} classes to play media
   *     files. It handles potential exceptions such as {@link URISyntaxException} for invalid URI
   *     syntax and {@link NullPointerException} for missing resources. Any other exceptions
   *     encountered during media playback are also caught and logged.
   */
  public static void playMedia(String filePath) {
    try {
      // If sound is playing, turn off the sound
      if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
        mediaPlayer.stop();
      }

      Media sound = new Media(App.class.getResource(filePath).toURI().toString());
      mediaPlayer = new MediaPlayer(sound);
      mediaPlayer.play();
    } catch (URISyntaxException e) {
      System.err.println("Invalid URI syntax: " + e.getMessage());
    } catch (NullPointerException e) {
      System.err.println("Resource not found: " + filePath);
    } catch (Exception e) {
      System.err.println("An error occurred while trying to play media: " + e.getMessage());
    }
  }

  // Instance fields
  @FXML private Line bottomLine;
  @FXML private QuadCurve leftLine;
  @FXML private Rectangle rectStart;
  @FXML private QuadCurve rightLine;
  @FXML private Line topLine;

  // Instance methods
  /**
   * Initializes the menu. If it's the first time initialization, it will provide instructions via
   * text-to-speech.
   */
  @FXML
  public void initialize() {
    if (isFirstTimeInit) {
      // Provide intro by media player
      playMedia("/sounds/sound01.mp3");

      // we can make it say tts with this code when they first load the game
      isFirstTimeInit = false;
    }
  }

  /**
   * Handles mouse clicks on rectangles representing start button.
   *
   * @param event the mouse event triggered by clicking a rectangle
   */
  @FXML
  private void handleStartClick(MouseEvent event) {
    Rectangle rect = (Rectangle) event.getSource();
    Scene scene = rect.getScene();

    // Create a black overlay rectangle to cover the entire scene.
    Rectangle blackOverlay = new Rectangle(scene.getWidth(), scene.getHeight());
    blackOverlay.setFill(Color.BLACK);
    blackOverlay.setOpacity(0.0); // Start invisible

    // Add the black overlay to the scene's root temporarily.
    ((Pane) scene.getRoot()).getChildren().add(blackOverlay);

    // Create a fade-in transition for the black overlay.
    FadeTransition fadeToBlack = new FadeTransition(Duration.millis(500), blackOverlay);
    fadeToBlack.setFromValue(0.0);
    fadeToBlack.setToValue(1.0);

    fadeToBlack.setOnFinished(
        fadeOutEvent -> {
          // After fade to black, start loading the new scenes in the background.
          Task<Void> loadTask =
              new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                  try {
                    // Load FXMLs.
                    FXMLLoader main =
                        new FXMLLoader(App.class.getResource("/fxml/" + "mainRoom" + ".fxml"));
                    FXMLLoader woman =
                        new FXMLLoader(App.class.getResource("/fxml/" + "womanRoom" + ".fxml"));
                    FXMLLoader oldMan =
                        new FXMLLoader(App.class.getResource("/fxml/" + "oldManRoom" + ".fxml"));
                    FXMLLoader youngMan =
                        new FXMLLoader(App.class.getResource("/fxml/" + "youngManRoom" + ".fxml"));
                    FXMLLoader guess =
                        new FXMLLoader(App.class.getResource("/fxml/" + "guessingRoom" + ".fxml"));

                    // Load scenes and set controllers on SceneManager.
                    SceneManager.addUi(AppUi.WOMANROOM, woman.load());
                    SceneManager.setWomanController(woman.getController());

                    SceneManager.addUi(AppUi.GUESSROOM, guess.load());
                    SceneManager.setGuessController(guess.getController());

                    SceneManager.addUi(AppUi.OLDMANROOM, oldMan.load());
                    SceneManager.setOldManController(oldMan.getController());

                    SceneManager.addUi(AppUi.YOUNGMANROOM, youngMan.load());
                    SceneManager.setYoungManController(youngMan.getController());

                    SceneManager.addUi(AppUi.MAINROOM, main.load());
                    SceneManager.setMainController(main.getController());

                  } catch (Exception e) {
                    System.out.println("Error loading FXML files: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                  }
                  return null;
                }
              };

          loadTask.setOnSucceeded(
              loadEvent -> {
                // Set the new scene root after loading is complete.
                Parent newRoot = SceneManager.getUiRoot(AppUi.MAINROOM);
                scene.setRoot(newRoot);

                // Re-add the black overlay to the new root.
                ((Pane) newRoot).getChildren().add(blackOverlay);

                // Create a fade-out transition for the black overlay.
                FadeTransition fadeFromBlack =
                    new FadeTransition(Duration.millis(500), blackOverlay);
                fadeFromBlack.setFromValue(1.0);
                fadeFromBlack.setToValue(0.0);

                // Remove the black overlay after fading out.
                fadeFromBlack.setOnFinished(
                    event1 -> ((Pane) newRoot).getChildren().remove(blackOverlay));

                fadeFromBlack.play();
              });

          loadTask.setOnFailed(
              loadEvent -> {
                System.out.println("Error loading FXML in background task.");
                System.exit(0);
              });

          // Run the Task in a new Thread.
          Thread loadThread = new Thread(loadTask);
          loadThread.setDaemon(true);
          loadThread.start();
        });

    // Start the fade-to-black transition immediately.
    fadeToBlack.play();
  }

  /**
   * This method handles the hover effects turning on.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOn(MouseEvent event) {
    topLine.setVisible(true);
    bottomLine.setVisible(true);
    leftLine.setVisible(true);
    rightLine.setVisible(true);
  }

  /**
   * This method handles the hover effects turning off.
   *
   * @param event the mouse event that is triggered by hovering over
   */
  @FXML
  private void hoverOff(MouseEvent event) {
    topLine.setVisible(false);
    bottomLine.setVisible(false);
    leftLine.setVisible(false);
    rightLine.setVisible(false);
  }
}
