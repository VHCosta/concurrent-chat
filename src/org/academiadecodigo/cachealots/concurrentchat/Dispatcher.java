package org.academiadecodigo.cachealots.concurrentchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

    private void setupClient() throws IOException {

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());

        out.print("Username: ");
        out.flush();
        username = in.readLine();

        if(username == null) username = "user";

        server.broadcast(username + " has joined the chat!\n");

    }


    private void getInputToSend() throws IOException {

        while(clientSocket.isBound()){

            String message = in.readLine();

            if(message == null) continue;

            //if message is command i.e. starts with "/"
            if(message.charAt(0) == ("/".charAt(0))){
                String[] command = message.split(" ");

                // if changing username
                if (command[0].equals("/name")) {

                    //incorrect format
                    if(command.length != 2){
                        out.print("Usage: /name <new_username>");
                        out.flush();

                    } else {

                        //change and broadcast new username
                        server.broadcast(username + " changed username to " + command[1] + "\n");
                        username = command[1];
                    }
                }

                // if quitting from server
                else if(command[0].equals("/quit")){

                    //announce to other users
                    server.broadcast(username + " has left the server!" + "\n");

                    //remove from server list
                    server.eject(this);

                    //close streams
                    out.close();
                    in.close();

                    //close socket
                    clientSocket.close();

                    //break out of contained loop (do i need this?)
                    break;
                }

                // if requesting for other users
                else if(command[0].equals("/list")) {

                    out.print(server.getUsers());
                    out.flush();
                }




                // if other command
                else {

                    out.print(command[0] + ": not a recognized command " + "\n");
                    out.flush();
                }


            //normal message i.e. not a command
            } else {

                server.broadcast(username + ": " + message + "\n");
            }
        }
    }

    public String getDetails() {
        return username + " on connection " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
    }

    public void receiveMessage(String message) {
        out.print(message);
        out.flush();
    }

}
