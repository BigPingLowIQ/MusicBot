package tk.bigpinglowiq.TCPServer;


import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread {

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(5000)) {
            while(true) {
                new Echoer(serverSocket.accept()).start();
            }
        } catch(IOException e) {
            System.out.println("Server exception " + e.getMessage());
        }
    }
}
