package ChattingProject;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.*;

import ChattingProject.Login.JImagePanel;

public class ChattingPage extends JPanel implements ActionListener {

	JTextArea roomInfo, roomUserNum;
	
	JScrollPane sp_roomInfo, sp_roomUserNum;
	JButton bt_create, bt_enter, bt_exit;
	JPanel mainPanel, rightPanel, rightUpPanel, rightDownPanel;
	JPanel leftPanel;
	
	JLabel chatlbl;
	
	JTextField input;
	String roomName;
	
	JImagePanel jip = null;
	WaitingPage wp;
	public ChattingPage(String name, String roomName, WaitingPage wp) {
		this.wp = wp;
		this.setSize(1024,768);
		this.roomName = roomName;
		
		chatlbl= new JLabel();

		mainPanel = new JPanel();
		rightPanel = new JPanel();
		rightUpPanel = new JPanel();
		rightDownPanel = new JPanel();
		leftPanel = new JPanel();
		
		mainPanel.setLayout(new BorderLayout(50,50));
		rightPanel.setLayout(new BorderLayout(5,5));
		rightUpPanel.setLayout(new BorderLayout(5,5));
		rightDownPanel.setLayout(new BorderLayout(5,5));
		leftPanel.setLayout(new BorderLayout());
		
		roomInfo = new JTextArea();
		roomInfo.setEditable(false);
		roomInfo.setBorder(new TitledBorder("채팅"));
	
		roomUserNum = new JTextArea();
		roomUserNum.setEditable(false);
		roomUserNum.setBorder(new TitledBorder("인원정보"));

		sp_roomInfo = new JScrollPane(roomInfo);
		sp_roomUserNum = new JScrollPane(roomUserNum);
		
		input = new JTextField(10);
		input.addActionListener(this);

		bt_create = new JButton("방청소"); //채팅방 글 다 지우기
		bt_enter = new JButton("초대하기");
		bt_exit = new JButton("나가기");
		
		bt_create.setBackground(new Color(104,149,197));
		bt_enter.setBackground(new Color(104,149,197));
		bt_exit.setBackground(new Color(104,149,197));
		
		bt_create.setFont(new Font("맑은고딕", Font.BOLD, 15));
		bt_enter.setFont(new Font("맑은고딕", Font.BOLD, 15));
		bt_exit.setFont(new Font("맑은고딕", Font.BOLD, 15));
		
		bt_create.setForeground(Color.WHITE);
		bt_enter.setForeground(Color.WHITE);
		bt_exit.setForeground(Color.WHITE);
		
		bt_create.addActionListener(this);
		bt_enter.addActionListener(this);
		bt_exit.addActionListener(this);
		
		
		rightUpPanel.add(sp_roomUserNum);
		
		rightDownPanel.add(bt_create,"North");
		rightDownPanel.add(bt_enter,"Center");
		rightDownPanel.add(bt_exit,"South");
		
		rightPanel.add(rightUpPanel,"Center");
		rightPanel.add(rightDownPanel,"South");
		
		leftPanel.add(sp_roomInfo, "Center");
		leftPanel.add(input, "South");
		
		rightPanel.setBounds(680,20,250,600);
		leftPanel.setBounds(20,20,650,680);
		mainPanel.setBounds(0,0,1020,720);
		
		rightPanel.setBackground(new Color(0,0,0,0));
		rightUpPanel.setBackground(new Color(0,0,0,0));
		rightDownPanel.setBackground(new Color(0,0,0,0));
		mainPanel.setBackground(new Color(0,0,0,0));
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(tk.getImage("img/채팅배경.jpg"), 0);
		jip = new JImagePanel( tk.getImage("img/채팅배경.jpg") );
		jip.setOpaque(false);
		
		mainPanel.add(leftPanel, "Center");
		mainPanel.add(rightPanel, "East");
		mainPanel.add(chatlbl);
		mainPanel.setOpaque(false);
		mainPanel.setBounds(0,0,1000,720);
		jip.setBounds(0,0,1020,768);
		this.setLayout(null);
		
		this.add(mainPanel);
		this.add(jip);
		
		this.revalidate();
		this.repaint();

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

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == input) {
			String msg = input.getText();
			ObejctChatData data = new ObejctChatData();
			data.setMsg("roomMsg/" + roomName + "/" + msg);

			try {
				wp.oos.writeObject(data);
				wp.oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			input.selectAll();
			input.requestFocus();
		}
		
		if(e.getSource() == bt_create){
			
		}
		else if(e.getSource() == bt_enter){
			
		}
		else if(e.getSource() == bt_exit){
			wp.mf.closePanel();
		}
	}
}