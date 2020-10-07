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
	    pm_item1 = new JMenuItem("��������");
	    pm_item2 = new JMenuItem("��޺���");
	    
	 // �˾��޴��� �޴������� �߰�
        pm.add(pm_item1);
        pm.addSeparator(); // ���м�
        pm.add(pm_item2);
 
        add(pm); // �˾��޴��� �����ӿ� �߰�
        
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
		// ������ ���콺 ��ư�� ������ PopupMenu�� ȭ�鿡 �����ش�.
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
			tf.setText("��ü����");
		}else if(e.getSource()==pm_item2){
			tf.setText("�����ϱ�");
		}
		
	}
	public static void main(String[] args) {
		new MousePopTest();
	}
		
}