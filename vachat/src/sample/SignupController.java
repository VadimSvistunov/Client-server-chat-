package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signupBut;

    @FXML
    private PasswordField repeatPassField;

    @FXML
    public void initialize() {
        ClientWindow.delegateSignup = this;
    }

    @FXML
    void signup(ActionEvent event) {
        if(passwordField.getText().equals(repeatPassField.getText())) {
            ClientWindow.window = signupBut.getScene().getWindow();
            Main.client.sendMessage(new String[]{usernameField.getText(), passwordField.getText(), firstnameField.getText(),
                lastnameField.getText()});
        } else {
            errorLabel.setText("Passwords are not the same");
        }
    }

    public void clearFields(String errorMsg) {
        passwordField.setText("");
        repeatPassField.setText("");
        errorLabel.setText(errorMsg);
    }


}
