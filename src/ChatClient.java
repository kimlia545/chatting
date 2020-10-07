import java.net.*;			
import java.io.*;			
import javax.swing.*;			
public class ChatClient {
	private Socket sock = null;	
	private BufferedReader br = null;	
	private PrintWriter pw = null;
	public ChatClient() {
		String ip = JOptionPane.showInputDialog("접속할 IP를 입력하세요");
		String id = JOptionPane.showInputDialog("사용할 ID를 입력하세요");
		if(ip.length() == 0 || id.length() == 0) {
			System.out.println("IP와 ID를 제대로 입력하지 않아 프로그램을 종료합니다.");
			System.exit(-1);
		}
		try{	
			sock = new Socket(ip, 10001);
			pw = new PrintWriter(
				new OutputStreamWriter(sock.getOutputStream()));		
			br = new BufferedReader(
				new InputStreamReader(sock.getInputStream()));		
			BufferedReader keyboard = new BufferedReader(
				new InputStreamReader(System.in)
			);	
			



			// 사용자의 id를 전송한다.		
			pw.println(id);		
			pw.flush();		
			new Thread() {
				public void run() {
					try {
						// 사용자 메세지 읽기
						String line = null;		
						while((line = br.readLine()) != null){		
							if(line.equals("/quit")) {
								throw new Exception();
							}
							System.out.println(line);	
						}			
					} catch(Exception e) {
					} finally {
						exit();
					}
				} 
			}.start();		
			String line = null;		
			// 키보드입력 읽기
			// /quit
			while((line = keyboard.readLine()) != null){		
				pw.println(line);	
				pw.flush();					
			}				
		}catch(Exception e){			
			System.out.println(e);	
		}finally{			
			exit();
		} // finally	
	}
	private void exit() {
		try{		
			if(pw != null) {	
				pw.close();					
			}
		}catch(Exception ex){}		
		try{		
			if(br != null) 	
				br.close();
		}catch(Exception ex){}		
		try{		
			if(sock != null)	
				sock.close();
		}catch(Exception ex){}	
		
		System.out.println("클라이언트의 접속을 종료합니다.");	
		System.exit(0);
	}
	public static void main(String[] args) {		
		new ChatClient();			
	} // main				
} // class					
























