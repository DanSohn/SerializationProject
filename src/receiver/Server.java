package receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        try{
            int port = 4444;
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object object = inputStream.readObject();
            socket.close();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();;
        }
    }
}
