import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MousePopTest extends JFrame implements MouseListener, ActionListener{
	JPopupMenu pm;
	JMenuItem pm_item1,pm_item2,pm_item3,pm_item4;
	JPanel pnl;
	JTextField tf;
	MousePopTest(){
		pm = new JPopupMenu();
	    pm_item1 = new JMenuItem("강제퇴장");
	    pm_item2 = new JMenuItem("등급변경");
	    
	 // 팝업메뉴에 메뉴아이템 추가
        pm.add(pm_item1);
        pm.addSeparator(); // 구분선
        pm.add(pm_item2);
 
        add(pm); // 팝업메뉴를 프레임에 추가
        
        pm_item1.addActionListener(this);
        pm_item2.addActionListener(this);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(this);
        setLayout(null);
        
        pnl = new JPanel();        
        pnl.setBounds(0, 0, 300, 400);
        pnl.setBackground(Color.PINK);
        
        tf = new JTextField(20);
        pnl.add(tf);
        
        add(pnl);
        setSize(300, 400);
        setVisible(true);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// 오른쪽 마우스 버튼을 누르면 PopupMenu를 화면에 보여준다.
        if (e.getButton() == MouseEvent.BUTTON3)
            pm.show(this, e.getX(), e.getY());
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
    public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==pm_item1){
			tf.setText("전체선택");
		}else if(e.getSource()==pm_item2){
			tf.setText("복사하기");
		}
		
	}
	public static void main(String[] args) {
		new MousePopTest();
	}
		
}