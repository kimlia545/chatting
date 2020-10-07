package ChattingProject;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class CreateChatRoomDialog extends JDialog implements ActionListener{

	JButton createBtn, cancelBtn;
	JLabel roomNameLabel;
	JTextField roomNameTextField;
	
	JPanel btnPanel,roomNamePanel, ImagePanel;
	
	//방이 생성되었을때 true로 변환
	boolean isCreateRoom = false;
	String roomName;
	
	public CreateChatRoomDialog(JPanel panel) {
		//패널 중앙에 띄우기
		this.setLocationRelativeTo(panel);
		//모달 다이얼로그
		this.setModal(true);
		this.setSize(400, 200);
		this.setTitle("방만들기");
		this.setLayout(new BorderLayout());
		
		roomNamePanel = new JPanel();
		btnPanel = new JPanel();
		ImagePanel = new JPanel(new BorderLayout());
		JLabel label= new JLabel(new ImageIcon("img/abc.jpg"));
		
		createBtn = new JButton("방만들기");
		cancelBtn = new JButton("취소");
		
		btnPanel.add(createBtn);
		btnPanel.add(cancelBtn);
		
		roomNameLabel = new JLabel("방 이름: ");
		roomNameTextField = new JTextField(10);
		
		roomNamePanel.add(roomNameLabel);
		roomNamePanel.add(roomNameTextField);
		
		this.add(roomNamePanel, "North");
		this.add(btnPanel, "South");
		this.add(label);
		
		createBtn.addActionListener(this);
		cancelBtn.addActionListener(this);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createBtn){
			roomName = roomNameTextField.getText();
			isCreateRoom = true;
			dispose();
		}
		else if(e.getSource() == cancelBtn){
			isCreateRoom = false;
			dispose();
		}
				
	}
}
