import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CertificateConnection extends Thread {

    private final int port;
    private ServerSocket serverSocket;

    public CertificateConnection(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Certificate server is up and running on port " + port);
        } catch (IOException e) {
            System.out.println("IO Creating Server Socket Error!");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            listenAndAccept();
        }
    }

    private void listenAndAccept() {
        Socket s;
        try {
            s = serverSocket.accept();
            System.out.println("A certificate connection was established with a client on the address of " + s.getRemoteSocketAddress());
            CertificateConnectionThread certificateSenderThread = new CertificateConnectionThread(s);
            certificateSenderThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on accepting socket during certificate connection!");
        }
    }
}