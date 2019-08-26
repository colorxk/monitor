package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.Message;
import common.MessageType;
import encryption.AES;
import encryption.AESEn;
import model.recevieFileServer;
import tools.OperationClient;

public class selectFile extends JFrame implements ActionListener{
	JPanel jp1;
	JLabel jl1;
	JTextField jtf1;
	JButton jb1;
	String path;
	String IP;
	
	public selectFile(String IP) {
		this.IP = IP;
		this.setTitle("文件下载");
		this.setSize(400,200);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		jp1 = new JPanel(new BorderLayout(5,5));
		jl1 = new JLabel("请输入要下载的文件路径及名称",JLabel.CENTER);
		jp1.add("North",jl1);
		jtf1 = new JTextField();
		jp1.add("Center",jtf1);
		jb1 = new JButton("确认");
		jb1.addActionListener(this);
		jp1.add("South",jb1);
		this.add(jp1);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command =e.getActionCommand();  
        if(command.equals("确认")){  
        	path = jtf1.getText();
        	if(path.equals(null)||path!="") {
        		southUI south = new southUI();
        		south.updateRemind("你想下载的文件是: "+path);
        		Message message = new Message();
        		message.setMesType(MessageType.message_downFile);
        		byte[] enmess = AESEn.encrypt(path, AES.getAESPrivateKey());
	        	message.setContent(enmess);
	        	recevieFileServer r = new recevieFileServer(IP,message);
	        	r.start();
	        	this.dispose();
        	}
        	else {
        		JOptionPane.showMessageDialog(this, "请输入需要下载文件的绝对路径！", "提示：", JOptionPane.INFORMATION_MESSAGE);
        	}
        }
	}
}
