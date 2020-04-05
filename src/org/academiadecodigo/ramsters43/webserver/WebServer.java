package org.academiadecodigo.ramsters43.webserver;

import java.io.*;
import java.net.*;

public class WebServer {

    public static final String DOCUMENT_ROOT = "www/";
    private static final int PORT_NUMBER = 6789;

    private ServerSocket serverSocket = null;

    public static void main(String[] args) {

        WebServer webServer = new WebServer();

        webServer.start();
    }

    private void start() {

        try {

            serverSocket = new ServerSocket(PORT_NUMBER);

            while (true) {

                Socket clientSocket = serverSocket.accept();

                Thread thread = new Thread(new Task(clientSocket));

                thread.start();
            }

        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
