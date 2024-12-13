package dev.jlipka.pickly.server;

import dev.jlipka.pickly.security.Connection;
import dev.jlipka.pickly.security.SecureChannelHandshaker;
import dev.jlipka.pickly.security.UnsecuredConnection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.Flow;

@Slf4j
public class ClientSubscriber implements Flow.Subscriber<ServerEventDTO> {
    @Getter
    private final String clientID;
    @Getter
    private Connection connection;
    private volatile Flow.Subscription subscription;
    private MessageMonitor messageMonitor;

    public ClientSubscriber(Socket clientSocket) throws IOException {
        this.clientID = UUID.randomUUID().toString();
        this.connection = new UnsecuredConnection(clientSocket);
        SecureChannelHandshaker secureChannelHandshaker =
                new SecureChannelHandshaker((UnsecuredConnection) connection);
        this.connection = secureChannelHandshaker.establishSecureChannel();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.messageMonitor = new MessageMonitor(connection, subscription);
        subscription.request(1);
        log.debug("Subscribed to events for client: {}", clientID);
    }

    @Override
    public void onNext(ServerEventDTO event) {
        try {
            connection.write(event);
            subscription.request(1);
            log.trace("Sent event to client: {}", clientID);
        } catch (IOException e) {
            log.error("Failed to send event to client: {}", clientID, e);
            subscription.cancel();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Event subscription cancelled for client: {}", clientID, throwable);
        cleanup();
    }

    @Override
    public void onComplete() {
        log.info("Event subscription completed for client: {}", clientID);
        cleanup();
    }

    private void cleanup() {
        if (subscription != null) {
            subscription.cancel();
            connection.close();
        }
    }

}