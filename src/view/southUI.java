package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class southUI extends JPanel{
	static String IP;
	static String[] info = new String[6];
	
	static int count = 0;
 	JLabel jl1;
	static JTextArea textarea;
	//初始化south控件
	public southUI(){
	}
	
	public southUI(String IP){
		this.IP = IP;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.lightGray);
		this.setPreferredSize(new Dimension(0, 150));
		jl1 = new JLabel("  操作记录",JLabel.LEFT);
		this.add("North",jl1);
		textarea=new JTextArea();
		textarea.setLineWrap(true);//自动换行
		textarea.setWrapStyleWord(true);//换行不断字
		this.add("Center",textarea);
	}
	
	public void updateRemind(String content2) {
		String content = "  "+content2 +"  ("+ new java.util.Date().toString()+")";
		if(count<6) {
			this.textarea.append(content+"\n");
			info[count] = content+"\n";
			count++;
		}
		else {
			this.textarea.setText("");
			this.moveLeft(content+"\n");
		}
	}
	
	
	public static void moveLeft(String endDate){
	     String temp = info[0];
	     for(int i = 0;i<info.length-1;i++){
	    	 info[i] = info[i+1]; 
	     }
	     info[info.length-1] = endDate;	
	     for (int i=0;i<info.length;i++) {
	    	 textarea.append("   [提示]: "+info[i]);
			}
	}


}
