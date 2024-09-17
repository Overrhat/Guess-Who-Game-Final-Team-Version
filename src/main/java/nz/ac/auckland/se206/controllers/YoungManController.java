package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class YoungManController {
  @FXML private Label lblTime;
  @FXML private Rectangle rectCrimeScene;
  @FXML private Rectangle rectOldMan;
  @FXML private Rectangle rectWoman;

  public void initialize() {}

  public void setLblTime(String time) {
    lblTime.setText(time);
  }

  /**
   * This switches the scene to the crime scene.
   */
  @FXML
  private void crimeScene(MouseEvent event) {
    try {
      Rectangle rect = (Rectangle) event.getSource();
      Scene scene = rect.getScene();
      scene.setRoot(SceneManager.getUiRoot(AppUi.MAINROOM));
    } catch (Exception e) {
      System.out.println("Error loading mainRoom.fxml");
      System.exit(0);
    }
  }

  /**
   * This switches to the old man room.
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
   * This switches to the woman room.
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
