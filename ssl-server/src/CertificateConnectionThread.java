import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Ahmet Uysal @ahmetuysal
 */
public class CertificateConnectionThread extends Thread {
    private static final int BUFFER_SIZE = 4096;
    private Socket socket;
    private BufferedReader is;
    private PrintWriter os;
    private boolean isAuthSuccessful = false;
    private ArrayList<User> userList;

    public CertificateConnectionThread(Socket socket) {
        this.socket = socket;
        CustomUserCreator customUserCreator = new CustomUserCreator();
        customUserCreator.initialize();
        userList = customUserCreator.getUserList();
    }

    public void run() {
        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String username = requestUsername();
            String password = requestPassword();
            for (User user : userList) {
                if (user.getUsername().compareTo(username) == 0 && user.getPassword().compareTo(password) == 0) {
                    isAuthSuccessful = true;
                }
            }

            if (isAuthSuccessful) {
                os.println("You have been successfully login. Sending the certificate...");
                System.out.println("New user successfully login!");
                sendCertificateFile();
            } else {
                os.println("Invalid username and password. You have been disconnected.");
                System.out.println("The user who enters \"" + username
                        + "\" for username has been disconnected due to entering invalid password.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Closing the certificate connection");
                if (is != null) {
                    is.close();
                    System.out.println("Certificate Socket Input Stream Closed");
                }

                if (os != null) {
                    os.close();
                    System.out.println("Certificate Socket Out Closed");
                }
                if (socket != null) {
                    socket.close();
                    System.out.println("Certificate Socket Closed");
                }

            } catch (IOException ie) {
                System.out.println("Certificate Socket Close Error");
            }
        }
    }

    private String requestUsername() throws IOException {
        os.println("Enter your username: ");
        String username = is.readLine();
        return username;
    }

    private String requestPassword() throws IOException {
        os.println("Enter your password: ");
        String password = is.readLine();
        return password;
    }

    public void sendCertificateFile() {
        File certificateFile = new File("server_crt.crt");
        try {
            System.out.println("Start sending the file");
            FileInputStream certificateFileInputStream = new FileInputStream(certificateFile);
            byte[] bytes = new byte[BUFFER_SIZE];
            int count;
            while ((count = certificateFileInputStream.read(bytes)) > 0) {
                socket.getOutputStream().write(bytes, 0, count);
            }
            // send EOF character
            socket.getOutputStream().write(26);
            System.out.println("Done sending the file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}