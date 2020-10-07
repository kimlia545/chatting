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
			
			broadcast(id + "���� �����Ͽ����ϴ�.");	
			server.chat_ta.append("������ ������� ���̵�� " + id + "�Դϴ�.\n");
			server.chat_ta.setCaretPosition(server.chat_ta.getDocument().getLength());
			server.nowConnectUser = server.model.getRowCount();
			server.user_lb_num.setText(server.nowConnectUser + "");

		}catch(Exception ex){		
			System.out.println("server thread constructor: " + ex);	
		}		
	} // ������			
	
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
					//Ŭ���̾�Ʈ���� ������ roomout �޼���
					//:: roomout/�����������Ǿ��̵�/���������
					
					broadcastRoom(array[2], "roomMsg/"+array[1]+"���� ä�ù濡�� �������ϴ�.");
					server.removeRoom(array[2]);
					server.removeGuestRoom(array[2], this);
				}else if(array[0].equals("roomjoin")){
					//Ŭ���̾�Ʈ���� ������ roomjoin �޼���
					//:: roomjoin/���������Ǿ��̵�/���������
					server.addGuestRoom(array[2], this); // �� �濡 �ڱ⸦ ����
					server.makeGuestlistRoom(array[2]); // �濡 ������ �����ڸ���Ʈ ����
				}
				else if(array[0].equals("roomMsg")){
					//Ŭ���̾�Ʈ���� ������ ä�÷� ���� �޼���
					//:: roomMsg/���������/�޼�������
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
			String info = id + " ���� ���� �����Ͽ����ϴ�.";	
			
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
	
	// ��� �ӼӸ�
		public void sendmsg(String msg, String id){
			ObjectOutputStream oos = getObjectOutputStreamById(id);
			if(oos != null){
				try {
					ObejctChatData data = new ObejctChatData();
					data.setMsg("[���]���� ������ �ӼӸ��� �����̽��ϴ�. :" + msg);
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
					data.setMsg(id + " ���� ������ �ӼӸ��� �����̽��ϴ�. :" + msg2);
					oos.writeObject(data);
					oos.flush();	
				}catch (IOException e) {
					e.printStackTrace();
				}	
			}
			
			try {
				oos = hm.get(id);
				ObejctChatData data = new ObejctChatData();
				data.setMsg(id + " �Բ� ������ �ӼӸ��� ���½��ϴ�. :" + msg2);
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
		//�޼����� �������� �ϴ� ������� �̸��� �޾ƿ�
		//�� �̸��� ���� ä�ù��� ����Ʈ(=������)�� �޾ƿ´�
		ArrayList<GUIChatThread> list2 = server.roomHm.get(rn);
		
		//�ش� ���� �����ڵ鿡�Ը�
		for (GUIChatThread g : list2) {
			//�޼����� ������.
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
			//msg/�����̸�/����
		}
	}
	
	public ObjectOutputStream getObjectOutputStreamById(String id){
		ObjectOutputStream oos = hm.get(id);
		return oos;
	}
}			
