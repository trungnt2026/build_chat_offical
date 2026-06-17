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

	public ClientHandler(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(
					new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));

			output = new PrintWriter(new java.io.OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8),
					true);

			String inputName = input.readLine();
			name = (inputName != null) ? inputName.trim() : ""; //tránh user gõ dư dấu cách
			
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

						String newName = parts[1].trim();
						
						if (newName.isEmpty()) {
							sendMessage("Ten khong duoc de trong! Vui long nhap ten khac.");
							continue;
						}
						
						if (Server.isNameExists(newName)) {
							
							sendMessage("Ten da ton tai, vui long chon ten khac!");
							continue;
						}
						
						String oldName = name;
						
						name = newName;

						System.out.println(oldName + " da doi ten thanh: " + newName);

						Server.broadcast("[Thong bao] " + oldName + " da doi ten thanh: " + newName, this);

						sendMessage("Ban da doi ten thanh: " + newName);

					} else {
						sendMessage("Sai cu phap! Dung: /rename ten_moi");
					}

					continue;

				}

				if (message.startsWith("/creategroup")) {

					String[] parts = message.split(" ", 2);

					if (parts.length == 2) {

						String groupName = parts[1].trim();

						Server.createGroup(groupName, this);
					} else {
						sendMessage("Sai cu phap! Dung: /creategroup ten_nhom");
					}

					continue;
				}

				if (message.startsWith("/join")) {

					String[] parts = message.split(" ", 2);

					if (parts.length == 2) {
						String groupName = parts[1].trim();

						Server.joinGroup(groupName, this);
					} else {
						sendMessage("Sai cu phap! Dung: /join ten_nhom");
					}
					continue;
				}

				if (message.startsWith("/invite")) {

					String[] parts = message.split(" ", 3);

					if (parts.length == 3) {

						String userName = parts[1].trim();

						String groupName = parts[2].trim();

						Server.inviteToGroup(userName, groupName, this);
					} else {
						sendMessage("Sai cu phap! Dung: /invite ten_nguoi_dung ten_nhom");
					}
					continue;
				}
				
				if (message.equalsIgnoreCase("/groups")) {

					sendMessage(Server.getGroups());

					continue;
				}

				if (message.startsWith("/group ")) {

					String[] parts = message.split(" ", 3);

					if (parts.length == 3) {

						try {

							int groupId = Integer.parseInt(parts[1]);

							String content = parts[2].trim();

							Server.groupMessageById(groupId, content, this);
						} catch (NumberFormatException e) {
							sendMessage("Id nhom phai la so");
						}
					} else {
						sendMessage("Sai cu phap! Dung: /group id_nhom noi_dung");
					}
					continue;
				}
				
				if (message.startsWith("/pm ")) {

					String[] parts = message.split(" ", 3);

					if (parts.length == 3) {
						String receiver = parts[1].trim();
						String content = parts[2].trim();

						Server.privateMessage(receiver, content, this);

					} else {
						sendMessage("Sai cu phap! Dung: /pm ten nguoi_nhan noi_dung");
					}
					continue;	
				} 
				
				if (message.equals("/help")) {
					sendMessage(
							"===============List==============\n"
							+ "'/rename ten_moi'\n"
							+ "   -> Doi ten moi.\n\n"
							
							+ "'/online'\n"
							+ "   -> Xem danh sach online.\n\n"
							
							+ "/pm ten_nguoi_nhan noi_dung\n"
							+ "   -> Gui tin nhan rieng.\n\n"
									
							+ "/creategroup ten_nhom\n"
							+ "   -> Tao nhom chat moi.\n\n"
							
							+ "/join ten_nhom\n"
							+ "   -> Tham gia nhom chat moi.\n\n"

                            + "'/groups'\n"
                            + "   -> Xem danh sach nhom.\n\n"
                            
							+ "/invite ten_nguoi_nhan ten_nhom\n"
							+ "   -> Moi mot nguoi vao nhom.\n\n"

							+ "/group id_nhom noi_dung\n"
							+ "   -> Gui tin nhan vao nhom.\n\n"

							+ "'/exit'\n"
							+ "   -> Thoat khoi he thong.\n\n"
							
							+ "'/help'\n"
							+ "   -> Xem danh sach lenh.\n\n"
							
							+ "=============================="
					);
					continue;	
				}
				
				message = message.trim();
				if (message.startsWith("/")) {
						sendMessage("[He thong] Lenh khong hop le hoac sai cu phap. Go /help de xem danh sach lenh.");
						
						
					} else {

						System.out.println(name + ": " + message);

						Server.broadcast(name + ": " + message, this);
					}
				}
			
		} catch (Exception e) {
			System.out.println(name + " mat ket noi");
		} finally {
			
			Server.clients.remove(this);
			
			Server.broadcast(
					"[Thong bao]: " + name + " da offline", this);
			
			try {
				client.close();
			} catch (Exception ex) {
				
			}
		}

	}

	public void sendMessage(String message) {
		output.println(message);
	}

	public String getClientName() {
		return name;
	}

}