import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
    COSC 4P14 - Lab 2
    Server.java - UDP Client
    Tennyson Demchuk S# 6190532
    Mutaz Fattal S# 6362156
*/

public class Server {
    private final int PORT;
    private final int BUFFER_LENGTH = 4096;     // 4 KiB (kibibyte)
    private final String MSG = "pong";

    public Server(int PORT) {
        this.PORT = PORT;
    }

    public void start() {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            DatagramPacket pkt;
            byte[] pktbuffer;       // packet buffer
            String received = "";   // message received
            int clientPort;
            System.out.println("UDP Server Created. Listening on " + InetAddress.getLocalHost() + ':' + PORT);
            
            while (true) {                                              // continuously listen for packets
                pktbuffer = new byte[BUFFER_LENGTH];
                pkt = new DatagramPacket(pktbuffer, BUFFER_LENGTH);
                socket.receive(pkt);                                    // wait until server receives a packet
                clientPort = pkt.getPort();
                received = new String(pkt.getData()).trim();               

                System.out.println("received: " + received);
                if ("ping".equals(received)) {
                    pkt = new DatagramPacket(MSG.getBytes(), MSG.length(), InetAddress.getLocalHost(), clientPort);       // send 'pong' back to client
                    socket.send(pkt);
                }
                else {                                                  // close the server if anything but "ping" is received
                    System.out.println("Closing UDP Server...");
                    socket.close();
                    System.exit(0);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        // validate args
        if (args.length != 1) {
            System.out.println("Invalid Args.\nUsage: java Server.java <PORT#>");
            return;
        }

        Server s = new Server(Integer.parseInt(args[0]));       // pass in port number to create UDP server
        s.start();                                              // start listening
    }
}