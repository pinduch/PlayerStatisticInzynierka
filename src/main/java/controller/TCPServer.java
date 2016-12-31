package controller;

import common.ServerRequest;
import model.ServerModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Mateusz on 22.11.2016.
 *
 * TCP Server. This class allows to connect with application by socket. Client sent information or data to this server
 * and then server will send response to client.
 */

public class TCPServer extends Thread {

    public enum TcpConnectionState {CONNECTED, DISCONNECTED}

    private String clientRequest;
    private PrintWriter out;
    private BufferedReader in;
    private ServerSocket listener;
    private Socket socket;
    private ServerModel serverModel;

    private static TCPServer instance = null;
    private TcpConnectionState tcpConnectionState;

    public static TCPServer getInstance(){
        if (instance == null){
            instance = new TCPServer();
        }
        return instance;
    }

    /**
     * Server constructor
     */
    protected TCPServer(){
        serverModel = ServerModel.getInstance();
        tcpConnectionState = TcpConnectionState.DISCONNECTED;
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
            tcpConnectionState = TcpConnectionState.CONNECTED;
            System.out.println("CONNECTED");

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
            @Override
            public void run() {
                while (!socket.isClosed()) {
                    try {
                        clientRequest = in.readLine();

                        if (clientRequest.equals(ServerRequest.DISCONNECT)) {
                            closeConnection();
                        }

                        if (clientRequest != null) {
                            serverModel.setReceivedMessage(clientRequest);
                        }

                    } catch (IOException e) {
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
    public void closeConnection(){
        try {
            if (socket != null) socket.close();
            if (listener != null) listener.close();
            instance = null;
            tcpConnectionState = TcpConnectionState.DISCONNECTED;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to check if some device is connected to server.
     *
     * @return
     */
    public boolean isDeviceConnected(){
        if ( tcpConnectionState.equals(TcpConnectionState.CONNECTED) )
            return true;
        else
            return false;
    }

}
