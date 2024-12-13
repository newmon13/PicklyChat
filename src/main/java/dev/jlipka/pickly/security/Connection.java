package dev.jlipka.pickly.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Connection {
    protected Socket clientSocket;
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;

    public Connection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
    }

    public abstract Object read() throws IOException, ClassNotFoundException;
    public abstract void write(Object object) throws IOException;

    public boolean isConnected() {
        return !clientSocket.isClosed();
    }
    public void close() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}