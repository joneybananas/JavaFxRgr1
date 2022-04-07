package com.example.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger extends Thread {
    public static void log (String message) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("demo\\src\\main\\resources\\com\\example\\demo\\log.txt", true));
            pw.append(new Date() + ": " + message + "\n");
            pw.close();
        } catch (IOException e) {
            System.out.println("Logger thread has been interrupted");
        }
    }
}
