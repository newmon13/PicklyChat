package dev.jlipka.pickly.server;

import dev.jlipka.pickly.Message;
import dev.jlipka.pickly.security.Connection;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

@Slf4j
class MessageMonitor {
    private final Connection connection;
    private final Flow.Subscription subscription;

    public MessageMonitor(Connection connection, Flow.Subscription subscription) {
        this.connection = connection;
        this.subscription = subscription;
        CompletableFuture.runAsync(this::startMonitoring);
    }

    public Message readMessage() throws Exception{
        return (Message) connection.read();
    }

    private void startMonitoring() {
        try {
            while (connection.isConnected()) {
                Message message = readMessage();
                if (message != null) {
                    log.info("Message received: {}", message);
                }
            }
        } catch (Exception e) {
            log.warn("Client disconnected");
            subscription.cancel();
        }
    }
}