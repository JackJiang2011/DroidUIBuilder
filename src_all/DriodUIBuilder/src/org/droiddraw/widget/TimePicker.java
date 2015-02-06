/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * TimePicker.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.droiddraw.gui.ImageResources;

public class TimePicker extends AbstractWidget
{
	public static final String TAG_NAME = "TimePicker";
//	Image up_arrow;
//	Image down_arrow;
//	NinePatch btn;
////	NineWayImage img;
	private static Image img = null, img_small = null;

	public TimePicker()
	{
		super(TAG_NAME);
		
//		up_arrow = ImageResources.instance().getImage("light/arrow_up_float");
//		down_arrow = ImageResources.instance().getImage(
//				"light/arrow_down_float");

//		String theme = AndroidEditor.instance().getTheme();
//		if (theme == null || theme.equals("default"))
//		{
//			btn = WidgetDefaultNPIconFactory.getInstance().getButton_normal();
////				ImageResources.instance()
////					.getImage("def/btn_default_normal.9");
////			img = new NineWayImage(btn, 10, 10);
//		}
		apply();
	}

	@Override
	public void apply()
	{
		super.apply();
		this.baseline = 22;
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
	
	/**
	 * 此图用于该 widget在设计器中时的ui背景.
	 * 
	 * @return
	 */
	public static Image getBgImg()
	{
		if (img == null)
			img = ImageResources.instance().getImage("time_picker");
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
			img_small = ImageResources.instance().getImage("time_picker_small");
		return img_small;
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
				this.isInDesignViewer()?img:img_small;
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
		
//		g.setColor(Color.black);
//		g.drawString("12:15 PM", getX(), getY() + 22);
//		if (up_arrow != null)
//		{
//			g.drawImage(up_arrow, getX() + 20, getY(), null);
//			g.drawImage(down_arrow, getX() + 20, getY() + 24, null);
//		}
//		if (btn != null)
//		{
////			img.paint(g, getX() + 60, getY(), 50, 50);
//			btn.draw((Graphics2D)g, getX() + 60, getY(), 50, getContentHeight());
//			g.drawString("Set", getX() + 75, getY()+Utils.getFontCenterY(g.getFont(), getContentHeight()));
//		}
	}

}
