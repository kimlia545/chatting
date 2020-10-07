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
			System.out.println("서버시작");
			
			while(true){
				System.out.println("접속을 기다립니다.");
				socket = serverSocket.accept();
				
				//서버 중지 상태, 클라이언트 접속 기다림
				//클라이언트 접속시 다음 상태로 넘어감
				System.out.println("클라이언트 접속됨");
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));;
				//클라이언트로 내보낼 스트림
				ois = new ObjectInputStream(
						socket.getInputStream());
				//클라이언트로부터 받아올 스트림 
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
