package org.academiadecodigo.cachealots.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPServer {

    private static Scanner reader = new Scanner(System.in);
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {

        try {

            initServer();
            listenForConnections();

        } catch (IOException e) { System.out.println(e.getMessage()); }

    }


    public static void initServer() throws IOException {
        System.out.println("Server Port: ");
        int port = reader.nextInt();

        System.out.println("Starting Server on port " + port + "…");
        serverSocket = new ServerSocket(port);
    }

    public static void listenForConnections() throws IOException {

        System.out.println("Waiting for Connections…");
        clientSocket = serverSocket.accept(); //blocks until receiving connection;

        System.out.println("Connected to " + clientSocket.getLocalAddress() + ":" + clientSocket.getPort());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while(clientSocket.isBound()){



            if(in.ready()){
                String message = in.readLine();
                System.out.println(clientSocket.getPort() + ": " + message);
            }

        }

        System.out.println("Disconnected from " + clientSocket.getLocalAddress() + ":" + clientSocket.getPort());
        clientSocket.close();
        System.out.println("Restart? (yes/no)");
        if(reader.nextLine().contains("yes")){
            listenForConnections();
        }

    }




}
