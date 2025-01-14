package dev.jlipka.pickly.db;

import dev.jlipka.pickly.model.Status;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
@Data
@Builder
public class User {
    @Builder.Default
    private final UUID userID = UUID.randomUUID();
    @NonNull
    private final String login;
    @NonNull
    private final String passwordHash;
    @NonNull
    private final String username;
    private final String name;
    private final String surname;
    @NonNull
    @Builder.Default
    private final Status status = Status.OFFLINE;
    @Builder.Default
    private final Blob avatar = null;
    private final int age;
    @Builder.Default
    private final Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}
