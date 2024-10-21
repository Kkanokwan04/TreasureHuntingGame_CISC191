package edu.sdccd.cisc191.template.template;

import java.io.IOException;
import java.net.*;

/**
 * The GameServer class represents a simple UDP server that listens for messages from clients
 * and sends responses back to the clients. It runs in its own thread and can handle multiple clients.
 */
public class GameServer extends Thread {

    private static final int PORT = 1331; // Change: Constant for server port
    private static final int BUFFER_SIZE = 1024; // Change: Constant for the size of the buffer
    private DatagramSocket socket;

    /**
     * Constructs a GameServer and binds it to port 1331.
     * If the port binding fails, it prints the error stack trace.
     */
    public GameServer() {
        try {
            this.socket = new DatagramSocket(PORT);
            System.out.println("Server started on: " + socket.getLocalPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main loop of the server, which listens for incoming UDP packets,
     * processes the received data, and sends a response back to the client.
     * This method runs continuously while the server is running.
     */
    public void run() {
        while (true) {
            byte[] data = new byte[BUFFER_SIZE]; // Changed to BUFFER_SIZE from 1024
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData(), 0 , packet.getLength()).trim(); // Added 0 and packet.getLength()
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();

            System.out.println("Received from client (" + clientAddress.getHostAddress() + "): " + message);

            sendData(("Server received: " + message).getBytes(), clientAddress, clientPort);
        }
    }

    /**
     * Sends data back to the client.
     *
     * @param data       The data to be sent to the client, as a byte array.
     * @param ipAddress  The IP address of the client.
     * @param port       The port number of the client.
     */
    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main method to start the GameServer as a standalone application.
     * It creates a GameServer instance and starts its thread.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
    }
}
