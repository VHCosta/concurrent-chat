package org.academiadecodigo.cachealots.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Dispatcher implements Runnable {

    private String username;
    private final TCPServer server;
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public Dispatcher(Socket clientSocket, TCPServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {

        try {

            setupClient();

            getInputToSend();

        } catch (IOException e) { System.out.println(e.getMessage()); }


    }

    void setupClient() throws IOException {

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());

        out.print("Username: ");
        out.flush();
        username = in.readLine();

        if(username == null) username = "user";

        server.broadcast(username + " has joined the chat!\n");

    }


    void getInputToSend() throws IOException {

        while(clientSocket.isBound()){

            out.print("\n: "); out.flush();

            String message = in.readLine();

            server.broadcast(username + ": " + message + "\n");
        }


    }

    void receiveMessage(String message) throws IOException {
        out.print(message);
        out.flush();
    }





}
