package org.academiadecodigo.cachealots.simplechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

    private static String hostName;
    private static int portNumber;
    private static Socket clientSocket;
    private static Scanner reader;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) {

        try {

            getUserInput();

            initClient();

            getInputToSend();

        } catch (IOException e) { System.out.println(e.getMessage()); }


    }

    static void getUserInput(){

        reader = new Scanner(System.in);

        System.out.println("Host name: ");
        hostName = reader.nextLine();

        System.out.println("Port number: ");
        portNumber = reader.nextInt();

    }


    static void initClient() throws IOException {

        System.out.println("Connecting to " + hostName + ":" + portNumber+ "…");
        clientSocket = new Socket(hostName, portNumber);

        System.out.println("Setting up I/O…");
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Connected!\n");

    }

    static void getInputToSend() throws IOException {

        reader = new Scanner(System.in);

        while(clientSocket.isBound()){

            System.out.println("Message: ");
            String message = reader.nextLine();

            if(message.equals("/close")) {
               break;
            }

            out.println(message);

        }

        System.out.println("Disconnecting...");
        clientSocket.close();
        System.out.println("Disconnected from server.");
    }


}
