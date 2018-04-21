package network;

import model.FallObject;
import model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class Server implements Runnable {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inStream = null;
    private Player player;
    private List<FallObject> fallobjectlist;

    public Server() {

    }

    public Player getPlayer() {
        return player;
    }

    public List<FallObject> getFallobjectlist() {
        return fallobjectlist;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(7777);
            socket = serverSocket.accept();
            System.out.println("Connected");
            inStream = new ObjectInputStream(socket.getInputStream());

            while (true) {

                player = (Player) inStream.readObject();
                fallobjectlist = (List<FallObject>) inStream.readObject();
                //System.out.println("Object received = " + player);
                //System.out.println("Object received = " + fallobjectlist);
                System.out.println(fallobjectlist.size());
                System.out.println(player.getAlcoholLevel());

            }

        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
        }

    }
}