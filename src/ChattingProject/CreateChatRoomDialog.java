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
	
	//���� �����Ǿ����� true�� ��ȯ
	boolean isCreateRoom = false;
	String roomName;
	
	public CreateChatRoomDialog(JPanel panel) {
		//�г� �߾ӿ� ����
		this.setLocationRelativeTo(panel);
		//��� ���̾�α�
		this.setModal(true);
		this.setSize(400, 200);
		this.setTitle("�游���");
		this.setLayout(new BorderLayout());
		
		roomNamePanel = new JPanel();
		btnPanel = new JPanel();
		ImagePanel = new JPanel(new BorderLayout());
		JLabel label= new JLabel(new ImageIcon("img/abc.jpg"));
		
		createBtn = new JButton("�游���");
		cancelBtn = new JButton("���");
		
		btnPanel.add(createBtn);
		btnPanel.add(cancelBtn);
		
		roomNameLabel = new JLabel("�� �̸�: ");
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
