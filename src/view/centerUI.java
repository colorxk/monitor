package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectOutputStream;

import javax.swing.*;
import common.Message;
import common.MessageType;
import encryption.AES;
import encryption.AESEn;
import tools.OperationClient;


public class centerUI extends JPanel implements ActionListener{
	static String IP;
	//����
	static JLabel jlb1;
	//�в�
	static JTextArea textarea;
	JScrollPane scrollPane;
	//�ײ�
	JPanel jp1;
	JLabel jlb2;
	JButton jb1;
	JTextField jtf;
	
	public void updateJLabel(String content) {
		System.out.println("�������߿ͻ���...");
		this.jlb1.setText(content);
	}
	
	public void updateJTextArea(String content) {
		this.textarea.append(content);
		this.textarea.setSelectionEnd(0);
	}
	
	public centerUI(String IP){
		this.IP = IP;
		this.setBackground(Color.cyan);
		jlb1 = new JLabel("Shellcode��������",JLabel.CENTER);
		this.setLayout(new BorderLayout(5,5));
		setFont(new Font("Helvetica", Font.PLAIN, 14));
		this.add("North", jlb1);
		textarea=new JTextArea();
		textarea.setLineWrap(true);//�Զ�����
		textarea.setWrapStyleWord(true);//���в�����
		scrollPane = new JScrollPane(textarea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane);
		
		jp1 = new JPanel(new FlowLayout(1));
		jlb2 = new JLabel("������CMD:");
		jtf = new JTextField();
		jtf.setPreferredSize(new Dimension(300,30));
		jb1 = new JButton("����");
		jb1.addActionListener(this);
		jp1.add(jlb2);
		jp1.add(jtf);
		jp1.add(jb1);
		this.add("South",jp1);
	}
	public centerUI(){
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command =e.getActionCommand();  
        if(command.equals("����")){  
        	southUI south = new southUI();
        	if(IP==null) {
        		JOptionPane.showMessageDialog(this, "����ѡ��ͻ��ˣ�", "��ʾ��", JOptionPane.INFORMATION_MESSAGE);		
        		south.updateRemind("����ѡ��ͻ��ˣ� ");
        	}
        	else {
	        	Message message = new Message();
	        	message.setMesType(MessageType.message_shellcode);
	        	String cmd = jtf.getText();
				byte[] enmess = AESEn.encrypt(cmd, AES.getAESPrivateKey());
	        	message.setContent(enmess);
	        	message.setSendTime(new java.util.Date().toString());		
				try {
					//��ȡ�ÿͻ��˵�Socket
					OperationClient sc = tools.ManageClientThread.getClientThread(IP);
					System.out.println("ͨ��IP"+IP+"��ȡ����OperationClient����"+sc);
					//System.out.println("��ӡsc�����е�Socket:"+sc);
					ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
					oos.writeObject(message);
					jtf.setText("");
					south.updateRemind("��Կͻ���: "+IP+" ������Shell����: "+cmd);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
	        	
        	}
        }
	}

}
