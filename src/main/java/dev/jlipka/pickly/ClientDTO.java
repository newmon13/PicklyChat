package dev.jlipka.pickly;

import java.io.Serializable;

public record ClientDTO(String clientId, String nickname, SerializableFile avatar) implements Serializable {
}
