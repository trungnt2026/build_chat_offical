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
	
	public static void privateMessage(String receiver, String message, ClientHandler sender) {
		
		for (ClientHandler client : clients) {
			
			if (client.getClientName().equalsIgnoreCase(receiver)) {
				client.sendMessage("[Private] " + sender.getClientName() + ": " + message);
				
				sender.sendMessage("[Private to " + receiver + "]" + message);
				
				return;
			}
		}
		
		sender.sendMessage("Khong tim thay nguoi dung: " + receiver);
	}
	
	public static String getOnlineUsers() {
		String result = "Nguoi dang online:\n";
		
		for (ClientHandler client : clients) {
			if (client.getClientName() != null) {
				result += "- " + client.getClientName() + "\n";
			}
		}
		return result;
	}
	
	public static void notifyUserOnline(ClientHandler newClient) {
		for (ClientHandler client : clients) {
			if (client != newClient) {
				client.sendMessage("[Thong bao] " + newClient.getClientName() + " da online");
			}
			
		}
	}
	
}