/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * DatePicker.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.gui.ImageResources;

public class DatePicker extends AbstractWidget
{
	public static final String TAG_NAME = "DatePicker";
	
	private static Image img = null, img_small = null;

	public DatePicker()
	{
		super(TAG_NAME);
		
		apply();
	}
	
	/**
	 * 此图用于该 widget在设计器中时的ui背景.
	 * 
	 * @return
	 */
	public static Image getBgImg()
	{
		if (img == null)
			img = ImageResources.instance().getImage("date_picker");
		return img;
	}
	
	/**
	 * 此图用于该 widget在工具箱时的ui背景.
	 * 
	 * @return
	 */
	public static Image getBgImg_small()
	{
		if(img_small == null)
			img_small = ImageResources.instance().getImage("date_picker_small");
		return img_small;
	}

	@Override
	protected int getContentHeight()
	{
		return getBgImg().getHeight(null);
	}

	@Override
	protected int getContentWidth()
	{
		return getBgImg().getWidth(null);
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		// * 移动坐标原点（该 原点是排除padding和margin之后的结果）
		g.translate(getX(), getY());
		// 要绘制的ui图
		Image ig = // 处于工具面板时用小图标，节省空间
				this.isInDesignViewer()?getBgImg():getBgImg_small();
		// widget的宽度是否小于要绘的图片宽度，true表示是
		boolean isWidthLowerToImage = ig.getWidth(null)>getWidth();
		// widget的高度是否小于要绘的图片高度，true表示是
		boolean isHeightLowerToImage = ig.getHeight(null)>getHeight();
		
		// 变形缩放方式
//		g.drawImage(ig
//			, getX()+(isWidthLowerToImage?0:(getWidth() - ig.getWidth(null))/2)
//			, getY(), isWidthLowerToImage?getWidth():ig.getWidth(null)
//					, isHeightLowerToImage?getHeight():ig.getHeight(null)
//					, null);
		
		// 如果widget的宽度小于要绘的图片宽度，则最终的图像绘制宽度是widget宽度而非图像原宽度
		// ，否则在widget区域不够的情况下，图片会绘制到widget外面去，它就不好看了
		int drawW = isWidthLowerToImage?getWidth():ig.getWidth(null);
		// 如果widget的高度小于要绘的图片高度，则最终的图像绘制高度是widget高度而非图像原高度
		// ，否则在widget区域不够的情况下，图片会绘制到widget外面去，它就不好看了
		int drawH = isHeightLowerToImage?getHeight():ig.getHeight(null);
		// 如果widget的宽度小于要绘的图片宽度，则绘制目标坐标原
		// 点从0开始，否则在widget水平区域内居中绘制图片
		int drawX = isWidthLowerToImage?0:(getWidth() - ig.getWidth(null))/2;
		// 不变形裁剪方式
		g.drawImage(ig
				// 要绘制的目的地区域(坐标原点是相对于widget所处的整体位置)
				, drawX, 0, drawX+drawW, drawH // 从坐标1到坐标2
				// 要裁剪的源图像区域(坐标原点是相对于图片本身，所以要是(0,0))
				, 0, 0, drawW, drawH           // 从坐标1到坐标2
				, null);
		
		// * 恢复坐标原点
		g.translate(-getX(), -getY());
		
//		Date d = new Date();
//		Calendar c = Calendar.getInstance();
//		c.setTime(d);
//		int month = c.get(Calendar.MONTH);
//
//		g.setColor(Color.black);
//		SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");
//		g.drawString(df.format(d), getX() + 2, getY() + 14);
//
//		int day = c.get(Calendar.DATE);
//		c.add(Calendar.DATE, -day + 1);
//
//		while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
//			c.add(Calendar.DATE, -1);
//
//		g.setColor(Color.darkGray);
//		g.drawString("S", getX() + 2, getY() + 34);
//		g.drawString("M", getX() + 22, getY() + 34);
//		g.drawString("T", getX() + 42, getY() + 34);
//		g.drawString("W", getX() + 62, getY() + 34);
//		g.drawString("T", getX() + 82, getY() + 34);
//		g.drawString("F", getX() + 102, getY() + 34);
//		g.drawString("S", getX() + 122, getY() + 34);
//
//		for (int j = 0; j < 6; j++)
//		{
//			for (int i = 0; i < 7; i++)
//			{
//				if (c.get(Calendar.MONTH) == month)
//					g.setColor(Color.black);
//				else
//					g.setColor(Color.lightGray);
//				g.drawString("" + c.get(Calendar.DATE), getX() + 2 + i * 20,
//						getY() + 54 + j * 20);
//				c.add(Calendar.DATE, 1);
//			}
//		}
	}

}
