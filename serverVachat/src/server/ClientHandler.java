package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler implements Runnable {

    private Server server;
    private DatabaseHandler db = new DatabaseHandler();

    private PrintWriter outMessage;

    private BufferedReader inMessage;

    private static final String HOST = "localhost";
    private static final int PORT = 3443;

    private Socket clientSocket = null;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.clientSocket = socket;
            outMessage = new PrintWriter(socket.getOutputStream());
            inMessage = new BufferedReader (new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                String line;
                if((line = inMessage.readLine()) != null) {
                    System.out.println(line);
                    processingMessage(line);
                }
                Thread.sleep(100);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.removeClient(this);
        }
    }

    public void processingMessage(String line) {
        String[] data = line.split(" ");
        if(data[0].equals("#")) {
            Server.sendMessageToAllClients(line.substring(2));
        } else if(data.length == 4) {
            signup(data);
        } else {
            login(data);
        }
    }

    private void signup(String[] data) {
        Clients client = db.signup(data[0], data[1], data[2], data[3]);
        if(client == null) {
            sendMessage("##USERNAME_USE##");
        } else {
            sendMessage("##SIGNUP_CORRECT##");
            String clientName = client.getFirstname() + " " + client.getLastname();
            sendMessage(clientName);
        }
    }

    private void login(String[] data) {
        Clients client = db.login(data[0], data[1]);
        if (client == null) {
            sendMessage("##USERNAME_INCORRECT##");
        } else {
            sendMessage("##LOGIN_CORRECT##");
            String clientName = client.getFirstname() + " " + client.getLastname();
            System.out.println(clientName);
            sendMessage(clientName);
        }
    }

    public void sendMessage(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
