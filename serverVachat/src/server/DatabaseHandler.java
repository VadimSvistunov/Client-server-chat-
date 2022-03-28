package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseHandler extends Configs {

    Connection dbConnection;

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public Clients signup (String username, String password, String firstname, String lastname) {
        ArrayList<Clients> clients = getFields();
        Clients client = new Clients(username, password, firstname, lastname);
        for (Clients tempClient : clients) {
            if(tempClient.getUsername().equals(username)) {
                return null;
            }
        }
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" + Const.USER_USERNAME +
                "," + Const.USER_PASSWORD + "," + Const.USER_FIRSTNAME + "," + Const.USER_LASTNAME + ")" + "VALUES(?,?,?,?)";

        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, client.getUsername());
            prSt.setString(2, client.getPassword());
            prSt.setString(3, client.getFirstname());
            prSt.setString(4, client.getLastname());

            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return client;
    }

    public Clients login (String username, String password) {
        Clients client = new Clients();
        ArrayList<Clients> clients = getFields();
        for (Clients tempClient : clients) {
            if(tempClient.getUsername().equals(username) && tempClient.getPassword().equals(password)) {
                return tempClient;
            }
        }
        return null;
    }

    private ArrayList<Clients> getFields() {
        String insert = "SELECT * FROM " + Const.USER_TABLE;
        ArrayList<Clients> clients = new ArrayList<>();
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            ResultSet rs = prSt.executeQuery();

            while(rs.next()) {
                Clients tempClient = new Clients();
                tempClient.setUsername(rs.getString("username"));
                tempClient.setPassword(rs.getString("password"));
                tempClient.setFirstname(rs.getString("firstname"));
                tempClient.setLastname(rs.getString("lastname"));
                clients.add(tempClient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clients;
    }


}
