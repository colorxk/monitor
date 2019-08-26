package model;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import common.Message;
import common.MessageType;
import encryption.AES;
import encryption.AESEn;
import encryption.RSADEn;
import tools.ManageClientThread;
import tools.OperationClient;
import view.southUI;
import view.westUI;

public class mainServer {
	public ServerSocket server;
	static boolean b = true;
	
	public void mainServer() {
		try {
			System.out.println("���������ڼ���9999...");
			ServerSocket ss = new ServerSocket(9999);
			while (b) {
				Socket s = ss.accept();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				String ip = s.getInetAddress().getHostAddress();
				System.out.println("�ͻ��˵�IP��ַΪ:"+ip);
				
				//����RSA��Կ
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				String RSApublicKey = (String) ois.readObject();
				
				//ʹ��RSA��Կ����AES��Կ
				RSADEn RSA = new RSADEn();
				
				String AESPrivateKey = AES.getAESPrivateKey();
				String messageEn = RSA.encrypt(AESPrivateKey,RSApublicKey); 
				
				//���ͼ��ܺ��AES��Կ
				oos.writeObject(messageEn);
				
				//����AES�ӽ���
				Message m = new Message();
				m.setMesType(MessageType.message_comm);
				String name = "������";
				byte[] enmess = AESEn.encrypt(name, AESPrivateKey);
		        for(int i=0;i<enmess.length;i++) {
		        	System.out.print(enmess[i]+" ");
		        }
		        System.out.println();
				System.out.println("��Կ��:"+AESPrivateKey);
				m.setContent(enmess);
				oos.writeObject(m);
				
				//�����Կͻ��˵��̲߳���
				OperationClient operation = new OperationClient(s,AESPrivateKey);
				//�����߳���ӵ��̹߳����з���ͨ��IP��ȡ���߳�
				ManageClientThread.addClientThread(ip,operation);
				
				westUI westUI = new westUI();
				westUI.upateOnLineIP();
				southUI south = new southUI();
				south.updateRemind("�ͻ���: "+ip+" ���ӵ��˷�����");
				
				//������ͻ���ͨѶ���߳�
				operation.start();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.b = false;
			e.printStackTrace();
			//System.exit(0);
		}
		
		
	}

}
