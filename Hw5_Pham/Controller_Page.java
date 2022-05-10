
// CS4345, Srping/2022, Extra Credit- Assignment 5, Thuong Pham
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
public class Controller_Page implements Runnable {
    public static ArrayList<Controller_Page> controllers = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String userName;
    // Class constructor
    public Controller_Page(Socket socket) {
            this.socket = socket; 
            joinIn(); 
    }
    @Override
    public void run() {
        String userMessage="";
        while (socket.isConnected()) {
            try {
                // Read what the client sent
            	userMessage = reader.readLine();
            	// sent to the conversation for every clients
                conversationMessage(userMessage);
            } catch (IOException e) {
                // Close everything gracefully.
                close(socket, reader, writer);
                break;
            }
        }
    }
    // helper methods
    public void joinIn() {
    	try {
	    	 this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // retrieve the input that is a user name
	         this.writer= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	         this.userName = reader.readLine(); // get the user name
	    	// Add the new client handler to the array so they can receive messages from others.
	        controllers.add(this);
	        conversationMessage("----- "+ userName + " has joined the room");
    	}
        catch (IOException e) {
            // Close everything 
            close(socket, reader, writer);
        }
    	
    }
    public void conversationMessage(String messageToSend) {
        for (Controller_Page clientCtr : controllers) {
            try {
                // check if the user in the list users is sending the message then add to the conversation
                if (!clientCtr.userName.equals(userName)) {
                    clientCtr.writer.write(messageToSend);
                    clientCtr.writer.newLine();
                    clientCtr.writer.flush();
                }
            } catch (IOException e) {
                close(socket, reader, writer);
            }
        }
    }
    public void removeUser() {
    	// remove when disconnected
        controllers.remove(this);
        conversationMessage("----- "+ userName + " has left the room");
    }

    // Helper method to close everything
    public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeUser();
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
}