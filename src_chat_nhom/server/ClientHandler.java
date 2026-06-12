package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {

	private Socket client;
	private PrintWriter output;
	private String name;
	
	public ClientHandler (Socket client) {
		this.client = client;
	}
	
	@Override
	public void run () {
		try {
			BufferedReader input = new BufferedReader (
					new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8)
					);
			
			output = new PrintWriter (
					new java.io.OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true
					);
			
			name = input.readLine();
			
			System.out.println(name + " da ket noi");
			
			Server.notifyUserOnline(this);
		
			String message;
			
			while ((message = input.readLine()) != null) {
				
				if (message.equalsIgnoreCase("/exit")) {
					System.out.println(name + " da thoat");
					break;
				}
				
				if (message.equalsIgnoreCase("/online")) {
					sendMessage(Server.getOnlineUsers());
					continue;
				}
				
				if (message.startsWith("/rename")) {
					
					String[] parts = message.split(" ", 2);
					
					if (parts.length == 2) {
						
						String newName = parts[1];
						
						String oldName = name;
						
						name = newName;
						
						System.out.println(oldName + " da doi ten thanh: " + newName);
						
						Server.broadcast ("[Thong bao]" + oldName + " da doi ten thanh: " + newName, this);
					
						sendMessage("Ban da doi ten thanh: " + newName);
					
					} else {
						sendMessage("Sai cu phap! Dung: /rename ten_moi");
					}
					
					continue;
					
				}
				
				if (message.startsWith("/creategroup")) {
					
					String[] parts = message.split(" ", 2);
					
					if (parts.length == 2) {
						
						String groupName = parts[1];
						
						Server.createGroup(groupName, this);
					} else {
						sendMessage("Sai cu phap! Dung: /creategroup ten_nhom");
					}
					
					continue;
				}
				
				if (message.startsWith("/join")) {
					
					String[] parts = message.split(" ", 2);
					
					if (parts.length == 2) {
						String groupName = parts[1];
						
						Server.joinGroup(groupName, this);
					} else {
						sendMessage("Sai cu phap! Dung: /join ten_nhom");
					}
					continue;
				}
				
 				if (message.startsWith("/pm ")) {
					
					String[] parts = message.split(" ", 3);
					
					if (parts.length == 3) {
						String receiver = parts[1];
						String content = parts[2];
						
						Server.privateMessage(receiver, content, this);
						
					} else {
						sendMessage("Sai cu phap! Dung: /pm ten nguoi_nhan noi_dung");
					}
				
					
				} else {
					
					System.out.println(name + ": " + message);
					
					Server.broadcast(name + ": " + message, this);
				}
			}
			
			client.close();
			Server.clients.remove(this);
			Server.broadcast("[Thong bao] " + name + " da offline", this);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void sendMessage(String message) {
		output.println(message);
	}
	
	public String getClientName() {
		return name;
	}
	
}