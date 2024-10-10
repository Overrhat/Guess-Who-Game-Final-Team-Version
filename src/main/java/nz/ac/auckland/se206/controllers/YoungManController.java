package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

public class YoungManController extends BaseCharacterController {
  public void initialize() {
    super.initialize("youngMan");
  }

  @Override
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isYoungManClicked = true;
    super.onSendMessage(event);
  }
}
