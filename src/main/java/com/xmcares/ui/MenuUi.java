package com.xmcares.ui;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xpath.internal.operations.Equals;
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
public class MenuUi extends JFrame implements ActionListener {
	private JPanel contentJPanel;
	private JFrame jFrame;
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuUi frame = new MenuUi();
					frame.setResizable(false);
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

	public MenuUi() {
		//jFrame = new JFrame();
		setTitle("人脸服务器测试工具");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 450);
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
		JButton functionJb=new JButton("功能测试");
		functionJb.setBounds(80, 120, 120, 40);
		contentJPanel.add(functionJb);
		JButton performanceJb=new JButton("性能测试");
		performanceJb.setBounds(80, 190, 120, 40);
		contentJPanel.add(performanceJb);
		JButton accuracyJb=new JButton("准确率测试");
		accuracyJb.setBounds(80, 260, 120, 40);
		contentJPanel.add(accuracyJb);
		//jFrame.add(contentJPanel);
		// 加入事件监听
		functionJb.addActionListener(this);
		performanceJb.addActionListener(this);
		accuracyJb.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();//点击按钮时frame1销毁,new一个frame2
		if ("功能测试" == e.getActionCommand().toString()){
			JFrame jFrame = new MainUiFunction();
			jFrame.setVisible(true);
		}else if ("性能测试" == e.getActionCommand().toString()){
			JFrame jFrame = new MainUiPerformance();
			jFrame.setVisible(true);
		}else if ("准确率测试" == e.getActionCommand().toString()){
			JFrame jFrame = new MainUiAccuracyRate();
			jFrame.setVisible(true);
		}
	}
}
