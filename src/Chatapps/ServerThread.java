package Chatapps;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
//Create the Server Thread
//In ServerThread, make ServerThread implement Runnable
public class ServerThread implements Runnable {
    private Socket socket;
    private String userName;
    private boolean isAlived;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;
//Create a new ServerThread using socket.
    public ServerThread(Socket socket, String userName){
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }
//Create a new Thread using the ServerThread, and then call start on the thread.
    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    @Override
    //Create the ServerThread constructor and add the method run.
    public void run(){
        System.out.println("Welcome :" + userName);

        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try{
        	/*Initialize the PrintWriter in run with the socket's output stream, the server's BufferedReader with a new InputStreamReader using the socket's input stream, and the user's BufferedReader with a new InputStreamReader using System.In*/
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);
            // BufferedReader userBr = new BufferedReader(new InputStreamReader(userInStream));
            // Scanner userIn = new Scanner(userInStream);
            
            
            
            
            //Create a while loop in run that checks for any new input from the server and prints the input to the console, and checks for any new input from the user and prints that input to the server.
            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(serverIn.hasNextLine()){
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend = "";
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.println(userName + " > " + nextSend);
                    serverOut.flush();
                }
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

    }
}