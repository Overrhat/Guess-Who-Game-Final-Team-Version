package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {
  public static MediaPlayer mediaPlayer;

  @FXML private Rectangle rectStart;

  private static boolean isFirstTimeInit = true;

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
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      SceneManager.addUi(
          AppUi.MAINROOM,
          new FXMLLoader(App.class.getResource("/fxml/" + "mainRoom" + ".fxml")).load());
      scene.setRoot(SceneManager.getUiRoot(AppUi.MAINROOM));
    } catch (Exception e) {
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
}
