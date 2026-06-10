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
		
			String message;
			
			while ((message = input.readLine()) != null) {
				
				if (message.equalsIgnoreCase("/exit")) {
					System.out.println(name + " da thoat");
					break;
				}
				
				System.out.println(name + ": " + message);
				
				Server.broadcast(name + ": " + message, this);
				
			}
			client.close();
			Server.clients.remove(this);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		output.println(message);
	}
}