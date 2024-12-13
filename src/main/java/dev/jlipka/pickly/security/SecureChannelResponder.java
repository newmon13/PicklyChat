package dev.jlipka.pickly.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SecureChannelResponder {
    private static final int KEY_SIZE_AES = 256;
    private final UnsecuredConnection connection;
    private Key sessionKey;
    private Key serverPublicKey;

    public SecureChannelResponder(UnsecuredConnection connection) {
        this.connection = connection;
    }

    public SecuredConnection establishSecureChannel() throws IOException {
        serverPublicKey = receivePublicKey();
        sessionKey = generateAesKey();
        sendEncryptedSessionKey();
        return new SecuredConnection(connection, sessionKey);
    }

    private Key receivePublicKey() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Key> futureKey = executor.submit(() -> {
                long startTime = System.currentTimeMillis();
                long timeout = 10000;

                while (connection.isConnected()) {
                    if (System.currentTimeMillis() - startTime > timeout) {
                        throw new RuntimeException("Timeout waiting for public key");
                    }

                    Object received = connection.read();
                    if (received instanceof Key) {
                        return (Key) received;
                    }
                }
                throw new RuntimeException("Connection closed while waiting for public key");
            });

            return futureKey.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to receive public key", e);
        } finally {
            executor.shutdown();
        }
    }

    private void sendEncryptedSessionKey() {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
            byte[] encryptedKey = rsaCipher.doFinal(sessionKey.getEncoded());
            connection.write(encryptedKey);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to encrypt and send session key", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Key generateAesKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(KEY_SIZE_AES);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate AES key", e);
        }
    }
}
