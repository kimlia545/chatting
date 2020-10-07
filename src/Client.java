import java.io.*;
import java.util.*;
import java.net.*;


public class Client {
	public static void main(String args[]) {
		try {
			System.out.println("클라이언트 시작");
			Socket socket = new Socket("127.0.0.1",4000);
			//소켓은 반드시 서버부터 실행되고 있어야 한다
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Unit order = new Unit();
			order.setCode("코드");
			order.setSize(100);
			ArrayList data = new ArrayList();
			data.add("자바");
			data.add("웹");
			order.setData(data);
			oos.writeObject(order);
			oos.flush();
			
			String msg = br.readLine();
			System.out.println(msg);
			oos.close();
			socket.close();
			
			
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
