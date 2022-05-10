// CS4345, Srping/2022, Extra Credit- Assignment 5, Thuong Pham
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Client_Page {
    // A client has a socket to connect to the server and a reader and writer to receive and send messages respectively.
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;
    // class constructor
    public Client_Page(Socket socket, String username) {
    	this.socket = socket;
    	this.username = username;
    	getInput();
    }
    public void getInput() {
    	  try {
              this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
              this.writer= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
          } catch (IOException e) {
              closeEverything(socket, reader, writer);
          }
    }
    public void msgSend() {
        try {
            // Initially send the username of the client.
            writer.write(username);
            writer.newLine();
            writer.flush();
            Scanner scnr = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scnr.nextLine(); // get the msg sent till program disconnected 
                writer.write(username + ": " + messageToSend);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    // Loading the msg add to a thread
    public void msgLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
            	msgSent();
            }
        }).start();
    }
    public void msgSent() {
    	String msg="";
        while (socket.isConnected()) {
            try {
                // Get the messages sent from other users and print it to the console.
                msg = reader.readLine();
                System.out.println(msg);
            } catch (IOException e) {
                // Close everything gracefully.
                closeEverything(socket, reader, writer);
            }
        }
    }

    // Helper method to close 
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Run the program.
    public static void main(String[] args) throws IOException {
        Scanner scnr = new Scanner(System.in);  					   System.out.print("Welcome to the group chat. ");
          System.out.println("if you want to Exit the room. press \"Ctrl+C\", or Close the page");
          System.out.println("Enter your NickName: ");
        String username = scnr.nextLine();
        // Create a socket
        Socket socket = new Socket("localhost", 4978);
        Client_Page client = new Client_Page(socket, username);
        client.msgLoad();
        client.msgSend();
    }
}