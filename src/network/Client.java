package network;

import model.FallObject;
import model.Player;

import java.io.IOException;
//import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


public class Client {
    private String ip;
    private Socket socket = null;
    //private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private testobject to;

    public Client(String ipaddress) {
        this.ip = ipaddress;

    }


    public void ConnectToServer() {

        while (!isConnected) {
            try {
                System.out.println("Try to connect to:" + ip);
                socket = new Socket(ip, 7777);
                System.out.println("Connected!");
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());

            } catch (SocketException se) {
                se.printStackTrace();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Disconnect() {

        try {
            System.out.println("Try to close socket, HOST:" + socket.getInetAddress());
            outputStream.close();
            socket.close();
            System.out.println("Success!");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void SendPlayerData(Player player) {

        System.out.println("Player to be written = " + player);
        try {
            outputStream.writeObject(player);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void SendFallObjectList(List<FallObject> list) {
        System.out.println("List to be written = " + list);

        try {
            outputStream.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendAll(Player player, List<FallObject> list) {

        System.out.println("Player and list sending = " + player + list);
        try {
            outputStream.writeObject(player);
            outputStream.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
