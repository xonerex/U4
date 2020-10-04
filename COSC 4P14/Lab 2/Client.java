import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

/*
    COSC 4P14 - Lab 2
    Client.java - UDP Client
    Tennyson Demchuk S# 6190532
    Mutaz Fattal S# 6362156
*/

public class Client {
    private final int PORT;
    private final int BUFFER_LENGTH = 4096;     // 4 KiB (kibibyte)
    private final String MSG = "ping";
    private InetAddress IP = null;


    public Client(int PORT) {
        this.PORT = PORT;
        try {
            this.IP = InetAddress.getLocalHost();
        } catch (IOException e) {System.out.println(e);}
    }

    public void start() {
        if (IP == null) {
            System.err.println("IP Address Cannot Be Null");
            System.exit(0);
        }
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket pkt;
            byte[] pktbuffer;       // packet buffer
            String received = "";
            long sendtime = 0;      // send time and response time 
            long restime = 0;   
            long rtt = 0;  

            socket.setSoTimeout(1000);          // set timeout to 1 second - wont wait for responses after 1 second
            
            for (int i=0; i<10; i++) {                  // send 10 pings
                // send packet to server
                pkt = new DatagramPacket(MSG.getBytes(), MSG.length(), IP, PORT);
                socket.send(pkt);                                    // wait until server receives a packet
                sendtime = System.nanoTime();
                System.out.println(new Date(System.currentTimeMillis()).toString() + " : ping sent");

                // wait for response
                pktbuffer = new byte[BUFFER_LENGTH];
                pkt = new DatagramPacket(pktbuffer, BUFFER_LENGTH);
                try {
                    socket.receive(pkt); 
                    restime = System.nanoTime();
                } catch (Exception e) {         // catch response timeout
                    System.out.println("Response timeout. Packet may have been lost.");
                    continue;
                }
                received = new String(pkt.getData()).trim();
                if ("pong".equals(received)) {
                    System.out.print(new Date(System.currentTimeMillis()).toString() + " : pong received");
                    rtt = restime - sendtime;
                    System.out.println(" [RTT = " + rtt + "ns]");
                }
            }
            // send message other than 'ping' to server to tell it to terminate
            pkt = new DatagramPacket("terminate".getBytes(), "terminate".length(), IP, PORT);
            socket.send(pkt);
            socket.close();         // close socket -> free resources
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        // validate args
        if (args.length != 1) {
            System.err.println("Invalid Args.\nUsage: java Client.java <PORT# of Server>");
            return;
        }

        Client c = new Client(Integer.parseInt(args[0]));       // pass in port number to create UDP server
        c.start();                                              // start listening
    }
}