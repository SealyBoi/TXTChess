package Network;

import java.io.*;
import java.net.*;

public class Server extends Network {
    static ServerSocket ss;
    static Socket s;
    static InputStream inputStream;
    static ObjectInputStream objectInputStream;
    static OutputStream outputStream;
    static ObjectOutputStream objectOutputStream;

    public void run() throws Exception {

        // Create Server Socket
        ss = new ServerSocket(9999);

        // Connect to client socket
        System.out.println("[*]Waiting for player 2 to connect...");
        s = ss.accept();
        System.out.println("[!]Player connected");

        // Get the input stream from the connected socket
        inputStream = s.getInputStream();

        // Create a data stream to receive objects
        objectInputStream = new ObjectInputStream(inputStream);

        // Get the output stream from the connected socket
        outputStream = s.getOutputStream();

        // Create a data stream to send objects
        objectOutputStream = new ObjectOutputStream(outputStream);

    }

    public String[] readInput() throws Exception {
        try {
            String[] move = null;
            while (move == null) {
                move = (String[]) objectInputStream.readObject();
            }
            return move;
        } catch (Exception e) {
            throw new Exception("[!]Could not read from Client");
        }
    }

    public void sendOutput(String[] move) throws Exception {
        try {
            objectOutputStream.writeObject(move);
            objectOutputStream.flush();
        } catch (Exception e) {
            throw new Exception("[!]Could not write to Client");
        }
    }

    public void closeNetwork() throws IOException {
        s.close();
        outputStream.close();
        objectOutputStream.close();
        inputStream.close();
        objectInputStream.close();
    }
    
}
