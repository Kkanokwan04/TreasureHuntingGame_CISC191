package edu.sdccd.cisc191;
import edu.sdccd.cisc191.GamePanel;
import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private GamePanel gp;

    public GameClient(GamePanel gp, String ipAddress) {
        this.gp = gp;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);

            // Retrieve and print the local IP address of the client
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Client running on local IP: " + localAddress.getHostAddress());

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);  // Receive response from the server
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server > " + new String(packet.getData()));
        }
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);  // Send data to the server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}