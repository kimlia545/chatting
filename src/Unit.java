			
import java.io.*;
import java.util.*;
//ū���������� �������뽺���� ����
public class Unit implements Serializable{ //����ȭ�� ��ü
	
	//serialVersionUID�� ����ϴ� �ڹٽý��۰� �����ؾ��Ѵ� 
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
