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
				
				PrintWriter output = new PrintWriter (
						client.getOutputStream(), true);
				
				String message = input.readLine();
				
				System.out.println("Client gui: " + message);
				
				output.println("Server da nhan duoc tin nhan cua ban");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}