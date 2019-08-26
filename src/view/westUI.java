package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tools.ManageClientThread;

public class westUI extends JPanel implements MouseListener{
	//west的控件
	JLabel jl1;
	static JLabel[] jls = new JLabel[30];
	mainView mainView;
	//初始化west控件
	public westUI(){
		
	}
	
	public westUI(mainView mainView){
		this.mainView = mainView;
		jl1 = new JLabel("在线客户端",JLabel.CENTER);
		jl1.setPreferredSize(new Dimension(150, 0));

		
		this.setLayout(new GridLayout(50,1,20,20));
		this.add(jl1);

		for(int i=0;i<jls.length;i++)
		{
			jls[i]=new JLabel("未有IP连接",JLabel.CENTER);
			jls[i].setEnabled(false);
			jls[i].addMouseListener(this);
			this.add(jls[i]);	
		}
		
	}

	public void upateOnLineIP() {
		System.out.println("更新在线客户端...");
		String onLineFriend[]=ManageClientThread.getAllOnLineIP().split(" ");
		System.out.println("目前在线IP数量:"+onLineFriend.length);
		for(int i=0;i<onLineFriend.length;i++){
			System.out.println("打印目前IP:"+onLineFriend[i]);
			if(onLineFriend[i].equals("")||onLineFriend[i].equals(null)) {
				this.jls[i].setText("未有IP连接");
				this.jls[i].setEnabled(false);
			}
			else{
				this.jls[i].setText(onLineFriend[i]);
				this.jls[i].setEnabled(true);
			}
		}
	}
	
	
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getClickCount()==2){
		String IP=((JLabel)e.getSource()).getText();
		southUI south = new southUI();
		south.updateRemind("你选择了:"+IP);
		mainView.IP = IP;
		centerUI.IP = IP;
		eastUI.IP = IP;
		centerUI centerUI = new centerUI();
		centerUI.updateJLabel("客户端IP:"+IP);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)e.getSource();
		jl.setForeground(Color.red);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)e.getSource();
		jl.setForeground(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
