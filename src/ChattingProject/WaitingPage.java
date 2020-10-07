//대기실 패널

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

	String name;// 친구값
	String address; // 주소값
	JTable table;

	// 친구찾기
	JTextField findFriendtf, findAlltf;
	JButton findAllBtn, findFriendBtn;
	JScrollPane allUserListScrollPane, friendUserListScrollPane;

	// 서버와 연결을 위한
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Socket sock;

	int selectedRow; // 마우스로 클릭한 열의 값
	JImagePanel jip = null;

	boolean isChatTime;
	boolean is_joinroom; // 참가인지 생성인지
	// 현재 참여중인 채팅방이름 = 없을시 null
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
		// 패널 생성==============================================================

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

		// 패널 레이아웃==============================================================

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

		// 채널변경 버튼==============================================================

		namelbl = new JLabel("대기실");// 대기실
		namelbl.setFont(new Font("맑은고딕", Font.BOLD, 20));
		namelbl.setForeground(Color.WHITE);
		namelbl.setOpaque(false);
		namelbl.setBackground(new Color(0, 0, 0, 0));
		chanelChangeBtn = new JButton("채널변경");
		chanelChangeBtn.setBackground(new Color(125,159,209));
		chanelChangeBtn.setForeground(Color.white);
		channelChangePanel.add(namelbl);
		channelChangePanel.add(chanelChangeBtn);
		channelChangePanel.setOpaque(false);
		channelChangePanel.setBackground(new Color(0, 0, 0, 0));
		topPanel.add(channelChangePanel, "West");

		// 채팅리스트
		// 테이블==============================================================

		String chatListHeader[] = { "번호", "상태", "제목", "방장", "인원" };
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

		// 채팅방 리스트 마우스액션
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

		// 유저리스트
		// 테이블==============================================================

		userListTab = new JTabbedPane();

		// 친구
		// 찾기======================================================================
		findAlltf = new JTextField(10);
		findAllBtn = new JButton("찾기");
		findAllBtn.setBackground(new Color(175, 224, 230));
		findAllBtn.setForeground(Color.white);

		findAllPanel.add(findAlltf);
		findAllPanel.add(findAllBtn);

		findFriendtf = new JTextField(10);
		findFriendBtn = new JButton("찾기");
		findFriendBtn.setBackground(new Color(175, 224, 230));
		findFriendBtn.setForeground(Color.white);
		findFriendPanel.add(findFriendtf);
		findFriendPanel.add(findFriendBtn);

		// topPanel.add(findPersonPanel, "East");

		findAllBtn.addActionListener(this);
		findFriendBtn.addActionListener(this);

		// 현재 접속중인 전체유저 테이블
		String allUserListHeader[] = { "이름", "위치" };
		/*
		 * String allUserListContents[][] = { { "a", "대기실" }, { "ㅠㅠㅠㅠ", "1번방" },
		 * { "ㅁㄴㅇ", "???" } };
		 */

		tableModel_AllUserList = new DefaultTableModel(null, allUserListHeader);
		nowConnectAllTable = new JTable(tableModel_AllUserList);
		JTableHeader ncahead = nowConnectAllTable.getTableHeader();
		ncahead.setBackground(new Color(130,153,187));
		ncahead.setForeground(Color.white);
		JScrollPane allUserListScrollPane = new JScrollPane(nowConnectAllTable);
		nowConnectAllTable.addMouseListener(this);

		// 현재 접속중인 친구 테이블
		String friendUserListHeader[] = { "이름", "위치" };
		// String friendUserListContents[][] = { { "a", "대기실" } };

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

		userListTab.add("전체", nowConnectAllPanel);
		userListTab.add("친구", nowConnectFriendsPanel);
		userListTab.setPreferredSize(new Dimension(290, 335));
		userListPanel.add(userListTab);

		nowConnectFriendsTable.addMouseListener(this);

		centerPanel.add(userListPanel, "East");

		menupm = new JPopupMenu(); // 전체에서 친구추가
		addpm_item = new JMenuItem("친구추가");
		chat_item = new JMenuItem("1:1채팅");

		deletepm = new JPopupMenu();
		deletepm_item = new JMenuItem("친구삭제"); // 친구테이블에서 친구삭제

		// 팝업메뉴에 메뉴아이템 추가//
		menupm.add(addpm_item);
		menupm.addSeparator(); // 구분선
		menupm.add(chat_item);

		deletepm.add(deletepm_item);
		add(deletepm); // 팝업메뉴를 프레임에 추가

		addpm_item.addActionListener(this); // 메뉴버튼에 액션리스너 달기
		chat_item.addActionListener(this);
		deletepm_item.addActionListener(this);

		// 대기실 전체
		// 채팅==============================================================

		allChatPanel.setPreferredSize(new Dimension(600, 300));
		JPanel chatPan2 = new JPanel();
		display = new JTextArea();
		display.setEditable(false);
		JScrollPane chatSp = new JScrollPane(display);
		chatTf = new JTextField(26);
		chatTf.registerKeyboardAction(this, "send",
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_FOCUSED);
		message = new JButton("보내기");
		message.setBackground(new Color(175, 224, 230));
		message.setForeground(Color.white);
		message.setActionCommand("send");
		message.addActionListener(this);
		chatPan2.add(chatTf);
		chatPan2.add(message);
		allChatPanel.add(display, "Center");
		allChatPanel.add(chatPan2, "South");
		bottomPanel.add(allChatPanel, "West");

		// 바로입장, 방만들기
		// 버튼==============================================================

		quickAccessBtn = new JButton("바로입장");
		quickAccessBtn.setBackground(new Color(125,159,209));
		quickAccessBtn.setForeground(Color.white);
		quickAccessPanel.add(quickAccessBtn);

		makeRoomBtn = new JButton("방만들기");
		makeRoomBtn.setBackground(new Color(125,159,209));
		makeRoomBtn.setForeground(Color.white);
		quickAccessPanel.add(makeRoomBtn);
		makeRoomBtn.addActionListener(this);

		channelChangePanel.add(quickAccessBtn);
		channelChangePanel.add(makeRoomBtn);

		// 유저정보==============================================================

		JLabel userInfo = new JLabel("유저정보");
		userInfo.setFont(new Font("맑은고딕", Font.BOLD, 30));
		userInfo.setForeground(Color.white);
		JLabel id = new JLabel("ID : ");
		id.setFont(new Font("맑은고딕", Font.PLAIN, 25));
		id.setForeground(Color.white);
		JPanel pan = new JPanel();
		pan.setBackground(new Color(0, 0, 0, 0));
		pan.setOpaque(false);
		userId = new JLabel();
		userId.setFont(new Font("맑은고딕", Font.PLAIN, 25));
		userId.setForeground(Color.white);
		pan.add(id);
		pan.add(userId);
		userInfoPanel.add(userInfo, "North");
		userInfoPanel.add(pan, "West");
		bottomPanel.add(userInfoPanel, "Center");

		Toolkit tk = Toolkit.getDefaultToolkit();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(tk.getImage("img/대기실배경.jpg"), 0);
		jip = new JImagePanel(tk.getImage("img/대기실배경.jpg"));
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
	}// 생성자

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
			System.out.println("서버와 접속시 오류가 발생하였습니다.");
			System.out.println(e);
		}
	}

	// 채팅룸 입장시
	public void inputChatRoom() {
		mf.closePanel();
	}

	// 대기실의 전체채팅을 위한 내부 스레드 클래스
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

					if (line.indexOf("[귓속말]") == -1
							&& line.indexOf("[운영자]님이") == -1
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
						// 방이 만들어졌을때
					} else if (line.equals("/roomout")) {
						// 방에서 나갈때
					} else if (line.equals("/removeroom")) {
						// 방이 비었을때
					} else if (line.indexOf("roomMsg") != -1) {
						String msg[] = line.split("/");
						System.out.println("채팅창: " + msg[1]);
						mf.chat.roomInfo.append(msg[1] + "\n");
						mf.chat.roomInfo.setCaretPosition(mf.chat.roomInfo
								.getDocument().getLength());
					} else {
						System.out.println("대기실: " + line);
						display.append(line + "\n");
						display.setCaretPosition(display.getDocument()
								.getLength());
					}
				}
				// 강퇴당했을 경우
				JOptionPane.showMessageDialog(WaitingPage.this,
						"방에서 강퇴 당하셨습니다.", "", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				System.out.println("client thread : " + e);
				JOptionPane.showMessageDialog(WaitingPage.this, "프로그램을 종료합니다.");
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
				// FileReader를 인자로 하는 BufferedReader 객체 생성
				BufferedReader br = new BufferedReader(new FileReader(file));

				name = br.readLine(); // 파일을 한 문장씩 읽어온다.
				// address = br.readLine();

				while (name != null) {// EOF는 null문자를 포함하고 있다.

					System.out.println(name);// 읽은 문자열을 출력한다.

					name = br.readLine();// 다음 문자열을 가르켜준다.
				}

				br.close();// FileReader와는 다르게 사용 후 꼭 닫아주어야 한다.
			} else {
				System.out.println("파일에 접근할 수 없습니다.");
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

	void writeFriend() { // 파일에 친구 입력 메소드
		String friend = select_name;
		String address = select_address;

		File file = new File("TextFile/FriendList.txt"); // 파일주소
		FileWriter writer = null;

		try {
			writer = new FileWriter(file, true);

			writer.write(friend);
			writer.write("/" + address);
			writer.write("\r\n");
			writer.flush();
			System.out.println(select_name + "친구를 추가하였습니다");
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

	void removeFriend() { // 파일 친구 삭제
		BufferedReader in = null;
		FileWriter pw = null;
		ArrayList<String> al = new ArrayList<String>();
		String friend = (String) table.getValueAt(selectedRow, 0); // 친구 값
		String line = null;

		try {
			in = new BufferedReader(new FileReader("TextFile/FriendList.txt"));
			while ((line = in.readLine()) != null) {
				al.add(line); // al에 모든 값 넣기

				for (String s : al) {
					String[] check = s.split("/"); // s를 이름/위치 자르기 string배열에 넣고
					if (check[0].equals(friend)) { // 같은 값 확인
						System.out.println(s + "삭제하였습니다.");
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

	void findFriend() { // 파일에서 친구 찾기
		BufferedReader bReader = null;

		try {
			String friend = findFriendtf.getText();
			File file = new File("TextFile/FriendList.txt");
			bReader = new BufferedReader(new FileReader(file));
			String st;
			while ((st = bReader.readLine()) != null) {
				if (st.equals(friend)) {
					System.out.println(friend + "찾았습니다.");

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
		else if (e.getSource() == addpm_item) { // 선택한 열과 행 값 가져와서 붙이기
			if (!select_name.equals(userId.getText())) {
				writeFriend(); // 파일에 친구 추가
			} else {
				JOptionPane.showMessageDialog(WaitingPage.this,
						"나는 친구로 추가할 수 없습니다.", "", JOptionPane.ERROR_MESSAGE);
			}
			tableModel_FriendsList.addRow(new Object[] {
					nowConnectAllTable.getValueAt(selectedRow, 0),
					nowConnectAllTable.getValueAt(selectedRow, 1) });
		}
		else if (e.getSource() == chat_item) {
			// listFriend();
		}
		else if (e.getSource() == deletepm_item) { // 친구삭제
			removeFriend();
			tableModel_FriendsList.removeRow(selectedRow);
		}
		else if (e.getSource() == findAllBtn) { // 전체테이블에서 친구 찾기
			String value = findAlltf.getText();
			
			for (int i = 0; i < tableModel_AllUserList.getRowCount(); i++) { 
				// 만약 검색한 이름이 전체테이블에 있다면
				if (value.equals(tableModel_AllUserList.getValueAt(i, 0))) { 
					// 검색한 이름을 표시
					nowConnectAllTable.setRowSelectionInterval(i, i); 
					// index0 로부터index1까지의 행(상하한치를 포함한다)를 현재의 선택영역에 추가합니다.
				}
			}
		} else if (e.getSource() == findFriendBtn) { // 친구 찾기
			findFriend();
			String value = findFriendtf.getText(); // 찾을 친구의 값
			// 전체테이블에 있는 친구 목록
			for (int i = 0; i < tableModel_FriendsList.getRowCount(); i++) { 
				// 같은 값이 있는지 비교해서
				if (value.equals(tableModel_FriendsList.getValueAt(i, 0))) { 
					nowConnectFriendsTable.setRowSelectionInterval(i, i); // 찾은 값을 화면에 표시
				}
			}
		}
		//방만들기 버튼 눌렀을 경우
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
	//방에서 누가 나갔을때 실행
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
		if (e.getButton() == MouseEvent.BUTTON3) { // 마우스 우측클릭시
			table = (JTable) e.getSource(); // getSource(); = 이벤트를 발생시킨
											// 객체의 값을 가져온다
			selectedRow = table.rowAtPoint(e.getPoint()); // 마우스가 위치한 열의 값을 가져오고

			if (table == nowConnectAllTable) { // 만약 전체테이블이라면
				menupm.show(table, e.getX(), e.getY()); // 테이블 위치

				if (selectedRow != -1) {// 만약에 마우스가 선택한 행이 -1이 아닐때 테이블내부를 선택하고
										// 있을때
					select_name = (String) table.getValueAt(selectedRow, 0); // 이름
																				// 값을
																				// 받아서
					select_address = (String) table.getValueAt(selectedRow, 1);

					for (int i = 0; i < tableModel_FriendsList.getRowCount(); i++) {
						if (select_name.equals(tableModel_FriendsList
								.getValueAt(i, 0))) { // 선택한 행의 이름이 친구목록에 있는지
														// 확인해서
							JOptionPane
									.showMessageDialog(null, "이미 등록된 친구입니다."); // 이미등록된친구라면경고창"친구추가오류"
							break;
						}
					}
				}
			} else if (table == nowConnectFriendsTable) { // 친구테이블이라면
				deletepm.show(table, e.getX(), e.getY());
				int row = table.rowAtPoint(e.getPoint());
				if (row != -1) { // 열이 값이 있으면
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
