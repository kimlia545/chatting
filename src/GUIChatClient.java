
import java.awt.*;		
import java.awt.event.*;		

import java.net.*;		
import java.io.*;		

import javax.swing.*;
import javax.swing.border.*;

public class GUIChatClient extends JFrame implements ActionListener{		
	
	public static final int NORMAL = 0;
	public static final int EXCEPTIONAL = -1;

	private JTextField input;
	private JTextArea display;
	private BufferedReader br;
	private PrintWriter pw;
	private Socket sock;	

	public GUIChatClient(){	
		super("ä�� Ŭ���̾�Ʈ");
		init();
		connect();
		setDisplay();
		addListeners();
		showFrame();		
	}		
	
	private void init() {
		input = new JTextField();	
		input.setBorder(new TitledBorder("Input"));
		display = new JTextArea();
		display.setEditable(false);		
	}

	private void setDisplay() {
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(new JScrollPane(display));
		pnlCenter.setBorder(new TitledBorder("Chat"));
		add(pnlCenter, BorderLayout.CENTER);	
		add(input, BorderLayout.SOUTH);	
	}
	private void addListeners() {
		input.addActionListener(this);
		addWindowListener(new WindowAdapter(){	
			@Override
			public void windowClosing(WindowEvent e){
				pw.println("/quit");	
				pw.flush();					
			}		
		});
	}

	private void showFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(500, 500);	
		setResizable(false);
		setVisible(true);			
		input.requestFocus();
	}

	private void connect() {
		String ip = null;
		do {
			ip = JOptionPane.showInputDialog(this, "�����Ǹ� �Է��Ͻÿ�","127.0.0.1");
		} while(ip == null || ip.equals(""));
		String id = null;
		do {
			id = JOptionPane.showInputDialog(this, "�г����� �Է��Ͻÿ�","a"+((int)(Math.random()*1000)));
		} while(id == null || id.equals(""));
		
		try{	
			sock = new Socket(ip, 10001);
			pw = new PrintWriter(
				new OutputStreamWriter(
					sock.getOutputStream()
				)
			);
			
			br = new BufferedReader(
				new InputStreamReader(
					sock.getInputStream()
				)
			);
			pw.println(id.trim());	
			pw.flush();	
			WinInputThread wit = new WinInputThread();	
			wit.start();
		}catch(Exception e){	
			System.out.println("������ ���ӽ� ������ �߻��Ͽ����ϴ�.");
			System.out.println(e);
			System.exit(EXCEPTIONAL);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		if(e.getSource() == input){			
			String msg = input.getText();		
			pw.println(msg);		
			pw.flush();			
			input.selectAll();		
			input.requestFocus();		
		}			
	} 
			
	class WinInputThread extends Thread{				
		public void run(){			
			try{						
				String line = null;	
				while((line = br.readLine()) != null){					
					if(line.equals("/quit")) {
						throw new Exception();
					}else if(line.equals("/ban")) {
						break;
					}else{
					display.append(line + "\n");
					
					
					// Ŀ�� ��ġ ����(��ũ�ѹ���)
					display.setCaretPosition(
						display.getDocument().getLength()	
					);
					}
				}
				JOptionPane.showMessageDialog(GUIChatClient.this, "�濡�� ���� ���ϼ̽��ϴ�.");
			}catch(Exception e){	
				System.out.println("client thread : " + e);
				JOptionPane.showMessageDialog(GUIChatClient.this, "���α׷��� �����մϴ�.");
			}finally{			
				try{		
					if(br != null) br.close();
				}catch(Exception e){}	
				try {
					if(pw != null) pw.close();
				} catch(Exception e) {}
				try{		
					if(sock != null) sock.close();
				}catch(Exception e){}					
				System.exit(NORMAL);
			}			
		} // run end				
	} // WinInputThread end	
	
	public static void main(String[] args) {
		new GUIChatClient();		
	}

} // GUIChatClient end
						
