import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerProcess extends Thread{

    @Override
    public void run() {
        try {
            readFromSocket();
        } catch (IOException e) {
            System.out.println("Did not receive message from client");
        }
    }

    public void readFromSocket() throws IOException {
        ServerSocket serveSock = new ServerSocket(7777);
        Socket sock = serveSock.accept();
        InputStream input = sock.getInputStream();
        DataInputStream dataInput = new DataInputStream(input);

        String fromClient = dataInput.readUTF();
        System.out.println(fromClient);

        serveSock.close();
        sock.close();
    }
}
