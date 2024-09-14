package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.speech.TextToSpeech;

public class MenuController {
  @FXML private Rectangle rectStart;

  private static boolean isFirstTimeInit = true;
  private static GameStateContext context = new GameStateContext();

  /**
   * Initializes the menu. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   */
  @FXML
  public void initialize() {
    if (isFirstTimeInit) {
      System.out.println("test"); // we can make it say tts with this code when they first load the game
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
      scene.setRoot(SceneManager.getUiRoot(AppUi.MAINROOM));
    } catch (Exception e) {
      System.out.println("Error loading mainRoom.fxml");
      System.exit(0);
    }
  }
}
