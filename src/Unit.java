			
import java.io.*;
import java.util.*;
//큰파일전달은 파일전용스레드 생성
public class Unit implements Serializable{ //직렬화된 객체
	
	//serialVersionUID는 통신하는 자바시스템간 동일해야한다 
	private static final long serialVersionUID = 1L;
	
	private String code;
	private int size;
	private ArrayList data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public ArrayList getData() {
		return data;
	}
	public void setData(ArrayList data) {
		this.data = data;
	}
	
	
	
}
