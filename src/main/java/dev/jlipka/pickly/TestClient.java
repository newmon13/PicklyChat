package dev.jlipka.pickly;

import dev.jlipka.pickly.security.Connection;
import dev.jlipka.pickly.security.SecureChannelResponder;
import dev.jlipka.pickly.security.UnsecuredConnection;
import dev.jlipka.pickly.server.ServerEventDTO;
import dev.jlipka.pickly.server.ServerEventType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

@Slf4j
public class TestClient {
    private String clientID;
    private final Socket socket;
    private Connection connection;

    public TestClient() throws IOException {
        socket = new Socket("localhost", 9898);
        connection = new UnsecuredConnection(socket);
        SecureChannelResponder secureChannelResponder = new SecureChannelResponder((UnsecuredConnection) connection);
        connection = secureChannelResponder.establishSecureChannel();
        new Thread(this::listen).start();
    }

    private void listen() {
        try {
            while (connection.isConnected()) {
                ServerEventDTO event = (ServerEventDTO) connection.read();
                if (event.getType() == ServerEventType.INITIAL_STATE) {

                }
                handleEvent(event);
            }
        } catch (Exception e) {
            System.err.println("Connection lost: " + e.getMessage());
        }
    }

    private void handleEvent(ServerEventDTO event) {
        switch (event.getType()) {
            case USER_CONNECTED -> {
                log.info("User connected: " + event.getClientID());
            }
            case USER_DISCONNECTED -> log.info("User disconnected");
            case MESSAGE_RECEIVED -> log.info("Message received");
            case INITIAL_STATE -> {
                if (Objects.isNull(clientID)) {

                    System.out.println(event.getMetadata().get("connectedUsers"));

                    clientID = event.getClientID();
                    log.info("Setting up client id");
                } else {
                    log.info("Updating clients");
                }
            }
            case SERVER_DOWN -> log.error("Server down");
        }
    }

    public void close() throws IOException {
        socket.close();
    }
}