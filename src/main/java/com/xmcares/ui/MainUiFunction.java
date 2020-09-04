package com.xmcares.ui;

import com.alibaba.fastjson.JSONArray;
import com.xmcares.entrance.FaceAuthorizationFunction;
import com.xmcares.entrance.FaceFunction;
import com.xmcares.enums.EventType;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * 人脸服务器接口方法测试前端页面 lix
 */
public class MainUiFunction extends JFrame implements ActionListener {
	private JPanel contentJPanel;

	private JLabel ipLabel,portLabel,nameLabel,pwdLabel,requestParamLabel,responseParamLabel,funcLabel,tipsLabel;
	private JTextField ipField,portField,nameField;
	private JComboBox functionComboBox;// 功能下拉框
	private JButton sendButton,clearButton,menuButton;// 发送清除按钮
	private JTextArea questionTextArea;// 请求参数,返回参数
	private JTextPane responseParamTextArea,requestParamTextArea;
	private JCheckBox authCheckBox;
	private JPasswordField passwordField;
	FaceFunction funcFaceUtilsTest = new FaceFunction();
	FaceAuthorizationFunction faceAuthorizationFunction = new FaceAuthorizationFunction();
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUiFunction frame = new MainUiFunction();
					double lx = Toolkit.getDefaultToolkit().getScreenSize()
							.getWidth();
					double ly = Toolkit.getDefaultToolkit().getScreenSize()
							.getHeight();
					// 设定窗口出现位置
					frame.setLocation(new Point((int) (lx / 2) - 500,
							(int) (ly / 2) - 300));
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainUiFunction() {

		setTitle("API功能测试工具");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 600);
		contentJPanel = new JPanel();
		contentJPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentJPanel.setLayout(null);
		setContentPane(contentJPanel);
		try {
			// 设置主题
			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceSaharaLookAndFeel");
			SwingUtilities.updateComponentTreeUI(contentJPanel);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		ipLabel = new JLabel();
		ipLabel.setText("IP：");
		ipLabel.setBounds(10, 5, 100, 30);
		ipField = new JTextField("10.83.2.232");
		ipField.setBounds(70, 5, 150, 30);
		contentJPanel.add(ipLabel);
		contentJPanel.add(ipField);
		
		portLabel = new JLabel();
		portLabel.setText("端口：");
		portLabel.setBounds(250, 5, 100, 30);
		portField = new JTextField("10010");
		portField.setBounds(310, 5, 150, 30);
		contentJPanel.add(portLabel);
		contentJPanel.add(portField);

		nameLabel = new JLabel();
		nameLabel.setText("用户名：");
		nameLabel.setBounds(10, 50, 100, 30);
		nameField = new JTextField("megvii");
		nameField.setBounds(70, 50, 150, 30);
		contentJPanel.add(nameLabel);
		contentJPanel.add(nameField);

		pwdLabel = new JLabel();
		pwdLabel.setText("密码：");
		pwdLabel.setBounds(250, 50, 100, 30);
		passwordField = new JPasswordField("megvii");
		passwordField.setBounds(310, 50, 150, 30);
		contentJPanel.add(pwdLabel);
		contentJPanel.add(passwordField);

		authCheckBox=new JCheckBox("鉴权");
		authCheckBox.setBounds(10, 95, 100, 30);
		contentJPanel.add(authCheckBox);
		requestParamLabel = new JLabel();

		funcLabel = new JLabel("功能：");
		funcLabel.setBounds(120, 95, 50, 30);
		functionComboBox = new JComboBox();
		functionComboBox.setBounds(180, 95, 276, 30);
		functionComboBox.addItem("--请选择--");
		functionComboBox.addItem("底库创建:db/create");
		functionComboBox.addItem("底库删除：db/delete");
		functionComboBox.addItem("底库信息查询：db/query");
		functionComboBox.addItem("底库信息修改：db/edit");
		functionComboBox.addItem("查询所有底库信息：db/queryAllInfo");
		functionComboBox.addItem("底库加载特征数查询：db/queryFeatureCount");

		functionComboBox.addItem("人脸创建:face/add");
		functionComboBox.addItem("人脸删除：face/delete");
		functionComboBox.addItem("人脸批量删除：face/deleteBatch");

		functionComboBox.addItem("引擎状态：status");

		functionComboBox.addItem("人脸识别：recog/search");
		functionComboBox.addItem("提取特征：recog/feature");
		functionComboBox.addItem("人脸检测：recog/detect");
		functionComboBox.addItem("人脸验证：recog/compare");

		contentJPanel.add(funcLabel);
		contentJPanel.add(functionComboBox);
		functionComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(final ItemEvent e) {
				int index = functionComboBox.getSelectedIndex();
				if (index == 1) { // ==0表示选中的是第一个 新增
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					parameters.put("name", "testDB");
					parameters.put("desc", "测试数据库");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));

				}
				if (index == 2) { // ==0表示选中的是第一个 删除
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 3) { // ==0表示选中的是第一个 底库信息查询
					String content = functionComboBox.getSelectedItem().toString();
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					parameters.put("name", "testDB");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 4) { // ==0表示选中的是第一个 底库信息修改
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					parameters.put("name", "testDBEdit");
					parameters.put("desc", "测试数据库修改");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 5) { // ==0表示选中的是第一个 查询所有底库信息
					requestParamTextArea.setText(null);
				}
				if (index == 6) { // ==0表示选中的是第一个 底库加载特征数查询
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 7) { // ==0表示选中的是第一个 人脸创建
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					parameters.put("faceId", "001");
					String dir = System.getProperty("user.dir");
					String[] exts = {"jpg", "jpeg"};
					try {
						Collection<File> fileList = FileUtils.listFiles(new File(dir), exts, true);
						BASE64Encoder encoder = new BASE64Encoder();
						String base64 = encoder.encode(FileUtils.readFileToByteArray(fileList.iterator().next()));
						parameters.put("img", base64);
						parameters.put("url", "");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 8) { // ==0表示选中的是第一个 人脸删除
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					parameters.put("faceId", "001");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 9) { // ==0表示选中的是第一个 人脸批量删除
					JSONObject parameters = new JSONObject();
					JSONArray jsonArray = new JSONArray();
					JSONObject faceId1 = new JSONObject();
					faceId1.put("faceId","001");
					JSONObject faceId2 = new JSONObject();
					faceId2.put("faceId","002");
					jsonArray.add(faceId1);
					jsonArray.add(faceId2);
					parameters.put("DBId", "testDB");
					parameters.put("faces", jsonArray);
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 10) { // ==0表示选中的是第一个 引擎状态
					requestParamTextArea.setText(null);
				}

				if (index == 11) { // ==0表示选中的是第一个 人脸识别
					JSONObject parameters = new JSONObject();
					parameters.put("DBId", "testDB");
					parameters.put("topN", "001");
					parameters.put("reqId", "001");
					String dir = System.getProperty("user.dir");
					try {
						String[] exts = {"jpg", "jpeg"};
						Collection<File> fileList = FileUtils.listFiles(new File(dir), exts, true);
						BASE64Encoder encoder = new BASE64Encoder();
						String base64 = null;
						base64 = encoder.encode(FileUtils.readFileToByteArray(fileList.iterator().next()));
						parameters.put("img", base64);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					parameters.put("url", "");
					parameters.put("feature", "");
					parameters.put("type", "1");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 12) { // ==0表示选中的是第一个 提取特征
					JSONObject parameters = new JSONObject();
					try {
						String dir = System.getProperty("user.dir");
						String[] exts = {"jpg", "jpeg"};
						Collection<File> fileList = FileUtils.listFiles(new File(dir), exts, true);
						BASE64Encoder encoder = new BASE64Encoder();
						String base64 = null;
						base64 = encoder.encode(FileUtils.readFileToByteArray(fileList.iterator().next()));
						parameters.put("img", base64);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					parameters.put("url", "");
					parameters.put("x", 0);
					parameters.put("y", 0);
					parameters.put("w", 0);
					parameters.put("h", 0);
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 13) { // ==0表示选中的是第一个 人脸检测

					JSONObject parameters = new JSONObject();
					try {
						String dir = System.getProperty("user.dir");
						String[] exts = {"jpg", "jpeg"};
						Collection<File> fileList = FileUtils.listFiles(new File(dir), exts, true);
						BASE64Encoder encoder = new BASE64Encoder();
						String base64 = null;
						base64 = encoder.encode(FileUtils.readFileToByteArray(fileList.iterator().next()));
						parameters.put("img", base64);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					parameters.put("url", "");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				if (index == 14) { // ==0表示选中的是第一个 人脸验证
					JSONObject parameters = new JSONObject();
					try {
						String dir = System.getProperty("user.dir");
						String[] exts = {"jpg", "jpeg"};
						Collection<File> fileList = FileUtils.listFiles(new File(dir), exts, true);
						BASE64Encoder encoder = new BASE64Encoder();
						String base64 = null;
						base64 = encoder.encode(FileUtils.readFileToByteArray(fileList.iterator().next()));
						parameters.put("imgA", base64);
						parameters.put("imgB", base64);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					parameters.put("urlA", "");
					parameters.put("urlB", "");
					requestParamTextArea.setText(funcFaceUtilsTest.formatJson(parameters.toString()));
				}
				responseParamTextArea.setText("");
				questionTextArea.setText("");
			}
		});

		/*requestParamLabel = new JLabel();
		requestParamTextArea = new JTextArea();
		requestParamTextArea.setLineWrap(true);        //激活自动换行功能
		requestParamTextArea.setWrapStyleWord(true);            // 激活断行不断字功能
		requestParamLabel = new JLabel();
		requestParamLabel.setText("请求参数：");
		requestParamLabel.setBounds(10, 140, 100, 30);
		requestParamTextArea.setBounds(10, 170, 450, 300);
		contentJPanel.add(requestParamLabel);
		contentJPanel.add(requestParamTextArea);*/
		requestParamLabel = new JLabel();
		requestParamTextArea = new JTextPane();
		final JScrollPane js1=new JScrollPane(requestParamTextArea);
		requestParamLabel.setText("请求参数：");
		requestParamLabel.setBounds(10, 140, 100, 30);
		js1.setBounds(10, 170, 450, 300);
		contentJPanel.add(requestParamLabel);
		js1.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js1.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentJPanel.add(js1);
		contentJPanel.add(requestParamLabel);

		sendButton = new JButton("发送");
		sendButton.setBounds(80, 490, 80, 35);
		contentJPanel.add(sendButton);
		sendButton.setContentAreaFilled(false);

		clearButton = new JButton("清除");
		clearButton.setBounds(180, 490, 80, 35);
		contentJPanel.add(clearButton);
		clearButton.setContentAreaFilled(false);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				requestParamTextArea.setText("");
			}
		});

