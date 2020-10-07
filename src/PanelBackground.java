import javax.swing.*;
import java.awt.*;


public class PanelBackground extends JFrame{
	JPanel pnl;
	ImageIcon  icon;
	PanelBackground(){
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(500, 400);
		
		// 여기서 부터
		icon = new ImageIcon("Image/1.png"); //패널에 그림을 넣고 컨텐츠페인으로 지정
		pnl = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(icon.getImage(), 0, 0, null);
                setOpaque(false); //그림을 표시하게 설정,투명하게 조절
                super.paintComponent(g);
            }
        };
        setContentPane(pnl);        
        //여기까지
        
		this.setVisible(true);
	}    

	public static void main(String args[]){
		new PanelBackground();
	}
}
