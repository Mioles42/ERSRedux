package com.miolean.ers;/*
 * Miles Krusniak
 * ERSConfig.java (ERS)
 * 
 * ERSConfig deals with getting controls from, and opening, the config file.
 */

import java.io.*;
import java.util.Scanner;
import java.awt.Desktop;

public class ERSConfig {
    public ERSConfig() {
        //Simple constructor.
    }
    public char[] parseConfig(InputStream file)
    throws FileNotFoundException {
        //Returns an array containing all config items.
        
        //Variables
        Scanner scan = new Scanner(file);
        char[] result = new char[158]; //The max it will ever have to hold is 157 (52 max players * 3 controls + quit)
        String input = "";
        int iter = 0;
        
        while(scan.hasNext()) { //While there is another token (word, character, etc) in the config
            input = scan.next();
            if(input.length() == 1) { //If the input is one letter - which is what we want - 
                result[iter] = input.charAt(0); //We've found one result, store it
                for(int i = iter- 1; i >= 0; i--) {
                    if(result[iter] == result[i]) {
                        //Shoud our config have repeats, throw an exception.
                        //I don't know how to make exceptions very well so I will use one from Java.io.
                        //This exception could also be thrown from a missing config, so by catching it I
                        //can hopefully kill two birds with one well-aimed Exception.
                        throw new FileNotFoundException("Invalid configuration");
                    }
                }
                iter++; //Prepare to find the next result
            }
        }
        return result;
    }
    public char nextConfig(File file)
    throws FileNotFoundException {
        //Gets one config item. Unused currently.
        Scanner scan = new Scanner(file);
        char result = '!';
        String input = "";
        
        //Find one char using a process similar to the last one
        while(scan.hasNext()) {
            input = scan.next();
            if(input.length() == 1) {
                result = input.charAt(0);
                break;
            }
        }
        return result;
    }
    
    public void openConfig(File file)
    throws FileNotFoundException, IOException {
        //Opens config file, if possible.
        Desktop.getDesktop().open(file);
    }
}