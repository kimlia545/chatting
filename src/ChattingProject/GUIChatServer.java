package ChattingProject;

import java.net.*;				
import java.awt.*;
import java.awt.event.*;
import java.io.*;				
import java.util.*;	

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
				
public class GUIChatServer extends JFrame implements MouseListener, ActionListener{
	
	//UI
	JTextArea chat_ta;
	JTable table;
	DefaultTableModel model;
	JScrollPane chat_ta_sp,tableScroll;
	JPanel pan1,pan2,pan3,pan4, pan5, pan6;
	JLabel ip_lb, user_lb,user_lb_num, notice_lb;
	JButton btn;
	JTextField notice_tf;	
	
	GUIChatThread activeThread;
	
	//���콺�˾�
	JPopupMenu pm;
	JMenuItem pm_item1,pm_item2;
	String select_id;
	
	// �� �����ڼ�
	int nowConnectUser = 0;
	JImagePanel jip = null;
	HashMap<String, ObjectOutputStream> hm ;
	
	//***���̸�+�����ڽ�����
	HashMap<String, ArrayList<GUIChatThread>> roomHm;
	
	//���̺� ��� ����
	DefaultTableCellRenderer align_center = new DefaultTableCellRenderer();
	
	GUIChatServer(){
		guiInit();
		serverInit();				
	}
	
	public void guiInit(){
		this.setSize(815,640);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}//�ڽ��� IP
		
		//���� �� �г�
		pan1 = new JPanel(new BorderLayout());
		pan1.setOpaque(false);
		pan1.setBackground(new Color(0,0,0,0));
		ip_lb = new JLabel("����IP : " + local.getHostAddress());
		ip_lb.setFont(new Font("Serif", Font.PLAIN, 15));
		ip_lb.setForeground(new Color(255,255,255));
		ip_lb.setBackground(new Color(0,0,0,0));
		chat_ta = new JTextArea(20,25);
		chat_ta.setEditable(false);
		chat_ta.setOpaque(false);
		chat_ta.setBackground(new Color(0,0,0,0));
		chat_ta.setForeground(new Color(255,255,255));	
		chat_ta.setFont(chat_ta.getFont().deriveFont(13.5f));
		chat_ta_sp = new JScrollPane(chat_ta);
		chat_ta_sp.setOpaque(false);
		chat_ta_sp.setBackground(new Color(0,0,0,0));
		chat_ta_sp.getViewport().setOpaque(false);
		chat_ta_sp.setForeground(new Color(255,255,255));
		chat_ta_sp.setFont(chat_ta_sp.getFont().deriveFont(15.0f));
		Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
		Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		chat_ta_sp.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		pan1.add(ip_lb,"North");pan1.add(chat_ta_sp,"Center");
		
		//������ �� �г�
		pan2 = new JPanel();
		pan2.setOpaque(false);
		pan2.setBackground(new Color(0,0,0,0));
		user_lb = new JLabel("�� �����ڼ�");
		user_lb.setFont(new Font("Serif", Font.PLAIN, 15));
		user_lb.setForeground(new Color(255,255,255));
		user_lb.setBackground(new Color(0,0,0,0));
		user_lb_num = new JLabel(nowConnectUser + "");
		user_lb_num.setForeground(new Color(255,255,255));
		user_lb_num.setBackground(new Color(0,0,0,0));
		pan2.add(user_lb); pan2.add(user_lb_num);
		
		//������ �߰� �г�
		pan3 = new JPanel(new BorderLayout());
		pan3.setOpaque(false);
		pan3.setBackground(new Color(0,0,0,0));
		pan4 = new JPanel();
		pan4.setPreferredSize(new Dimension(100,400));
		String [][] contents={};
		String [] header = {"������","���"};
		model = new DefaultTableModel(contents, header);
		table = new JTable(model);
		table.setRowHeight(24);
		table.setForeground(Color.white);
		align_center.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm1 = table.getColumnModel() ;
		tcm1.getColumn(0).setCellRenderer(align_center); 
		tcm1.getColumn(1).setCellRenderer(align_center); 
		JTableHeader head = table.getTableHeader();
	    head.setBackground(new Color(36,100,172));
	    head.setForeground(Color.white);
		table.setOpaque(false);
		table.setBackground(new Color(0,0,0,0));
		tableScroll = new JScrollPane(table);		
		tableScroll.setPreferredSize(new Dimension(200, 700));
		tableScroll.setOpaque(false);
		tableScroll.setBackground(new Color(0,0,0,0));
		tableScroll.getViewport().setOpaque(false);
		tableScroll.setForeground(new Color(255,255,255));
		Border lineBorder2 = BorderFactory.createLineBorder(Color.WHITE, 0);
		Border emptyBorder2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		tableScroll.setBorder(BorderFactory.createCompoundBorder(lineBorder2, emptyBorder2));
		pan3.add(pan2,"North");
		pan3.add(tableScroll,"Center");
		
