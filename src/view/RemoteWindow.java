package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import common.Message;
import common.MessageType;
import model.RemoteServer;
import tools.OperationClient;

public class RemoteWindow extends JFrame{
	
	String IP;
	private JLabel label;
	//重写背景图片方法
	public void repainImage(byte [] imageBytes){
	    label.setIcon(new ImageIcon(imageBytes));
	    this.repaint();
	}
	public RemoteWindow(String IP){
		this.IP = IP;
	    this.setTitle("远程控制程序");
	    label = new JLabel();
	    JPanel p = new JPanel();
	    p.add(label);
	    JScrollPane scroll = new JScrollPane(p);//给p面板添加滚动条
	    this.add(scroll);
	    this.setSize(1124,868);
	    //this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e){
	        	try {
	        	    Message message = new Message();
	    	        message.setMesType(MessageType.message_End_remote);
	    		    OperationClient sc = tools.ManageClientThread.getClientThread(IP);
	    		    ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
	    		    oos.writeObject(message);
	        	  }catch(Exception e2) {
	        		  e2.printStackTrace();
	        	  }
	        	  RemoteServer.b = false;
	          }
	      });
	      this.setVisible(true);
	  }
}
