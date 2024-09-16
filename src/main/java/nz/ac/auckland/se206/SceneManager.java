package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

/**
 * Manages the scenes of the application so when we want to switch from the main room to the chat and back to the main room
 * it makes sure we switch back to the same instance of the main room so it will remember stuff like timer and clues
 */
public class SceneManager {
  
  public enum AppUi {
    MENU, MAINROOM, OLDMANROOM, WOMANROOM, YOUNGMANROOM
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<>();

  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
