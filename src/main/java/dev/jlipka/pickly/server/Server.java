package dev.jlipka.pickly.server;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server implements Runnable {
    private final int port;
    private final ServerSocket serverSocket;
    private final Thread serverThread;
    private final ClientPublisher clientPublisher;

    public Server(int port) {
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
            this.clientPublisher = new ClientPublisher();
            this.serverThread = new Thread(this, "Server-Main-Thread");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Server started on port {}", port);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                clientPublisher.onNewConnection(clientSocket);
                log.info("New client connected");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() {
        serverThread.start();
    }

    public static void main(String[] args) {
        Server server = new Server(9898);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown signal received - initiating graceful shutdown");
            try {
                server.serverThread.join(5000);
                server.clientPublisher.close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Shutdown interrupted", e);
            } catch (Exception e) {
                log.error("Error during shutdown", e);
            }
            log.info("Server shutdown completed");
        }, "Server-Shutdown-Hook"));

        server.start();
    }
}