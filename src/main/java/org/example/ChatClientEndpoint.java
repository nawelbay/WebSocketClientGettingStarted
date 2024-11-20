package org.example;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

import java.net.URI;
import java.util.Scanner;

@ClientEndpoint
public class ChatClientEndpoint {
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to the server.");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Server: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Disconnected from the server.");
    }

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                System.err.println("Error sending message: " + e.getMessage());
            }
        } else {
            System.out.println("Cannot send message. Session is closed.");
        }
    }

    public static void main(String[] args) {
        try {
            // Create Tyrus client and connect to the server
            ClientManager client = ClientManager.createClient();
            URI uri = URI.create("ws://localhost:8080/chat");
            ChatClientEndpoint clientEndpoint = new ChatClientEndpoint();
            client.connectToServer(clientEndpoint, uri);

            System.out.println("Chat client started. Type your messages:");

            // Simple infinite loop for sending messages
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                clientEndpoint.sendMessage(message);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
