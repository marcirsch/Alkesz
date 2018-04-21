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
    private ObjectOutputStream outStream = null;
    private ObjectInputStream inStream = null;
    private Player player;
    private List<FallObject> fallobjectlist;
    private Container container;
    //private testobject to;

    public Server() {

    }

    public void StartServer() {

        System.out.println("Start server");

        try {
            serverSocket = new ServerSocket(7777);
            socket = serverSocket.accept();
            System.out.println("Connected");
            inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendTest(){

        System.out.println("Player and FallObjectList to be written = " + to);

        try {
            outputStream.writeObject(to);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        try {

            while (true) {

               /* container = (Container) inStream.readObject();
                System.out.println("Object received = " + container);
                System.out.println(container.getX());
                System.out.println(container.getAlcoholLevel());
                */

                testobject to = (testobject) inStream.readObject();
                System.out.println("Object rec:" + to);
                System.out.println("Object" + to.getValue());

            }

        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
        }

    }


    public Container getContainer() {
        return container;
    }
}