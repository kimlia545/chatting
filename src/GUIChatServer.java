import java.net.*;				
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;				
import java.util.*;	
import java.util.concurrent.BrokenBarrierException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
				
public class GUIChatServer extends JFrame implements ActionListener{
	JTextArea textarea;
	JScrollPane taScroll;
	JTable table;
	DefaultTableModel model;
	JScrollPane tableScorll;
	
	JTextField inputField;
	JButton sendBtn;
	JPanel southPanel;
	
	JPopupMenu popMenu;
	JMenuItem itemWhisper;
	JMenu itemSetLevel;
	JMenuItem selLevel1;
	JMenuItem selLevel2;
	JMenuItem selLevel3;
	
	JMenuItem itemBan;
	int selectedRow;
	GUIChatThread activeThread;
	
	GUIChatServer(){
		super("ä�� ����");
		guiInit();
		serverInit();				
	}
	public void guiInit(){
		this.setSize(500,700);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		textarea = new JTextArea(20,25);
		taScroll = new JScrollPane(textarea);
		
		String [][] contents={};
		String [] header = {"������","���"};
		model = new DefaultTableModel(contents, header);
		table = new JTable(model);
		tableScorll = new JScrollPane(table);		
		tableScorll.setPreferredSize(new Dimension(200, 700));
		
		popMenu = new JPopupMenu();
		itemWhisper = new JMenuItem("�ӼӸ�");
		itemSetLevel = new JMenu("�������");
		
		selLevel1 = new JMenuItem("����1");
		selLevel2 = new JMenuItem("����2");
		selLevel3 = new JMenuItem("����3");
		
		itemSetLevel.add(selLevel1);
		itemSetLevel.add(selLevel2);
		itemSetLevel.add(selLevel3);
		
		selLevel1.addActionListener(this);
		selLevel2.addActionListener(this);
		selLevel3.addActionListener(this);
		
		itemBan = new JMenuItem("����");		
		
		itemWhisper.addActionListener(this);				
		itemBan.addActionListener(this);
		popMenu.add(itemWhisper);
		popMenu.add(itemSetLevel);
		popMenu.add(itemBan);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
					popMenu.show(GUIChatServer.this, e.getX()+300, e.getY());
					selectedRow = table.rowAtPoint(e.getPoint());					
			}
		});
		
		
		southPanel  = new JPanel();
		inputField = new JTextField(30);
		sendBtn = new JButton("���� ����");
		inputField.addActionListener(this);
		sendBtn.addActionListener(this);
		
		southPanel.add(inputField);
		southPanel.add(sendBtn);
		
		
		this.add(southPanel,"South");
		
		this.add(tableScorll,"East");
		this.add(taScroll,"Center");
		this.setVisible(true);
	}
	public void serverInit(){
		try{		
			ServerSocket server = new ServerSocket(10001);
			textarea.append("������ ��ٸ��ϴ�.\n");
			HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
			while(true){	
				Socket sock = server.accept();
				GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model);
				activeThread = chatthread;
				chatthread.start();
				
			} // while			
		}catch(Exception e){	
			System.out.println("server main : " + e);
		}	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==itemWhisper){
			String msg = JOptionPane.showInputDialog(this, "�ӼӸ��� �Է��ϼ���");
			activeThread.sendmsg(msg, model.getValueAt(selectedRow, 0)+"");
		}else if(e.getSource()==selLevel1){
			model.setValueAt("����1", selectedRow, 1);
			textarea.append("[����] "+model.getValueAt(selectedRow, 0)+"���� ����1 �� ���� �Ǿ����ϴ�.\n");			
			activeThread.broadcast("[����] "+model.getValueAt(selectedRow, 0)+"���� ����1 �� ���� �Ǿ����ϴ�.");
		}else if(e.getSource()==selLevel2){
			model.setValueAt("����2", selectedRow, 1);
			textarea.append("[����] "+model.getValueAt(selectedRow, 0)+"���� ����2 �� ���� �Ǿ����ϴ�.\n");			
			activeThread.broadcast("[����] "+model.getValueAt(selectedRow, 0)+"���� ����2 �� ���� �Ǿ����ϴ�.");
		}else if(e.getSource()==selLevel3){
			model.setValueAt("����3", selectedRow, 1);
			textarea.append("[����] "+model.getValueAt(selectedRow, 0)+"���� ����3 �� ���� �Ǿ����ϴ�.\n");			
			activeThread.broadcast("[����] "+model.getValueAt(selectedRow, 0)+"���� ����3 �� ���� �Ǿ����ϴ�.");
		}else if(e.getSource()==itemBan){
			textarea.append("[����] "+model.getValueAt(selectedRow, 0)+"���� �����Ͽ����ϴ�.\n");			
			PrintWriter pw = activeThread.getPrintWriterById(model.getValueAt(selectedRow, 0)+"");
			model.removeRow(selectedRow);
			pw.println("/ban");
			pw.flush();
		}else if(e.getSource()==inputField){
			textarea.append("[����] "+inputField.getText()+"\n");
			activeThread.broadcast("[����] "+inputField.getText());
		}else if(e.getSource()==sendBtn){
			textarea.append("[����] "+inputField.getText()+"\n");
			activeThread.broadcast("[����] "+inputField.getText());
		}		
	}
	public static void main(String[] args) {			
		new GUIChatServer();
	} // main
}			
			
