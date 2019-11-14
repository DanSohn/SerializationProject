package receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Object Receiver(){
        Object object = null;
        int port = 4411;
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            object = inputStream.readObject();
            socket.close();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();;
        }
        return object;
    }
}
