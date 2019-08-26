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
			System.out.println("服务器正在监听9999...");
			ServerSocket ss = new ServerSocket(9999);
			while (b) {
				Socket s = ss.accept();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				String ip = s.getInetAddress().getHostAddress();
				System.out.println("客户端的IP地址为:"+ip);
				
				//接收RSA公钥
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				String RSApublicKey = (String) ois.readObject();
				
				//使用RSA公钥加密AES秘钥
				RSADEn RSA = new RSADEn();
				
				String AESPrivateKey = AES.getAESPrivateKey();
				String messageEn = RSA.encrypt(AESPrivateKey,RSApublicKey); 
				
				//发送加密后的AES秘钥
				oos.writeObject(messageEn);
				
				//测试AES加解密
				Message m = new Message();
				m.setMesType(MessageType.message_comm);
				String name = "张晓坤";
				byte[] enmess = AESEn.encrypt(name, AESPrivateKey);
		        for(int i=0;i<enmess.length;i++) {
		        	System.out.print(enmess[i]+" ");
		        }
		        System.out.println();
				System.out.println("秘钥是:"+AESPrivateKey);
				m.setContent(enmess);
				oos.writeObject(m);
				
				//建立对客户端的线程操作
				OperationClient operation = new OperationClient(s,AESPrivateKey);
				//将该线程添加到线程管理中方便通过IP获取该线程
				ManageClientThread.addClientThread(ip,operation);
				
				westUI westUI = new westUI();
				westUI.upateOnLineIP();
				southUI south = new southUI();
				south.updateRemind("客户端: "+ip+" 连接到了服务器");
				
				//开启与客户端通讯的线程
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
