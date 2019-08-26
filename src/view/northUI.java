package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JPanel;

import common.Message;
import common.MessageType;
import model.mainServer;
import tools.ManageClientThread;
import tools.OperationClient;

public class northUI extends JPanel implements ActionListener{
	JButton jb1;
	JButton jb2;
	String IP;
	
	public northUI(String IP){
		this.IP = IP;
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 10, 10);
		this.setLayout(layout);
		this.setBackground(Color.lightGray);
		this.setPreferredSize(new Dimension(0, 50));
		jb1 = new JButton("��������");
		jb1.addActionListener(this);
		this.add(jb1);
		jb2 = new JButton("�رշ���");
		this.add(jb2);
		jb2.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command =e.getActionCommand();  
        if(command.equals("��������")){  
            //�������������߼�����  
        	mainServer server = new mainServer();  
            jb2.setEnabled(true);  
            jb1.setEnabled(false);  
            
			southUI south = new southUI();
			south.updateRemind("�����������ɹ�");
			south.updateRemind("�ȴ��ͻ�������...");
            
            //���̴߳�����������  
            new Thread(){  
                public void run() {  
                	server.mainServer();
                };
            }.start();  
              
        }else if(command.equals("�رշ���")){
        	Message message = new Message();
        	message.setMesType(MessageType.message_all_End);
    		String onLineFriend[]=ManageClientThread.getAllOnLineIP().split(" ");
    		try {
    			if(onLineFriend[0].length()>0) {
		    		for(int i=0;i<onLineFriend.length;i++){
		    			OperationClient sc = tools.ManageClientThread.getClientThread(onLineFriend[i]);
		    			System.out.println(onLineFriend[i]);
		    			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
		    			oos.writeObject(message);
		    			sc.s.close();
		    		}
    			}
	    		System.exit(0);
    		}catch(Exception e2) {
    			e2.printStackTrace();
    		}
    		
        	System.exit(0);
              
        }  
	}
}
