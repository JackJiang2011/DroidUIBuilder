/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Button.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;
import org.droiddraw.property.StringProperty;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.jb2011.ninepatch4j.NinePatch;

public class Button extends TextView
{
	public static final String TAG_NAME = "Button";
//	NineWayImage img;
	protected NinePatch img_base;
	StringProperty onClick;
	
	public final static int DEFAULT_CONTENT_HEIGHT = 38;

	public Button(String txt)
	{
		super(txt);
		this.setTagName(TAG_NAME);

		pad_x = 10;
		pad_y = 0;

		img_base = null;
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			img_base = WidgetDefaultNPIconFactory.getInstance().getButton_normal();
//					ImageResources.instance().getImage(
//					"def/btn_default_normal.9");
//			if (img_base != null)
//			{
//				this.img = new NineWayImage(img_base, 10, 10);
//			}
		}
//		else if (theme.equals("light"))
//		{
//			img_base = ImageResources.instance().getImage(
//					"light/button_background_normal.9");
//			if (img_base != null)
//			{
//				this.img = new NineWayImage(img_base, 10, 10);
//			}
//		}
		this.onClick = new StringProperty("OnClick",//"Click Listener Classname",
				"android:onClickListener", null);
		addProperty(onClick);
		apply();
	}

	@Override
	public void apply()
	{
		super.apply();
		this.baseline = fontSize + 2;
	}

	@Override
	protected int getContentHeight()
	{
		if (img_base != null)
			return DEFAULT_CONTENT_HEIGHT;
//			return img_base.getHeight() - 4;//.getHeight(null) - 4;
		else
			return 10;
	}
	
	/**
	 * 提取出本方法为了赋予子类自由决定(而不一定非
	 * 得像Button一样)contentWidth的能力.
	 * 
	 * @return
	 */
	protected int getContentWidthImpl()
	{
		int w = super.getContentWidth();
		if (img_base != null && w < img_base.getWidth())
			return img_base.getWidth();
		return w;
	}
	@Override
	protected int getContentWidth()
	{
		return getContentWidthImpl()+10;// 10是delta增益值，为了好看而已
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
	}

	@Override
	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		// 处于可见状态才填充内容
		if(this.isVisible())
		{
			if (img_base == null)
			{
				g.setColor(Color.white);
				g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);

				g.setColor(Color.black);
				g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
			}
			else
			{
//				img_base.paint(g, getX(), getY(), getWidth(), getHeight());
				img_base.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
				g.setColor(Color.black);
			}
		}
		
		g.setFont(f);
		// int w = g.getFontMetrics(f).stringWidth(text.getStringValue());
		g.setColor(textColor.getColorValue());

		drawText(g, 0, getHeight() / 2 + fontSize / 2 - 2, CENTER);
		// g.drawString(text.getStringValue(), getX()+getWidth()/2-w/2,
		// getY()+fontSize+2);
	}
}
