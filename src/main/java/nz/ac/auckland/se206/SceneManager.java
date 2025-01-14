package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.GuessingRoomController;
import nz.ac.auckland.se206.controllers.MainRoomController;
import nz.ac.auckland.se206.controllers.OldManController;
import nz.ac.auckland.se206.controllers.WomanController;
import nz.ac.auckland.se206.controllers.YoungManController;

/**
 * Manages the scenes of the application so when we want to switch from the main room to the chat
 * and back to the main room it makes sure we switch back to the same instance of the main room so
 * it will remember stuff like timer and clues.
 */
public class SceneManager {

  /** All the possible scenes for the game. */
  public enum AppUi {
    MENU,
    MAINROOM,
    OLDMANROOM,
    WOMANROOM,
    YOUNGMANROOM,
    GUESSROOM
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<>();
  private static OldManController oldManController;
  private static YoungManController youngManController;
  private static WomanController womanController;
  private static GuessingRoomController guesssController;
  private static MainRoomController mainController;
  private static AppUi currentRoom;

  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  public static Parent getUiRoot(AppUi appUi) {
    currentRoom = appUi;
    return sceneMap.get(appUi);
  }

  public static OldManController getOldManController() {
    return oldManController;
  }

  public static void setOldManController(OldManController oldManController) {
    SceneManager.oldManController = oldManController;
  }

  public static YoungManController getYoungManController() {
    return youngManController;
  }

  public static void setYoungManController(YoungManController youngManController) {
    SceneManager.youngManController = youngManController;
  }

  public static WomanController getWomanController() {
    return womanController;
  }

  public static void setWomanController(WomanController womanController) {
    SceneManager.womanController = womanController;
  }

  public static void setGuessController(GuessingRoomController guessController) {
    SceneManager.guesssController = guessController;
  }

  public static GuessingRoomController getGuessController() {
    return guesssController;
  }

  public static AppUi getCurrentRoom() {
    return currentRoom;
  }

  public static MainRoomController getMainController() {
    return mainController;
  }

  public static void setMainController(MainRoomController mainController) {
    SceneManager.mainController = mainController;
  }
}
