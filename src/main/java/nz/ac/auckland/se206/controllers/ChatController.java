package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the chat view. Handles user interactions and communication with the GPT
 * model via the API proxy.
 */
public class ChatController {

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;

  private ChatCompletionRequest chatCompletionRequest;
  private String profession;
  private String guess = "None so don't say anything yet.";

  private boolean loading = false;

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
  }

  // Setter for txtaChat
  public void setTxtaChat(TextArea txtaChat) {
    this.txtaChat = txtaChat;
  }

  // Setter for txtFieldInput
  public void setTxtInput(TextField txtInput) {
    this.txtInput = txtInput;
  }

  // Setter for btnSend
  public void setBtnSend(Button btnSend) {
    this.btnSend = btnSend;
  }

  // Setter for guess
  public void setGuess(String guess) {
    this.guess = guess;
  }

  /**
   * Generates the system prompt based on the profession.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt(String person) {
    Map<String, String> map = new HashMap<>();
    map.put("person", guess);
    return PromptEngineering.getPrompt(person, map);
  }

  /**
   * Sets the profession for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
   */
  public void setProfession(String profession) {
    this.profession = profession;
    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.5)
              .setMaxTokens(100);
      runGpt(new ChatMessage("system", getSystemPrompt(profession + ".txt")));
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    String role = msg.getRole();
    if (role.equals("assistant")) {
      switch (profession) {
        case "oldMan":
          role = "Old man";
          break;
        case "youngMan":
          role = "Young man";
          break;
        case "woman":
          role = "Woman";
          break;
        case "guess":
          role = "Feedback";
          break;
        default:
          // No fall-through inteded.
          break;
      }
    } else if (role.equals("user")) {
      role = "Dr. Watson (You)";
    }
    txtaChat.appendText(role + ": " + msg.getContent() + "\n\n");
  }

  public boolean isLoading() {
    return loading;
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private void runGpt(ChatMessage msg) throws ApiProxyException {
    Task<ChatMessage> task =
        new Task<>() {
          @Override
          protected ChatMessage call() throws ApiProxyException {
            // Disable other requests while doing task
            loading = true;
            btnSend.setDisable(true);
            chatCompletionRequest.addMessage(msg);
            ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
            Choice result = chatCompletionResult.getChoices().iterator().next();
            chatCompletionRequest.addMessage(result.getChatMessage());
            return result.getChatMessage();
          }

          @Override
          protected void succeeded() {
            // Append successful message
            loading = false;
            btnSend.setDisable(false);
            ChatMessage response = getValue();
            appendChatMessage(response);
          }

          @Override
          protected void failed() {
            Throwable exception = getException();
            exception.printStackTrace();
            // Handle failure (e.g., show an error message to the user)
          }
        };

    new Thread(task).start();
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      // Do nothing if theres nothing to send.
      return;
    }
    // Clear any previous messages.
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    runGpt(msg);
  }

  /**
   * This method is a public version of the FXML function so other classes can use it.
   *
   * @param event the action event triggered by the send button
   */
  public void sendMessage(ActionEvent event) {
    try {
      onSendMessage(event);
    } catch (Exception e) {
      System.out.println("Failed sending message");
    }
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    App.setRoot("room");
  }
}
