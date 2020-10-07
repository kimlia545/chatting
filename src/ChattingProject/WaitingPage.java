//���� �г�

package ChattingProject;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class WaitingPage extends JPanel implements ActionListener,
		MouseListener {

	JPanel allPanel, topPanel, centerPanel, bottomPanel;
	JPanel channelChangePanel, waitRoomPanel, userListPanel, findAllPanel,
			findFriendPanel;
	JPanel allChatPanel, quickAccessPanel, userInfoPanel;
	JPanel nowConnectFriendsPanel, nowConnectAllPanel;

	JButton chanelChangeBtn, quickAccessBtn, makeRoomBtn, message;
	JLabel userId, namelbl;
	DefaultTableModel tableModel_ChatList, tableModel_AllUserList,
			tableModel_FriendsList;
	JTable waitRoomTable, nowConnectAllTable, nowConnectFriendsTable;

	JTabbedPane userListTab;

	JTextField chatTf;
	JTextArea display;

	String allUserListContents[][] = null;

	JPopupMenu menupm, deletepm;
	JMenuItem addpm_item, deletepm_item, chat_item;
	String select_name;
	String select_address;

	String name;// ģ����
	String address; // �ּҰ�
	JTable table;

	// ģ��ã��
	JTextField findFriendtf, findAlltf;
	JButton findAllBtn, findFriendBtn;
	JScrollPane allUserListScrollPane, friendUserListScrollPane;

	// ������ ������ ����
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Socket sock;

	int selectedRow; // ���콺�� Ŭ���� ���� ��
	JImagePanel jip = null;

	boolean isChatTime;
	boolean is_joinroom; // �������� ��������
	// ���� �������� ä�ù��̸� = ������ null
	String myChatroom;

	MainFrame mf;
	Login login;

	public WaitingPage(MainFrame mf) {
		this.mf = mf;
		this.setSize(1024, 768);
		this.setLayout(new BorderLayout());

		allPanel = new JPanel();
		allPanel.setLayout(new BorderLayout(10, 10));
		allPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// �г� ����==============================================================

		topPanel = new JPanel();
		topPanel.setBackground(new Color(0, 0, 0, 0));
		topPanel.setOpaque(false);
		centerPanel = new JPanel();
		centerPanel.setBackground(new Color(0, 0, 0, 0));
		centerPanel.setOpaque(false);
		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(0, 0, 0, 0));
		bottomPanel.setOpaque(false);

		channelChangePanel = new JPanel();
		channelChangePanel.setOpaque(false);
		channelChangePanel.setBackground(new Color(0, 0, 0, 0));
		waitRoomPanel = new JPanel();
		waitRoomPanel.setOpaque(false);
		waitRoomPanel.setBackground(new Color(0, 0, 0, 0));
		userListPanel = new JPanel();
		userListPanel.setOpaque(false);
		userListPanel.setBackground(new Color(0, 0, 0, 0));
		findAllPanel = new JPanel();
		findAllPanel.setOpaque(false);
		findAllPanel.setBackground(new Color(0, 0, 0, 0));
		findFriendPanel = new JPanel();
		findFriendPanel.setOpaque(false);
		findFriendPanel.setBackground(new Color(0, 0, 0, 0));

		allChatPanel = new JPanel();
		allChatPanel.setOpaque(false);
		allChatPanel.setBackground(new Color(0, 0, 0, 0));
		quickAccessPanel = new JPanel();
		quickAccessPanel.setOpaque(false);
		quickAccessPanel.setBackground(new Color(0, 0, 0, 0));
		userInfoPanel = new JPanel();
		userInfoPanel.setOpaque(false);
		userInfoPanel.setBackground(new Color(0, 0, 0, 0));

		nowConnectFriendsPanel = new JPanel();
		nowConnectFriendsPanel.setOpaque(false);
		nowConnectFriendsPanel.setBackground(new Color(0, 0, 0, 0));
		nowConnectAllPanel = new JPanel();
		nowConnectAllPanel.setOpaque(false);
		nowConnectAllPanel.setBackground(new Color(0, 0, 0, 0));

		// �г� ���̾ƿ�==============================================================

		topPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new BorderLayout());
		bottomPanel.setLayout(new BorderLayout());

		waitRoomPanel.setLayout(new BorderLayout());

		nowConnectFriendsPanel.setLayout(new BorderLayout());
		nowConnectAllPanel.setLayout(new BorderLayout());

		userInfoPanel.setLayout(new BorderLayout());
		userInfoPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
		allChatPanel.setLayout(new BorderLayout());
		// quickAccessPanel.setLayout(new GridLayout(0,1,5,5));

		// ä�κ��� ��ư==============================================================

		namelbl = new JLabel("����");// ����
		namelbl.setFont(new Font("�������", Font.BOLD, 20));
		namelbl.setForeground(Color.WHITE);
		namelbl.setOpaque(false);
		namelbl.setBackground(new Color(0, 0, 0, 0));
		chanelChangeBtn = new JButton("ä�κ���");
		chanelChangeBtn.setBackground(new Color(125,159,209));
		chanelChangeBtn.setForeground(Color.white);
		channelChangePanel.add(namelbl);
		channelChangePanel.add(chanelChangeBtn);
		channelChangePanel.setOpaque(false);
		channelChangePanel.setBackground(new Color(0, 0, 0, 0));
		topPanel.add(channelChangePanel, "West");

		// ä�ø���Ʈ
		// ���̺�==============================================================

		String chatListHeader[] = { "��ȣ", "����", "����", "����", "�ο�" };
		String chatListContents[][] = {};

		tableModel_ChatList = new DefaultTableModel(chatListContents,
				chatListHeader) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column >= 0) {
					return false;
				} else {
					return true;
				}
			}

			@Override
			public Class<?> getColumnClass(int rowNum) {
				return this.getValueAt(0, rowNum).getClass();
			}
		};

		waitRoomTable = new JTable(tableModel_ChatList);
		JTableHeader wrhead = waitRoomTable.getTableHeader();
		wrhead.setBackground(new Color(130,153,187));
		wrhead.setForeground(Color.white);
		JScrollPane chatListScrollPane = new JScrollPane(waitRoomTable);

		// ä�ù� ����Ʈ ���콺�׼�
		waitRoomTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selRow = waitRoomTable.getSelectedRow();

				if (selRow < -1)
					return;
				if (e.getClickCount() == 2) {
					if (selRow != -1) {
						inputroom((String) waitRoomTable.getValueAt(selRow, 2));
						is_joinroom = true;
						joinroom(selRow);
					}
				}
			}
		});

		waitRoomPanel.setPreferredSize(new Dimension(680, 300));
		waitRoomPanel.add(chatListScrollPane);
		centerPanel.add(waitRoomPanel, "West");

		// ��������Ʈ
		// ���̺�==============================================================

		userListTab = new JTabbedPane();

		// ģ��
		// ã��======================================================================
		findAlltf = new JTextField(10);
		findAllBtn = new JButton("ã��");
		findAllBtn.setBackground(new Color(175, 224, 230));
		findAllBtn.setForeground(Color.white);

		findAllPanel.add(findAlltf);
		findAllPanel.add(findAllBtn);

		findFriendtf = new JTextField(10);
		findFriendBtn = new JButton("ã��");
		findFriendBtn.setBackground(new Color(175, 224, 230));
		findFriendBtn.setForeground(Color.white);
		findFriendPanel.add(findFriendtf);
		findFriendPanel.add(findFriendBtn);

		// topPanel.add(findPersonPanel, "East");

		findAllBtn.addActionListener(this);
		findFriendBtn.addActionListener(this);

		// ���� �������� ��ü���� ���̺�
		String allUserListHeader[] = { "�̸�", "��ġ" };
		/*
		 * String allUserListContents[][] = { { "a", "����" }, { "�ФФФ�", "1����" },
		 * { "������", "???" } };
		 */

		tableModel_AllUserList = new DefaultTableModel(null, allUserListHeader);
		nowConnectAllTable = new JTable(tableModel_AllUserList);
		JTableHeader ncahead = nowConnectAllTable.getTableHeader();
		ncahead.setBackground(new Color(130,153,187));
		ncahead.setForeground(Color.white);
		JScrollPane allUserListScrollPane = new JScrollPane(nowConnectAllTable);
		nowConnectAllTable.addMouseListener(this);

		// ���� �������� ģ�� ���̺�
		String friendUserListHeader[] = { "�̸�", "��ġ" };
		// String friendUserListContents[][] = { { "a", "����" } };

		tableModel_FriendsList = new DefaultTableModel(null,
				friendUserListHeader);
		nowConnectFriendsTable = new JTable(tableModel_FriendsList);
		JTableHeader ncfhead = nowConnectFriendsTable.getTableHeader();
		ncfhead.setBackground(new Color(130,153,187));
		ncfhead.setForeground(Color.white);
		JScrollPane friendUserListScrollPane = new JScrollPane(
				nowConnectFriendsTable);
		friendlist();
		nowConnectAllPanel.add(allUserListScrollPane);
		nowConnectAllPanel.add(findAllPanel, BorderLayout.SOUTH);

		nowConnectFriendsPanel.add(friendUserListScrollPane);
		nowConnectFriendsPanel.add(findFriendPanel, BorderLayout.SOUTH);

		userListTab.add("��ü", nowConnectAllPanel);
		userListTab.add("ģ��", nowConnectFriendsPanel);
		userListTab.setPreferredSize(new Dimension(290, 335));
		userListPanel.add(userListTab);

		nowConnectFriendsTable.addMouseListener(this);

		centerPanel.add(userListPanel, "East");

		menupm = new JPopupMenu(); // ��ü���� ģ���߰�
		addpm_item = new JMenuItem("ģ���߰�");
		chat_item = new JMenuItem("1:1ä��");

		deletepm = new JPopupMenu();
		deletepm_item = new JMenuItem("ģ������"); // ģ�����̺��� ģ������

		// �˾��޴��� �޴������� �߰�//
		menupm.add(addpm_item);
		menupm.addSeparator(); // ���м�
		menupm.add(chat_item);

		deletepm.add(deletepm_item);
		add(deletepm); // �˾��޴��� �����ӿ� �߰�

		addpm_item.addActionListener(this); // �޴���ư�� �׼Ǹ����� �ޱ�
		chat_item.addActionListener(this);
		deletepm_item.addActionListener(this);

		// ���� ��ü
		// ä��==============================================================

		allChatPanel.setPreferredSize(new Dimension(600, 300));
		JPanel chatPan2 = new JPanel();
		display = new JTextArea();
		display.setEditable(false);
		JScrollPane chatSp = new JScrollPane(display);
		chatTf = new JTextField(26);
		chatTf.registerKeyboardAction(this, "send",
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_FOCUSED);
		message = new JButton("������");
		message.setBackground(new Color(175, 224, 230));
		message.setForeground(Color.white);
		message.setActionCommand("send");
		message.addActionListener(this);
		chatPan2.add(chatTf);
		chatPan2.add(message);
		allChatPanel.add(display, "Center");
		allChatPanel.add(chatPan2, "South");
		bottomPanel.add(allChatPanel, "West");

		// �ٷ�����, �游���
		// ��ư==============================================================

		quickAccessBtn = new JButton("�ٷ�����");
		quickAccessBtn.setBackground(new Color(125,159,209));
		quickAccessBtn.setForeground(Color.white);
		quickAccessPanel.add(quickAccessBtn);

		makeRoomBtn = new JButton("�游���");
		makeRoomBtn.setBackground(new Color(125,159,209));
		makeRoomBtn.setForeground(Color.white);
		quickAccessPanel.add(makeRoomBtn);
		makeRoomBtn.addActionListener(this);

		channelChangePanel.add(quickAccessBtn);
		channelChangePanel.add(makeRoomBtn);

		// ��������==============================================================

		JLabel userInfo = new JLabel("��������");
		userInfo.setFont(new Font("�������", Font.BOLD, 30));
		userInfo.setForeground(Color.white);
		JLabel id = new JLabel("ID : ");
		id.setFont(new Font("�������", Font.PLAIN, 25));
		id.setForeground(Color.white);
		JPanel pan = new JPanel();
		pan.setBackground(new Color(0, 0, 0, 0));
		pan.setOpaque(false);
		userId = new JLabel();
		userId.setFont(new Font("�������", Font.PLAIN, 25));
		userId.setForeground(Color.white);
		pan.add(id);
		pan.add(userId);
		userInfoPanel.add(userInfo, "North");
		userInfoPanel.add(pan, "West");
		bottomPanel.add(userInfoPanel, "Center");

		Toolkit tk = Toolkit.getDefaultToolkit();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(tk.getImage("img/���ǹ��.jpg"), 0);
		jip = new JImagePanel(tk.getImage("img/���ǹ��.jpg"));
		jip.setOpaque(false);

		allPanel.add(topPanel, "North");
		allPanel.add(centerPanel, "Center");
		allPanel.add(bottomPanel, "South");
		allPanel.setOpaque(false);
		allPanel.setBounds(0, 0, 1015, 720);
		jip.setBounds(0, 0, 1020, 768);

		isChatTime = false;
		myChatroom = null;
		is_joinroom = false;

		this.setLayout(null);

		this.add(allPanel);
		this.add(jip);

		setVisible(true);
	}// ������

	class JImagePanel extends JPanel {
		private Image img;

		public JImagePanel(Image img) {
			this.img = img;
		}

		public void paintComponent(Graphics g) {
			g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}

	public void connect(String id, String ip) {
		try {
			sock = new Socket(ip, 10001);
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());

			ObejctChatData data = new ObejctChatData();

			data.setMsg(id);
			oos.writeObject(data);
			oos.flush();

			WinInputThread wit = new WinInputThread();
			wit.start();
		}

		catch (Exception e) {
			System.out.println("������ ���ӽ� ������ �߻��Ͽ����ϴ�.");
			System.out.println(e);
		}
	}

	// ä�÷� �����
	public void inputChatRoom() {
		mf.closePanel();
	}

	// ������ ��üä���� ���� ���� ������ Ŭ����
	class WinInputThread extends Thread {

		// private ObejctChatData data;

		public void run() {
			try {
				String line = null;
				boolean keepGoing = true;
				ObejctChatData data = null;
				DefaultTableModel model;
				Vector varg0 = null;
				Vector varg1 = null;
				Vector roomNameData = null;
				
				while (keepGoing) {

					try {
						data = (ObejctChatData) ois.readObject();
						line = data.getMsg();
					} catch (IOException e) {
						e.printStackTrace();
						break;
					}

					if (line.indexOf("[�ӼӸ�]") == -1
							&& line.indexOf("[���]����") == -1
							&& line.indexOf("/ban") == -1) {
						varg0 = data.getVarg0();
						varg1 = data.getVarg1();
						roomNameData = data.getRoomNameVector();
						tableModel_AllUserList.setNumRows(0);
						for (int i = 0; i < varg0.size(); i++) {
							tableModel_AllUserList.addRow(new Object[] {
									varg0.get(i), varg1.get(i) });
						}
						tableModel_ChatList.setNumRows(0);
						for (int i = 0; i < roomNameData.size(); i++) {
							tableModel_ChatList.addRow(new Object[] { "", "",
									roomNameData.get(i), "", "" });
						}
					}
					if (line.equals("/quit")) {
						System.out.println("quit");
						throw new Exception();
					} else if (line.equals("/ban")) {
						System.out.println("ban");
						break;
					} else if (line.equals("/makeroom")) {
						// ���� �����������
					} else if (line.equals("/roomout")) {
						// �濡�� ������
					} else if (line.equals("/removeroom")) {
						// ���� �������
					} else if (line.indexOf("roomMsg") != -1) {
						String msg[] = line.split("/");
						System.out.println("ä��â: " + msg[1]);
						mf.chat.roomInfo.append(msg[1] + "\n");
						mf.chat.roomInfo.setCaretPosition(mf.chat.roomInfo
								.getDocument().getLength());
					} else {
						System.out.println("����: " + line);
						display.append(line + "\n");
						display.setCaretPosition(display.getDocument()
								.getLength());
					}
				}
				// ��������� ���
				JOptionPane.showMessageDialog(WaitingPage.this,
						"�濡�� ���� ���ϼ̽��ϴ�.", "", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				System.out.println("client thread : " + e);
				JOptionPane.showMessageDialog(WaitingPage.this, "���α׷��� �����մϴ�.");
			} finally {
				try {
					if (ois != null)
						ois.close();
				} catch (Exception e) {
				}
				try {
					if (oos != null)
						oos.close();
				} catch (Exception e) {
				}
				try {
					if (sock != null)
						sock.close();
				} catch (Exception e) {
				}
				System.exit(0);
			}
		} // run end
	} // WinInputThread end

	void listFriend() {
		File file = new File("TextFile/FriendList.txt");
		try {
			if (checkBeforeFile(file)) {
				// FileReader�� ���ڷ� �ϴ� BufferedReader ��ü ����
				BufferedReader br = new BufferedReader(new FileReader(file));

				name = br.readLine(); // ������ �� ���徿 �о�´�.
				// address = br.readLine();

				while (name != null) {// EOF�� null���ڸ� �����ϰ� �ִ�.

					System.out.println(name);// ���� ���ڿ��� ����Ѵ�.

					name = br.readLine();// ���� ���ڿ��� �������ش�.
				}

				br.close();// FileReader�ʹ� �ٸ��� ��� �� �� �ݾ��־�� �Ѵ�.
			} else {
				System.out.println("���Ͽ� ������ �� �����ϴ�.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static boolean checkBeforeFile(File file) {
		if (file.exists()) {
			if (file.isFile() && file.canRead()) {
				return true;
			}
		}
		return false;
	}

	public void friendlist() {
		FileInputStream fis;
		try {
			fis = new FileInputStream("TextFile/FriendList.txt");
			BufferedReader br1 = new BufferedReader(new InputStreamReader(fis));

			String line;

			while ((line = br1.readLine()) != null) {
				String str[] = line.split("/");
				tableModel_FriendsList.addRow(new Object[] { str[0], str[1] });
			}
			br1.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void writeFriend() { // ���Ͽ� ģ�� �Է� �޼ҵ�
		String friend = select_name;
		String address = select_address;

		File file = new File("TextFile/FriendList.txt"); // �����ּ�
		FileWriter writer = null;

		try {
			writer = new FileWriter(file, true);

			writer.write(friend);
			writer.write("/" + address);
			writer.write("\r\n");
			writer.flush();
			System.out.println(select_name + "ģ���� �߰��Ͽ����ϴ�");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void removeFriend() { // ���� ģ�� ����
		BufferedReader in = null;
		FileWriter pw = null;
		ArrayList<String> al = new ArrayList<String>();
		String friend = (String) table.getValueAt(selectedRow, 0); // ģ�� ��
		String line = null;

		try {
			in = new BufferedReader(new FileReader("TextFile/FriendList.txt"));
			while ((line = in.readLine()) != null) {
				al.add(line); // al�� ��� �� �ֱ�

				for (String s : al) {
					String[] check = s.split("/"); // s�� �̸�/��ġ �ڸ��� string�迭�� �ְ�
					if (check[0].equals(friend)) { // ���� �� Ȯ��
						System.out.println(s + "�����Ͽ����ϴ�.");
						al.remove(s);
						break;
					}
				}
			}

		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		try {

			pw = new FileWriter("TextFile/FriendList.txt", false);
			for (String s : al) {
				pw.write(s + "\n");
				pw.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (pw != null)
					pw.close();

			} catch (IOException e) {
			}
		}
	}

	void findFriend() { // ���Ͽ��� ģ�� ã��
		BufferedReader bReader = null;

		try {
			String friend = findFriendtf.getText();
			File file = new File("TextFile/FriendList.txt");
			bReader = new BufferedReader(new FileReader(file));
			String st;
			while ((st = bReader.readLine()) != null) {
				if (st.equals(friend)) {
					System.out.println(friend + "ã�ҽ��ϴ�.");

					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bReader != null)
					bReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "send") {
			String msg = chatTf.getText();
			ObejctChatData data = new ObejctChatData();
			data.setMsg(msg);
			try {
				oos.writeObject(data);
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chatTf.setText("");
			chatTf.requestFocus();
		}
		else if (e.getSource() == addpm_item) { // ������ ���� �� �� �����ͼ� ���̱�
			if (!select_name.equals(userId.getText())) {
				writeFriend(); // ���Ͽ� ģ�� �߰�
			} else {
				JOptionPane.showMessageDialog(WaitingPage.this,
						"���� ģ���� �߰��� �� �����ϴ�.", "", JOptionPane.ERROR_MESSAGE);
			}
			tableModel_FriendsList.addRow(new Object[] {
					nowConnectAllTable.getValueAt(selectedRow, 0),
					nowConnectAllTable.getValueAt(selectedRow, 1) });
		}
		else if (e.getSource() == chat_item) {
			// listFriend();
		}
		else if (e.getSource() == deletepm_item) { // ģ������
			removeFriend();
			tableModel_FriendsList.removeRow(selectedRow);
		}
		else if (e.getSource() == findAllBtn) { // ��ü���̺��� ģ�� ã��
			String value = findAlltf.getText();
			
			for (int i = 0; i < tableModel_AllUserList.getRowCount(); i++) { 
				// ���� �˻��� �̸��� ��ü���̺� �ִٸ�
				if (value.equals(tableModel_AllUserList.getValueAt(i, 0))) { 
					// �˻��� �̸��� ǥ��
					nowConnectAllTable.setRowSelectionInterval(i, i); 
					// index0 �κ���index1������ ��(������ġ�� �����Ѵ�)�� ������ ���ÿ����� �߰��մϴ�.
				}
			}
		} else if (e.getSource() == findFriendBtn) { // ģ�� ã��
			findFriend();
			String value = findFriendtf.getText(); // ã�� ģ���� ��
			// ��ü���̺� �ִ� ģ�� ���
			for (int i = 0; i < tableModel_FriendsList.getRowCount(); i++) { 
				// ���� ���� �ִ��� ���ؼ�
				if (value.equals(tableModel_FriendsList.getValueAt(i, 0))) { 
					nowConnectFriendsTable.setRowSelectionInterval(i, i); // ã�� ���� ȭ�鿡 ǥ��
				}
			}
		}
		//�游��� ��ư ������ ���
		else if(e.getSource() == makeRoomBtn){
			CreateChatRoomDialog tempDialog = new CreateChatRoomDialog(this);
			
			if(tempDialog.isCreateRoom){
				isChatTime = true;
				mf.chat = new ChattingPage(mf.id,tempDialog.roomName ,this);
				inputChatRoom();
				ObejctChatData data = new ObejctChatData();
				myChatroom = tempDialog.roomName;
				data.setMsg("mkroom/" + myChatroom);
				
				try {
					oos.writeObject(data);
					oos.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void inputroom(String str){
		isChatTime = true;
		mf.chat = new ChattingPage(mf.id, str ,this);
		myChatroom = mf.chat.roomName;
		inputChatRoom();
	}

	public void joinroom(int selRow){
		ObejctChatData data = new ObejctChatData();
		data.setMsg("roomjoin/" + mf.id + "/" + waitRoomTable.getValueAt(selRow, 2));
		
		try {
			oos.writeObject(data);
			oos.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	//�濡�� ���� �������� ����
	public void removeChatroom() {
		isChatTime = false;
		is_joinroom = false;
		ObejctChatData data = new ObejctChatData();
		data.setMsg("roomout/" + mf.id + "/" + myChatroom);
		
		try {
			oos.writeObject(data);
			oos.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) { // ���콺 ����Ŭ����
			table = (JTable) e.getSource(); // getSource(); = �̺�Ʈ�� �߻���Ų
											// ��ü�� ���� �����´�
			selectedRow = table.rowAtPoint(e.getPoint()); // ���콺�� ��ġ�� ���� ���� ��������

			if (table == nowConnectAllTable) { // ���� ��ü���̺��̶��
				menupm.show(table, e.getX(), e.getY()); // ���̺� ��ġ

				if (selectedRow != -1) {// ���࿡ ���콺�� ������ ���� -1�� �ƴҶ� ���̺��θ� �����ϰ�
										// ������
					select_name = (String) table.getValueAt(selectedRow, 0); // �̸�
																				// ����
																				// �޾Ƽ�
					select_address = (String) table.getValueAt(selectedRow, 1);

					for (int i = 0; i < tableModel_FriendsList.getRowCount(); i++) {
						if (select_name.equals(tableModel_FriendsList
								.getValueAt(i, 0))) { // ������ ���� �̸��� ģ����Ͽ� �ִ���
														// Ȯ���ؼ�
							JOptionPane
									.showMessageDialog(null, "�̹� ��ϵ� ģ���Դϴ�."); // �̵̹�ϵ�ģ�������â"ģ���߰�����"
							break;
						}
					}
				}
			} else if (table == nowConnectFriendsTable) { // ģ�����̺��̶��
				deletepm.show(table, e.getX(), e.getY());
				int row = table.rowAtPoint(e.getPoint());
				if (row != -1) { // ���� ���� ������
					select_name = (String) table.getValueAt(row, 0);
					select_address = (String) table.getValueAt(selectedRow, 1);
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
