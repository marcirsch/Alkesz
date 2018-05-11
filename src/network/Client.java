package network;


import model.Arena;
import model.FallObject;
import model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;


public class Client {
    private String ip;
    private Socket socket = null;
    private ObjectInputStream inStream = null;
    private ObjectOutputStream outStream = null;
    private boolean isConnected = false;
    private Arena arena_tx;
    private Arena arena_rx;
    Thread rx_thread;
    private volatile boolean rx_ON = false;


    public Client() {

    }


    public void ConnectToServer(String ipaddress) {

        while (!isConnected) try {
            System.out.println("Try to connect to:" + ip);
            socket = new Socket(ipaddress, 7777);
            System.out.println("Connected!");
            isConnected = true;

            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();

            inStream = new ObjectInputStream(socket.getInputStream());


        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {

        try {
            System.out.println("Try to close socket, HOST:" + socket.getInetAddress());
            outStream.close();
            socket.close();
            System.out.println("Success!");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void SendDatatoServer(Arena arena) {


        System.out.println("Arena to be written = " + arena);

        try {
            outStream.writeObject(arena);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void receive() {

        try {
            arena_rx = (Arena) inStream.readObject();
            System.out.println("Object received");
            System.out.println(arena_rx);
            System.out.println(arena_rx.getPlayer().getAlcoholLevel());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void receive_loop() {

        while (rx_ON == true) {
            receive();
        }

    }

    public void start_receive(){

        rx_ON = true;
    }
    public void stop_receive() {

        rx_ON = false;

    }

    public static void main(String[] args) throws InterruptedException {

        Client client = new Client();
        client.ConnectToServer("127.0.0.1");


        client.start_receive();
        new Thread(client::receive_loop).start();


        Arena arena = new Arena();
        arena.getPlayer().setX(3);
        arena.getPlayer().setAlcoholLevel(5);

        while (true) {
            TimeUnit.SECONDS.sleep(1);
            client.SendDatatoServer(arena);
        }



    }
}
