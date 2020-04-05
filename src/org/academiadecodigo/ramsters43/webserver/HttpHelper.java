package org.academiadecodigo.ramsters43.webserver;

public class HttpHelper {

    public static String ok() {
        return "HTTP/1.0 200 Document Follows\r\n";
    }

    public static String notFound() {
        return "HTTP/1.0 404 Not Found\r\n";
    }

    public static String contentType(String file) {

        if (isImage(file)) {
            return "Content-Type: image/" + getExtension(file) + "\r\n";
        }

        return "Content-Type: text/html; charset=UTF-8\r\n";
    }

    public static String contentLength(long length) {

        return "Content-Length: " + length + "\r\n\r\n";
    }

    private static boolean isImage(String file) {

        return getExtension(file).equals("jpg") || getExtension(file).equals("png") || getExtension(file).equals("gif");
    }

    private static String getExtension(String file) {

        return file.substring(file.lastIndexOf(".") + 1);
    }
}
