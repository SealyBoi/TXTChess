package Network;

import java.io.*;
import java.net.*;

public class Client extends Network{
    static Socket s;
    static InputStream inputStream;
    static ObjectInputStream objectInputStream;
    static OutputStream outputStream;
    static ObjectOutputStream objectOutputStream;

    public void run() throws Exception {
        // Create client socket
        s = new Socket("localhost", 9999);

        // get the output stream from socket
        outputStream = s.getOutputStream();

        // to send data to the server
        objectOutputStream = new ObjectOutputStream(outputStream);

        // get input stream from socket
        inputStream = s.getInputStream();

        // to receive data from server
        objectInputStream = new ObjectInputStream(inputStream);

        String[] tmp = {"a", "b", "c", "d"};
        objectOutputStream.writeObject(tmp);
        objectOutputStream.flush();
    }

    public String[] readInput() throws Exception {
        try {
            String[] move = null;
            while (move == null) {
                move = (String[]) objectInputStream.readObject();
            }
            return move;
        } catch (Exception e) {
            throw new Exception("[!]Could not read from Server");
        }
    }

    public void sendOutput(String[] move) throws Exception {
        try {
            objectOutputStream.writeObject(move);
            objectOutputStream.flush();
        } catch (Exception e) {
            throw new Exception("[!]Could not write to Server");
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
