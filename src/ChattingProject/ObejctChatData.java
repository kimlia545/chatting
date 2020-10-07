package ChattingProject;

import java.io.Serializable;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class ObejctChatData implements Serializable {
	private String msg;
	private Vector varg0;
	private Vector varg1;
	private Vector roomNameVector;
	private static final long serialVersionUID = 1L;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Vector getVarg0() {
		return varg0;
	}

	public void setVarg0(Vector varg0) {
		this.varg0 = varg0;
	}

	public Vector getVarg1() {
		return varg1;
	}

	public void setVarg1(Vector varg1) {
		this.varg1 = varg1;
	}
	
	public Vector getRoomNameVector() {
		return roomNameVector;
	}

	public void setRoomNameVector(Vector roomNameVector) {
		this.roomNameVector = roomNameVector;
	}

	
	

	

	

	
	
}
