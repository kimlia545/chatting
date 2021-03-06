//企奄叔 鳶確

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

	String name;// 庁姥葵
	String address; // 爽社葵
	JTable table;

	// 庁姥達奄
	JTextField findFriendtf, findAlltf;
	JButton findAllBtn, findFriendBtn;
	JScrollPane allUserListScrollPane, friendUserListScrollPane;

	// 辞獄人 尻衣聖 是廃
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Socket sock;

	int selectedRow; // 原酔什稽 適遣廃 伸税 葵
	JImagePanel jip = null;

	boolean isChatTime;
	boolean is_joinroom; // 凧亜昔走 持失昔走
	// 薄仙 凧食掻昔 辰特号戚硯 = 蒸聖獣 null
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
		// 鳶確 持失==============================================================

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

		// 鳶確 傾戚焼数==============================================================

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

		// 辰確痕井 獄動==============================================================

		namelbl = new JLabel("企奄叔");// 企奄叔
		namelbl.setFont(new Font("玄精壱拒", Font.BOLD, 20));
		namelbl.setForeground(Color.WHITE);
		namelbl.setOpaque(false);
		namelbl.setBackground(new Color(0, 0, 0, 0));
		chanelChangeBtn = new JButton("辰確痕井");
		chanelChangeBtn.setBackground(new Color(125,159,209));
		chanelChangeBtn.setForeground(Color.white);
		channelChangePanel.add(namelbl);
		channelChangePanel.add(chanelChangeBtn);
		channelChangePanel.setOpaque(false);
		channelChangePanel.setBackground(new Color(0, 0, 0, 0));
		topPanel.add(channelChangePanel, "West");

		// 辰特軒什闘
		// 砺戚鷺==============================================================

		String chatListHeader[] = { "腰硲", "雌殿", "薦鯉", "号舌", "昔据" };
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

		// 辰特号 軒什闘 原酔什衝芝
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

		// 政煽軒什闘
		// 砺戚鷺==============================================================

		userListTab = new JTabbedPane();

		// 庁姥
		// 達奄======================================================================
		findAlltf = new JTextField(10);
		findAllBtn = new JButton("達奄");
		findAllBtn.setBackground(new Color(175, 224, 230));
		findAllBtn.setForeground(Color.white);

		findAllPanel.add(findAlltf);
		findAllPanel.add(findAllBtn);

		findFriendtf = new JTextField(10);
		findFriendBtn = new JButton("達奄");
		findFriendBtn.setBackground(new Color(175, 224, 230));
		findFriendBtn.setForeground(Color.white);
		findFriendPanel.add(findFriendtf);
		findFriendPanel.add(findFriendBtn);

		// topPanel.add(findPersonPanel, "East");

		findAllBtn.addActionListener(this);
		findFriendBtn.addActionListener(this);

		// 薄仙 羨紗掻昔 穿端政煽 砺戚鷺
		String allUserListHeader[] = { "戚硯", "是帖" };
		/*
		 * String allUserListContents[][] = { { "a", "企奄叔" }, { "ばばばば", "1腰号" },
		 * { "けいし", "???" } };
		 */

		tableModel_AllUserList = new DefaultTableModel(null, allUserListHeader);
		nowConnectAllTable = new JTable(tableModel_AllUserList);
		JTableHeader ncahead = nowConnectAllTable.getTableHeader();
		ncahead.setBackground(new Color(130,153,187));
		ncahead.setForeground(Color.white);
		JScrollPane allUserListScrollPane = new JScrollPane(nowConnectAllTable);
		nowConnectAllTable.addMouseListener(this);

		// 薄仙 羨紗掻昔 庁姥 砺戚鷺
		String friendUserListHeader[] = { "戚硯", "是帖" };
		// String friendUserListContents[][] = { { "a", "企奄叔" } };

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

		userListTab.add("穿端", nowConnectAllPanel);
		userListTab.add("庁姥", nowConnectFriendsPanel);
		userListTab.setPreferredSize(new Dimension(290, 335));
		userListPanel.add(userListTab);

		nowConnectFriendsTable.addMouseListener(this);

		centerPanel.add(userListPanel, "East");

		menupm = new JPopupMenu(); // 穿端拭辞 庁姥蓄亜
		addpm_item = new JMenuItem("庁姥蓄亜");
		chat_item = new JMenuItem("1:1辰特");

		deletepm = new JPopupMenu();
		deletepm_item = new JMenuItem("庁姥肢薦"); // 庁姥砺戚鷺拭辞 庁姥肢薦

		// 橡穣五敢拭 五敢焼戚奴 蓄亜//
		menupm.add(addpm_item);
		menupm.addSeparator(); // 姥歳識
		menupm.add(chat_item);

		deletepm.add(deletepm_item);
		add(deletepm); // 橡穣五敢研 覗傾績拭 蓄亜

		addpm_item.addActionListener(this); // 五敢獄動拭 衝芝軒什格 含奄
		chat_item.addActionListener(this);
		deletepm_item.addActionListener(this);

		// 企奄叔 穿端
		// 辰特==============================================================

		allChatPanel.setPreferredSize(new Dimension(600, 300));
		JPanel chatPan2 = new JPanel();
		display = new JTextArea();
		display.setEditable(false);
		JScrollPane chatSp = new JScrollPane(display);
		chatTf = new JTextField(26);
		chatTf.registerKeyboardAction(this, "send",
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_FOCUSED);
		message = new JButton("左鎧奄");
		message.setBackground(new Color(175, 224, 230));
		message.setForeground(Color.white);
		message.setActionCommand("send");
		message.addActionListener(this);
		chatPan2.add(chatTf);
		chatPan2.add(message);
		allChatPanel.add(display, "Center");
		allChatPanel.add(chatPan2, "South");
		bottomPanel.add(allChatPanel, "West");

		// 郊稽脊舌, 号幻級奄
		// 獄動==============================================================

		quickAccessBtn = new JButton("郊稽脊舌");
		quickAccessBtn.setBackground(new Color(125,159,209));
		quickAccessBtn.setForeground(Color.white);
		quickAccessPanel.add(quickAccessBtn);

		makeRoomBtn = new JButton("号幻級奄");
		makeRoomBtn.setBackground(new Color(125,159,209));
		makeRoomBtn.setForeground(Color.white);
		quickAccessPanel.add(makeRoomBtn);
		makeRoomBtn.addActionListener(this);

		channelChangePanel.add(quickAccessBtn);
		channelChangePanel.add(makeRoomBtn);

		// 政煽舛左==============================================================

		JLabel userInfo = new JLabel("政煽舛左");
		userInfo.setFont(new Font("玄精壱拒", Font.BOLD, 30));
		userInfo.setForeground(Color.white);
		JLabel id = new JLabel("ID : ");
		id.setFont(new Font("玄精壱拒", Font.PLAIN, 25));
		id.setForeground(Color.white);
		JPanel pan = new JPanel();
		pan.setBackground(new Color(0, 0, 0, 0));
		pan.setOpaque(false);
		userId = new JLabel();
		userId.setFont(new Font("玄精壱拒", Font.PLAIN, 25));
		userId.setForeground(Color.white);
		pan.add(id);
		pan.add(userId);
		userInfoPanel.add(userInfo, "North");
		userInfoPanel.add(pan, "West");
		bottomPanel.add(userInfoPanel, "Center");

		Toolkit tk = Toolkit.getDefaultToolkit();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(tk.getImage("img/企奄叔壕井.jpg"), 0);
		jip = new JImagePanel(tk.getImage("img/企奄叔壕井.jpg"));
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
	}// 持失切

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
			System.out.println("辞獄人 羨紗獣 神嫌亜 降持馬心柔艦陥.");
			System.out.println(e);
		}
	}

	// 辰特結 脊舌獣
	public void inputChatRoom() {
		mf.closePanel();
	}

	// 企奄叔税 穿端辰特聖 是廃 鎧採 什傾球 適掘什
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

					if (line.indexOf("[詠紗源]") == -1
							&& line.indexOf("[錘慎切]還戚") == -1
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
						// 号戚 幻級嬢然聖凶
					} else if (line.equals("/roomout")) {
						// 号拭辞 蟹哀凶
					} else if (line.equals("/removeroom")) {
						// 号戚 搾醸聖凶
					} else if (line.indexOf("roomMsg") != -1) {
						String msg[] = line.split("/");
						System.out.println("辰特但: " + msg[1]);
						mf.chat.roomInfo.append(msg[1] + "\n");
						mf.chat.roomInfo.setCaretPosition(mf.chat.roomInfo
								.getDocument().getLength());
					} else {
						System.out.println("企奄叔: " + line);
						display.append(line + "\n");
						display.setCaretPosition(display.getDocument()
								.getLength());
					}
				}
				// 悪盗雁梅聖 井酔
				JOptionPane.showMessageDialog(WaitingPage.this,
						"号拭辞 悪盗 雁馬写柔艦陥.", "", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				System.out.println("client thread : " + e);
				JOptionPane.showMessageDialog(WaitingPage.this, "覗稽益轡聖 曽戟杯艦陥.");
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
				// FileReader研 昔切稽 馬澗 BufferedReader 梓端 持失
				BufferedReader br = new BufferedReader(new FileReader(file));

				name = br.readLine(); // 督析聖 廃 庚舌梢 石嬢紳陥.
				// address = br.readLine();

				while (name != null) {// EOF澗 null庚切研 匂敗馬壱 赤陥.

					System.out.println(name);// 石精 庚切伸聖 窒径廃陥.

					name = br.readLine();// 陥製 庚切伸聖 亜牽佃層陥.
				}

				br.close();// FileReader人澗 陥牽惟 紫遂 板 伽 丸焼爽嬢醤 廃陥.
			} else {
				System.out.println("督析拭 羨悦拝 呪 蒸柔艦陥.");
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

	void writeFriend() { // 督析拭 庁姥 脊径 五社球
		String friend = select_name;
		String address = select_address;

		File file = new File("TextFile/FriendList.txt"); // 督析爽社
		FileWriter writer = null;

		try {
			writer = new FileWriter(file, true);

			writer.write(friend);
			writer.write("/" + address);
			writer.write("\r\n");
			writer.flush();
			System.out.println(select_name + "庁姥研 蓄亜馬心柔艦陥");
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

	void removeFriend() { // 督析 庁姥 肢薦
		BufferedReader in = null;
		FileWriter pw = null;
		ArrayList<String> al = new ArrayList<String>();
		String friend = (String) table.getValueAt(selectedRow, 0); // 庁姥 葵
		String line = null;

		try {
			in = new BufferedReader(new FileReader("TextFile/FriendList.txt"));
			while ((line = in.readLine()) != null) {
				al.add(line); // al拭 乞窮 葵 隔奄

				for (String s : al) {
					String[] check = s.split("/"); // s研 戚硯/是帖 切牽奄 string壕伸拭 隔壱
					if (check[0].equals(friend)) { // 旭精 葵 溌昔
						System.out.println(s + "肢薦馬心柔艦陥.");
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

	void findFriend() { // 督析拭辞 庁姥 達奄
		BufferedReader bReader = null;

		try {
			String friend = findFriendtf.getText();
			File file = new File("TextFile/FriendList.txt");
			bReader = new BufferedReader(new FileReader(file));
			String st;
			while ((st = bReader.readLine()) != null) {
				if (st.equals(friend)) {
					System.out.println(friend + "達紹柔艦陥.");

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
		else if (e.getSource() == addpm_item) { // 識澱廃 伸引 楳 葵 亜閃人辞 細戚奄
			if (!select_name.equals(userId.getText())) {
				writeFriend(); // 督析拭 庁姥 蓄亜
			} else {
				JOptionPane.showMessageDialog(WaitingPage.this,
						"蟹澗 庁姥稽 蓄亜拝 呪 蒸柔艦陥.", "", JOptionPane.ERROR_MESSAGE);
			}
			tableModel_FriendsList.addRow(new Object[] {
					nowConnectAllTable.getValueAt(selectedRow, 0),
					nowConnectAllTable.getValueAt(selectedRow, 1) });
		}
		else if (e.getSource() == chat_item) {
			// listFriend();
		}
		else if (e.getSource() == deletepm_item) { // 庁姥肢薦
			removeFriend();
			tableModel_FriendsList.removeRow(selectedRow);
		}
		else if (e.getSource() == findAllBtn) { // 穿端砺戚鷺拭辞 庁姥 達奄
			String value = findAlltf.getText();
			
			for (int i = 0; i < tableModel_AllUserList.getRowCount(); i++) { 
				// 幻鉦 伊事廃 戚硯戚 穿端砺戚鷺拭 赤陥檎
				if (value.equals(tableModel_AllUserList.getValueAt(i, 0))) { 
					// 伊事廃 戚硯聖 妊獣
					nowConnectAllTable.setRowSelectionInterval(i, i); 
					// index0 稽採斗index1猿走税 楳(雌馬廃帖研 匂敗廃陥)研 薄仙税 識澱慎蝕拭 蓄亜杯艦陥.
				}
			}
		} else if (e.getSource() == findFriendBtn) { // 庁姥 達奄
			findFriend();
			String value = findFriendtf.getText(); // 達聖 庁姥税 葵
			// 穿端砺戚鷺拭 赤澗 庁姥 鯉系
			for (int i = 0; i < tableModel_FriendsList.getRowCount(); i++) { 
				// 旭精 葵戚 赤澗走 搾嘘背辞
				if (value.equals(tableModel_FriendsList.getValueAt(i, 0))) { 
					nowConnectFriendsTable.setRowSelectionInterval(i, i); // 達精 葵聖 鉢檎拭 妊獣
				}
			}
		}
		//号幻級奄 獄動 喚袈聖 井酔
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
	//号拭辞 刊亜 蟹穐聖凶 叔楳
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
		if (e.getButton() == MouseEvent.BUTTON3) { // 原酔什 酔著適遣獣
			table = (JTable) e.getSource(); // getSource(); = 戚坤闘研 降持獣轍
											// 梓端税 葵聖 亜閃紳陥
			selectedRow = table.rowAtPoint(e.getPoint()); // 原酔什亜 是帖廃 伸税 葵聖 亜閃神壱

			if (table == nowConnectAllTable) { // 幻鉦 穿端砺戚鷺戚虞檎
				menupm.show(table, e.getX(), e.getY()); // 砺戚鷺 是帖

				if (selectedRow != -1) {// 幻鉦拭 原酔什亜 識澱廃 楳戚 -1戚 焼諌凶 砺戚鷺鎧採研 識澱馬壱
										// 赤聖凶
					select_name = (String) table.getValueAt(selectedRow, 0); // 戚硯
																				// 葵聖
																				// 閤焼辞
					select_address = (String) table.getValueAt(selectedRow, 1);

					for (int i = 0; i < tableModel_FriendsList.getRowCount(); i++) {
						if (select_name.equals(tableModel_FriendsList
								.getValueAt(i, 0))) { // 識澱廃 楳税 戚硯戚 庁姥鯉系拭 赤澗走
														// 溌昔背辞
							JOptionPane
									.showMessageDialog(null, "戚耕 去系吉 庁姥脊艦陥."); // 戚耕去系吉庁姥虞檎井壱但"庁姥蓄亜神嫌"
							break;
						}
					}
				}
			} else if (table == nowConnectFriendsTable) { // 庁姥砺戚鷺戚虞檎
				deletepm.show(table, e.getX(), e.getY());
				int row = table.rowAtPoint(e.getPoint());
				if (row != -1) { // 伸戚 葵戚 赤生檎
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
