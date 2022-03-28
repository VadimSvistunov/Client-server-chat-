package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientWindow {
//172.20.10.3

    private static final String SERVER_HOST = "localhost";

    private static final int SERVER_PORT = 3443;

    protected Socket clientSocket;

    protected BufferedReader inMessage;

    protected PrintWriter outMessage;

    protected String clientName = "";

    public static Window window;

    public static LoginController delegateLogin;
    public static SignupController delegateSignup;
    public static ChatController delegateChat;

    public ClientWindow() {
        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            try {
                String buffer;
                while (true) {

                    if ((buffer = inMessage.readLine()) != null) {

                        switch (buffer) {
                            case "##USERNAME_USE##":
                                Platform.runLater(() -> delegateSignup.clearFields("Username is already in use"));
                                break;
                            case "##SIGNUP_CORRECT##":
                                clientName = inMessage.readLine();
                                System.out.println(clientName);
                                Platform.runLater(this::openChatWindow);
                            case "##LOGIN_CORRECT##":
                                clientName = inMessage.readLine();
                                Platform.runLater(this::openChatWindow);
                                break;
                            case "##USERNAME_INCORRECT##":
                                Platform.runLater(() -> delegateLogin.clearFields("Incorrect username or password"));
                                break;
                            default:
                                String msg = buffer;
                                Platform.runLater(() -> delegateChat.getMessage(msg));
                                break;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }).start();
    }

    public void sendMessage(String[] arr) {
        String temp = "";
        for (String data : arr) {
            temp += data + " ";
        }
        outMessage.println(temp.trim());
        outMessage.flush();
    }

    public void sendMessage(String str) {
        outMessage.println("# " + clientName + " : " + str);
        outMessage.flush();
    }

    public void openChatWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("chat.fxml"));
        try {
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ClientWindow.window.hide();
        }
    }

}
