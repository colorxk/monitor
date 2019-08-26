package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectOutputStream;

import javax.swing.*;

import common.Message;
import common.MessageType;
import model.RemoteServer;
import tools.ManageClientThread;
import tools.OperationClient;

public class mainView extends JFrame{
	static String IP = null;
	//定义BorderLayout的5个控件
	northUI north;
	westUI west;  
	JScrollPane jsp1;
	centerUI center;
	eastUI east;
	southUI south;

	public void initMain(mainView mainView){
		setTitle("我的程序");
		setSize(1400,900);
		//this.getContentPane().setBackground(Color.RED);
		this.setLocationRelativeTo(null);
		// false是窗口大小不可改变
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//消除边框
        setLayout(new BorderLayout(5,5)); 
        north = new northUI(IP);
        getContentPane().add("North", north);
        south = new southUI(IP);
        getContentPane().add("South", south);
        east = new eastUI(IP);
        getContentPane().add("East",  east);
        center = new centerUI(IP);
        getContentPane().add("Center",center);
        west = new westUI(mainView);
        jsp1 = new JScrollPane(west,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add("West",  jsp1);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e){
	        	try {
	        		Message message = new Message();
	            	message.setMesType(MessageType.message_all_End);
	        		String onLineFriend[]=ManageClientThread.getAllOnLineIP().split(" ");
	        		if(onLineFriend[0].length()>0) {
		        		for(int i=0;i<onLineFriend.length;i++){
			    			OperationClient sc = tools.ManageClientThread.getClientThread(onLineFriend[i]);
			    			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
			    			oos.writeObject(message);
			    			sc.s.close();
			    		}
	        		}
	        		System.exit(0);
	        	  }catch(Exception e2) {
	        		  e2.printStackTrace();
	        	  }
	        	  
	          }
	      });
	}
	public static void main(String[] arg0) {
		mainView m = new mainView();
		m.initMain(m);
	}

}
