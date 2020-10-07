import javax.swing.*;
import java.awt.*;


public class PanelBackground extends JFrame{
	JPanel pnl;
	ImageIcon  icon;
	PanelBackground(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 400);
		
		// ���⼭ ����
		icon = new ImageIcon("Image/1.png"); //�гο� �׸��� �ְ� �������������� ����
		pnl = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(icon.getImage(), 0, 0, null);
                setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
                super.paintComponent(g);
            }
        };
        setContentPane(pnl);        
        //�������
        
		this.setVisible(true);
	}    

	public static void main(String args[]){
		new PanelBackground();
	}
}
