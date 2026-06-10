package client;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class Client {
	
	public static void main (String[] args) {
		
		try {
			
			Socket socket = new Socket("localhost", 5000);
			System.out.println("Da ket noi toi Server");
			
			PrintWriter output = new PrintWriter (
					new java.io.OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true
					);			
			
			BufferedReader input = new BufferedReader (
					new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
					);
			
			Scanner scanner = new Scanner(System.in);
			
			System.out.print("Nhap ten cua ban: ");
			String name = scanner.nextLine();
			output.println(name);
			
			//Thread nhan tin tu Server
			Thread receiveThread = new Thread (() -> {
				try {
					String serverMessage;
					
					while ((serverMessage = input.readLine()) != null) {
						if (serverMessage.equalsIgnoreCase("/exit")) {
							System.out.println("Server da thoat");
							break;
						}
						
						System.out.println(serverMessage);
						
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
					
					);
			
			//Thread gui tin cho Server
			Thread sendThread = new Thread(() -> {
				try {
					while (true) {
						String clientMessage = scanner.nextLine();
						
						output.println(clientMessage);
						
						if (clientMessage.equalsIgnoreCase("/exit")) {
							System.out.println(name + " da thoat");
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
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}