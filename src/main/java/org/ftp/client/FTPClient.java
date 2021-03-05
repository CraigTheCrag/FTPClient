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

    /**
     * Main method
     * @param args - Indexes: 0 = File to send, 1 = destination ip, 2 = destination port
     * @throws FTPException thrown if error at any point
     */
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

    /**
     * Opens <code>Socket</code> to server
     * @return the <code>Socket</code>
     * @throws FTPException thrown if error caused while opening <code>Socket</code>
     */
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

    /**
     * Closes <code>Socket</code> to server
     * @throws FTPException thrown if error caused while closing <code>Socket</code>
     */
    private static void closeSocket() throws FTPException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new FTPException("Error closing socket");
        }
        System.out.printf("Closed Socket: %s:d%n", ip, port);
    }

    /**
     * Sends data to server over <code>Socket</code> created
     * @param data - Data to be sent to server over <code>Socket</code>
     * @throws FTPException thrown if error caused while creating <code>OutputStream</code>, writing <code>Object</code>
     * to <code>OutputStream</code>, flushing <code>OutputStream</code>, or closing <code>OutputStream</code>
     */
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

    /**
     * Creates <code>FTPFileData</code> <code>Object</code> from <code>File</code>
     * @return newly created <code>FTPFileData</code> <code>Object</code>
     */
    private static FTPFileData getData() {
        return new FTPFileData(fileToSend);
    }
}
