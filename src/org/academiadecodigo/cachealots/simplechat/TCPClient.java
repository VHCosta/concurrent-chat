package org.academiadecodigo.cachealots.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient implements Runnable {

    private String hostName;
    private int portNumber;
    private TCPServer server;
    private Socket clientSocket;
    private Scanner reader;
    private PrintWriter out;
    private BufferedReader in;

    public TCPClient(Socket clientSocket, TCPServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {

        try {

            getServerAddress();

            initClient();

            getInputToSend();

        } catch (IOException e) { System.out.println(e.getMessage()); }


    }

    void getServerAddress(){

        reader = new Scanner(System.in);

        System.out.println("Host name: ");
        hostName = reader.nextLine();

        System.out.println("Port number: ");
        portNumber = reader.nextInt();

    }


    void initClient() throws IOException {

        System.out.println("Connecting to " + hostName + ":" + portNumber+ "…");
        clientSocket = new Socket(hostName, portNumber);

        System.out.println("Setting up I/O…");
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Connected!\n");

    }

    void getInputToSend() throws IOException {

        reader = new Scanner(System.in);

        System.out.println("Message: ");
        String message = reader.nextLine();

        server.broadcast(message);

    }

    void receiveMessage(String message){
        System.out.println(message);
    }




}
