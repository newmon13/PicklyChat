package dev.jlipka.pickly.server;

public enum ServerEventType {
    USER_CONNECTED,
    USER_DISCONNECTED,
    MESSAGE_RECEIVED,
    INITIAL_STATE,
    SERVER_DOWN
}