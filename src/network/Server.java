package network;

import controller.GameEngine;
import model.Arena;
import model.FallObject;
import model.Player;
import model.Settings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectOutputStream outStream = null;
    private ObjectInputStream inStream = null;
    private volatile Arena arena_rx;
    private volatile boolean rx_ON = false;
    private GameEngine controller;

    public Server(GameEngine controller) {
        this.controller = controller;
    }

    public void StartServer() {

        System.out.println("Start server");

        try {
            serverSocket = new ServerSocket(7777);
            socket = serverSocket.accept();
            System.out.println("Connected");


            inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.flush();
            this.controller.startGame(Settings.GAME_MODE.MULTIPLAYER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void SendDatatoClient(Arena arena) {

        System.out.println("Arena to be written = " + arena);

        try {
            outStream.reset();
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

//    public static void main(String[] args) throws InterruptedException {
//
//        Server server = new Server();
//        server.StartServer();
//        System.out.println("OK");
//        server.start_receive();
//        new Thread(server::receive_loop).start();
//
//        Arena arena = new Arena();
//        arena.getPlayer().setX(3);
//        arena.getPlayer().setAlcoholLevel(7);
//
//        while (true) {
//            TimeUnit.SECONDS.sleep(1);
//            server.SendDatatoClient(arena);
//        }
//
//
//    }

}