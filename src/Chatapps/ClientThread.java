package Chatapps;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
//Create the Client Threads
//In ClientThread, make ClientThread extend ChatServer, and implement Runnable
public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private ChatServer server;
    //With the socket received from serversocket.accept, create a new ClientThread .
    public ClientThread(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter(){
        return clientOut;
    }
   
   
   
   

    @Override
    //Create the ClientThread constructor and add the method run.
    public void run() {
        try{
            // setup
        	//In ClientThread make a private BufferedReader in to receive data from clients, and a PrintWriter to write to the client.
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            // start communicating
            //Create a while loop in run that checks for any new input and prints the input to all clients using the list of ClientThreads and the getter for the PrintWriter
            while(!socket.isClosed()){
                if(in.hasNextLine()){
                    String input = in.nextLine();
                    // NOTE: if you want to check server can read input, uncomment next line and check server file console.
                    // System.out.println(input);
                    for(ClientThread thatClient : server.getClients()){
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if(thatClientOut != null){
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}