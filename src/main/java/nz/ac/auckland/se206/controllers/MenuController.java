package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {

  // Static fields
  public static MediaPlayer mediaPlayer;
  private static boolean isFirstTimeInit = true;

  // Instance fields
  @FXML private Line bottomLine;
  @FXML private QuadCurve leftLine;
  @FXML private Rectangle rectStart;
  @FXML private QuadCurve rightLine;
  @FXML private Line topLine;

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
    // this switches the scene to the main room
    try {
      // Load fxmls.
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      FXMLLoader main = new FXMLLoader(App.class.getResource("/fxml/" + "mainRoom" + ".fxml"));
      FXMLLoader woman = new FXMLLoader(App.class.getResource("/fxml/" + "womanRoom" + ".fxml"));
      FXMLLoader oldMan = new FXMLLoader(App.class.getResource("/fxml/" + "oldManRoom" + ".fxml"));
      FXMLLoader youngMan =
          new FXMLLoader(App.class.getResource("/fxml/" + "youngManRoom" + ".fxml"));

      // Connect the controllers.
      SceneManager.addUi(AppUi.WOMANROOM, woman.load());
      SceneManager.setWomanController(woman.getController());

      FXMLLoader guess = new FXMLLoader(App.class.getResource("/fxml/" + "guessingRoom" + ".fxml"));
      SceneManager.addUi(AppUi.GUESSROOM, guess.load());
      SceneManager.setGuessController(guess.getController());

      SceneManager.addUi(AppUi.OLDMANROOM, oldMan.load());
      SceneManager.setOldManController(oldMan.getController());

      SceneManager.addUi(AppUi.YOUNGMANROOM, youngMan.load());
      SceneManager.setYoungManController(youngMan.getController());
      SceneManager.addUi(AppUi.MAINROOM, main.load());
      SceneManager.setMainController(main.getController());
      scene.setRoot(SceneManager.getUiRoot(AppUi.MAINROOM));
    } catch (Exception e) {
      // Print error loading room.
      System.out.println("Error loading mainRoom.fxml");
      System.exit(0);
    }
  }

  /**
   * Plays media from the specified file path. If a media file is already playing, it stops the
   * current playback before starting the new one.
   *
   * @param filePath The path to the media file to be played. This should be a valid resource path.
   * @throws IllegalArgumentException if the file path is null or the media file cannot be found.
   * @throws MediaException if an error occurs while trying to play the media.
   *     <p>Note: This method uses JavaFX's {@link Media} and {@link MediaPlayer} classes to play
   *     media files. It also handles potential exceptions such as {@link URISyntaxException} and
   *     {@link NullPointerException}.
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

  @FXML
  private void hoverOn(MouseEvent event) {
    topLine.setVisible(true);
    bottomLine.setVisible(true);
    leftLine.setVisible(true);
    rightLine.setVisible(true);
  }

  @FXML
  private void hoverOff(MouseEvent event) {
    topLine.setVisible(false);
    bottomLine.setVisible(false);
    leftLine.setVisible(false);
    rightLine.setVisible(false);
  }
}