		//�ؿ� �г�
		pan5 = new JPanel();
		pan5.setOpaque(false);
		notice_lb = new JLabel("��������");
		notice_lb.setFont(new Font("SansSerif", Font.BOLD, 15));
		notice_lb.setForeground(new Color(255,255,255));
		notice_tf = new JTextField(30);
		notice_tf.setBackground(new Color(100,149,237));
		notice_tf.setOpaque(false);
		notice_tf.setBorder(new RoundedBorder());
		notice_tf.setForeground(new Color(255,255,255));
		notice_tf.registerKeyboardAction(this,"notice",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), JComponent.WHEN_FOCUSED);
		btn = new JButton("������");
		btn.setFont(new Font("SansSerif", Font.BOLD, 15));
		btn.setBackground(new Color(100,149,237));
		btn.setOpaque(false);
		btn.setForeground(new Color(255,255,255));
		btn.setBorder(new RoundedBorder());
		btn.setActionCommand("notice");
		btn.addActionListener(this);
		pan5.add(notice_lb); pan5.add(notice_tf); pan5.add(btn);
		
		pan6 = new JPanel(new BorderLayout(10,10));
		pan6.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		pan6.setOpaque(false);
		pan6.add(pan1,"Center");
		pan6.add(pan3,"East");
		pan6.add(pan5,"South");
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		  MediaTracker mt = new MediaTracker(this);
		  mt.addImage(tk.getImage("img.jpg"), 0);
		  jip = new JImagePanel( tk.getImage("img/img.jpg") );
		  jip.setLayout( null );
		  jip.setBounds( 0, 0, 100, 100 );
		  jip.setOpaque(false);
		
		//���콺 �˾��޴�
		pm = new JPopupMenu();
	    pm_item1 = new JMenuItem("��������");
	    pm_item2 = new JMenuItem("�ӼӸ�");
	    // �˾��޴��� �޴������� �߰�
        pm.add(pm_item1);
        pm.addSeparator(); // ���м�
        pm.add(pm_item2);
        add(pm); // �˾��޴��� �����ӿ� �߰�

		table.addMouseListener(this);
        pm_item1.addActionListener(this);
        pm_item2.addActionListener(this);
        
        this.setLayout(null);
		pan6.setBounds(0,0,800,600);
		jip.setBounds(0,0,800,600);
		this.add(pan6);
		this.add(jip);
		
		this.setVisible(true);
	}
	
	class JImagePanel extends JPanel {  
        private Image img;
        public JImagePanel(Image img){
           this.img = img;
       }
        public void paintComponent(Graphics g) {  
            g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), this);  
        }  
    }  
	
	class RoundedBorder extends AbstractBorder {//"ID"�ʵ��� �ձ� �𼭸� ������ ���� Ŭ���� 
		   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) { 
		      Graphics2D g2 = (Graphics2D) g; 
		      int arc = 5;//�𼭸� �ձ۱� ���� 
		      g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc); //�ձ� �𼭸� �׵θ� �׸��� 
		     
		   }
	}
	
	public void serverInit(){
		
		roomHm = new HashMap<String, ArrayList<GUIChatThread>>();
		
		try{		
			ServerSocket server = new ServerSocket(10001);
			chat_ta.append("������ ��ٸ��ϴ�.\n");
			hm = new HashMap<String, ObjectOutputStream>();
			
			while(true){	
				Socket sock = server.accept();
				GUIChatThread chatthread = new GUIChatThread(sock, hm, this);
				activeThread = chatthread;
				chatthread.start();
			} // while
		}catch(Exception e){	
			System.out.println("server main : " + e);
		}	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="notice"){
			chat_ta.append("[����] " + notice_tf.getText() + "\n");
			activeThread.broadcast("[����] " + notice_tf.getText());
			notice_tf.setText("");
		}

		if(e.getSource() == pm_item1){		
			//���õ� id���� ������ id�� ���Ѵ�
			int result = JOptionPane.showConfirmDialog(null, select_id + "���� ���� �����Ű�ڽ��ϱ�?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			//���̾�α׿��� ���� ������ ��� ������ �����Ų��
			if(result == JOptionPane.YES_OPTION){
				chat_ta.append(select_id + "���� �����Ͽ����ϴ�.\n");
				
				for(int i=0;i<model.getRowCount();i++){
					if(model.getValueAt(i, 0).equals(select_id)){
						model.removeRow(i);
					}
				}
				
				try {
					ObjectOutputStream oos = activeThread.getObjectOutputStreamById(select_id);
					ObejctChatData data = new ObejctChatData();
					data.setMsg("/ban");
					oos.writeObject(data);
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				//��������
				user_lb_num.setText(nowConnectUser + "");
				pan2.revalidate();
				pan2.repaint();
				//������ ����޼����� ��½�Ų��
			}
			select_id = null ;
		}else if (e.getSource() == pm_item2) {
			String msg = JOptionPane.showInputDialog(this, "�ӼӸ��� �Է��ϼ���");
			activeThread.sendmsg(msg, select_id);
		}
	}
	
	//���콺�˾�
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) { // ��Ŭ����
			JTable table = (JTable) e.getSource();
			pm.show(table, e.getX(), e.getY());
			
			int row = table.rowAtPoint(e.getPoint()); // ���õǾ��� row���ϱ�
			
			if (row != -1) { // ���� ���õǾ��� �����ΰ��
				//���õ� ���� id���� �����´�
				select_id = (String) table.getValueAt(row, 0);
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	// ================================================================
	// *** ��Ƽ��� ���õ� �޼ҵ��
	// ================================================================
	
	//���ǿ��� ������� ä�ù��� ����Ʈ �����Ϸ���
	public void makeRoomlist(){
		activeThread.broadcast("/makeroom");
	}
	
	void makeGuestlistRoom(String roomNum){
		//�濡 �ִ� �����ڸ� ��� ����
	}
	
	//�濡 �ƹ��� ���� �� ������� �ϴ� �޼ҵ�
	public void removeRoom(String roomName){ 
		//���� ������ �渮��Ʈ �� �ش� �̸��� ���� ��
		if(roomHm.get(roomName).size()==0){
			//������ ���� 0�̸� map���� �����ش�
			roomHm.remove(roomName);
		}
		
		activeThread.broadcast("/removeroom");
	}
	
	// �濡�� ��� ������������ ����Ǵ� �޼ҵ�
	public void removeGuestRoom(String roomName , GUIChatThread guest){ 
		//�濡�� ���� ������������ ã�Ƽ� �¸� ����������
		roomHm.get(roomName).remove(guest);
		
		
		try {
			activeThread.broadcast("/roomout");
		} catch (Exception e) {
			System.out.println("Client Room out Error");
		}
	}
	
	//�̹� ��������ִ� �濡 �����Ϸ� �� �� �����ϴ� �޼ҵ�
	public void addGuestRoom(String rn, GUIChatThread guest) {
		//��������ִ� ��(map)�� ��, �����ϰ��� �ϴ� ���� �̸�(key)�� ����
		ArrayList<GUIChatThread> list2 = roomHm.get(rn);
		//�����ڸ� ����ִ� �迭����Ʈ�� Guest��(�ڱ��ڽ����� ���̰Ե�) ����ش�.
		list2.add(guest);
	}

	//���� ������ �� ����Ǵ� �޼ҵ�
	public void addRoom(String roomname, GUIChatThread guest) {
		//�迭����Ʈ(���� �����Ǵ� �濡 �߰��� ������ ����Ʈ)�� ���� �����Ͽ�
		ArrayList<GUIChatThread> arraylist2 = new ArrayList<GUIChatThread>();
		//�ڱ��ڽ��� �־��ְ�(guset)
		arraylist2.add(guest);
		//�ڽ��� ������ ���� �̸��� ������ ����Ʈ�� ä�ù� �ؽøʿ� �߰��Ѵ�.
		roomHm.put(roomname, arraylist2);
	}
	
	//ä�ù� ���ο� ����� ����� �ִ��� �޾ƿ��� �޼ҵ�
	void Roomnumber(Set<String> roomlist){
		//��� ���� �����ؿ��� ���� roomnum ���ڿ� ����
		StringBuffer buffer2 = new StringBuffer("roomnum/"); //�濡 ����� 

		//�޾ƿ� roomlist(������ ��� �� �̸�)
		for(String t : roomlist){
			//���̸����� ������ �ִ� ����� ���� map�� ���� �޾ƿ�,
			//buffer2�� ����Ѵ�.
			buffer2.append(roomHm.get(t).size()+"/");
		}
		//����� ���� ��� �������� ������Ʈ���ش�
		activeThread.broadcast(buffer2.toString());
	}
	
	public static void main(String[] args) {			
		new GUIChatServer();
	} // main		
}