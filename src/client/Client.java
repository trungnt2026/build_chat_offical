package client;

import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Client {
	
	public static void main (String[] args) {
		
		try {
			
			Socket socket = new Socket("localhost", 5000);
			System.out.println("Da ket noi toi Server!");
			
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
						
			BufferedReader input = new BufferedReader (
					new InputStreamReader(socket.getInputStream())
					);
			
			Scanner scanner = new Scanner(System.in);
			
			System.out.println("Nhap tin nhan: ");
			String message = scanner.nextLine();
			
			output.println(message);
			
			String reply = input.readLine();
			System.out.println("Server: " + reply);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}