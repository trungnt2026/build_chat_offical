package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
		public static void main (String[] args) {
			try {
				ServerSocket server = new ServerSocket(5000);
				System.out.println("Server da khoi dong tai cong 5000");
				
				Socket client = server.accept();
				System.out.println("da co Client ket noi");
				
				BufferedReader input = new BufferedReader (
						new InputStreamReader (client.getInputStream())
				);
				
				PrintWriter output = new PrintWriter (client.getOutputStream(), true);
				
				String message;
				
				while (true) {
					message = input.readLine();
					
					if (message == null) {
						System.out.println("Client da ngat ket noi");
						break;
					}
					
					if (message.equalsIgnoreCase("/exit")) {
						System.out.println("Client da thoat");
						output.println("Server: Tam biet!");
						break;
					}
					
					System.out.println("Client gui: " + message);
					output.println("Server da nhan: " + message);
				}
				
				client.close();
				server.close();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}