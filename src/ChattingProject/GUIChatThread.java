package ChattingProject;

import java.io.*;
import java.net.Socket;
import java.util.*;

class GUIChatThread extends Thread{			
	GUIChatServer server;
	private Socket sock;
	private String id;
	private HashMap<String, ObjectOutputStream> hm;		
	private boolean initFlag = false;	
	String line = null;
	private ObjectInputStream ois;	
	private ObejctChatData data;
	
	public GUIChatThread(Socket sock, HashMap<String, ObjectOutputStream> hm, GUIChatServer server){		
		this.server = server;
		this.sock = sock;
		this.hm = hm;
		try{	
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());	
			ois = new ObjectInputStream(sock.getInputStream());
			
			ObejctChatData data =(ObejctChatData) ois.readObject(); 
			id = data.getMsg();
			
			synchronized(hm){
				hm.put(id, oos);
			}
			
			server.model.addRow(new String[]{id,"guest"});
			
			broadcast(id + "님이 접속하였습니다.");	
			server.chat_ta.append("접속한 사용자의 아이디는 " + id + "입니다.\n");
			server.chat_ta.setCaretPosition(server.chat_ta.getDocument().getLength());
			server.nowConnectUser = server.model.getRowCount();
			server.user_lb_num.setText(server.nowConnectUser + "");

		}catch(Exception ex){		
			System.out.println("server thread constructor: " + ex);	
		}		
	} // 생성자			
	
	public void run(){			
		try{		
			
			boolean keepGoing = true;
			while(keepGoing) {				
	        	 try {
	                 data =  (ObejctChatData) ois.readObject();
	             }
	             catch (IOException e) {
	                 System.out.println(id + " Exception reading Streams: " + e);
	                 break;             
	             }
	        	line = data.getMsg();	        	
	        	String array[] = line.split("/");
	        	
				if(line.equals("/quit")){
					break;
				}
				if(line.indexOf("/to ") == 0){					
					sendmsg(line);
				}
				else if(array[0].equals("mkroom")){
					server.addRoom(array[1], this);
					server.makeGuestlistRoom(array[1]);
					server.makeRoomlist();
				}else if(array[0].equals("roomout")){
					//클라이언트에서 보내는 roomout 메세지
					//:: roomout/나가는유저의아이디/어느방인지
					
					broadcastRoom(array[2], "roomMsg/"+array[1]+"님이 채팅방에서 나갔습니다.");
					server.removeRoom(array[2]);
					server.removeGuestRoom(array[2], this);
				}else if(array[0].equals("roomjoin")){
					//클라이언트에서 보내는 roomjoin 메세지
					//:: roomjoin/들어가는유저의아이디/어느방인지
					server.addGuestRoom(array[2], this); // 그 방에 자기를 넣음
					server.makeGuestlistRoom(array[2]); // 방에 참여한 접속자리스트 생성
				}
				else if(array[0].equals("roomMsg")){
					//클라이언트에서 보내는 채팅룸 내의 메세지
					//:: roomMsg/어느방인지/메세지내용
					broadcastRoom(array[1], "roomMsg/" + id + " : " + array[2]);
				}
				else {					
					broadcast(id + " : " + line);
				}				
			}				
		}catch(Exception ex){		
			System.out.println(id + ":" + "server thread run : " + ex);		
		}finally{
			synchronized(hm){
				try {
					ObjectOutputStream oos = hm.remove(id);
					ObejctChatData data = new ObejctChatData();
					data.setMsg("/quit");
					oos.writeObject(data);
					oos.flush();
				} catch (IOException e) {
						e.printStackTrace();
					}	
			}			
			String info = id + " 님이 접속 종료하였습니다.";	
			
			for(int i=0;i<server.model.getRowCount();i++){
				if(server.model.getValueAt(i, 0).equals(id)){
					server.model.removeRow(i);
				}
			}

			broadcast(info);
			server.chat_ta.append(info + "\n");
			server.chat_ta.setCaretPosition(server.chat_ta.getDocument().getLength());
			server.nowConnectUser = server.model.getRowCount();
			server.user_lb_num.setText(server.nowConnectUser + "");
			/*server.user_lb_num.setText(server.nowConnectUser + "");
			server.pan2.revalidate();
			server.pan2.repaint();*/
			try{		
				if(sock != null) {	
					sock.close();
				}
			}catch(Exception ex){}		
		}			
	} // run	
	
	// 운영자 귓속말
		public void sendmsg(String msg, String id){
			ObjectOutputStream oos = getObjectOutputStreamById(id);
			if(oos != null){
				try {
					ObejctChatData data = new ObejctChatData();
					data.setMsg("[운영자]님이 다음의 귓속말을 보내셨습니다. :" + msg);
					oos.writeObject(data);
					oos.flush();	
				} catch (IOException e) {
					e.printStackTrace();
				}	
				
			}
		}
		
		
	// /to id message
	public void sendmsg(String msg){			
		int start = msg.indexOf(" ") +1;			
		int end = msg.indexOf(" ", start);			
		
		if(end != -1){			
			String to = msg.substring(start, end);		
			String msg2 = msg.substring(end+1);		
			ObjectOutputStream oos = hm.get(to);							
			if(oos != null){			
				try{
					ObejctChatData data = new ObejctChatData();
					data.setMsg(id + " 님이 다음의 귓속말을 보내셨습니다. :" + msg2);
					oos.writeObject(data);
					oos.flush();	
				}catch (IOException e) {
					e.printStackTrace();
				}	
			}
			
			try {
				oos = hm.get(id);
				ObejctChatData data = new ObejctChatData();
				data.setMsg(id + " 님께 다음의 귓속말을 보냈습니다. :" + msg2);
				oos.writeObject(data);
				oos.flush();
			} catch (IOException e) {				
				e.printStackTrace();
			}
			
		}		
	} // sendmsg	
		
	public void broadcast(String msg){		
		synchronized(hm){		
			Collection<ObjectOutputStream> collection = hm.values();	
			Iterator<ObjectOutputStream> iter = collection.iterator();	
			
			while(iter.hasNext()){	
				
				try {
					ObjectOutputStream oos = iter.next();
					ObejctChatData data = new ObejctChatData();
					data.setMsg(msg);
					Vector varg0 = new Vector();
					Vector varg1 = new Vector();
					for(int i=0;i<server.model.getRowCount();i++){
						varg0.add(server.model.getValueAt(i, 0));
						varg1.add(server.model.getValueAt(i, 1));
					}
					
					Set tempSet = server.roomHm.keySet();
					Iterator iterator = tempSet.iterator();

					Vector roomName = new Vector();
					
					while(iterator.hasNext()){
						roomName.add((String)iterator.next());
					}
					
					data.setVarg0(varg0);
					data.setVarg1(varg1);	
					data.setRoomNameVector(roomName);	
					oos.writeObject(data);
					oos.flush();
				} catch (IOException e) {					
					e.printStackTrace();
				}		
			}	
		}		
	} // broadcast			
	
	void broadcastRoom(String rn, String msg) throws Exception {
		//메세지를 보내고자 하는 사용자의 이름을 받아와
		//그 이름이 속한 채팅방의 리스트(=접속자)를 받아온다
		ArrayList<GUIChatThread> list2 = server.roomHm.get(rn);
		
		//해당 방의 접속자들에게만
		for (GUIChatThread g : list2) {
			//메세지를 보낸다.
			try {
				ObjectOutputStream oos = hm.get(g.id);
				ObejctChatData data = new ObejctChatData();
				data.setMsg(msg);
				
				Vector varg0 = new Vector();
				Vector varg1 = new Vector();
				for(int i=0;i<server.model.getRowCount();i++){
					varg0.add(server.model.getValueAt(i, 0));
					varg1.add(server.model.getValueAt(i, 1));
				}
				data.setVarg0(varg0);
				data.setVarg1(varg1);
				
				Set tempSet = server.roomHm.keySet();
				Iterator iterator = tempSet.iterator();

				Vector roomName = new Vector();
				
				while(iterator.hasNext()){
					roomName.add((String)iterator.next());
				}
				
				data.setRoomNameVector(roomName);	
				
				oos.writeObject(data);
				oos.flush();
			} catch (IOException e) {					
				e.printStackTrace();
			}	
			//msg/유저이름/내용
		}
	}
	
	public ObjectOutputStream getObjectOutputStreamById(String id){
		ObjectOutputStream oos = hm.get(id);
		return oos;
	}
}			
