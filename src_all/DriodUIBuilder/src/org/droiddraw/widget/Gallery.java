/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Gallery.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.IntProperty;

import com.jb2011.drioduibuilder.util.Utils;

public class Gallery extends AbstractWidget
{

	public static final String TAG_NAME = "Gallery";
	public static String[] propertyNames = { "android:spacing",
			"android:animationDuration" };

	IntProperty spacing;
	IntProperty animationDuration;

	Image paint;

	public Gallery()
	{
		super(TAG_NAME);
		paint = ImageResources.instance().getImage("gallery");//"paint");

		animationDuration = new IntProperty("Anim. Duration",
				"android:animationDuration", 200);
		spacing = new IntProperty("Spacing", "android:spacing", 0);

		addProperty(animationDuration);
		addProperty(spacing);
		apply();
	}

	@Override
	protected int getContentHeight()
	{
		return 200;
	}

	@Override
	protected int getContentWidth()
	{
		return 300;
	}

	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
	}
	
	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
//		int w1 = getWidth() / 4;
//		int w2 = getWidth() / 3;
//		int off_x = 
//				(getWidth() - w1 * 2 - w2 - spacing.getIntValue() * 2) / 2 
//			+ 13; // delta
			
		if (paint != null)
		{
//			g.drawImage(paint, getX() + off_x, getY(), w1, w1, null);
//			g.drawImage(paint, getX() + off_x + spacing.getIntValue() * 2 + w1 + w2 
//					- 26 // delta
//					, getY(), w1, w1, null);
//			g.drawImage(paint, getX() + off_x + spacing.getIntValue() + w1 
//					- 13 // delta
//					, getY() + 2, w2, w2, null);
			g.drawImage(paint, getX(), getY() +2
					, getWidth(), getHeight()-15, null);
		}
		g.setColor(this.isInDesignViewer()?Color.LIGHT_GRAY:Color.black);//Color.black);
		final String showName = "Gallery";
		g.drawString(showName, 
				getX() + (getWidth()-Utils.getStrPixWidth(g.getFont(), showName))/2
//				+ getWidth() / 2
//					- 19 // delta
				, getY() + getHeight()
					- 4); // delta
	}
}