class GUIChatThread extends Thread{			
	private Socket sock;		
	private String id;		
	private JTextArea ta;
	private DefaultTableModel model;
	private BufferedReader br;		
	private HashMap<String, PrintWriter> hm;		
	private boolean initFlag = false;		
	String line = null;
	public GUIChatThread(Socket sock, HashMap<String, PrintWriter> hm, JTextArea ta, DefaultTableModel model){		
		this.sock = sock;	
		this.hm = hm;
		this.ta = ta;
		this.model = model;		
		try{	
			PrintWriter pw = 
				new PrintWriter(new OutputStreamWriter(
				sock.getOutputStream()));	
			
			br = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));	
			
			id = br.readLine();	
			broadcast(id + "���� �����Ͽ����ϴ�.");	
			ta.append("������ ������� ���̵�� " + id + "�Դϴ�.\n");
			model.addRow(new String[]{id,"guest"});
			ta.setCaretPosition(ta.getDocument().getLength());
			synchronized(hm){	
				hm.put(id, pw);
			}
		}catch(Exception ex){		
			System.out.println("server thread constructor: " + ex);	
		}		
	} // ������			
	public void run(){			
		try{		
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
			String info = id + " ���� ���� �����Ͽ����ϴ�.";			
			
			for(int i=0;i<model.getRowCount();i++){
				if(model.getValueAt(i, 0).equals(id)){
					model.removeRow(i);
				}
			}			
			
			broadcast(info);
			ta.append(info + "\n");
			ta.setCaretPosition(ta.getDocument().getLength());
			
			try{		
				if(sock != null) {	
					sock.close();
				}
			}catch(Exception ex){}		
		}			
	} // run
	
	public PrintWriter getPrintWriterById(String id){
		PrintWriter pw = hm.get(id);
		return pw;
	}
	
	// admin wisper method
	public void sendmsg(String msg, String id){
		PrintWriter pw = getPrintWriterById(id);
		if(pw != null){						
			pw.println("[���]���� ������ �ӼӸ��� �����̽��ϴ�. :" + msg);	
			pw.flush();	
		}
	}
	
	// /to id message
	public void sendmsg(String msg){			
		int start = msg.indexOf(" ") +1;			
		int end = msg.indexOf(" ", start);			
		if(end != -1){			
			String to = msg.substring(start, end);		
			String msg2 = msg.substring(end+1);		
			PrintWriter pw = hm.get(to);							
			if(pw != null){						
				pw.println(id + " ���� ������ �ӼӸ��� �����̽��ϴ�. :" + msg2);	
				pw.flush();	
			} // if	
			pw = hm.get(id);
			pw.println(id + " �Բ� ������ �ӼӸ��� ���½��ϴ�. :" + msg2);
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




























