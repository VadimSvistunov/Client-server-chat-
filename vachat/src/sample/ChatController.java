package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController{

    @FXML
    private TextArea chatField;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendBut;

    @FXML
    public void initialize() {
        ClientWindow.delegateChat = this;
    }

    @FXML
    void sendMessage(ActionEvent event) {
        ClientWindow.window = sendBut.getScene().getWindow();
        Main.client.sendMessage(messageField.getText().trim());
        messageField.clear();
    }

    void getMessage(String str) {
        String temp = chatField.getText();
        chatField.setText(temp + str + "\n");
    }
}
