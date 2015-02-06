/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * LaconicTip.java at 2015-2-6 16:12:01, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package com.jb2011.drioduibuilder.swingw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jb2011.lnf.beautyeye.utils.BEUtils;

import com.jb2011.drioduibuilder.swingw.border.LaconicBorder;

/**
 * 一个具有现代审美的信息提示复合组件实现类.
 * 
 * @author Jack Jiang, 2012-11-16
 * @version 1.0
 */
public class LaconicTip extends JPanel
{
	private final Color COLOR_INFO = new Color(58,135,173);
	private final Color COLOR_WARN = new Color(239,145,0);
	private final Color COLOR_ERROR = new Color(235,0,0);
	
	private final String TEXT_INFO = "Tips";
	private final String TEXT_WARN = "Warning";
	private final String TEXT_ERROR = "Error";
	
	private JLabel lbTipIcon = null;
	private JLabel lbTipContent = null;
	private JButton btnClose = null;
	
	public LaconicTip(boolean defaultVisible)
	{
		super(new BorderLayout());
		
		this.setVisible(defaultVisible);
		initGUI();
		initListeners();
	}
	
	protected void initGUI()
	{
		// 左图标
		lbTipIcon = new JLabel(TEXT_INFO)
		{
			public void paintComponent(Graphics g) {
				// 绘制一个大圆角背景
				BEUtils.setAntiAliasing((Graphics2D)g, true);
				Color oldColor = g.getColor();
				g.setColor(this.getBackground());
				g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 20, 20);
				g.setColor(oldColor);
				BEUtils.setAntiAliasing((Graphics2D)g, false);
				
				super.paintComponent(g);
			}
		};
		Font lbTipIconFont = lbTipIcon.getFont().deriveFont(Font.BOLD);
		lbTipIcon.setFont(lbTipIconFont);
		lbTipIcon.setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 6));
		lbTipIcon.setForeground(Color.white);
		lbTipIcon.setBackground(COLOR_INFO);// default bg color
		
		// 文本信息显示组件
		lbTipContent = new JLabel("");
		lbTipContent.setBorder(BorderFactory.createEmptyBorder(1, 4, 1, 4));
		
		// 关闭按钮
		btnClose = new JButton("");
		btnClose.setContentAreaFilled(false);
		btnClose.setFocusable(false);
		btnClose.setMargin(new Insets(0,0,0,0));
		btnClose.setIcon(IconFactory.getInstance().getLaconicTipCloseIcon_normal());
		btnClose.setRolloverIcon(IconFactory.getInstance().getLaconicTipCloseIcon_rover());
		btnClose.setPressedIcon(IconFactory.getInstance().getLaconicTipCloseIcon_rover());
		
		// 总体布局
		add(lbTipIcon,BorderLayout.WEST);
		add(lbTipContent,BorderLayout.CENTER);
		add(btnClose,BorderLayout.EAST);
		
		// 设置一个border，使总体视觉效果更佳
		this.setBorder(BorderFactory.createCompoundBorder(new LaconicBorder()
			, BorderFactory.createEmptyBorder(2, 4, 3, 4)));
	}
	
	protected void initListeners()
	{
		btnClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				hideIt();
			}
		});
	}
	
	public void info(Object txt)
	{
		lbTipIcon.setBackground(COLOR_INFO);
		lbTipIcon.setText(TEXT_INFO);
		setContentMessage(txt);
		showIt();
	}
	
	public void warn(Object txt)
	{
		lbTipIcon.setBackground(COLOR_WARN);
		lbTipIcon.setText(TEXT_WARN);
		setContentMessage(txt);
		showIt();
	}
	
	public void error(Object txt)
	{
		lbTipIcon.setBackground(COLOR_ERROR);
		lbTipIcon.setText(TEXT_ERROR);
		setContentMessage(txt);
		showIt();
	}
	
	private void setContentMessage(Object obj)
	{
		if(obj == null)
		{
			lbTipContent.setText("[message is empty!]");
			return;
		}
			
		if(obj instanceof String)
			lbTipContent.setText((String)obj);
		else if(obj instanceof Exception)
		{
			Exception e = (Exception)obj;
			if(e != null)
			{
				lbTipContent.setText(e.toString());
				((Exception)obj).printStackTrace();
			}
		}
	}
	
	public void showIt()
	{
		this.setVisible(true);
	}
	public void hideIt()
	{
		this.setVisible(false);
	}
}
