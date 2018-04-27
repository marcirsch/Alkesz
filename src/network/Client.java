package network;


import model.FallObject;
import model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Client implements Runnable {
    private Container container;
    private testobject to;
    private String ip;
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;


    public Client() {
        to = new testobject(3,"abc");
    }


    public void ConnectToServer(String ipaddress) {

        while (!isConnected) {
            try {
                System.out.println("Try to connect to:" + ip);
                socket = new Socket(ipaddress, 7777);
                System.out.println("Connected!");
                isConnected = true;
                inputStream = new ObjectInputStream(socket.getInputStream());
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

    public void SendData(Player player, List<FallObject> list) {

        container.setX(player.getX());
        container.setMissed(player.getMissed());
        container.setAlcoholLevel(player.getAlcoholLevel());
        container.setFallObjectList(list);

        System.out.println("Player and FallObjectList to be written = " + container);

        try {
            outputStream.writeObject(container);
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

                testobject to = (testobject) inputStream.readObject();
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

    public static void main(String[] args) throws InterruptedException {
        Client client;

        client = new Client();

        client.ConnectToServer("localhost");
        Thread ct = new Thread(client);

        while(true){
            TimeUnit.SECONDS.sleep(3);
            client.SendTest();

        }

    }
}
