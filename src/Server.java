import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	public static void main(String args[]) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		ObjectInputStream ois = null;
		PrintWriter pw= null;
		
		try{
			serverSocket = new ServerSocket(4000);
			System.out.println("��������");
			
			while(true){
				System.out.println("������ ��ٸ��ϴ�.");
				socket = serverSocket.accept();
				
				//���� ���� ����, Ŭ���̾�Ʈ ���� ��ٸ�
				//Ŭ���̾�Ʈ ���ӽ� ���� ���·� �Ѿ
				System.out.println("Ŭ���̾�Ʈ ���ӵ�");
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));;
				//Ŭ���̾�Ʈ�� ������ ��Ʈ��
				ois = new ObjectInputStream(
						socket.getInputStream());
				//Ŭ���̾�Ʈ�κ��� �޾ƿ� ��Ʈ�� 
				Unit order = (Unit) ois.readObject();
				System.out.println(order.getCode());
				System.out.println(order.getSize());
				ArrayList data = order.getData();
				System.out.println(data.get(0));
				System.out.println(data.get(1));	
				
				pw.write("okay");
				pw.close();
				socket.close();
				
			}
		}catch(Exception e){
			
		}
		
	}
}