		menuButton = new JButton("目录");
		menuButton.setBounds(280, 490, 80, 35);
		contentJPanel.add(menuButton);
		menuButton.setContentAreaFilled(false);
		menuButton.addActionListener((ActionListener) this);
		responseParamTextArea=new JTextPane();
		responseParamLabel = new JLabel();
		JScrollPane js=new JScrollPane(responseParamTextArea);
		//responseParamTextArea.setLineWrap(true);        //激活自动换行功能
		//responseParamTextArea.setWrapStyleWord(true);   // 激活断行不断字功能
		responseParamLabel.setText("返回：");
		responseParamLabel.setBounds(470, 5, 100, 30);
		js.setBounds(470, 50, 600, 420);
		contentJPanel.add(responseParamLabel);
		js.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		js.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentJPanel.add(js);

		tipsLabel = new JLabel();
		tipsLabel.setText("提示");
		tipsLabel.setForeground(Color.RED);
		tipsLabel.setBounds(470, 480, 30, 25);
		contentJPanel.add(tipsLabel);
		questionTextArea = new JTextArea();
		questionTextArea.setBounds(500, 480, 570, 25);
		contentJPanel.add(questionTextArea);

		// 按钮点击事件
		sendButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String ip = ipField.getText();
				String port = portField.getText();
				String name = nameField.getText();
				String pwd = passwordField.getText();
				// 是否鉴权
				if(authCheckBox.isSelected()){
					// 如果鉴权 则用户名密码必填
					if (StringUtils.isBlank(name) || StringUtils.isBlank(pwd)){
						JOptionPane.showMessageDialog(null,"用户名密码不能为空！", "执行结果:", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					// 获取ip端口方法拼接成请求路径
					String method = functionComboBox.getSelectedItem().toString();
					// 根据方法设置参数，并将参数赋值在requestParamTextArea
					try {
						if (method.indexOf("db/create") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.dbadd,paramters);
							// 将返回的报文粘贴到
							/*JSONObject parameters = new JSONObject();
							parameters.put("result","0");
							parameters.put("extInfo",111111);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbCreateReturn(result,responseParamTextArea);
					}
						if (method.indexOf("db/delete") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.dbdel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							dbCreateReturn(result,responseParamTextArea);
						}
						if (method.indexOf("底库信息查询：db/query") >= 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.dbsel,paramters);
							// 将返回的报文粘贴到
							/*Map<String, String> chidrenMap = new HashMap<String, String>();
							JSONObject listChildren = new JSONObject();
							listChildren.put("DBId",00);
							listChildren.put("name","haha");
							listChildren.put("desc",11);
							JSONObject parameters = new JSONObject();
							parameters.put("result","Integer");
							parameters.put("extInfo","String");
							JSONArray jsonArray = new JSONArray();
							jsonArray.add(listChildren);
							parameters.put("db_list",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbQueryReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("db/edit") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.dbupd,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							dbCreateReturn(result,responseParamTextArea);
						}
						if (method.indexOf("db/queryAllInfo") > 0){
							//org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.dballsel,null);
							// 将返回的报文粘贴到
							/*JSONObject listChildren = new JSONObject();
							listChildren.put("DBId","00");
							listChildren.put("name","haha");
							listChildren.put("desc","11");
							JSONObject parameters = new JSONObject();
							parameters.put("result",0);
							parameters.put("extInfo","String");
							JSONArray jsonArray = new JSONArray();
							jsonArray.add(listChildren);
							parameters.put("list",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbQueryAllInfoReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("db/queryFeatureCount") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.dbpicsel,paramters);
							// 将返回的报文粘贴到
							/*JSONObject parameters = new JSONObject();
							parameters.put("result","0");
							parameters.put("extInfo",111);
							parameters.put("faceCount",123);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbQueryFeatureCountReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}

						if (method.indexOf("face/add") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.picadd,paramters);
							// 将返回的报文粘贴到
							/*JSONObject parameters = new JSONObject();
							parameters.put("result",0);
							parameters.put("extInfo","111");
							parameters.put("qualityScore","123");
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							faceAddReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("人脸删除：face/delete") >= 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.picdel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							faceDelReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("face/deleteBatch") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.picalldel,paramters);
							// 将返回的报文粘贴到
							// bug 数组中多个对象 部分对象节点类型不正确时会出现问题
							/*JSONObject listChildren1 = new JSONObject();
							listChildren1.put("DBId","00");
							listChildren1.put("faceId","haha");
							listChildren1.put("extInfo","11");
							JSONObject listChildren2 = new JSONObject();
							listChildren2.put("DBId","00");
							listChildren2.put("faceId","haha");
							listChildren2.put("extInfo",11);
							JSONObject parameters = new JSONObject();
							parameters.put("result","04");
							JSONArray jsonArray = new JSONArray();
							//jsonArray.add(listChildren);
							jsonArray.add(listChildren1);
							jsonArray.add(listChildren2);
							parameters.put("allResponse",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							JSONObject jsonObject  = JSONObject.fromObject(result);
							if(jsonObject.getInt("result") == 0){
								faceDelMuchSuccessReturn(result,responseParamTextArea);
							}else {
								faceDelMuchErrorReturn(result, responseParamTextArea);
							}
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("status") > 0){
							//org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.sysstatus,null);
							// 将返回的报文粘贴到
							/*JSONObject chidrenMap = new JSONObject();
							chidrenMap.put("name","String");
							chidrenMap.put("version","String");
							chidrenMap.put("status","String");
							chidrenMap.put("cpu","String");
							chidrenMap.put("disk","String");
							chidrenMap.put("mem",1000);
							chidrenMap.put("alive","String");
							JSONObject parameters = new JSONObject();
							parameters.put("result",0);
							parameters.put("extInfo",333333);
							JSONArray jsonArray = new JSONArray();
							jsonArray.add(chidrenMap);
							parameters.put("machine_list",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							statusReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("recog/search") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.picsel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							if(paramters.toString().indexOf("\"type\":\"2\"")>0){
								// type:2的时候未实现
								responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
								//searchReturn2(result,responseParamTextArea);
							}else{
								searchReturn(result,responseParamTextArea);
							}
						}
						if (method.indexOf("recog/feature") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.picfeasel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("recog/detect") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.picfacedet,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							detectReturn(result,responseParamTextArea);
						}
						if (method.indexOf("recog/compare") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = funcFaceUtilsTest.operation( ip, port,name,pwd,EventType.picfacecomp,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							compareReturn(result,responseParamTextArea);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}else{
					// 获取ip端口方法拼接成请求路径
					String method = functionComboBox.getSelectedItem().toString();
					// 根据方法设置参数，并将参数赋值在requestParamTextArea
					try {
						if (method.indexOf("db/create") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.dbadd,paramters);
							// 将返回的报文粘贴到
							/*JSONObject parameters = new JSONObject();
							parameters.put("result","0");
							parameters.put("extInfo",111111);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbCreateReturn(result,responseParamTextArea);
						}
						if (method.indexOf("db/delete") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.dbdel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							dbCreateReturn(result,responseParamTextArea);
						}
						if (method.indexOf("底库信息查询：db/query") >= 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.dbsel,paramters);
							// 将返回的报文粘贴到
							/*Map<String, String> chidrenMap = new HashMap<String, String>();
							JSONObject listChildren = new JSONObject();
							listChildren.put("DBId",00);
							listChildren.put("name","haha");
							listChildren.put("desc",11);
							JSONObject parameters = new JSONObject();
							parameters.put("result","Integer");
							parameters.put("extInfo","String");
							JSONArray jsonArray = new JSONArray();
							jsonArray.add(listChildren);
							parameters.put("db_list",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbQueryReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("db/edit") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.dbupd,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							dbCreateReturn(result,responseParamTextArea);
						}
						if (method.indexOf("db/queryAllInfo") > 0){
							//org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.dballsel,null);
							// 将返回的报文粘贴到
							/*JSONObject listChildren = new JSONObject();
							listChildren.put("DBId","00");
							listChildren.put("name","haha");
							listChildren.put("desc","11");
							JSONObject parameters = new JSONObject();
							parameters.put("result",0);
							parameters.put("extInfo","String");
							JSONArray jsonArray = new JSONArray();
							jsonArray.add(listChildren);
							parameters.put("list",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbQueryAllInfoReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("db/queryFeatureCount") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.dbpicsel,paramters);
							// 将返回的报文粘贴到
							/*JSONObject parameters = new JSONObject();
							parameters.put("result","0");
							parameters.put("extInfo",111);
							parameters.put("faceCount",123);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							dbQueryFeatureCountReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}

						if (method.indexOf("face/add") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.picadd,paramters);
							// 将返回的报文粘贴到
							/*JSONObject parameters = new JSONObject();
							parameters.put("result",0);
							parameters.put("extInfo","111");
							parameters.put("qualityScore","123");
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							faceAddReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("人脸删除：face/delete") >= 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.picdel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							faceDelReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("face/deleteBatch") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.picalldel,paramters);
							// 将返回的报文粘贴到
							// bug 数组中多个对象 部分对象节点类型不正确时会出现问题
							/*JSONObject listChildren1 = new JSONObject();
							listChildren1.put("DBId","00");
							listChildren1.put("faceId","haha");
							listChildren1.put("extInfo","11");
							JSONObject listChildren2 = new JSONObject();
							listChildren2.put("DBId","00");
							listChildren2.put("faceId","haha");
							listChildren2.put("extInfo",11);
							JSONObject parameters = new JSONObject();
							parameters.put("result","04");
							JSONArray jsonArray = new JSONArray();
							//jsonArray.add(listChildren);
							jsonArray.add(listChildren1);
							jsonArray.add(listChildren2);
							parameters.put("allResponse",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							JSONObject jsonObject  = JSONObject.fromObject(result);
							if(jsonObject.getInt("result") == 0){
								faceDelMuchSuccessReturn(result,responseParamTextArea);
							}else {
								faceDelMuchErrorReturn(result, responseParamTextArea);
							}
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("status") > 0){
							//org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.sysstatus,null);
							// 将返回的报文粘贴到
							/*JSONObject chidrenMap = new JSONObject();
							chidrenMap.put("name","String");
							chidrenMap.put("version","String");
							chidrenMap.put("status","String");
							chidrenMap.put("cpu","String");
							chidrenMap.put("disk","String");
							chidrenMap.put("mem",1000);
							chidrenMap.put("alive","String");
							JSONObject parameters = new JSONObject();
							parameters.put("result",0);
							parameters.put("extInfo",333333);
							JSONArray jsonArray = new JSONArray();
							jsonArray.add(chidrenMap);
							parameters.put("machine_list",jsonArray);
							String result = parameters.toString();*/
							responseParamTextArea.setText(result);
							statusReturn(result,responseParamTextArea);
							//responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("recog/search") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.picsel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							if(paramters.toString().indexOf("\"type\":\"2\"")>0){
								// type:2的时候未实现
								responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
								//searchReturn2(result,responseParamTextArea);
							}else{
								searchReturn(result,responseParamTextArea);
							}
						}
						if (method.indexOf("recog/feature") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.picfeasel,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(funcFaceUtilsTest.formatJson(result));
						}
						if (method.indexOf("recog/detect") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.picfacedet,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							detectReturn(result,responseParamTextArea);
						}
						if (method.indexOf("recog/compare") > 0){
							org.json.JSONObject paramters = new org.json.JSONObject(requestParamTextArea.getText());
							String result = faceAuthorizationFunction.operation( ip, port,name,pwd,EventType.picfacecomp,paramters);
							// 将返回的报文粘贴到
							responseParamTextArea.setText(result);
							compareReturn(result,responseParamTextArea);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public void dbCreateReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<String, String> map = new LinkedHashMap<String, String>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						map.put(element.getKey(),  String.valueOf(start)+";"+ String.valueOf(end));
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						map.put(element.getKey(),  String.valueOf(start)+";"+ String.valueOf(end));
					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element+"节点；");
			}
		}
		for(Map.Entry<String, String> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			String[] points = point.getValue().split(";");
			result = result.replaceAll(point.getKey(),"");
		}
		responseParamTextArea.setText(result);
		for(Map.Entry<String, String> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			String[] points = point.getValue().split(";");
			//result.substring(Integer.parseInt(points[0]),Integer.parseInt(points[1]));
			setDocs(point.getKey(),jTextPane, Color.red, Integer.parseInt(points[0]));
		}
	}
	public void dbQueryReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("db_list","List");
		Map<String, String> chidrenMap = new HashMap<String, String>();
		chidrenMap.put("DBId","String");
		chidrenMap.put("name","String");
		chidrenMap.put("desc","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();
						for(Map.Entry<String, String> chidrenElement : chidrenMap.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf(chidrenElement.getKey());
											int end = result.indexOf(chidrenElement.getKey())+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put(chidrenElement.getKey(),end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void dbQueryAllInfoReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("list","List");
		Map<String, String> chidrenMap = new HashMap<String, String>();
		chidrenMap.put("DBId","String");
		chidrenMap.put("name","String");
		chidrenMap.put("desc","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();
						for(Map.Entry<String, String> chidrenElement : chidrenMap.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf(chidrenElement.getKey());
											int end = result.indexOf(chidrenElement.getKey())+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put(chidrenElement.getKey(),end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void dbQueryFeatureCountReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("faceCount","Integer");
		rootMap.put("loadCount","Integer");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void statusReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("machine_list","List");
		Map<String, String> chidrenMap = new HashMap<String, String>();
		chidrenMap.put("name","String");
		chidrenMap.put("version","String");
		chidrenMap.put("status","String");
		chidrenMap.put("cpu","String");
		chidrenMap.put("disk","String");
		chidrenMap.put("mem","String");
		chidrenMap.put("alive","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();
						for(Map.Entry<String, String> chidrenElement : chidrenMap.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf(chidrenElement.getKey());
											int end = result.indexOf(chidrenElement.getKey())+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put(chidrenElement.getKey(),end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void faceAddReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("qualityScore","String");
		rootMap.put("DBId","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void faceDelReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("DBId","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void setDocs(String str, JTextPane jTextPane, Color color, Integer i)   {
		MutableAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet,color);//颜色
		javax.swing.text.Document doc = jTextPane.getDocument();
		try{
			doc.insertString(i, str, attrSet);
		}catch(BadLocationException e){
			System.out.println("BadLocationException:" + e);
		}
	}
	public void searchReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("reqId","String");
		rootMap.put("extInfo","String");
		rootMap.put("faces","List");
		Map<String, String> chidrenMap = new HashMap<String, String>();
		chidrenMap.put("score","Double");
		chidrenMap.put("DBId","String");
		chidrenMap.put("faceId","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Double".equals(element.getValue())){
					if(!(o instanceof Double)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();
						for(Map.Entry<String, String> chidrenElement : chidrenMap.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf(chidrenElement.getKey());
											int end = result.indexOf(chidrenElement.getKey())+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put(chidrenElement.getKey(),end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void searchReturn2(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("reqId","String");
		rootMap.put("extInfo","String");
		rootMap.put("faces","List");
		rootMap.put("result","List");
		Map<String, String> chidrenMap = new HashMap<String, String>();
		chidrenMap.put("score","Double");
		chidrenMap.put("DBId","String");
		chidrenMap.put("faceId","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						if("result".equals(jsonObject.get(element.getValue()))){
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();
						for(Map.Entry<String, String> chidrenElement : chidrenMap.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf(chidrenElement.getKey());
											int end = result.indexOf(chidrenElement.getKey())+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put(chidrenElement.getKey(),end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void detectReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("faces","List");
		Map<String, String> chidrenMap = new HashMap<String, String>();
		chidrenMap.put("img","String");
		chidrenMap.put("x","Integer");
		chidrenMap.put("y","Integer");
		chidrenMap.put("w","Integer");
		chidrenMap.put("h","Integer");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();
						for(Map.Entry<String, String> chidrenElement : chidrenMap.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf("\""+chidrenElement.getKey()+"\"");
											int end = result.indexOf("\""+chidrenElement.getKey()+"\"")+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put("\""+chidrenElement.getKey()+"\"",end);
											map.put(start,innerMap);
										}
									}
									if("Integer".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof Integer)){
											// 获取result中该节点的位置
											int start = result.indexOf("\""+chidrenElement.getKey()+"\"");
											int end = result.indexOf("\""+chidrenElement.getKey()+"\"")+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put("\""+chidrenElement.getKey()+"\"",end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void compareReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("score","Double");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<String, String> map = new LinkedHashMap<String, String>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						map.put(element.getKey(),  String.valueOf(start)+";"+ String.valueOf(end));
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						map.put(element.getKey(),  String.valueOf(start)+";"+ String.valueOf(end));
					}
				}else if("Double".equals(element.getValue())){
					if(!(o instanceof Double)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						map.put(element.getKey(),  String.valueOf(start)+";"+ String.valueOf(end));
					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element+"节点；");
			}
		}
		for(Map.Entry<String, String> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			String[] points = point.getValue().split(";");
			result = result.replaceAll(point.getKey(),"");
		}
		responseParamTextArea.setText(result);
		for(Map.Entry<String, String> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			String[] points = point.getValue().split(";");
			//result.substring(Integer.parseInt(points[0]),Integer.parseInt(points[1]));
			setDocs(point.getKey(),jTextPane, Color.red, Integer.parseInt(points[0]));
		}
	}
	public void featureReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("extInfo","String");
		rootMap.put("quality_score","String");
		rootMap.put("feature","String");
		rootMap.put("face_pose","List");
		rootMap.put("age","List");
		rootMap.put("gender","List");
		rootMap.put("glass","List");
		Map<String, String> chidrenMap1 = new HashMap<String, String>();
		chidrenMap1.put("pitch","String");
		chidrenMap1.put("roll","String");
		chidrenMap1.put("yaw","String");
		Map<String, String> chidrenMap2 = new HashMap<String, String>();
		chidrenMap2.put("value","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();

						for(Map.Entry<String, String> chidrenElement : chidrenMap1.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf(chidrenElement.getKey());
											int end = result.indexOf(chidrenElement.getKey())+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put(chidrenElement.getKey(),end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void faceDelMuchErrorReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("allResponse","List");
		Map<String, String> chidrenMap = new HashMap<String, String>();
		chidrenMap.put("faceId","String");
		chidrenMap.put("DBId","String");
		chidrenMap.put("extInfo","String");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("String".equals(element.getValue())){
					if(!(o instanceof String)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}else{
						net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray(element.getKey());
						int rootCount = jsonArray.size();
						for(Map.Entry<String, String> chidrenElement : chidrenMap.entrySet()){
							for (int j = 0; j<rootCount;j++){
								if(jsonArray.getJSONObject(j).has(chidrenElement.getKey())){
									if("String".equals(chidrenElement.getValue())){
										if(!(jsonArray.getJSONObject(j).get(chidrenElement.getKey()) instanceof String)){
											// 获取result中该节点的位置
											int start = result.indexOf(chidrenElement.getKey());
											int end = result.indexOf(chidrenElement.getKey())+chidrenElement.getKey().length();
											Map<String, Integer> innerMap = new HashMap<String, Integer>();
											innerMap.put(chidrenElement.getKey(),end);
											map.put(start,innerMap);
										}
									}
								}else{
									//不包含 提示中写入
									questionTextArea.setText(questionTextArea.getText()+"缺少"+chidrenElement.getKey()+"节点；");
								}
							}
						}

					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}
	public void faceDelMuchSuccessReturn(String result, JTextPane jTextPane){
		Map<String, String> rootMap = new HashMap<String, String>();
		rootMap.put("result","Integer");
		rootMap.put("allResponse","List");
		JSONObject jsonObject = JSONObject.fromObject(result);
		Map<Integer, Map<String,Integer>> map = new LinkedHashMap<Integer, Map<String,Integer>>();
		int i = 1;
		for(Map.Entry<String, String> element : rootMap.entrySet()){
			if(jsonObject.has(element.getKey())){
				// 判断类型
				Object o = jsonObject.get(element.getKey());
				if("Integer".equals(element.getValue())){
					if(!(o instanceof Integer)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}else if("List".equals(element.getValue())){
					if(!(o instanceof List)){
						// 获取result中该节点的位置
						int start = result.indexOf(element.getKey());
						int end = result.indexOf(element.getKey())+element.getKey().length();
						Map<String, Integer> innerMap = new HashMap<String, Integer>();
						innerMap.put(element.getKey(),end);
						map.put(start,innerMap);
					}
				}
			}else{
				//不包含 提示中写入
				questionTextArea.setText(questionTextArea.getText()+"缺少"+element.getKey()+"节点；");
			}
		}
		for(Map.Entry<Integer, Map<String,Integer>> point : map.entrySet()){
			// 截取调该节点 插入带有样式的新节点
			result = result.replaceAll(point.getValue().keySet().iterator().next(),"");
		}
		responseParamTextArea.setText(result);
		TreeMap treemap = new TreeMap(map);
		for (Object o : treemap.keySet()) {
			// 截取调该节点 插入带有样式的新节点
			setDocs(treemap.get(o).toString().substring(1,treemap.get(o).toString().indexOf("=")),jTextPane, Color.red, Integer.parseInt(o.toString()));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();//点击按钮时frame1销毁,new一个frame2
		JFrame jFrame = new MenuUi();
		jFrame.setVisible(true);
	}
}
