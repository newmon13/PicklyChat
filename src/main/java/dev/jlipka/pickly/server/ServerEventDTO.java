package dev.jlipka.pickly.server;

import lombok.Value;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Value
public class ServerEventDTO implements Serializable {
    ServerEventType type;
    String clientID;
    String message;
    Map<String, Object> metadata;

    public ServerEventDTO(ServerEventType type) {
        this(type, "", "", Collections.emptyMap());
    }

    public ServerEventDTO(ServerEventType type, String clientID, String message) {
        this(type, clientID, message, Collections.emptyMap());
    }

    public ServerEventDTO(ServerEventType type, String clientID, String message, Map<String, Object> metadata) {
        this.type = type;
        this.clientID = clientID;
        this.message = message;
        this.metadata = new HashMap<>(metadata);
    }

    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }
}