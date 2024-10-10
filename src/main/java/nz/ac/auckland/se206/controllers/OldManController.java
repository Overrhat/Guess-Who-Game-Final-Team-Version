package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

public class OldManController extends BaseCharacterController {
  public void initialize() {
    super.initialize("oldMan");
  }

  @Override
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isOldManClicked = true;
    super.onSendMessage(event);
  }
}
