package dev.jlipka.pickly;

import dev.jlipka.pickly.security.Connection;
import dev.jlipka.pickly.security.SecureChannelResponder;
import dev.jlipka.pickly.security.UnsecuredConnection;
import dev.jlipka.pickly.server.ServerEventDTO;
import dev.jlipka.pickly.server.ServerEventType;

import java.io.IOException;
import java.net.Socket;

public class TestClient {
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
                    System.out.println(event.getClientID());
                }
                handleEvent(event);
            }
        } catch (Exception e) {
            System.err.println("Connection lost: " + e.getMessage());
        }
    }

    private void handleEvent(ServerEventDTO event) {
        System.out.println("Received event: " + event.getType() +
                         ", Message: " + event.getMessage() +
                         ", Metadata: " + event.getMetadata());
    }

    public void close() throws IOException {
        socket.close();
    }



    public void sendMessage(Message message) {
        try {
            connection.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        TestClient client = new TestClient();
        Message message = new Message.MessageBuilder("adasd", "asdsadas")
                .addMessageContent("co tam chuju")
                .build();
        client.sendMessage(message);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                client.close();
            } catch (IOException e) {
                System.err.println("Error during shutdown: " + e.getMessage());
            }
        }));
    }
}