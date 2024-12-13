package dev.jlipka.pickly.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class ClientPublisher {
    private final SubmissionPublisher<ServerEventDTO> publisher;
    private final ServerEventProcessor serverEventProcessor;
    private final ExecutorService executorService;

    public ClientPublisher() {
        this.executorService = Executors.newCachedThreadPool();
        this.publisher = new SubmissionPublisher<>(executorService, Flow.defaultBufferSize());
        this.serverEventProcessor = new ServerEventProcessor();
        publisher.subscribe(serverEventProcessor);
    }

    public void onNewConnection(Socket clientSocket) throws IOException {
        ClientSubscriber subscriber = new ClientSubscriber(clientSocket);
        serverEventProcessor.subscribe(subscriber);
        publishEvent(new ServerEventDTO(
                ServerEventType.USER_CONNECTED,
                subscriber.getClientID(),
                "New client connected"
        ));
    }

    public void publishEvent(ServerEventDTO event) {
        publisher.submit(event);
    }

    public void close() {
        publishEvent(new ServerEventDTO(ServerEventType.SERVER_DOWN));
        publisher.close();
        serverEventProcessor.close();
        executorService.shutdown();
    }
}