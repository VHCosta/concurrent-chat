package org.academiadecodigo.cachealots.concurrentchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {

    //State
    private final Scanner reader = new Scanner(System.in);
    private ServerSocket serverSocket;
    private List<Dispatcher> clientList;


    //Behavior


    public TCPServer() {

        try {

            initServer();
            listenForConnections();

        } catch (IOException e) { System.out.println(e.getMessage()); }

    }

    public void initServer() throws IOException {
        System.out.println("Server Port: ");
        int port = reader.nextInt();

        System.out.println("Starting Server on port " + port + "…");
        serverSocket = new ServerSocket(port);
    }

    public void listenForConnections() throws IOException {

        clientList = new LinkedList<>();

        ExecutorService threadPool = Executors.newFixedThreadPool(20);

        Socket clientSocket;

        while(true){

            //listen for connections
            clientSocket = serverSocket.accept();

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

    public void eject(Dispatcher dispatcher) {

        //ejects user and saves boolean return
        boolean success = clientList.remove(dispatcher);

        //temp check for errors: pipes back any error
        if(!success) dispatcher.receiveMessage("error ejecting form server");

    }

    public String getUsers(){

        StringBuilder userList = new StringBuilder();

        userList.append("Connected users: " + "\n");

        for (Dispatcher client : clientList) {
            userList.append(client.getDetails()).append("\n");
        }

        return userList.toString();
    }


    //main
    public static void main(String[] args) {
        new TCPServer();

    }



}





