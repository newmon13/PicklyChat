package dev.jlipka.pickly.server;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow;
import java.util.stream.Stream;

@Slf4j
public class ServerEventProcessor implements Flow.Processor<ServerEventDTO, ServerEventDTO> {
    private Flow.Subscription subscription;
    private final Set<String> connectedUsers;
    private final Map<String, Flow.Subscriber<ServerEventDTO>> subscribers;

    public ServerEventProcessor() {
        this.connectedUsers = ConcurrentHashMap.newKeySet();
        this.subscribers = new ConcurrentHashMap<>();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void subscribe(Flow.Subscriber<? super ServerEventDTO> subscriber) {
        String clientId = ((ClientSubscriber) subscriber).getClientID();
        Flow.Subscription newSubscription = new Flow.Subscription() {
            private volatile boolean cancelled = false;

            @Override
            public void request(long n) {
                if (!cancelled && n > 0) {
                    subscription.request(n);
                }
            }

            @Override
            public void cancel() {
                cancelled = true;
                unsubscribe(clientId);
            }

            public void send() {

            }
        };
        subscriber.onSubscribe(newSubscription);
        subscribers.put(clientId, (Flow.Subscriber<ServerEventDTO>) subscriber);
        connectedUsers.add(clientId);
        publishEvent(new ServerEventDTO(
                ServerEventType.INITIAL_STATE,
                clientId,
                "Initial state",
                Map.of("connectedUsers", new ArrayList<>(connectedUsers))
        ));
    }

    private void unsubscribe(String clientId) {
        subscribers.remove(clientId);
        connectedUsers.remove(clientId);
        publishEvent(new ServerEventDTO(
                ServerEventType.USER_DISCONNECTED,
                clientId,
                String.format("Client %s disconnected", clientId),
                Map.of(
                        "connectedUsers", new ArrayList<>(connectedUsers),
                            "timestamp", System.currentTimeMillis()
                )));
    }

    @Override
    public void onNext(ServerEventDTO event) {
        if (event == null) return;
        ServerEventDTO processedEvent = processEvent(event);
        if (processedEvent == null) return;
        List<Map.Entry<String, Flow.Subscriber<ServerEventDTO>>> filteredSubscribers =
                filterSubscribers(event).toList();
        for (Map.Entry<String, Flow.Subscriber<ServerEventDTO>> entry : filteredSubscribers) {
            try {
                entry.getValue().onNext(processedEvent);
            } catch (Exception e) {
                log.error("Error sending event to subscriber {}: {}", entry.getKey(), e.getMessage());
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error in processor: ", throwable);
        subscribers.values().forEach(subscriber -> subscriber.onError(throwable));
    }

    @Override
    public void onComplete() {
        close();
    }

    private Stream<Map.Entry<String, Flow.Subscriber<ServerEventDTO>>> filterSubscribers(ServerEventDTO eventDTO) {
        return switch (eventDTO.getType()) {
            case USER_CONNECTED, USER_DISCONNECTED -> subscribers.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(eventDTO.getClientID()));
            case MESSAGE_RECEIVED, INITIAL_STATE -> subscribers.entrySet().stream()
                    .filter(entry -> entry.getKey().equals(eventDTO.getClientID()));
            case SERVER_DOWN -> subscribers.entrySet().stream();
        };
    }

    private ServerEventDTO processEvent(ServerEventDTO event) {
        return switch (event.getType()) {
            case USER_CONNECTED -> new ServerEventDTO(
                    ServerEventType.USER_CONNECTED,
                    event.getClientID(),
                    String.format("Client %s connected", event.getClientID()),
                    Map.of(
                            "connectedUsers", new ArrayList<>(connectedUsers),
                            "timestamp", System.currentTimeMillis()
                    ));
            case USER_DISCONNECTED -> new ServerEventDTO(
                    ServerEventType.USER_DISCONNECTED,
                    event.getClientID(),
                    String.format("Client %s disconnected", event.getClientID()),
                    Map.of(
                            "connectedUsers", new ArrayList<>(connectedUsers),
                            "timestamp", System.currentTimeMillis()
                    ));
            case MESSAGE_RECEIVED -> {
                if (!connectedUsers.contains(event.getClientID()) ||
                        event.getMessage() == null ||
                        event.getMessage().trim().isEmpty()) {
                    yield null;
                }
                yield new ServerEventDTO(
                        ServerEventType.MESSAGE_RECEIVED,
                        event.getClientID(),
                        event.getMessage(),
                        Map.of("timestamp", System.currentTimeMillis())
                );
            }
            default -> event;
        };
    }

    public void publishEvent(ServerEventDTO event) {
        onNext(event);
    }

    public boolean isUserConnected(String clientId) {
        return connectedUsers.contains(clientId);
    }

    public List<String> getConnectedUsers() {
        return new ArrayList<>(connectedUsers);
    }

    public void close() {
        subscribers.values().forEach(Flow.Subscriber::onComplete);
        subscribers.clear();
        connectedUsers.clear();
    }
}