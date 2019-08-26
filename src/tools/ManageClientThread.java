package tools;

import java.util.HashMap;
import java.util.Iterator;

import view.southUI;
import view.westUI;

public class ManageClientThread {

	//��ʼ����̬��HashMapʵ��
	public static HashMap hm = new HashMap<String, OperationClient>();
	
	// ��hm�����һ���ͻ���ͨѶ�߳�
	public static void addClientThread(String ip, OperationClient otc) {
		hm.put(ip, otc);
	}
	
	//����IP�Ƴ�һ���߳�
	public static OperationClient delClientThread(String ip) {
		hm.remove(ip);
		westUI westUI = new westUI();
		westUI.upateOnLineIP();
		southUI south = new southUI();
		south.updateRemind("�ͻ���: "+ip+" ��������Ͽ�����...");
		return null;
	}
	
	//�����û���ŷ���һ���߳�
	public static OperationClient getClientThread(String ip) {
		return (OperationClient) hm.get(ip);
	}
	
	// ���ص�ǰ���ߵ�IP
	public static String getAllOnLineIP() {
		// ʹ�õ��������
		Iterator it = hm.keySet().iterator();
		String res = "";
		while (it.hasNext()) {
			res += it.next().toString() + " ";
		}
		return res;
	}
}
