package org.academiadecodigo.cachealots.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {

    private final Scanner reader = new Scanner(System.in);
    private ServerSocket serverSocket;
    private BufferedReader in;

    private List<TCPClient> clientList;

    public static void main(String[] args) {


        new TCPServer();


    }

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

        while(true){

            ExecutorService threadPool = Executors.newFixedThreadPool(20);

            Socket clientSocket = serverSocket.accept();

            TCPClient client = new TCPClient(clientSocket, this);

            clientList.add(client);
            threadPool.submit(client);


        }


    }

    public void broadcast(String message){

        for (TCPClient client : clientList){
            client.receiveMessage(message);
        }

    }

}





