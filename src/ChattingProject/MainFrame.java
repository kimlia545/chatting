package ChattingProject;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;


public class MainFrame extends JFrame{
	
	//현재 페이지 상태를 알리기 위한 상수와 변수
	static final int LOGIN = 1;
	static final int WAITTING_ROOM = 2;
	static final int CHAT_ROOM = 3;
	int pageState = 0;
	boolean exit = false;
	
	Login login;
	WaitingPage wait;
	ChattingPage chat;
	JPanel mfPan;
	
	String id;
	
	MainFrame(){
		setSize(1024,768);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		login = new Login(this);
		wait = new WaitingPage(this);
		//chat = new ChattingPage();
		
		mfPan = new JPanel();
		mfPan.add(login);
		mfPan.setLayout(new BorderLayout());
		pageState = LOGIN;
		
		addListeners();
		
		add(mfPan);
		setVisible(true);
	}
	
	public void closePanel(){
		switch(pageState){
			case LOGIN:
				//로그인에서 웨이팅룸으로 넘어갈 때 실행
				mfPan.remove(login);
				exit = true;
				wait.connect(login.getID(), login.getIP());
				id = login.getID();
				mfPan.add(wait);
				mfPan.revalidate();
				mfPan.repaint();
				pageState = WAITTING_ROOM;
				break;
			case WAITTING_ROOM:
				//웨이팅룸 > 로그인 & 웨이팅룸 > 챗룸
				wait.setVisible(false);
				mfPan.add(chat);
				mfPan.revalidate();
				mfPan.repaint();	
				pageState = CHAT_ROOM;
				
				break;
			case CHAT_ROOM:
				//챗룸 > 웨이팅룸
				mfPan.remove(chat);
				wait.setVisible(true);
				wait.removeChatroom();
				mfPan.revalidate();
				mfPan.repaint();	
				pageState = WAITTING_ROOM;
				break;
			default:
				System.out.println("Panel close State Error");
		}
	}//closePanel
	
	private void addListeners() {
		addWindowListener(new WindowAdapter(){	
			@Override
			public void windowClosing(WindowEvent e){
				ObejctChatData data = new ObejctChatData();
				data.setMsg("/quit");
				if(exit==true){
				try {
					wait.oos.writeObject(data);
					wait.oos.flush();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				}						
			}		
		});
	}
	
	public static void main(String args[]){
		new MainFrame();
	}
}


