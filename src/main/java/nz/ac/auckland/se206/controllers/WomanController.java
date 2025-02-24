package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

/**
 * Controller class for the woman room. Handles all the timer stuff and user interaction for the
 * woman room.
 */
public class WomanController extends BaseCharacterController {
  public void initialize() {
    super.initialize("woman");
  }

  @FXML
  @Override
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isWomanClicked = true;
    super.onSendMessage(event);
  }
}
