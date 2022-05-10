// CS4345, Srping/2022, Extra Credit- Assignment 5, Thuong Pham
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Page {

    private final ServerSocket ss;

    public Server_Page(ServerSocket ss) {
        this.ss = ss;
    }
    public void start() {
    	// new user sign in 
        try {
            while (!ss.isClosed()) {
                Socket socket = ss.accept(); 
             // print out the output to indicate the new user connected to the server
                System.out.println("A new user just joined in"); 
                Controller_Page clients= new Controller_Page(socket); // call the client controller class to run the thread 
                Thread thread = new Thread(clients);
                thread.start();
            }
        } catch (IOException e) {
            close();
        }
    }

    // Close the server when the user sign out
    public void close() {
        try {
            if (ss != null) {
                ss.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Run the program.
    public static void main(String[] args) {
        ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(4978);
			Server_Page server = new Server_Page(serverSocket);
	        String str = "Server starts successfully!";
	        System.out.println(str);
	        server.start();
		} catch (IOException e) {
        	System.out.println("Server Fail!");
			e.printStackTrace();
		}
       
    }

}