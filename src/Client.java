import java.io.*;
import java.util.*;
import java.net.*;


public class Client {
	public static void main(String args[]) {
		try {
			System.out.println("Ŭ���̾�Ʈ ����");
			Socket socket = new Socket("127.0.0.1",4000);
			//������ �ݵ�� �������� ����ǰ� �־�� �Ѵ�
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Unit order = new Unit();
			order.setCode("�ڵ�");
			order.setSize(100);
			ArrayList data = new ArrayList();
			data.add("�ڹ�");
			data.add("��");
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
