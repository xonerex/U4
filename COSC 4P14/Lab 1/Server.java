import java.net.*;
import java.io.*;

// Tennyson Demchuk S# 6190532
// partner: Mutez Fattal S# 6362156

public class ServerLauncher {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");  //only port number required to identify connection
            System.exit(1);
        }

        Port.port = Integer.parseInt(args[0]);

        Server server = new Server();
        new Thread(server).start();     // launch socket server
    }
}

public class Server implements Runnable {

    private final String disconnectKey = "QUIT";        // Keyword for client to disconnect

    public void run() {
        try {   
            ServerSocket serverSocket = new ServerSocket(Port.port);    // create server socket
            
            while (true) {
                Socket clientSocket = serverSocket.accept();        // accept client connection request

                new Thread() {
                    public void run() {
                        try {
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  //takes input from client
                            System.out.println("Connection accepted!");
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                if (disconnectKey.equals(inputLine.toUpperCase())) {        // Check for client disconnect key -> terminate socket connection to client
                                    out.println("Disconnected from server.");
                                    return;
                                }
                                System.out.println("Client said: " + inputLine);            // 
                                out.println(Port.port + ": " + inputLine.toUpperCase());
                            }
                        } catch (Exception e) {
                            System.out.println("Exception caught when trying to listen on port "
                                + Port.port + " or listening for a connection");
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
        } catch (IOException e) {System.err.println(e);}
    }
}

public class Port {
    public static int port;
};