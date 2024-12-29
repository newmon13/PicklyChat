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
            case USER_CONNECTED -> log.info("User connected");
            case USER_DISCONNECTED -> log.info("User disconnected");
            case MESSAGE_RECEIVED -> log.info("Message received");
            case INITIAL_STATE -> {
                if (Objects.nonNull(clientID)) {
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

    public void sendMessage(Message message, String clientID) {
        try {
            connection.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) throws IOException {
//        TestClient client = new TestClient();
//        Message message = new Message.MessageBuilder("adasd", "asdsadas")
//                .addMessageContent("co tam chuju")
//                .build();
//        client.sendMessage(message);
//
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                client.close();
//            } catch (IOException e) {
//                System.err.println("Error during shutdown: " + e.getMessage());
//            }
//        }));
//    }
}