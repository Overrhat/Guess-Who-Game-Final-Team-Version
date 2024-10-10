package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;


public class WomanController extends BaseCharacterController {
  public void initialize() {
    super.initialize("woman");
  }

  @Override
  protected void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    MainRoomController.isWomanClicked = true;
    super.onSendMessage(event);
  }
}
