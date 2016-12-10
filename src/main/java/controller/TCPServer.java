package controller;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Mateusz on 22.11.2016.
 *
 * TCP Server. This class allows to connect with aplication by socket. Client sent information or data to this server
 * and then server will send response to client.
 */

public class TCPServer extends Thread {

    private String clientResponse;
    private PrintWriter out;
    private BufferedReader in;
    private ServerSocket listener;
    private Socket socket;
    private TextArea txtArea;

    /**
     * Constructor of TCPServer class
     *
     * @param txtArea - JavaFX text element, for now it shows data from client.
     */
    public TCPServer(TextArea txtArea) {
        this.txtArea = txtArea;
    }


    @Override
    public void run() {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Start server and accept incoming connection from client. Set out and in streams. To send and get data.
     *
     * @throws IOException
     */
    private void startServer() throws IOException{
        listener = new ServerSocket(9090);
        try {
            socket = listener.accept();

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            clientListenerThread();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to start new thread to get messages from client.
     */
    private void clientListenerThread(){

        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        clientResponse = in.readLine();

                        if (clientResponse != null) {
                            System.out.println( clientResponse );;
                        }

                        if (clientResponse.equals("Application is closed")){
                            closeConnection();
                            break;
                        }

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * Method which allow to send message to Client
     *
     * @param message - message which will be sent.
     */
    public void sendMessage(String message){
        out.println(message);
    }

    /**
     * Method to close server.
     */
    private void closeConnection(){
        try {
            listener.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnectionIfNotClosed(){
        if (!socket.isClosed())
    }


}
