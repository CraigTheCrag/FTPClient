package org.ftp.data;

import org.ftp.exception.FTPException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FTPFileData {

    private List<String> data;
    private String filename;

    /**
     * Constructor for <code>FTPFileData</code> with <code>File</code> to get data from
     * @param file - <code>File</code> to get data from
     */
    public FTPFileData(File file) {
        this.filename = file.getName();
        this.data = this.readData(file);
    }

    /**
     * Reads data from <code>File</code> into <code>List</code> of <code>String</code> objects
     * @param file - <code>File</code> to read data from
     * @return <code>List</code> of <code>String</code> objects
     * @throws FTPException thrown if error is caused while reading data from <code>File</code>
     */
    private List<String> readData(File file) throws FTPException {
        List<String> data = new ArrayList<>();
        Scanner scanner;
        try {
             scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new FTPException(String.format("File not found: %s", file.getName()));
        }
        return data;
    }

    /**
     * Get name of <code>File</code>
     * @return name of <code>File</code>
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Get data read from <code>File</code>
     * @return data from <code>File</code>
     */
    public List<String> getData() {
        return this.data;
    }
}
