package model;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
 
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import common.Message;
import tools.OperationClient;
import view.southUI;

public class recevieFileServer extends Thread implements ActionListener{
	private ServerSocket  connectSocket;
	private Socket socket;
	private JFrame frame;
	private Container contentPanel;
	private JProgressBar progressbar;
	private DataInputStream dis;
	private DataOutputStream dos;
	private RandomAccessFile rad;
	private JLabel label;
	String IP;
	Message message;
	southUI south = new southUI();
	
	public recevieFileServer(String IP,Message message) {
		this.IP = IP;
		this.message = message;
		
		OperationClient sc = tools.ManageClientThread.getClientThread(this.IP);
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(sc.s.getOutputStream());
			oos.writeObject(message);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			connectSocket=new ServerSocket(9998);
			socket=connectSocket.accept();
			south.updateRemind("文件下载的客户端: "+socket.getInetAddress().getHostAddress()+" 建立文件传输隧道成功！");
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame=new JFrame("接收文件");
	}
	
	public void run() {
		try {
			dis=new DataInputStream(socket.getInputStream());
			dos=new DataOutputStream(socket.getOutputStream());
			dis.readUTF();//接收OK
			
			int permit=JOptionPane.showConfirmDialog(frame, "是否下载文件？","文件传输请求：", JOptionPane.YES_NO_OPTION);
			if (permit==JOptionPane.YES_OPTION) {
			//if (true) {
				String filename=dis.readUTF();//读取文件名称
				dos.writeUTF("ok");//发送OK
				dos.flush();//清空缓冲区的数据流
				File file=new File(filename+".temp");//建立一个临时文件
				
				rad=new RandomAccessFile(filename+".temp", "rw");
				
				//获得文件大小
				long size=0;
				if(file.exists()&& file.isFile()){
					size=file.length();
				}
				
				dos.writeLong(size);//发送已接收的大小,如果是第一次接收则大小为0
				dos.flush();
				long allSize=dis.readLong();//接收文件的总长度
				String rsp=dis.readUTF();//接收OK
				int barSize=(int)(allSize/1024);//文件的总量
				int barOffset=(int)(size/1024);//上次接收到的位置偏移量		
				
				//传输界面
				frame.setSize(300,120);
				contentPanel =frame.getContentPane();
				contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
				progressbar = new JProgressBar();//进度条
				
				label=new JLabel(filename+" 接收中");
				contentPanel.add(label);
				
				progressbar.setOrientation(JProgressBar.HORIZONTAL);
				progressbar.setMinimum(0);
				progressbar.setMaximum(barSize);
				progressbar.setValue(barOffset);
			    progressbar.setStringPainted(true);
			    progressbar.setPreferredSize(new Dimension(150, 20));
			    progressbar.setBorderPainted(true);
			    progressbar.setBackground(Color.pink);
				
			    JButton cancel=new JButton("取消");
			    JPanel barPanel=new JPanel();
			    barPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			    barPanel.add(progressbar);
			    barPanel.add(cancel);
			    contentPanel.add(barPanel);
			    cancel.addActionListener(this);
			    
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.setVisible(true);
				
			  //接收文件
				if (rsp.equals("ok")) {
					rad.seek(size);//跳到size的位置
					int length;
					byte[] buf=new byte[1024];
					while((length=dis.read(buf, 0, buf.length))!=-1){
						rad.write(buf,0,length);
						progressbar.setValue(++barOffset);
					}
					//System.out.println("FileReceive end...");
				}
				
				label.setText(filename+" 结束接收");
				
				dis.close();
				dos.close();
				rad.close();
				frame.dispose();
				//文件重命名
				if (barOffset>=barSize) {
					file.renameTo(new File(filename));
					//System.out.println("文件重命名...");
				}
				south.updateRemind("文件下载成功");
			}
			else {
				dis.close();
				dos.close();
				frame.dispose();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			label.setText(" 已取消接收，连接关闭！");		
			frame.dispose();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			dis.close();
			dos.close();
			rad.close();
			JOptionPane.showMessageDialog(frame, "已取消接收，连接关闭！", "提示：", JOptionPane.INFORMATION_MESSAGE);	
			label.setText(" 取消接收,连接关闭");
			frame.dispose();
		}
		catch (IOException e1) {
			e1.printStackTrace();
			}
	}

}
