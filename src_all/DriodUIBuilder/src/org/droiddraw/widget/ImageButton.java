/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ImageButton.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;

import org.droiddraw.AndroidEditor;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.jb2011.ninepatch4j.NinePatch;

public class ImageButton extends ImageView
{
	public static final String TAG_NAME = "ImageButton";
//	NineWayImage img;
	NinePatch img_base;
	int cw;

	public ImageButton()
	{
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
			cw = 40;//50;
		}
//		else if (theme.equals("light"))
//		{
//			img_base = ImageResources.instance().getImage(
//					"light/button_background_normal.9");
//			if (img_base != null)
//			{
//				this.img = new NineWayImage(img_base, 10, 10);
//			}
//			cw = 50;
//		}
		this.setTagName(TAG_NAME);
		apply();
	}

	@Override
	protected int getContentHeight()
	{
		return cw;
	}

	@Override
	protected int getContentWidth()
	{
		return cw;
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
		
		if (img_base != null)
		{
//			img.paint(g, getX(), getY(), getWidth(), getHeight());
			img_base.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
		}
		if (super.img != null)
		{
			g.drawImage(super.img, getX() + 5, getY() + 5, getWidth() - 10,
					getHeight() - 10, null);
		}
		else if (paint != null)
		{
			g.drawImage(paint, getX() + 5, getY() + 5, getWidth() - 10,
					getHeight() - 10, null);
		}
	}
}
