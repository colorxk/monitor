package model;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Message;
import common.MessageType;
import tools.OperationClient;
import view.RemoteWindow;

public class RemoteServer extends Thread{
	  public static boolean b = true;
	  String IP;
	  public RemoteServer(String IP) {
		  this.IP = IP;
		  b = true;
	  }
	  
	  public void run() {
		  try {
			  Message message = new Message();
	          message.setMesType(MessageType.message_remote);
			  OperationClient sc = tools.ManageClientThread.getClientThread(IP);
			  ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
		      oos.writeObject(message);
			  ServerSocket server = new ServerSocket(8000);
		      System.out.println("Զ�̷������Ѿ���������");
		      Socket socket = server.accept();//�ȴ���������,��������
		      System.out.println(socket.getInetAddress().getHostAddress()+" �ͻ������ӳɹ�...");
		      DataInputStream dis = new DataInputStream(socket.getInputStream());
		      RemoteWindow cw = new RemoteWindow(IP);
		      byte[] imageBytes;
		      while(b){
		          imageBytes = new byte[dis.readInt()];   //���õ������������鳤��
		          dis.readFully(imageBytes);  //���е����ݴ�ŵ�byte��
		          cw.repainImage(imageBytes);
		      }
		      dis.close();
		      socket.close();
		      server.close();
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
	  }
}
