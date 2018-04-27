package network;

import model.FallObject;
import model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectOutputStream outStream = null;
    private ObjectInputStream inStream = null;
    private Player player;
    private List<FallObject> fallobjectlist;
    private Container container;
    private testobject to;

    public Server() {
        to = new testobject(2, "xyz");
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

    public void SendTest() {

        System.out.println("Player and FallObjectList to be written = " + to);

        try {
            outStream.writeObject(to);
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
                if(inStream.available() > 0) {
                    testobject to = (testobject) inStream.readObject();


                    System.out.println("Object rec:" + to);
                    System.out.println("Object" + to.getValue());
                }
                TimeUnit.MILLISECONDS.sleep(100);

            }

        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch( Exception e){

        }

    }


    public Container getContainer() {
        return container;
    }


    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();



        server.StartServer();
        TimeUnit.SECONDS.sleep(3);

        Thread ct = new Thread(server);
        ct.start();

        while(true){
            TimeUnit.SECONDS.sleep(3);
//            server.SendTest();

        }

    }
}