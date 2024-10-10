package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

/**
 * Controller class for the old man room. Handles all the timer stuff and user interaction for the
 * old man room.
 */
public class OldManController extends BaseCharacterController {
  public void initialize() {
    super.initialize("oldMan");
  }

  @FXML
  @Override
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isOldManClicked = true;
    super.onSendMessage(event);
  }
}
