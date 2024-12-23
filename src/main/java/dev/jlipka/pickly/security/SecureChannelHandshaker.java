package dev.jlipka.pickly.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class SecureChannelHandshaker {
    private static final int KEY_SIZE_RSA = 2048;
    private final UnsecuredConnection connection;
    private final KeyPair rsaKeyPair;

    public SecureChannelHandshaker(UnsecuredConnection connection) {
        this.connection = connection;
        this.rsaKeyPair = generateRsaKeyPair();
    }

    public SecuredConnection establishSecureChannel() throws IOException {
        sendPublicKey();
        Key sessionKey = receiveSessionKey();
        return new SecuredConnection(connection, sessionKey);
    }

    private void sendPublicKey() throws IOException {
        connection.write(rsaKeyPair.getPublic());
    }

    private Key receiveSessionKey() {
        ExecutorService executor = newSingleThreadExecutor();
        try {
            Future<Key> futureKey = executor.submit(() -> {
                long startTime = System.currentTimeMillis();
                long timeout = 10000;

                while (connection.isConnected()) {
                    if (System.currentTimeMillis() - startTime > timeout) {
                        throw new RuntimeException("Timeout waiting for session key");
                    }

                    Object received = connection.read();
                    if (received instanceof byte[]) {
                        Cipher rsaCipher = Cipher.getInstance("RSA");
                        rsaCipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
                        byte[] decryptedKey = rsaCipher.doFinal((byte[]) received);
                        return new SecretKeySpec(decryptedKey, "AES");
                    }
                }
                throw new RuntimeException("Connection closed while waiting for session key");
            });

            return futureKey.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to receive session key", e);
        } finally {
            executor.shutdown();
        }
    }

    private KeyPair generateRsaKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(KEY_SIZE_RSA);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }
}