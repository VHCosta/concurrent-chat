package org.academiadecodigo.cachealots.concurrentchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {

    //State
    private ServerSocket serverSocket;
    private static int PORT = 8080;
    private final List<Dispatcher> clientList;

    //Behavior
    public TCPServer() {

        clientList = Collections.synchronizedList(new ArrayList<>());

        try {

            serverSocket = new ServerSocket(PORT);
            initServer();
            listenForConnections();

        } catch (IOException e) { System.out.println(e.getMessage()); }
    }

    public void initServer() throws IOException {

        System.out.println("Starting Server on port " + PORT + "…");
    }

    public void listenForConnections() throws IOException {


        ExecutorService threadPool = Executors.newFixedThreadPool(20);

        Socket clientSocket;

        while(true){

            //listen for connections
            clientSocket = serverSocket.accept(); //blocks

            //create new Dispatcher (Runnable) object
            Dispatcher client = new Dispatcher(clientSocket, this);

            //add to LinkedList
            clientList.add(client);

            //submit task to thread pool
            threadPool.submit(client);

        }
    }

    public void broadcast(String message) {

        //for every connected client
        for (Dispatcher client : clientList){

            //everyone receives the message
            client.receiveMessage(message);

        }
    }

    public void whisper(String name, String message){

        synchronized (clientList) {

            for(Dispatcher client : clientList) {

                if(client.getUsername().equals(name)) {
                    client.receiveMessage(message);
                }
                break;
            }
        }
    }


    public void eject(Dispatcher dispatcher) {

        //ejects user and saves boolean return
        boolean success = clientList.remove(dispatcher);

        //temp check for errors: pipes back any error
        if(!success) dispatcher.receiveMessage("error ejecting form server");
    }

    public String getUsers(){

        StringBuilder userList = new StringBuilder("[Connected users:] " + "\n");

        synchronized (clientList) {

            for (Dispatcher client : clientList) {
                userList.append(client.getDetails()).append("\n");
            }
        }

        return userList.toString();
    }


    //main
    public static void main(String[] args) {

        if(args.length > 0) {
            int arg = Integer.parseInt(args[0]);
            if (arg >= 0 && arg <= 9999) PORT = arg;
        }

        new TCPServer();
    }
}





