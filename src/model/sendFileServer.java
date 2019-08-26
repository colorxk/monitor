package model;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import common.Message;
import common.MessageType;
import tools.OperationClient;
import view.southUI;

import java.awt.event.*;;

public class sendFileServer extends Thread implements ActionListener{
	private ServerSocket  connectSocket;
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private RandomAccessFile rad;
	private Container contentPanel;
    private JFrame frame;
    private JProgressBar progressbar;
    private JLabel label;
    southUI south = new southUI();
    String IP = "";
    
	public sendFileServer(String IP) {
		this.IP = IP;
		try {
        	Message message = new Message();
        	message.setMesType(MessageType.message_upFile);
			//��ȡ�ÿͻ��˵�Socket
			OperationClient sc = tools.ManageClientThread.getClientThread(IP);
			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
			oos.writeObject(message);
			south.updateRemind("��Կͻ���: "+IP+" �����з����ļ�... ");
			connectSocket=new ServerSocket(9998);
    		south.updateRemind("����������9998�˿ڵȴ��ͻ��˽����ļ��������...:");
			socket=connectSocket.accept();
			south.updateRemind("�ͻ���:"+socket.getInetAddress().getHostAddress()+" �ļ�����������ӳɹ�...");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame=new JFrame("�ļ�����");
	}
	
	public void run(){
		JFileChooser fc = new JFileChooser();	
		int status=fc.showOpenDialog(null);
		
		if (status==JFileChooser.APPROVE_OPTION) {
			String path=fc.getSelectedFile().getPath();
			System.out.println(path);
			try {
				dos=new DataOutputStream(socket.getOutputStream());
				dis=new DataInputStream(socket.getInputStream());
				dos.writeUTF("ok");//����OK
				rad=new RandomAccessFile(path, "r");
				File file=new File(path);
				
				byte[] buf=new byte[1024];
				dos.writeUTF(file.getName());//�����ļ�����
				dos.flush();//��ջ�������������
				String rsp=dis.readUTF();//����OK
				
				if (rsp.equals("ok")) {
					long size=dis.readLong();//��ȡ�ļ��ѷ��͵Ĵ�С
					dos.writeLong(rad.length());//�����ļ����ܳ���
					dos.writeUTF("ok");//����OK
					dos.flush();
					
					long offset=size;//�ֽ�ƫ����
					int barSize=(int) (rad.length()/1024);//�ļ�������
					int barOffset=(int)(offset/1024);//�ϴη��͵���λ��ƫ����
					
					//�������
					frame.setSize(380,120);
					contentPanel = frame.getContentPane();
					contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
					progressbar = new JProgressBar();//������
					
					label=new JLabel(file.getName()+" ������");
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
					
					//���ļ�ָ��λ�ÿ�ʼ����
					int length;
					if (offset<rad.length()) {
						rad.seek(offset);//����size��λ��
						while((length=rad.read(buf))>0){
							dos.write(buf,0,length);							
							progressbar.setValue(++barOffset);
							dos.flush();
						}
					}
					label.setText(file.getName()+" �������");
					south.updateRemind("�ļ��ϴ��ɹ� ");
				}
				dis.close();
				dos.close();
				rad.close();
				connectSocket.close();
				frame.dispose();
			}
			catch(IOException e) {
				label.setText(" ȡ������,���ӹر�");
				frame.dispose();
				try {
					socket.close();
					connectSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		try {
			south.updateRemind("��ȡ�����ļ�����... ");
			label.setText(" ȡ������,���ӹر�");
			JOptionPane.showMessageDialog(frame, "ȡ�����͸������ӹر�!", "��ʾ��", JOptionPane.INFORMATION_MESSAGE);				
			dis.close();
			dos.close();
			rad.close();
			frame.dispose();
			try {
				socket.close();
				connectSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
		catch (IOException e1) {
			e1.printStackTrace();
			}
	}

}
