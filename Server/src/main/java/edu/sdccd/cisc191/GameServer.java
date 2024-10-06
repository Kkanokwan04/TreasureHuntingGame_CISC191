package edu.sdccd.cisc191;
import edu.sdccd.cisc191.GamePanel;

import java.io.IOException;
import java.net.*;

public class GameServer extends Thread {

    private DatagramSocket socket;
    private GamePanel gp;

    public GameServer(GamePanel gp) {
        this.gp = gp;
        try {
            this.socket = new DatagramSocket(1331); // Make sure you're using the correct port
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);  // Receive packet from the client
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData()).trim();  // Get the message from the client

            // Get the client's IP address (localhost or another address)
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();

            // Print both the message and the client's IP address and port
            System.out.println("Received from client (" + clientAddress.getHostAddress() + "): " + message);

            // Respond to the client with a confirmation message
            sendData(("Server received: " + message).getBytes(), clientAddress, clientPort);
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
