import java.net.*;				
import java.io.*;				
import java.util.*;				
				
public class ChatServer {
	public static void main(String[] args) {			
		try{		
			ServerSocket server = new ServerSocket(10001);	
			System.out.println("접속을 기다립니다.");	
			HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
			while(true){	
				Socket sock = server.accept();
				ChatThread chatthread = new ChatThread(sock, hm);
				chatthread.start();
			} // while			
		}catch(Exception e){	
			System.out.println("server main : " + e);
		}	
	} // main		
}			
			
class ChatThread extends Thread{			
	private Socket sock;		
	private String id;		
	private BufferedReader br;		
	private HashMap<String, PrintWriter> hm;		
	private boolean initFlag = false;		
	public ChatThread(Socket sock, HashMap<String, PrintWriter> hm){		
		this.sock = sock;	
		this.hm = hm;
		try{	
			PrintWriter pw = 
				new PrintWriter(new OutputStreamWriter(
				sock.getOutputStream()));	
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));	
			id = br.readLine();	
			broadcast(id + "님이 접속하였습니다.");	
			System.out.println("접속한 사용자의 아이디는 " + id + "입니다.");	
			synchronized(hm){	
				hm.put(id, pw);
			}
		}catch(Exception ex){		
			System.out.println("server thread constructor: " + ex);	
		}		
	} // 생성자			
	public void run(){			
		try{		
			String line = null;	
			while((line = br.readLine()) != null){						
				if(line.equals("/quit"))	
					break;
				if(line.indexOf("/to ") == 0){					
					sendmsg(line);
				}else {					
					broadcast(id + " : " + line);
				}
			}		
		}catch(Exception ex){			
			System.out.println("server thread run : " + ex);		
		}finally{			
			synchronized(hm){
				PrintWriter pw = hm.remove(id);
				pw.println("/quit");	
				pw.flush();
			}			
			String info = id + " 님이 접속 종료하였습니다.";
			broadcast(info);
			System.out.println(info);
			try{		
				if(sock != null) {	
					sock.close();
				}
			}catch(Exception ex){}		
		}			
	} // run	
	
	// /to id message
	public void sendmsg(String msg){			
		int start = msg.indexOf(" ") +1;			
		int end = msg.indexOf(" ", start);			
		if(end != -1){			
			String to = msg.substring(start, end);		
			String msg2 = msg.substring(end+1);		
			PrintWriter pw = hm.get(to);							
			if(pw != null){						
				pw.println(id + " 님이 다음의 귓속말을 보내셨습니다. :" + msg2);	
				pw.flush();	
			} // if	
			pw = hm.get(id);
			pw.println(id + " 님께 다음의 귓속말을 보냈습니다. :" + msg2);
			pw.flush();
		}		
	} // sendmsg			
	public void broadcast(String msg){			
		synchronized(hm){		
			Collection<PrintWriter> collection = hm.values();	
			Iterator<PrintWriter> iter = collection.iterator();	
			while(iter.hasNext()){				
				PrintWriter pw = iter.next();
				pw.println(msg);
				pw.flush();				
			}	
		}		
	} // broadcast			
}			




























