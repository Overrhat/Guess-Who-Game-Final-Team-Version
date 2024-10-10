package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

/**
 * Controller class for the young man room. Handles all the timer stuff and user interaction for the
 * young man room.
 */
public class YoungManController extends BaseCharacterController {
  public void initialize() {
    super.initialize("youngMan");
  }

  @FXML
  @Override
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isYoungManClicked = true;
    super.onSendMessage(event);
  }
}
