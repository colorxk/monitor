package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import common.Message;
import common.MessageType;
import model.RemoteServer;
import model.sendFileServer;
import tools.OperationClient;



public class eastUI extends JPanel implements ActionListener{
	static String IP = "100.100.100.100";
	JButton jb1;
	JButton jb2;
	JButton jb3;
	JButton jb4;
	JButton jb5;
	
	public eastUI(String IP){
		this.IP = IP;
		this.setBackground(Color.lightGray);
		this.setPreferredSize(new Dimension(150,0));
		this.setLayout(new FlowLayout());
		jb1 = new JButton("�ļ��ϴ�");
		jb1.addActionListener(this);
		jb2 = new JButton("�ļ�����");
		jb2.addActionListener(this);
		jb3 = new JButton("������");
		jb3.addActionListener(this);
		jb4 = new JButton("HTTP����");
		jb4.addActionListener(this);
		jb5 = new JButton("�ļ�����");
		jb5.addActionListener(this);
		this.add(jb1);
		this.add(jb2);
		this.add(jb3);
		this.add(jb4);
		this.add(jb5);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command =e.getActionCommand();  
		southUI south = new southUI();
		centerUI center = new centerUI();
		if(IP==null) {
    		JOptionPane.showMessageDialog(center, "����ѡ��ͻ��ˣ�", "��ʾ��", JOptionPane.INFORMATION_MESSAGE);		
    		south.updateRemind("��δѡ��ͻ��ˣ� ");
    	}else {
	        if(command.equals("�ļ��ϴ�")){
		        sendFileServer s = new sendFileServer(IP);
		        s.start();
	        }
	        else if(command.equals("�ļ�����")) {
	        	selectFile s = new selectFile(IP);
	        }
	        else if(command.equals("������")) {
	        	RemoteServer r = new RemoteServer(IP);
	        	south.updateRemind("��ѡ���������� ");
	        	r.start();
	        }
	        else if(command.equals("HTTP����")) {
	        	try {
	        		south.updateRemind("������HTTP���� ");
	        		Message message = new Message();
	            	message.setMesType(MessageType.message_start_proxy);
	    			//��ȡ�ÿͻ��˵�Socket
	    			OperationClient sc = tools.ManageClientThread.getClientThread(IP);
	    			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
	    			oos.writeObject(message);
	    			jb4.setText("�رմ���");
	        	}catch(Exception e2) {
	        		e2.printStackTrace();
	        	}
	        }
	        else if(command.equals("�رմ���")) {
	        	try {
	        		south.updateRemind("�ر���HTTP���� ");
	        		Message message = new Message();
	            	message.setMesType(MessageType.message_End_proxy);
	    			//��ȡ�ÿͻ��˵�Socket
	    			OperationClient sc = tools.ManageClientThread.getClientThread(IP);
	    			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
	    			oos.writeObject(message);
	    			jb4.setText("HTTP����");
	        	}catch(Exception e2) {
	        		e2.printStackTrace();
	        	}
	        }
	        else if(command.equals("�ļ�����")) {
	        	try {
		        	Message message = new Message();
	            	message.setMesType(MessageType.message_diskList_fileManager);
	            	OperationClient sc = tools.ManageClientThread.getClientThread(IP);
	    			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
	    			oos.writeObject(message);
		        	//fileManager f = new fileManager();
		    		//f.initMain();
	        	}
	        	catch(Exception e3) {
	        		e3.printStackTrace();
	        	}
	        }
	        
	        
	        
    	}
        
	}
}
