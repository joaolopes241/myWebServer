package org.academiadecodigo.ramsters43.webserver;

import java.io.*;
import java.net.Socket;

public class Task implements Runnable {

    Socket clientSocket;

    BufferedReader in;
    DataOutputStream dataOut;

    public Task(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {

            setup();

            System.out.println("Server request");

            sendData();

        } catch (IOException error) {
            error.printStackTrace();
        } finally {

            closeStreams();
            closeSocket();
        }
    }

    private void setup() throws IOException {

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        dataOut = new DataOutputStream(clientSocket.getOutputStream());
    }

    private void sendData() throws IOException {

        String request = in.readLine();

        if (request.isEmpty()) {
            return;
        }

        String[] requestSplit = request.split(" ");

        String httpVerb = requestSplit[0];
        String resource = requestSplit[1];
        System.out.println(httpVerb);
        System.out.println(resource);

        if (!httpVerb.equals("GET")) {
            return;
        }

        if (resource.equals("/")) {
            String filePath = WebServer.DOCUMENT_ROOT + "index.html";
            File indexFile = new File(filePath);
            String header = HttpHelper.ok() + HttpHelper.contentType(filePath) + HttpHelper.contentLength(indexFile.length());
            dataOut.writeBytes(header);
            System.out.println(header);

            sendFile(indexFile);
            return;
        }

        File file = getFilePath(resource);

        if (!file.exists()) {
            String filePath = WebServer.DOCUMENT_ROOT + "error404.html";
            File error404File = new File(filePath);
            String header = HttpHelper.notFound() + HttpHelper.contentType(filePath) + HttpHelper.contentLength(error404File.length());
            dataOut.writeBytes(header);
            System.out.println(header);

            sendFile(error404File);
            return;
        }

        sendHeader(resource, file);

        sendFile(file);
    }

    private File getFilePath(String resource) {

        return new File(WebServer.DOCUMENT_ROOT + resource);
    }

    private void sendHeader(String resource, File file) throws IOException {

        String header = HttpHelper.ok() + HttpHelper.contentType(resource) + HttpHelper.contentLength(file.length());
        dataOut.writeBytes(header);
        System.out.println(header);
    }

    private void sendFile(File file) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(file);

        byte[] buffer = new byte[1024];

        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            dataOut.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
    }

    private void closeStreams() {

        try {

            in.close();
            dataOut.close();

        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private void closeSocket() {

        try {

            clientSocket.close();

        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
