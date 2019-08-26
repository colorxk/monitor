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
		jb1 = new JButton("文件上传");
		jb1.addActionListener(this);
		jb2 = new JButton("文件下载");
		jb2.addActionListener(this);
		jb3 = new JButton("桌面监控");
		jb3.addActionListener(this);
		jb4 = new JButton("HTTP代理");
		jb4.addActionListener(this);
		jb5 = new JButton("文件管理");
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
    		JOptionPane.showMessageDialog(center, "请先选择客户端！", "提示：", JOptionPane.INFORMATION_MESSAGE);		
    		south.updateRemind("尚未选择客户端！ ");
    	}else {
	        if(command.equals("文件上传")){
		        sendFileServer s = new sendFileServer(IP);
		        s.start();
	        }
	        else if(command.equals("文件下载")) {
	        	selectFile s = new selectFile(IP);
	        }
	        else if(command.equals("桌面监控")) {
	        	RemoteServer r = new RemoteServer(IP);
	        	south.updateRemind("你选择了桌面监控 ");
	        	r.start();
	        }
	        else if(command.equals("HTTP代理")) {
	        	try {
	        		south.updateRemind("开启了HTTP代理！ ");
	        		Message message = new Message();
	            	message.setMesType(MessageType.message_start_proxy);
	    			//获取该客户端的Socket
	    			OperationClient sc = tools.ManageClientThread.getClientThread(IP);
	    			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
	    			oos.writeObject(message);
	    			jb4.setText("关闭代理");
	        	}catch(Exception e2) {
	        		e2.printStackTrace();
	        	}
	        }
	        else if(command.equals("关闭代理")) {
	        	try {
	        		south.updateRemind("关闭了HTTP代理！ ");
	        		Message message = new Message();
	            	message.setMesType(MessageType.message_End_proxy);
	    			//获取该客户端的Socket
	    			OperationClient sc = tools.ManageClientThread.getClientThread(IP);
	    			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
	    			oos.writeObject(message);
	    			jb4.setText("HTTP代理");
	        	}catch(Exception e2) {
	        		e2.printStackTrace();
	        	}
	        }
	        else if(command.equals("文件管理")) {
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
