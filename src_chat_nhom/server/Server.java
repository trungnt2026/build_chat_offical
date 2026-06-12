package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {
	
	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	public static HashMap<String, ArrayList<ClientHandler>> groups = new HashMap<>();
	
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
	
	public static void createGroup(String groupName, ClientHandler creator) {
		
		if (groups.containsKey(groupName)) {
			
			creator.sendMessage("Nhom da ton tai");
			
			return;
		}
		
		ArrayList<ClientHandler> members = new ArrayList<ClientHandler>();
		
		members.add(creator);
		
		groups.put(groupName, members);
		
		creator.sendMessage("Da tao nhom " + groupName);
		
		System.out.println(creator.getClientName() + " da tao nhom " + groupName);
	}
	
	public static void joinGroup(String groupName, ClientHandler client) {
		
		ArrayList<ClientHandler> members = groups.get(groupName);
		
		if (members == null) {
			client.sendMessage("Khong tim thay nhom: " + groupName);
			return;
		}
		
		if (members.contains(client)) {
			client.sendMessage("Ban da o trong nhom " + groupName);
			return;
		}
		 
		members.add(client);
		
		client.sendMessage("Da tham gia nhom " + groupName);
	}

	public static void inviteToGroup(String userName, 
									String groupName,
									ClientHandler sender) {
		ArrayList<ClientHandler> members = groups.get(groupName);
		
		if (members == null) {
			sender.sendMessage("Khong tim thay nhom: " + groupName);
			return;
		}
		
		if (!members.contains(sender)) {
			sender.sendMessage("Ban khong thuoc nhom " + groupName);
			return;
		}
		
		for (ClientHandler client : clients) {
			if (client.getClientName().equalsIgnoreCase(userName)) {
				if (members.contains(client)) {
					sender.sendMessage(
							userName + " da o trong nhom " + groupName);
					return;
				}
				
				members.add(client);
				client.sendMessage("[Moi nhom] " 
									+ sender.getClientName()
									+ " da moi ban vao nhom "
									+ groupName);
				
				sender.sendMessage("Da them " + userName + " vao nhom " + groupName);
					
				return;
			}
		}
		
		sender.sendMessage("Khong tim thay nguoi dung: " + userName);
	}
	
}
	