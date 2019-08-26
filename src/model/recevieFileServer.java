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
			south.updateRemind("�ļ����صĿͻ���: "+socket.getInetAddress().getHostAddress()+" �����ļ���������ɹ���");
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame=new JFrame("�����ļ�");
	}
	
	public void run() {
		try {
			dis=new DataInputStream(socket.getInputStream());
			dos=new DataOutputStream(socket.getOutputStream());
			dis.readUTF();//����OK
			
			int permit=JOptionPane.showConfirmDialog(frame, "�Ƿ������ļ���","�ļ���������", JOptionPane.YES_NO_OPTION);
			if (permit==JOptionPane.YES_OPTION) {
			//if (true) {
				String filename=dis.readUTF();//��ȡ�ļ�����
				dos.writeUTF("ok");//����OK
				dos.flush();//��ջ�������������
				File file=new File(filename+".temp");//����һ����ʱ�ļ�
				
				rad=new RandomAccessFile(filename+".temp", "rw");
				
				//����ļ���С
				long size=0;
				if(file.exists()&& file.isFile()){
					size=file.length();
				}
				
				dos.writeLong(size);//�����ѽ��յĴ�С,����ǵ�һ�ν������СΪ0
				dos.flush();
				long allSize=dis.readLong();//�����ļ����ܳ���
				String rsp=dis.readUTF();//����OK
				int barSize=(int)(allSize/1024);//�ļ�������
				int barOffset=(int)(size/1024);//�ϴν��յ���λ��ƫ����		
				
				//�������
				frame.setSize(300,120);
				contentPanel =frame.getContentPane();
				contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
				progressbar = new JProgressBar();//������
				
				label=new JLabel(filename+" ������");
				contentPanel.add(label);
				
				progressbar.setOrientation(JProgressBar.HORIZONTAL);
				progressbar.setMinimum(0);
				progressbar.setMaximum(barSize);
				progressbar.setValue(barOffset);
			    progressbar.setStringPainted(true);
			    progressbar.setPreferredSize(new Dimension(150, 20));
			    progressbar.setBorderPainted(true);
			    progressbar.setBackground(Color.pink);
				
			    JButton cancel=new JButton("ȡ��");
			    JPanel barPanel=new JPanel();
			    barPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			    barPanel.add(progressbar);
			    barPanel.add(cancel);
			    contentPanel.add(barPanel);
			    cancel.addActionListener(this);
			    
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.setVisible(true);
				
			  //�����ļ�
				if (rsp.equals("ok")) {
					rad.seek(size);//����size��λ��
					int length;
					byte[] buf=new byte[1024];
					while((length=dis.read(buf, 0, buf.length))!=-1){
						rad.write(buf,0,length);
						progressbar.setValue(++barOffset);
					}
					//System.out.println("FileReceive end...");
				}
				
				label.setText(filename+" ��������");
				
				dis.close();
				dos.close();
				rad.close();
				frame.dispose();
				//�ļ�������
				if (barOffset>=barSize) {
					file.renameTo(new File(filename));
					//System.out.println("�ļ�������...");
				}
				south.updateRemind("�ļ����سɹ�");
			}
			else {
				dis.close();
				dos.close();
				frame.dispose();
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			label.setText(" ��ȡ�����գ����ӹرգ�");		
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
			JOptionPane.showMessageDialog(frame, "��ȡ�����գ����ӹرգ�", "��ʾ��", JOptionPane.INFORMATION_MESSAGE);	
			label.setText(" ȡ������,���ӹر�");
			frame.dispose();
		}
		catch (IOException e1) {
			e1.printStackTrace();
			}
	}

}
