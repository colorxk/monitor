package tools;

import java.util.HashMap;
import java.util.Iterator;

import view.southUI;
import view.westUI;

public class ManageClientThread {

	//初始化静态的HashMap实例
	public static HashMap hm = new HashMap<String, OperationClient>();
	
	// 向hm中添加一个客户端通讯线程
	public static void addClientThread(String ip, OperationClient otc) {
		hm.put(ip, otc);
	}
	
	//根据IP移除一个线程
	public static OperationClient delClientThread(String ip) {
		hm.remove(ip);
		westUI westUI = new westUI();
		westUI.upateOnLineIP();
		southUI south = new southUI();
		south.updateRemind("客户端: "+ip+" 与服务器断开连接...");
		return null;
	}
	
	//根据用户编号返回一个线程
	public static OperationClient getClientThread(String ip) {
		return (OperationClient) hm.get(ip);
	}
	
	// 返回当前在线的IP
	public static String getAllOnLineIP() {
		// 使用迭代器完成
		Iterator it = hm.keySet().iterator();
		String res = "";
		while (it.hasNext()) {
			res += it.next().toString() + " ";
		}
		return res;
	}
}
