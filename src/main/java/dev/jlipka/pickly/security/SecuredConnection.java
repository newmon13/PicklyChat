package dev.jlipka.pickly.security;


import javax.crypto.Cipher;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Key;

public class SecuredConnection extends Connection {
    private final Connection connection;
    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public SecuredConnection(Connection connection, Key sessionKey) throws IOException {
        super(connection.clientSocket);
        this.connection = connection;

        try {
            this.encryptCipher = Cipher.getInstance("AES");
            this.decryptCipher = Cipher.getInstance("AES");
            this.encryptCipher.init(Cipher.ENCRYPT_MODE, sessionKey);
            this.decryptCipher.init(Cipher.DECRYPT_MODE, sessionKey);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to initialize ciphers", e);
        }
    }

    @Override
    public Object read() throws IOException, ClassNotFoundException {
        try {
            byte[] encryptedData = (byte[]) connection.read();
            byte[] decryptedData = decryptCipher.doFinal(encryptedData);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                return ois.readObject();
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to read encrypted data", e);
        }
    }

    @Override
    public void write(Object object) throws IOException {
        try {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(object);
                byte[] data = bos.toByteArray();
                byte[] encryptedData = encryptCipher.doFinal(data);
                connection.write(encryptedData);
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to send encrypted data", e);
        }
    }
}