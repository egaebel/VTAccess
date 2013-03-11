package com.vtaccess.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Class used for encrypted input/output to/from a file passed into the constructor.
 * 
 * @author Ethan Gaebel (egaebel)
 * 
 */
public class SecureIO {

    // ~Constants
    /**
     * Key used to encrypt the username and password.
     * Randomly assigned a char value when PasswordIO object is constructed.
     */
    private static final char KEY = '#';

    // ~Data Fields--------------------------------------------
    /**
     * File which this class will read from and write to.
     */
    private File passwordFile;

    // ~Constructors--------------------------------------------
    /**
     * Takes a file to use for the IO that occurs in this class.
     * 
     * @param file the file that this class is to write to.
     */
    public SecureIO(File file) {

        passwordFile = file;
    }
    
    /**
     * Takes a file path to create a file to be used for the IO that
     * occurs in this class.
     * 
     * @param fileName String that is a fileName to be used for file IO.
     */
    public SecureIO(String fileName) {
        
        passwordFile = new File(fileName);
    }

    // ~Methods--------------------------------------------
    /**
     * Checks to see if the passed in file object exists.
     * does.
     * 
     * @return true if file exists, false otherwise.
     */
    public boolean fileExists(File file) {

        boolean value = false;

        if (file != null) {

            value = file.exists();
        }

        return value;
    }

    /**
     * Takes two strings(username and password), encrypts them, and rights them to file.
     * 
     * @param username the username
     * @param password the password
     */
    public void writeEncrypt(String username, String password) {

        String[] loginData = new String[2];
        loginData[0] = username;
        loginData[1] = password;

        writeEncrypt(loginData);
    }

    /**
     * Takes in a String array and encrypts the members of it, 
     * and writes the encrypted members to file.
     * 
     * @param loginData String array containing the strings to write encrypted to file.
     */
    public void writeEncrypt(String[] loginData) {

        // encrypt data
        decoderRing(loginData);
        // write data to file
        try {

            // add spaces to differentiate between strings in file
            for (int i = 0; i < loginData.length; i++) {
                loginData[i] += " ";   
            }

            //InputStream reference to be used for reading bytes from loginData
            InputStream is;
            // creates an output stream for the FILEIO's myFile
            OutputStream os = new FileOutputStream(passwordFile);

            //Reference for the byte array to transfer data
            byte[] data;

            for (int i = 0; i < loginData.length; i++) {
                
                
                is = new ByteArrayInputStream(loginData[i].getBytes());
                // creates a byte array the size of the available members in is
                data = new byte[is.available()];
                
                // read bytes into data byte array
                is.read(data);
                // copy bytes into file
                os.write(data);
                
                is.close();
            }

            // close down streams
            os.close();
        }
        catch (IOException e) {

            // Unable to create file, permissions issue
            e.printStackTrace();
        }
    }

    /**
     * Reads from file (if it exists) the user data saved, decrypts it, and
     * returns the decrypted strings in a String array.
     * 
     * @return loginData the user's unencrypted data in String form.
     */
    public String[] readDecrypt() {

        String[] loginData;
        LinkedList<String> temp = new LinkedList<String>();

        // read data from file
        if (fileExists(passwordFile)) {

            try {

                Scanner in = new Scanner(passwordFile);
                
                // loop through all space separated elements in passwordFile
                while (in.hasNext()) {

                    temp.add(in.next());
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            loginData = temp.toArray(new String[temp.size()]);
            
            // decrypt data
            decoderRing(loginData);
            
            return loginData;
        }
        else {

            return null;
        }
    }
    
    /**
     * Decrypts or encrypts user data, depends on what form it is in when its
     * passed in. Alters the passed in String array.
     * 
     * @param loginData
     *            the encrypted or regular strings of loginData from the user 
     *            (e.g. loginData[0] = username; loginDate[1] = password;)
     */
    private void decoderRing(String[] loginData) {

        if (loginData.length > 0) {

            char[] temp;
            
            for (int i = 0; i < loginData.length; i++) {
                
                temp = loginData[i].toCharArray();
                
                for (int j = 0; j < temp.length; j++) {
                    
                    temp[j] ^= KEY;
                }
                
                loginData[i] = String.valueOf(temp);
            }
        }
    }
}