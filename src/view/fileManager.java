package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.ObjectOutputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileSystemView;

import common.Message;
import common.MessageType;
import model.RemoteServer;
import tools.OperationClient;

public class fileManager extends JFrame implements ActionListener,MouseListener{
	static String IP = null;
	static String currentPath = "";
	private JPopupMenu menu = new JPopupMenu();
	//定义BorderLayout的5个控件
	JPanel north;
	JPanel west;  
	JPanel center;
	JPanel east;
	JPanel south;
	
	//north控件
	JLabel mulu;
	JButton getBack;
	
	//west控件
	JLabel info;
	JLabel[] JldiskList;
	
	//center控件
	JLabel[] directoryList;
	
	//south控件
	JButton upFile;
	JButton downFile;
	TextField tf;
	
	public void initNorth() {
		north = new JPanel(new BorderLayout());
		north.setBackground(Color.lightGray);
		mulu = new JLabel("尚未选择磁盘",JLabel.LEFT);
		getBack = new JButton("getBack");
		getBack.setPreferredSize(new Dimension(90,20));
		north.add(mulu,BorderLayout.CENTER);
		north.add(getBack,BorderLayout.EAST);
		getBack.addActionListener(this);
	}
	public void initWest() {
		west = new JPanel();
		west.setBackground(Color.lightGray);
		west.setLayout(new GridLayout(10,1,4,4));
		info = new JLabel("磁盘列表",JLabel.CENTER);
		info.setBorder(BorderFactory.createRaisedBevelBorder());
		west.add(info);
		//String[] diskList = {"C:"+File.separator,"D:"+File.separator,"E:"+File.separator,"F:"+File.separator,"G:"+File.separator};
		String[] diskList = {"C:"+File.separator,"G:"+File.separator};
		JldiskList = new JLabel[diskList.length];
		for(int i=0;i<diskList.length;i++){
			JldiskList[i] = new JLabel(diskList[i],new ImageIcon("Image"+File.separator+"cipan3.png"),JLabel.CENTER);
			JldiskList[i].setBorder(BorderFactory.createRaisedBevelBorder());
			JldiskList[i].setEnabled(true);
			JldiskList[i].addMouseListener(this);
			west.add(JldiskList[i]);
		}
	}
	public void updataWest(String result) {
		west.removeAll();
		west.repaint();
		info = new JLabel("磁盘列表",JLabel.CENTER);
		info.setBorder(BorderFactory.createRaisedBevelBorder());
		west.add(info);
		String[] diskList = result.split(",");
		for(int i=0;i<diskList.length;i++){
			JldiskList[i] = new JLabel(diskList[i],new ImageIcon("Image"+File.separator+"cipan3.png"),JLabel.CENTER);
			JldiskList[i].setBorder(BorderFactory.createRaisedBevelBorder());
			JldiskList[i].setEnabled(true);
			JldiskList[i].addMouseListener(this);
			west.add(JldiskList[i]);
		}
	}
	
	public void initCenter() {
		center = new JPanel();
		center.setBackground(Color.lightGray);
		center.setLayout(new GridLayout(25,4,50,5));
        center.add(menu);
	}
	public void upDateCenter(String str) {
		center.removeAll();
		center.repaint();
		directoryList = new JLabel[100];
		for(int i=0;i<100;i++) {
			directoryList[i] = new JLabel("");
			center.add(directoryList[i]);
		}
		if(str.length()>0&&str!=null) {
			String[] result = str.split(",");
			System.out.println("文件目录总数："+result.length);
			for(int i=0;i<result.length;i++) {
				directoryList[i].setText(result[i]);
				directoryList[i].setEnabled(true);
				directoryList[i].addMouseListener(this);
			}	
		}
        center.add(menu);
	}
	
	
	public void initEast() {
		east = new JPanel();
	}
	public void initSouth() {
		south = new JPanel(new BorderLayout());
		upFile = new JButton("上传");
		downFile = new JButton("下载");
		tf = new TextField();
		south.add(upFile,BorderLayout.WEST);
		south.add(downFile,BorderLayout.CENTER);
		south.add(tf,BorderLayout.EAST);
		upFile.setPreferredSize(new Dimension(100,20));
		downFile.setPreferredSize(new Dimension(100,20));
		tf.setPreferredSize(new Dimension(600,20));
	}

	public void initMain(){
		setTitle("文件管理");
		setSize(800,500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
        setLayout(new BorderLayout(5,5)); 
        this.initNorth();
        this.initSouth();
        this.initEast();
        this.initCenter();
        this.initWest();
        getContentPane().add("North", north);
        getContentPane().add("South", south);
        getContentPane().add("East",  east);
        getContentPane().add("Center",center);
        getContentPane().add("West",  west);
        setVisible(true);

        this.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e){
	        	try {
	        		dispose();
	        		System.out.println("关闭");
	        	}catch(Exception e2) {
	        		e2.printStackTrace();
	        	}
	          }
	      });
	}
/*	public static void main(String[] arg0) {
		fileManager f = new fileManager();
		f.initMain();
	}*/
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command =e.getActionCommand();  
		if(command.equals("getBack")) {
			System.out.println("当前currentPath："+currentPath);
			System.out.println("点击了返回");
			if(currentPath.length()>3) {
				String temp = currentPath.replace(File.separator, "|");
				System.out.println(temp);
				String[] temp1 = temp.split("\\|");
				System.out.println(temp1.length);
				currentPath = "";
				
				for(int i=0;i<temp1.length-2;i++) {
					currentPath += temp1[i]+File.separator;
				}
				currentPath += temp1[temp1.length-2];
				System.out.println("结果:"+currentPath);
				if(currentPath.equals("E:")) currentPath+=File.separator;//*********have a question*********
				File file=new File(currentPath);
				upDateCenter(this.listFile(file));
				mulu.setText(currentPath);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		File file;
		if(arg0.getClickCount()==2){
			String check=((JLabel)arg0.getSource()).getText();
			System.out.println("你点击了:"+check);
			if(check.startsWith("C:")||check.startsWith("D:")||check.startsWith("E:")||check.startsWith("F:")||check.startsWith("G:")||check.startsWith("H:")||check.startsWith("I:")) {
				currentPath = check;
				file=new File(currentPath);
				upDateCenter(this.listFile(file));
				currentPath = currentPath.substring(0, 2);
				System.out.println(currentPath);
			}
			else {
				currentPath = currentPath + File.separator+check;
				System.out.println("你选择了访问:"+currentPath);
				file=new File(currentPath);
				upDateCenter(this.listFile(file));
			}
			mulu.setText(currentPath);
			
		}/*else if(arg0.getButton()==MouseEvent.BUTTON3) {
			System.out.println("--------右键--------");
		}*/
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		((JLabel)arg0.getSource()).setBackground(Color.blue);
		
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		((JLabel)arg0.getSource()).setBackground(Color.GRAY);
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		tf.setText(currentPath+File.separator+((JLabel)arg0.getSource()).getText());
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public String listFile(File file){
		System.out.println("调用函数访问path为: "+file);
		String result = "";
		 if(file.exists()){
			 if(file.isDirectory()){
				 File f[]=file.listFiles();
				 if(f!=null){
					 result = f[0].getName();
	                    for(int i=1;i<f.length;i++){
	                    	result += ","+f[i].getName();
	                    }
	                    System.out.println(result);
	                }
			 }
			 else {
				 System.out.println("不是目录");
				 System.out.println(file);
				 return "C:"+File.separator;
			 }
		 }
		 else {
			 System.out.println("不存在该目录");
			 return "C:"+File.separator;
		 }
		 return result;
   }
	
	
	
	
}
