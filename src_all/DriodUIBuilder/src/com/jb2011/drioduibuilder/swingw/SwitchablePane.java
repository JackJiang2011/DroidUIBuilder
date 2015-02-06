/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * SwitchablePane.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package com.jb2011.drioduibuilder.swingw;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jb2011.drioduibuilder.util.NinePatchIcon;

/**
 * 一个可按页切换的复合面板(CardLayout实现).
 * 
 * @author Jack Jiang, 2012-12-03
 * @version 1.0
 * 
 * TODO 选中对应页后，对应的分页按钮的样式要优化——比如选中样式，这样就能告诉用户
 * TODO 当前点中的是哪一样哦！！！！！！
 */
public class SwitchablePane
{
	private final static int DEFAULT_BUTTON_HEIGHT = 28;
	/** 本复合组件的主面板 */
	private JPanel mainPane = null;
	/** 本复合组件的内容组件子面板(card布局) */
	private JPanel cardPane = null;
	/** 本复合组件的按钮子面板 */
	private JPanel btnPane = null;
	
	/** 
	 * 当前总card数量：本字段目前仅用于向前和向后
	 * 按钮自动计算当前显示card顺序号时使用. */
	private int sumCountOfCards = 0;
	/** 
	 * 记下当前顺序号的目的是为了绘制该 card对应顺序按钮的
	 * 选中状态哦 */
	private int currrentCardIndex = 1;// 起始索引号是1
	
	/** 当前正在显示的card对应的顺序按钮的装饰小图标 */
	private ImageIcon smallIconForCurrrentCard = com.jb2011.drioduibuilder.swingw.IconFactory
			.getInstance().getSwitchableBtnSelected_flag();
	
	public SwitchablePane(Object[][] cards)
	{
		if(cards == null || cards.length < 1)
			throw new IllegalArgumentException("无效的参数, cards="
						+cards+" cards.length="+(cards!=null?-1:cards.length));
		
		// 本面板中的卡片总数
		sumCountOfCards = cards.length;
		// 初始化GUI
		initGUI(cards);
	}
	
	/**
	 * 核心GUI初始化方法.
	 * 
	 * @param cards
	 */
	protected void initGUI(Object[][] cards)
	{
		// 实例化子各主要面板
		mainPane = new JPanel(new BorderLayout());
		cardPane = new JPanel(new CardLayout());
		FlowLayout btnPaneLayout = new FlowLayout(FlowLayout.CENTER);
		btnPaneLayout.setVgap(0);
		btnPane = new JPanel(btnPaneLayout);
		
		// 加入向前按钮
		addToBtnPane(initPreviousButton());
		// 加入页号按钮
		for(int i=0;i<cards.length;i++ )
		{
			Object[] cardInfo = cards[i];
//			String num = (String)cardInfo[0];
			JComponent c = (JComponent)cardInfo[0];
			String toolTipText = (String)cardInfo[1];
			
			// 此key即进分页组件的顺序号（从1开始），又将做为它
			// 位于CardLayout中的name(constraints)，一举多得
			final String key = (i+1)+"";
			// 先在把要显示的内容组件加入到内容子面板中
			this.addCard(key, c, toolTipText);
			// 再把该分页内容组件对应的页号按钮加入到按钮面板中
			addToBtnPane(createPageButton(key, toolTipText));
		}
		// 加入向后按钮
		addToBtnPane(initNextButton());
		
		// 总体布局
		mainPane.add(cardPane, BorderLayout.CENTER);
		mainPane.add(btnPane, BorderLayout.SOUTH);
	}
	
	/**
	 * 本方法用于点击向前或向后按钮时，自动计算出当前所显示的
	 * 卡片对应的顺序号，记下顺序号的目的是为了绘制该 card对应顺序按钮的
	 * 选中状态哦！
	 * 
	 * @param delta 矢量，+1用于向前按钮时，-1用于向后按钮时
	 */
	private void autoCalculateCurrrentCardIndex(int delta)
	{
		int _tempIndex = currrentCardIndex + delta;
		if(_tempIndex <1 )
			_tempIndex = sumCountOfCards;
		else if(_tempIndex > sumCountOfCards)
			_tempIndex = 1;
		
		// 把计算出来的当前所显示的卡片的顺序号存起来
		currrentCardIndex = _tempIndex;
	}
	
