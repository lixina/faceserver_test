package com.xmcares.ui;

import com.xmcares.entrance.FacePerformance;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * 人脸服务器性能测试前端页面 lix
 */
public class MainUiPerformance extends JFrame implements ActionListener {
	private JPanel contentJPanel;

	private JTextArea msgJTextArea;
	
	private JLabel ipLabel,portLabel,nameLabel,pwdLabel,DBIdLabel,dirLabel,methodLabel,markLabel, picCountLabel;
	private JTextField ipField,portField,nameField,pwdField,DBIdField,dirField,methodField,markField,picCountField;

	private JButton sendButton,menuButton;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUiPerformance frame = new MainUiPerformance();
					double lx = Toolkit.getDefaultToolkit().getScreenSize()
							.getWidth();
					double ly = Toolkit.getDefaultToolkit().getScreenSize()
							.getHeight();
					// 设定窗口出现位置
					frame.setLocation(new Point((int) (lx / 2) - 300,
							(int) (ly / 2) - 300));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUiPerformance() {

		setTitle("人脸服务器性能测试工具");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 460);
		contentJPanel = new JPanel();
		contentJPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentJPanel.setLayout(null);
		setContentPane(contentJPanel);
		
		ipLabel = new JLabel();
		ipLabel.setText("服务端IP地址：");
		ipLabel.setBounds(10, 5, 100, 30);
		ipField = new JTextField("10.83.2.232");
		ipField.setBounds(115, 5, 150, 30);
		contentJPanel.add(ipLabel);
		contentJPanel.add(ipField);
		
		portLabel = new JLabel();
		portLabel.setText("服务器端口号：");
		portLabel.setBounds(310, 5, 100, 30);
		portField = new JTextField("10010");
		portField.setBounds(415, 5, 150, 30);
		contentJPanel.add(portLabel);
		contentJPanel.add(portField);

		nameLabel = new JLabel();
		nameLabel.setText("用户名：");
		nameLabel.setBounds(10, 50, 100, 30);
		nameField = new JTextField("megvii");
		nameField.setBounds(115, 50, 150, 30);
		contentJPanel.add(nameLabel);
		contentJPanel.add(nameField);

		pwdLabel = new JLabel();
		pwdLabel.setText("密码：");
		pwdLabel.setBounds(310, 50, 100, 30);
		pwdField = new JTextField("megvii");
		pwdField.setBounds(415, 50, 150, 30);
		contentJPanel.add(pwdLabel);
		contentJPanel.add(pwdField);

		dirLabel = new JLabel();
		dirLabel.setText("图片路径：");
		dirLabel.setBounds(10, 95, 100, 30);
		dirField = new JTextField("F:\\rl\\pic");
		dirField.setBounds(115, 95, 150, 30);
		contentJPanel.add(dirLabel);
		contentJPanel.add(dirField);

		DBIdLabel = new JLabel();
		DBIdLabel.setText("DBId：");
		DBIdLabel.setBounds(310, 95, 100, 30);
		DBIdField = new JTextField("kstest1");
		DBIdField.setBounds(415, 95, 150, 30);
		contentJPanel.add(DBIdLabel);
		contentJPanel.add(DBIdField);

		methodLabel = new JLabel();
		methodLabel.setText("测试方法：");
		methodLabel.setBounds(10, 140, 100, 30);
		methodField = new JTextField("add/search");
		methodField.setBounds(115, 140, 150, 30);
		contentJPanel.add(methodLabel);
		contentJPanel.add(methodField);

		picCountLabel = new JLabel();
		picCountLabel.setText("图片数量");
		picCountLabel.setBounds(310, 140, 100, 30);
		picCountField = new JTextField("100");
		picCountField.setBounds(415, 140, 150, 30);
		contentJPanel.add(picCountLabel);
		contentJPanel.add(picCountField);

		markLabel = new JLabel();
		markLabel.setText("备注：");
		markLabel.setBounds(10, 185, 450, 60);
		markField = new JTextField("测试方法为add时，图片路径,图片数量可置空");
		markField.setBounds(115, 185, 450, 60);
		contentJPanel.add(markLabel);
		contentJPanel.add(markField);

		sendButton = new JButton("发送");
		sendButton.setBounds(200, 350, 100, 45);
		contentJPanel.add(sendButton);

		menuButton = new JButton("目录");
		menuButton.setBounds(350, 350, 100, 45);
		menuButton.addActionListener((ActionListener) this);
		contentJPanel.add(menuButton);

		// 按钮点击事件
		sendButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					String ip = ipField.getText();
					String port = portField.getText();
					String name = nameField.getText();
					String pwd = pwdField.getText();
					String dir = dirField.getText();
					String DBId = DBIdField.getText();
					String method = methodField.getText();
					String picCount = picCountField.getText();
					FacePerformance test = new FacePerformance();
					if( StringUtils.isBlank(ip) || StringUtils.isBlank(port) ||StringUtils.isBlank(DBId)||StringUtils.isBlank(method)){
						JOptionPane.showMessageDialog(null,"请补全参数", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					// 需鉴权
					if(StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(pwd)){
						// 判断是添加图片还是1：N
						if(method.equals("add")){
							test.sendFaceAdd(ip,port,name,pwd,DBId);
							JOptionPane.showMessageDialog(null,"添加图片结束，请查看日志", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
						}else if(method.equals("search")){
							test.sendRecogSearch(ip,port,name,pwd,dir,DBId,picCount);
							JOptionPane.showMessageDialog(null,"1：N查询结束，请查看日志", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
							return;
						}else{
							JOptionPane.showMessageDialog(null,"测试方法不存在", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
						}
					}else{
						// 不需要鉴权
						if(method.equals("add")){
							test.sendFaceAdd(ip,port,DBId);
							JOptionPane.showMessageDialog(null,"添加图片结束，请查看日志", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
						}else if(method.equals("search")){
							test.sendRecogSearch(ip,port,dir,DBId,picCount);
							JOptionPane.showMessageDialog(null,"1：N查询结束，请查看日志", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
							return;
						}else{
							JOptionPane.showMessageDialog(null,"测试方法不存在", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();//点击按钮时frame1销毁,new一个frame2
		JFrame jFrame = new MenuUi();
		jFrame.setVisible(true);
	}
}
