package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	
		public static void main (String[] args) {
			try {
				ServerSocket server = new ServerSocket(5000);
				System.out.println("Server da khoi dong tai cong 5000");
				
				Socket client = server.accept();
				System.out.println("Da co Client ket noi");
				
				BufferedReader input = new BufferedReader (
						new InputStreamReader (client.getInputStream())
						);
				
				PrintWriter output = new PrintWriter (client.getOutputStream(), true);
				
				Scanner scanner = new Scanner(System.in);

				//Thread nhan tin tu client
				
				Thread receiveThread = new Thread(() -> {
					try {
						String clientMessage;
						
						while ((clientMessage = input.readLine()) != null) {
							if (clientMessage.equalsIgnoreCase("/exit")) {
								System.out.println("Client da thoat");
								break;
							}
							
							System.out.println("Client gui: " + clientMessage);
						}	
					}catch (Exception e) {
						e.printStackTrace();
					}
				});
				
				//Thread gui tin cho Client
				Thread sendThread = new Thread (() -> {
					try {
						while (true) {
							String serverMessage = scanner.nextLine();
							
							output.println(serverMessage);
							
							if (serverMessage.equalsIgnoreCase("/exit")) {
								System.out.println("Server da thoat");
								break;
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
						
						);
				
				receiveThread.start();
				sendThread.start();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}