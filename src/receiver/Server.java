package receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Object Receiver(){
        Object object = null;
        int port = 4444;
        try{
            System.out.println("Waiting for connection");
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            object = inputStream.readObject();
            System.out.println("Object received");

            socket.close();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();;
        }
        return object;
    }
}
