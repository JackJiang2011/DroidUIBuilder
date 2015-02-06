/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ImageView.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.ImageProperty;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.jb2011.ninepatch4j.NinePatch;

public class ImageView extends AbstractWidget
{
	public static final String TAG_NAME = "ImageView";
	Image paint;
	BufferedImage img;

	ImageProperty src;
	/** 
	 * 本widget处于工具面板时绘制背景使用的NP图.
	 * 该背景仅为了出于美感而作，别无它用，并不对应于该widget在android真机上的效果哦.
	 */
	protected NinePatch bgInTools = null;

	public ImageView()
	{
		super(TAG_NAME);
		paint = ImageResources.instance().getImage("paint");
		bgInTools = WidgetDefaultNPIconFactory.getInstance().getTextView_normal();
		src = new ImageProperty("Image Source", "android:src", "");
		addProperty(src);
		apply();
	}

	@Override
	protected int getContentHeight()
	{
		if (img == null)
			return 30;
		else
			return img.getHeight();
	}

	@Override
	protected int getContentWidth()
	{
		if (img == null)
			return 30;
		else
			return img.getWidth();
	}

	@Override
	public void apply()
	{
		super.apply();
		if (src.getStringValue() != null
				&& src.getStringValue().startsWith("@drawable"))
		{
			img = AndroidEditor.instance().findDrawable(src.getStringValue());
		}
	}

	public void paint(Graphics g)
	{
		// 仅在本widget处于工具面板时绘制
		if(!this.isInDesignViewer())
		{
			if (bgInTools != null)
				bgInTools.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
			
			// 出一个默认图片，方便用户例谡widget，仅此而已
			if (paint != null)
				g.drawImage(paint, getX()+10, getY() +4 
						, paint.getWidth(null)-5 , paint.getHeight(null)-5 , null);
		}
		else
		{
			// 绘制背景
			super.paintBackground(g);
			
			if (img != null)
				g.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);
			// 如果该widget没有设置@drawable/中的图片，则画一个默认图片方便设计
			else if (paint != null)
				g.drawImage(paint, getX(), getY(), getWidth(), getHeight(), null);
		}
	}
}
