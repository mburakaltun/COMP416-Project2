import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ConnectToCertificateConnection {
    private static final int BUFFER_SIZE = 4096;
    private Socket socket;
    private DataInputStream dataInputStream;
    private BufferedReader is;
    private PrintWriter os;

    private BufferedReader keyboard;
    private static boolean isAuthSuccessful = false;


    public ConnectToCertificateConnection(String address, int port) {
        try {
            socket = new Socket(address, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            is = new BufferedReader(new InputStreamReader(dataInputStream));
            os = new PrintWriter(socket.getOutputStream(), true);
            keyboard = new BufferedReader(new InputStreamReader(System.in));
        } catch (EOFException e) {
            disconnect();
        } catch (IOException e) {
            System.err.println("Error: no server has been found on " + address + "/" + port);
        }
    }

    /**
     * Disconnects the socket and closes the buffers
     */
    public void disconnect() {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (os != null) {
                socket.getOutputStream().close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Certificate Connection is closed.");
        } catch (IOException e) {
            System.out.println("Certificate Connection closed by server!");
        } finally {
            dataInputStream = null;
            os = null;
            socket = null;
        }
    }

    public void sendUsername() {
        String question;
        String username;
        try {
            question = is.readLine();
            System.out.print(question);
            username = keyboard.readLine();
            os.println(username);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Username error!");
        }
    }

    public void sendPassword() {
        String question;
        String password;
        try {
            question = is.readLine();
            System.out.print(question);
            password = keyboard.readLine();
            os.println(password);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Password error!");
        }
    }

    public void responseUsernamePassword() {
        try {
            String response = is.readLine();
            if(response.compareTo("You have been successfully login. Sending the certificate...") == 0) {
                System.out.println("You have been successfully login. Receiving the certificate...");
                isAuthSuccessful = true;
            } else {
                System.out.println("Invalid username and password. You have been disconnected.");
                isAuthSuccessful = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Username-Password response error!");
        }
    }

    public boolean isAuthSuccessful() {
        return isAuthSuccessful;
    }

    public void receiveCertificateFile() {
        int count;
        byte[] buffer = new byte[BUFFER_SIZE];
        try (FileOutputStream fileOutputStream = new FileOutputStream("server_crt.crt")) {
            while ((count = dataInputStream.read(buffer)) > 0) {
                if (buffer[count - 1] == 26) {
                    // Break if EOF is received
                    fileOutputStream.write(buffer, 0, count - 1);
                    break;
                } else {
                    fileOutputStream.write(buffer, 0, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}