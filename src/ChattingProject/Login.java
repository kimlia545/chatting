package ChattingProject;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

public class Login extends JPanel implements ActionListener{
	JLabel chat_lbl,id_lbl,ip_lbl,ps_lbl;
	JTextField id_tf,ip_tf;
	JPasswordField pw_pf;
	JButton login_btn, join_btn;
	JPanel login_labPan, login_tfPan, login_sumPan, login_butPan, login_logPan, login_AllPan, loginTotalPan;
	MainFrame mf;
	Signup_Dialog su_dialogs;
	String pw;
	
	ImageIcon icon; //이미지
	JPanel login_imgPan;
	JLabel img_lbl;
	JImagePanel jip = null;
	
	Login(MainFrame mf){
		this.mf = mf;
		this.setSize(1024,768);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		  MediaTracker mt = new MediaTracker(this);
		  mt.addImage(tk.getImage("로그인창배경.gif"), 0);
		  jip = new JImagePanel( tk.getImage("img/로그인창배경.gif") );
		  jip.setLayout( null );
		  jip.setBounds( 0, 0, 100, 100 );
		  jip.setOpaque(false);
		
		chat_lbl = new JLabel();
		ImageIcon chat_img = new ImageIcon("img/chat.gif");
		chat_lbl = new JLabel(chat_img);
		
		id_lbl = new JLabel("I     D");
		id_lbl.setHorizontalAlignment(JLabel.CENTER);
		id_lbl.setForeground(new Color(255,255,255));
		ps_lbl = new JLabel("PASSWORD");
		ps_lbl.setHorizontalAlignment(JLabel.CENTER);
		ps_lbl.setForeground(new Color(255,255,255));
		ip_lbl = new JLabel("SERVER IP");
		ip_lbl.setHorizontalAlignment(JLabel.CENTER);
		ip_lbl.setForeground(new Color(255,255,255));
		login_labPan = new JPanel(new GridLayout(0,1,10,10));
		login_labPan.setOpaque(false);
		login_labPan.add(id_lbl); login_labPan.add(ps_lbl); login_labPan.add(ip_lbl);
		
		Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
		Border emptyBorder = BorderFactory.createEmptyBorder(1, 5, 1, 1);
		id_tf = new JTextField(20);
		id_tf.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		Border lineBorder3 = BorderFactory.createLineBorder(Color.WHITE, 2);
		Border emptyBorder3 = BorderFactory.createEmptyBorder(1, 5, 1, 1);
		pw_pf = new JPasswordField(20);
		pw_pf.setBorder(BorderFactory.createCompoundBorder(lineBorder3, emptyBorder3));
		pw_pf.setEchoChar('*');
		pw = ""; //tf_pw 필드에서 패스워드를 얻어옴
		Border lineBorder2 = BorderFactory.createLineBorder(Color.WHITE, 2);
		Border emptyBorder2 = BorderFactory.createEmptyBorder(1, 5, 1, 1);
		ip_tf = new JTextField(20);
		ip_tf.setBorder(BorderFactory.createCompoundBorder(lineBorder2, emptyBorder2));
		
		login_tfPan = new JPanel(new GridLayout(0,1,10,10));
		login_tfPan.setOpaque(false);
		login_tfPan.add(id_tf); login_tfPan.add(pw_pf); login_tfPan.add(ip_tf);
		
		login_sumPan = new JPanel();
		login_sumPan.setOpaque(false);
		login_sumPan.add(login_labPan); login_sumPan.add(login_tfPan);
		
		ImageIcon login_img = new ImageIcon("img/login.jpg");
		login_btn = new JButton(login_img);
		login_btn.setBorderPainted(false);
		login_btn.setContentAreaFilled(false);
		login_btn.setFocusPainted(false);
		login_btn.setPreferredSize(new Dimension(100,100));
		login_btn.addActionListener(this);
		
		ImageIcon join_img = new ImageIcon("img/join.png");
		join_btn = new JButton(join_img);
		join_btn.setContentAreaFilled(false);
		join_btn.setFocusPainted(false);
		//join_btn.setPreferredSize(new Dimension(100,30));
		join_btn.setBorderPainted(false);
		join_btn.addActionListener(this);
		login_butPan = new JPanel();
		login_butPan.setOpaque(false);
		login_butPan.add(join_btn);
		
		login_logPan = new JPanel();
		login_logPan.setOpaque(false);
		login_logPan.add(login_sumPan);
		login_logPan.add(login_btn);
		
		login_AllPan = new JPanel(new BorderLayout());
		login_AllPan.setOpaque(false);
		//login_AllPan.setBorder(BorderFactory.createEmptyBorder(250, 250, 250, 250));
		login_AllPan.add(chat_lbl,"North");
		login_AllPan.add(login_logPan,"Center");
		login_AllPan.add(login_butPan,"South");
		
		loginTotalPan = new JPanel(new FlowLayout());
		loginTotalPan.add(login_AllPan);
		loginTotalPan.setOpaque(false);
		
		loginTotalPan.setBounds(0, 0, 1024, 768);
		jip.setBounds(0, 0, 1024, 768);
		
		this.setLayout(null);
		this.add(loginTotalPan);
		this.add(jip);
		
		
		//미리 값주기
		id_tf.setText("a"+((int)(Math.random()*1000)));
		ip_tf.setText("127.0.0.1");
		
		setVisible(true);
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
	
	void readFromFile() { //로그인 화면 아이디 비밀번호 확인
		BufferedReader bReader = null;
		char[] secret_pw = pw_pf.getPassword();
		 //secret_pw 배열에 저장된 암호의 자릿수 만큼 for문 돌리면서 cha 에 한 글자씩 저장 
		for(char cha : secret_pw){Character.toString(cha); //cha 에 저장된 값 string으로 변환 
		//pw 에 저장하기, pw 에 값이 비어있으면 저장, 값이 있으면 이어서 저장하는 삼항연산자 
		pw += (pw.equals("")) ? ""+cha+"" : ""+cha+""; }
		
		try{
			String str;
			ArrayList<String[]> list = new ArrayList<String[]>();
			bReader = new BufferedReader(new FileReader("TextFile/MemberList.txt"));
			
			while((str=bReader.readLine())!=null){
				String [] array=str.split("/");
				list.add(array);
			}
				boolean isCheck=false;
				for(int i=0;i<list.size();i++){ //아이디와 비밀번호가 일치하면 true
					if(list.get(i)[0].equals(id_tf.getText())&&list.get(i)[1].equals(pw)){
						isCheck=true;
						break;
					}
				}		
					if(isCheck){
							JOptionPane.showMessageDialog(null,"로그인 되었습니다.");
							mf.closePanel();
					} else {
							JOptionPane.showMessageDialog(null,"가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
							
							id_tf.setText("");
							pw_pf.setText("");
					}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==login_btn){
			JOptionPane.showMessageDialog(null,"로그인 되었습니다.");
			mf.closePanel();
			//readFromFile();
			mf.wait.userId.setText(id_tf.getText());
		}else if(e.getSource()==join_btn){
			su_dialogs = new Signup_Dialog();
			su_dialogs.setVisible(true);
		}
		
	}
	
	public String getID(){
		return id_tf.getText();
	}
	
	public String getIP(){
		return ip_tf.getText();
	}
	
	class Signup_Dialog extends JDialog implements ActionListener{
		JLabel su_memberShip, su_name, su_id, su_pw, su_nickname, su_pw2,
		space_lb, space_lb2, img_lb;
		JTextField su_idtf,  su_nntf;
		JButton su_btn, su_exit, su_checkId;
		JPanel northPnl, lblPnl, tfPnl, centerPnl, southPnl, su_AllPan, checkPnl, tf_checkPnl, imgPnl;
		JPasswordField su_pwtf, su_pwtf2;
		String pw2;
		
		Signup_Dialog(){
			setSize(415,550);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			su_pwtf = new JPasswordField(15);
			su_pwtf.setBorder(new RoundedBorder());
			su_pwtf2 = new JPasswordField(15);
			su_pwtf2.setBorder(new RoundedBorder());
			su_pwtf.setEchoChar('*');
			pw2 = ""; //tf_pw 필드에서 패스워드를 얻어옴
			
			northPnl = new JPanel();
			northPnl.setBackground(new Color(255,255,255));
			ImageIcon su_top_img = new ImageIcon("img/회원가입상단.jpg");
			su_memberShip = new JLabel(su_top_img);
			northPnl.add(su_memberShip);
			
			lblPnl = new JPanel(new GridLayout(0,1,10,10));
			lblPnl.setBackground(new Color(255,255,255));
			ImageIcon id = new ImageIcon("img/아이디.jpg");
			su_id = new JLabel(id);
			su_id.setHorizontalAlignment(JLabel.RIGHT);
			
			ImageIcon password_img = new ImageIcon("img/비밀번호.jpg");
			su_pw = new JLabel( password_img);
			su_pw.setHorizontalAlignment(JLabel.RIGHT);
			ImageIcon password2_img = new ImageIcon("img/비밀번호확인.jpg");
			su_pw2 = new JLabel(password2_img);
			su_pw2.setHorizontalAlignment(JLabel.RIGHT);
			
			ImageIcon nickname_img = new ImageIcon("img/닉네임.jpg");
			su_nickname = new JLabel(nickname_img);
			su_nickname.setHorizontalAlignment(JLabel.RIGHT);
			lblPnl.add(su_id);
			lblPnl.add(su_pw); 
			lblPnl.add(su_pw2); 
			lblPnl.add(su_nickname);
			
			tfPnl = new JPanel(new GridLayout(0,1,10,10));
			tfPnl.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
			tfPnl.setBackground(new Color(255,255,255));
			tf_checkPnl = new JPanel(new BorderLayout());
			tf_checkPnl.setBackground(new Color(255,255,255));
			checkPnl = new JPanel();
			checkPnl.setBackground(new Color(255,255,255));
			su_idtf = new JTextField(15);
			su_idtf.setBorder(new RoundedBorder());
			ImageIcon su_checkId_img = new ImageIcon("img/중복확인.jpg");
			su_checkId = new JButton(su_checkId_img);
			su_checkId.setSize(15,15);
			su_checkId.setBorderPainted(false);
			su_checkId.setContentAreaFilled(false);
			su_checkId.setFocusPainted(false);
			checkPnl.add(su_idtf);
			checkPnl.add(su_checkId);
			su_nntf = new JTextField(15);
			su_nntf.setBorder(new RoundedBorder());
			tfPnl.add(su_pwtf); 
			tfPnl.add(su_pwtf2); 
			tfPnl.add(su_nntf);
			tf_checkPnl.add(checkPnl,"North");
			tf_checkPnl.add(tfPnl,"West");
			su_checkId.addActionListener(this);
			
			centerPnl = new JPanel();
			centerPnl.setBackground(new Color(255,255,255));
			centerPnl.add(lblPnl);
			centerPnl.add(tf_checkPnl);
			
			imgPnl = new JPanel(new BorderLayout());
			southPnl = new JPanel();
			southPnl.setBackground(new Color(255,255,255));
			ImageIcon join2_img = new ImageIcon("img/join2.png");
			su_btn = new JButton(join2_img);
			su_btn.setBorderPainted(false);
			su_btn.setContentAreaFilled(false);
			su_btn.setFocusPainted(false);
			ImageIcon cancel_img = new ImageIcon("img/cancel.png");
			su_exit = new JButton(cancel_img);
			su_exit.setBorderPainted(false);
			su_exit.setContentAreaFilled(false);
			su_exit.setFocusPainted(false);
			space_lb = new JLabel("");
			space_lb2 = new JLabel("");
			ImageIcon su_bottom = new ImageIcon("img/회원가입하단.jpg");
			img_lb = new JLabel(su_bottom);
			southPnl.add(space_lb);
			southPnl.add(su_btn); 
			southPnl.add(su_exit);
			southPnl.add(space_lb2);
			imgPnl.add(southPnl,"Center");
			imgPnl.add(img_lb,"South");
			su_btn.addActionListener(this);
			su_exit.addActionListener(this);
			//su_checkId.addActionListener(this);
			
			su_AllPan  = new JPanel(new BorderLayout());
			su_AllPan.add(northPnl,"North"); 
			su_AllPan.add(centerPnl,"Center"); 
			su_AllPan.add(imgPnl,"South");
			
			add(su_AllPan);
		}
		
		class RoundedBorder extends AbstractBorder {//"ID"필드의 둥근 모서리 설정을 위한 클래스 
			   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) { 
			      Graphics2D g2 = (Graphics2D) g; 
			      int arc = 10;//모서리 둥글기 설정 
			      g2.drawRoundRect(x, y, width-2, height-2, arc, arc); //둥근 모서리 테두리 그리기 
			     
			   }
		}

		void writeToFile(){ //아이디 비밀번호 닉네임 저장
			char[] secret_pw = su_pwtf.getPassword();
			 //secret_pw 배열에 저장된 암호의 자릿수 만큼 for문 돌리면서 cha 에 한 글자씩 저장 
			for(char cha : secret_pw){Character.toString(cha); //cha 에 저장된 값 string으로 변환 
			//pw 에 저장하기, pw 에 값이 비어있으면 저장, 값이 있으면 이어서 저장하는 삼항연산자 
			pw2+= (pw2.equals("")) ? ""+cha+"" : ""+cha+""; }
			String id = su_idtf.getText();
			String ps = pw2;
			String nick = su_nntf.getText();
			
			File file = new File("TextFile/MemberList.txt");
			FileWriter writer = null;
			
			try{  
				writer = new FileWriter(file,true);
				
				writer.write(id+"/");
				writer.write(ps+"/");
				writer.write(nick+"/");
				writer.write("\r\n"); //엔터
				writer.flush();
				
				System.out.println("회원정보를 저장하였습니다.");
			} catch(IOException e) {
				e.printStackTrace();
			}finally {
				try{
					if(writer != null) writer.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}		
		}
		
		void checkIdFile() { //아이디 중복 확인
			BufferedReader bReader = null;
			
			try{
				String str;
				ArrayList<String[]> list = new ArrayList<String[]>();
				bReader = new BufferedReader(new FileReader("TextFile/MemberList.txt"));
				
				
			while((str=bReader.readLine()) != null){ //만약 파일에 값이 있다면
				String [] array=str.split("/"); // /으로 값을 나눠서
				list.add(array); //list에 array 담기
		    } //while
			
			boolean isCheckId=false;
			for(int i=0;i<list.size();i++){	 //아이디는 각 줄에서 [0] 담겨있음 		
				if(list.get(i)[0].equals(su_idtf.getText())){ //입력한 아이디값과 비교
					isCheckId=true; 
					break;
				}
			}
				
			if(isCheckId){ 
				JOptionPane.showMessageDialog(this, "이미 사용중이거나 탈퇴한 아이디입니다.");
			}else{
				JOptionPane.showMessageDialog(this, "사용할 수 있는 아이디입니다.");
			}
				
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) { //회원가입
			if(e.getSource()==su_btn){ //아이디 비밀번호 닉네임 입력 확인
				if((su_idtf.getText().equals(""))||(su_idtf.getText()==null)) { //입력하지 않았을시
					JOptionPane.showMessageDialog(null, "아이디를 입력하세요.");
				}else if((su_pwtf.getText().equals(""))||(su_pwtf.getText()==null)) {
					JOptionPane.showMessageDialog(null, "비밀번호를 입력하세요.");
				}else if((su_pwtf2.getText().equals(""))||(su_pwtf2.getText()==null)) {
					JOptionPane.showMessageDialog(null, "비밀번호 확인을 입력하세요.");
				}else if((su_nntf.getText().equals(""))||(su_nntf.getText().equals(null))) {
					JOptionPane.showMessageDialog(null, "닉네임을 입력하세요.");
				}else if(!su_pwtf.getText().equals(su_pwtf2.getText())){ //비밀번호와 비밀번호확인이 같은지 확인
					JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
				}else{		//회원가입 성공
					writeToFile();
					JOptionPane.showMessageDialog(null, "회원가입을 완료하였습니다.");
					this.dispose();
				}
			}else if(e.getSource()==su_exit){ //취소버튼 시 화면 끄기
				this.dispose();
			}else if(e.getSource()==su_checkId){ //아이디 중복 확인
				checkIdFile();
			}
		}
	}
}


