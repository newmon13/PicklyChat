package dev.jlipka.pickly.security;

import java.io.IOException;
import java.net.Socket;

public class UnsecuredConnection extends Connection {
    public UnsecuredConnection(Socket clientSocket) throws IOException {
        super(clientSocket);
    }

    @Override
    public Object read() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }

    @Override
    public void write(Object object) throws IOException {
        outputStream.writeObject(object);
        outputStream.flush();
    }
}