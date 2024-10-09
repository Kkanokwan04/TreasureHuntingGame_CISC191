package edu.sdccd.cisc191.template.template;

/**
 * This class manages network communication between the client and server, as well as tracking the player's
 * time in the game. It utilizes the GameClient class for sending data and PlayerTimeManager for managing playtime.
 */
public class NetworkManager {
    private GameClient socketClient;
    private PlayerTimeManager timeManager;
    private long lastTimeSent = 0;
    private long sendInterval = 1000;

    /**
     * Constructs a NetworkManager and initializes the PlayerTimeManager.
     */
    public NetworkManager() {
        timeManager = new PlayerTimeManager(); // Initialize the time manager
    }

    /**
     * Starts the game client to connect to the server with the provided host IP address.
     * Also starts the game timer.
     *
     * @param host The IP address of the server to connect to.
     */
    public void startClient(String host) {
        socketClient = new GameClient(host);
        socketClient.start();
        timeManager.startTimer(); // Start the timer when the client starts
    }

    /**
     * Sends a string of data to the server by converting it into a byte array.
     *
     * @param data The string of data to be sent to the server.
     */
    public void sendData(String data) {
        if (socketClient != null) {
            socketClient.sendData(data.getBytes());
        }
    }

    /**
     * Periodically sends the total playtime to the server. The data is sent only if
     * the specified send interval (in milliseconds) has passed since the last time the data was sent.
     */
    public void updateServerTimeState() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeSent >= sendInterval) {
            String playTimeString = "Total time in the Game: " + timeManager.getFormattedPlayTime();
            sendData(playTimeString);
            lastTimeSent = currentTime;
        }
    }

    /**
     * Checks whether the game is completed. This method can be extended to send final playtime
     * or perform any necessary actions upon game completion.
     */
    public void checkGameCompletion()
    {

    }
}
