package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	
	public static void main (String[] args) {
		
		try {
			ServerSocket server = new ServerSocket(5000);
			System.out.println("Server da khoi dong tai cong 5000");
			
			while (true) {
				
				Socket client = server.accept();
				
				ClientHandler handler = new ClientHandler(client);
				
				clients.add(handler);
				
				handler.start();
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void broadcast(String message, ClientHandler sender) {
		for (ClientHandler client : clients) {
			if (client != sender) {
				client.sendMessage(message);
				
			}
		}
	}
	
}