package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController{

    @FXML
    public Button loginBut;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button signupBut;

    @FXML
    void login(ActionEvent event) {
        ClientWindow.window = loginBut.getScene().getWindow();
        Main.client.sendMessage(new String[]{usernameField.getText(), passwordField.getText()});
    }

    @FXML
    public void initialize() {
        ClientWindow.delegateLogin = this;
    }

    @FXML
    void openSignupScene(ActionEvent event) {
        signupBut.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("signup.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void clearFields(String errorMsg) {
        passwordField.setText("");
        errorLabel.setText(errorMsg);
    }

}