	private JButton initPreviousButton()
	{
		JButton btnPrevious = createImageButton("", false);
		btnPrevious.setIcon(new NinePatchIcon(36, DEFAULT_BUTTON_HEIGHT, 
						NPIconFactory.getInstance().getSwitchable_previous_normal()));
		btnPrevious.setPressedIcon(new NinePatchIcon(36, DEFAULT_BUTTON_HEIGHT, 
						NPIconFactory.getInstance().getSwitchable_previous_pressed()));
		btnPrevious.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				((CardLayout)cardPane.getLayout()).previous(cardPane);
				// 更新当前所显示的卡片的顺序号
				autoCalculateCurrrentCardIndex(-1);
				// 刷新按钮面板用于更新当前显示card对应按钮的小图标的绘制
				btnPane.repaint();
			}
		});
		return btnPrevious;
	}
	
	private JButton initNextButton()
	{
		JButton btnNext = createImageButton("", false);
		btnNext.setIcon(new NinePatchIcon(36, DEFAULT_BUTTON_HEIGHT, 
						NPIconFactory.getInstance().getSwitchable_next_normal()));
		btnNext.setPressedIcon(new NinePatchIcon(36, DEFAULT_BUTTON_HEIGHT, 
						NPIconFactory.getInstance().getSwitchable_next_pressed()));
		btnNext.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				((CardLayout)cardPane.getLayout()).next(cardPane);
				
				// 更新当前所显示的卡片的顺序号
				autoCalculateCurrrentCardIndex(+1);
				// 刷新按钮面板用于更新当前显示card对应按钮的小图标的绘制
				btnPane.repaint();
			}
		});
		return btnNext;
	}
	
	private JButton createPageButton(final String key, String toolTipText)
	{
		JButton btn = createImageButton(key, true);
		btn.setIcon(new NinePatchIcon(29, DEFAULT_BUTTON_HEIGHT, 
						NPIconFactory.getInstance().getSwitchable_btn_nornal()));
		btn.setPressedIcon(new NinePatchIcon(29, DEFAULT_BUTTON_HEIGHT, 
						NPIconFactory.getInstance().getSwitchable_btn_pressed()));
		btn.setToolTipText(toolTipText);
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				((CardLayout)cardPane.getLayout()).show(cardPane, key);
				// 把当前所显示的卡片的顺序号存起来
				currrentCardIndex = Integer.parseInt(key);// key就是该 card对应的顺序号
				// 刷新按钮面板用于更新当前显示card对应按钮的小图标的绘制
				btnPane.repaint();
			}
		});
		return btn;
	}
	
	private JButton createImageButton(String text, final boolean usedForPageButton)
	{
		JButton btn = new JButton(text){
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// 画选中时的装饰小图
				if(usedForPageButton 
						// 条件是本按钮就是当前正在显示的card所对应的顺序按钮
						&& (currrentCardIndex == Integer.parseInt(this.getText())))
				{
					g.drawImage(smallIconForCurrrentCard.getImage()
							, this.getWidth() - smallIconForCurrrentCard.getIconWidth() -1, 1
							, smallIconForCurrrentCard.getIconWidth(), smallIconForCurrrentCard.getIconHeight(), null);
				}
			}
		};
		btn.setHorizontalTextPosition(JButton.CENTER);
		btn.setMargin(new Insets(0,0,0,0));
		btn.setBorder(null);
		btn.setContentAreaFilled(false);
		btn.setFocusPainted(false);
		return btn;
	}
	
	/**
	 * 将要按页显示的内容组件加入到内容面板中.
	 * 
	 * @param num
	 * @param c
	 * @param toolTipText
	 */
	private void addCard(String num, JComponent c, String toolTipText)
	{
		if(num == null || c == null || toolTipText == null)
			throw new IllegalArgumentException(
					"无效的参数：num="+num+" ,c="+c+" , toolTipText="+toolTipText);
		// 为了后绪使用方便，把一个顺序号做为该组件位于CardLayout的constraint
		cardPane.add(c, num);
	}
	
	/**
	 * 将指定分页按钮加入到按钮子面板中.
	 * 
	 * @param btn
	 */
	private void addToBtnPane(AbstractButton btn)
	{
		btnPane.add(btn);
	}
	
	/**
	 * 返回本复合组件主面板.
	 * 
	 * @return
	 */
	public JPanel getMainPane()
	{
		return this.mainPane;
	}
	
//	public static void main(String[] args)
//	{
//		JFrame frame = new JFrame("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		frame.getContentPane().add(new SwitchablePane(
//				new Object[][]{new Object[]{new JButton("111"),"1111"}
//				, new Object[]{new JLabel("222222"),"222222"}
//				, new Object[]{new JLabel("3333333333"),"3333333333"}}).getMainPane());
//		frame.setSize(1000, 715);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//	}
}
