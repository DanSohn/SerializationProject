package sender;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception{
        String serverAddress = "192.168.1.69";
        int serverPort = 4444;
        try{
            Socket socket = new Socket(serverAddress, serverPort);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             Object object = Serializer.serializetoSocket();
             outputStream.writeObject(object);
             outputStream.flush();
             socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
