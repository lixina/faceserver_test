package com.xmcares.ui;

import com.xmcares.entrance.FaceAccuracyRate;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 人脸服务器准确率测试前端页面 lix
 */
public class MainUiAccuracyRate extends JFrame implements ActionListener {
	private JPanel contentJPanel;

	private JTextArea msgJTextArea;

	private JLabel ipLabel,portLabel,nameLabel,pwdLabel,DBIdLabel,dirLabel,methodLabel,markLabel, picCountLabel;
	private JTextField ipField,portField,nameField,pwdField,DBIdField,dirField,methodField,markField,picCountField;

	private JButton sendButton,menuButton;

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUiAccuracyRate frame = new MainUiAccuracyRate();
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

	public MainUiAccuracyRate() {

		setTitle("人脸服务器准确率测试工具");
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
		dirLabel.setText("关注库批次号：");
		dirLabel.setBounds(10, 95, 100, 30);
		dirField = new JTextField("20100120");
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
		methodLabel.setText("非关注库批次号：");
		methodLabel.setBounds(10, 140, 100, 30);
		methodField = new JTextField("2010012001");
		methodField.setBounds(115, 140, 150, 30);
		contentJPanel.add(methodLabel);
		contentJPanel.add(methodField);

		sendButton = new JButton("发送");
		sendButton.setBounds(190, 350, 100, 45);
		contentJPanel.add(sendButton);

		menuButton = new JButton("目录");
		menuButton.setBounds(310, 350, 100, 45);
		menuButton.addActionListener(this);
		contentJPanel.add(menuButton);


		// 按钮点击事件
		sendButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
					String ip = ipField.getText();
					String port = portField.getText();
					String name = nameField.getText();
					String pwd = pwdField.getText();
					String yseNum = dirField.getText();
					String DBId = DBIdField.getText();
					String noNum = methodField.getText();
					FaceAccuracyRate test = new FaceAccuracyRate();
					// 需要鉴权
					if( StringUtils.isBlank(ip) || StringUtils.isBlank(port) ||StringUtils.isBlank(yseNum)||StringUtils.isBlank(noNum)){
						JOptionPane.showMessageDialog(null,"忘记填了什么吧", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if( StringUtils.isNotBlank(name)&&StringUtils.isNotBlank(pwd)){
						// 准确率计算
						//String ip,String port,String DBId,String name, String pwd,String num1, String num2
						test.searchPicFace(ip,port,DBId,name,pwd,yseNum, noNum);
						String result = test.calculationAccuracy(yseNum, noNum);
						JOptionPane.showMessageDialog(null,result, "执行结果:", JOptionPane.INFORMATION_MESSAGE);
					}else{
						test.searchPicFace(ip,port,DBId,yseNum, noNum);
						String result = test.calculationAccuracy(yseNum, noNum);
						JOptionPane.showMessageDialog(null,result, "执行结果:", JOptionPane.INFORMATION_MESSAGE);
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
