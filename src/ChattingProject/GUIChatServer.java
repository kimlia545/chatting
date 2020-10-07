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
	
	//마우스팝업
	JPopupMenu pm;
	JMenuItem pm_item1,pm_item2;
	String select_id;
	
	// 총 접속자수
	int nowConnectUser = 0;
	JImagePanel jip = null;
	HashMap<String, ObjectOutputStream> hm ;
	
	//***방이름+접속자스레드
	HashMap<String, ArrayList<GUIChatThread>> roomHm;
	
	//테이블 가운데 정렬
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
		}//자신의 IP
		
		//왼쪽 위 패널
		pan1 = new JPanel(new BorderLayout());
		pan1.setOpaque(false);
		pan1.setBackground(new Color(0,0,0,0));
		ip_lb = new JLabel("서버IP : " + local.getHostAddress());
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
		
		//오른쪽 위 패널
		pan2 = new JPanel();
		pan2.setOpaque(false);
		pan2.setBackground(new Color(0,0,0,0));
		user_lb = new JLabel("총 접속자수");
		user_lb.setFont(new Font("Serif", Font.PLAIN, 15));
		user_lb.setForeground(new Color(255,255,255));
		user_lb.setBackground(new Color(0,0,0,0));
		user_lb_num = new JLabel(nowConnectUser + "");
		user_lb_num.setForeground(new Color(255,255,255));
		user_lb_num.setBackground(new Color(0,0,0,0));
		pan2.add(user_lb); pan2.add(user_lb_num);
		
		//오른쪽 중간 패널
		pan3 = new JPanel(new BorderLayout());
		pan3.setOpaque(false);
		pan3.setBackground(new Color(0,0,0,0));
		pan4 = new JPanel();
		pan4.setPreferredSize(new Dimension(100,400));
		String [][] contents={};
		String [] header = {"접속자","등급"};
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
		
		//밑에 패널
		pan5 = new JPanel();
		pan5.setOpaque(false);
		notice_lb = new JLabel("공지사항");
		notice_lb.setFont(new Font("SansSerif", Font.BOLD, 15));
		notice_lb.setForeground(new Color(255,255,255));
		notice_tf = new JTextField(30);
		notice_tf.setBackground(new Color(100,149,237));
		notice_tf.setOpaque(false);
		notice_tf.setBorder(new RoundedBorder());
		notice_tf.setForeground(new Color(255,255,255));
		notice_tf.registerKeyboardAction(this,"notice",KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), JComponent.WHEN_FOCUSED);
		btn = new JButton("보내기");
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
		
		//마우스 팝업메뉴
		pm = new JPopupMenu();
	    pm_item1 = new JMenuItem("강제퇴장");
	    pm_item2 = new JMenuItem("귓속말");
	    // 팝업메뉴에 메뉴아이템 추가
        pm.add(pm_item1);
        pm.addSeparator(); // 구분선
        pm.add(pm_item2);
        add(pm); // 팝업메뉴를 프레임에 추가

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
	
	class RoundedBorder extends AbstractBorder {//"ID"필드의 둥근 모서리 설정을 위한 클래스 
		   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) { 
		      Graphics2D g2 = (Graphics2D) g; 
		      int arc = 5;//모서리 둥글기 설정 
		      g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc); //둥근 모서리 테두리 그리기 
		     
		   }
	}
	
	public void serverInit(){
		
		roomHm = new HashMap<String, ArrayList<GUIChatThread>>();
		
		try{		
			ServerSocket server = new ServerSocket(10001);
			chat_ta.append("접속을 기다립니다.\n");
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
			chat_ta.append("[공지] " + notice_tf.getText() + "\n");
			activeThread.broadcast("[공지] " + notice_tf.getText());
			notice_tf.setText("");
		}

		if(e.getSource() == pm_item1){		
			//선택된 id값과 생성된 id를 비교한다
			int result = JOptionPane.showConfirmDialog(null, select_id + "님을 강제 퇴장시키겠습니까?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			//다이얼로그에서 예를 눌렀을 경우 퇴장을 진행시킨다
			if(result == JOptionPane.YES_OPTION){
				chat_ta.append(select_id + "님을 강퇴하였습니다.\n");
				
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
				//총접속자
				user_lb_num.setText(nowConnectUser + "");
				pan2.revalidate();
				pan2.repaint();
				//강제로 종료메세지를 출력시킨다
			}
			select_id = null ;
		}else if (e.getSource() == pm_item2) {
			String msg = JOptionPane.showInputDialog(this, "귓속말을 입력하세요");
			activeThread.sendmsg(msg, select_id);
		}
	}
	
	//마우스팝업
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) { // 우클릭시
			JTable table = (JTable) e.getSource();
			pm.show(table, e.getX(), e.getY());
			
			int row = table.rowAtPoint(e.getPoint()); // 선택되어진 row구하기
			
			if (row != -1) { // 셀이 선택되어진 상태인경우
				//선택된 셀의 id값을 가져온다
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
	// *** 멀티룸과 관련된 메소드들
	// ================================================================
	
	//대기실에서 만들어진 채팅방의 리스트 갱신하려고
	public void makeRoomlist(){
		activeThread.broadcast("/makeroom");
	}
	
	void makeGuestlistRoom(String roomNum){
		//방에 있는 접속자를 담기 위한
	}
	
	//방에 아무도 없을 때 사라지게 하는 메소드
	public void removeRoom(String roomName){ 
		//현재 생성된 방리스트 중 해당 이름을 가진 방
		if(roomHm.get(roomName).size()==0){
			//접속자 수가 0이면 map에서 지워준다
			roomHm.remove(roomName);
		}
		
		activeThread.broadcast("/removeroom");
	}
	
	// 방에서 사람 빠져나갔을때 실행되는 메소드
	public void removeGuestRoom(String roomName , GUIChatThread guest){ 
		//방에서 누가 빠져나갔는지 찾아서 걔를 지워버린다
		roomHm.get(roomName).remove(guest);
		
		
		try {
			activeThread.broadcast("/roomout");
		} catch (Exception e) {
			System.out.println("Client Room out Error");
		}
	}
	
	//이미 만들어져있는 방에 접속하려 할 때 실행하는 메소드
	public void addGuestRoom(String rn, GUIChatThread guest) {
		//만들어져있는 방(map)들 중, 접속하고자 하는 방의 이름(key)를 통해
		ArrayList<GUIChatThread> list2 = roomHm.get(rn);
		//접속자를 담고있는 배열리스트에 Guest를(자기자신으로 쓰이게됨) 담아준다.
		list2.add(guest);
	}

	//방을 개설할 때 실행되는 메소드
	public void addRoom(String roomname, GUIChatThread guest) {
		//배열리스트(새로 개설되는 방에 추가될 접속자 리스트)를 새로 생성하여
		ArrayList<GUIChatThread> arraylist2 = new ArrayList<GUIChatThread>();
		//자기자신을 넣어주고(guset)
		arraylist2.add(guest);
		//자신이 설정한 방의 이름과 접속자 리스트를 채팅방 해시맵에 추가한다.
		roomHm.put(roomname, arraylist2);
	}
	
	//채팅방 내부에 몇명의 사람이 있는지 받아오는 메소드
	void Roomnumber(Set<String> roomlist){
		//사람 수를 저장해오기 위해 roomnum 문자열 생성
		StringBuffer buffer2 = new StringBuffer("roomnum/"); //방에 사람수 

		//받아온 roomlist(생성된 모든 방 이름)
		for(String t : roomlist){
			//방이름마다 가지고 있는 사람의 수를 map을 통해 받아와,
			//buffer2에 기록한다.
			buffer2.append(roomHm.get(t).size()+"/");
		}
		//기록한 값을 모든 유저에게 업데이트해준다
		activeThread.broadcast(buffer2.toString());
	}
	
	public static void main(String[] args) {			
		new GUIChatServer();
	} // main		
}