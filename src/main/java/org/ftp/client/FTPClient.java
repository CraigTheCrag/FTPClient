package org.ftp.client;

import org.ftp.data.FTPFileData;
import org.ftp.exception.FTPException;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class FTPClient {

    private static final String DEFAULT_FILE = "test.txt";
    private static final String DEFAULT_IP = "localhost";
    private static final Integer DEFAULT_PORT = 6666;

    private static File fileToSend;
    private static String ip;
    private static Integer port;

    private static Socket socket;

    public static void main(String[] args) throws FTPException{
        if ((args.length > 0) & (args.length < 4)) {
            fileToSend = new File(args[0]);
            ip = args[1];
            port = Integer.parseInt(args[2]);
        } else {
            fileToSend = new File(DEFAULT_FILE);
            ip = DEFAULT_IP;
            port = DEFAULT_PORT;
        }
        socket = openSocket();
        sendData(getData());
        closeSocket();
    }

    private static Socket openSocket() throws FTPException {
        Socket s;
        try {
            s = new Socket(ip, port);
        } catch (IOException e) {
            throw new FTPException(String.format("Error opening connection with %s:%d",
                    ip, port));
        }
        System.out.printf("Opened Socket: %s:d%n", ip, port);
        return s;
    }

    private static void closeSocket() throws FTPException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new FTPException("Error closing socket");
        }
        System.out.printf("Closed Socket: %s:d%n", ip, port);
    }

    private static void sendData(FTPFileData data) throws FTPException {
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new FTPException("Error getting output stream");
        }
        try {
            outputStream.writeObject(data);
        } catch (IOException e) {
            throw new FTPException("Error writing file to stream");
        }
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new FTPException("Error flushing stream");
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new FTPException("Error closing steam");
        }
        System.out.printf("Sent file %s to %s:d%n", fileToSend.getName(), ip, port);
    }

    private static FTPFileData getData() {
        return new FTPFileData(fileToSend);
    }
}
