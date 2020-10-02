import java.net.*;
import java.io.*;

// Tennyson Demchuk S# 6190532
// partner: Mutez Fattal S# 6362156

public class Client {
    public static void main(String[] args) throws IOException {

        final String disconnectKey = "QUIT";        // Keyword for client to disconnect

        if (args.length != 2) {
            System.err.println("Usage: java Client.java <host IP> <port number>");  //only port number required to identify connection
            System.exit(1);
        }

        String ip = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try  {
            Socket client = new Socket(ip,portNumber);
            PrintWriter output = new PrintWriter(client.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connection accepted!");
            String userInput;
            while((userInput = stdIn.readLine()) != null) {
                output.println(userInput);
                System.out.println("Server Received: " + input.readLine());

                if (disconnectKey.equals(userInput.toUpperCase())) {        // Check for client disconnect key -> exit loop
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}