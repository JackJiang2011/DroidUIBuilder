/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ListView.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;

public class ListView extends AbstractWidget
{
	public static final String TAG_NAME = "ListView";
	/** 该wdiget位于工具面板时的效果图 */
	private static Image imgInTools = null;

	public ListView()
	{
		super(TAG_NAME);
		props.add(new StringProperty("List Selector", "android:listSelector", ""));
		props.add(new BooleanProperty("Selector on Top",
				"android:drawSelectorOnTop", false));

		props.add(new StringProperty("Entry Array Id.", "android:entries", ""));
		props.add(new SelectProperty("Entry Gravity", "android:gravity",
				new String[] { "left", "center", "right" }, 0));
		
		if(imgInTools == null)
			imgInTools = ImageResources.instance().getImage("listview_small");
		
		apply();
	}

	@Override
	protected int getContentHeight()
	{
		return 16;
	}

	@Override
	protected int getContentWidth()
	{
		return 55;
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		if(this.isInDesignViewer())
			super.paintNormalBorder(g);
		// 本widget在tools面板中不需要绘制border的哦
		else
			;
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);

		// 该wdiget位于工具面板时
		if(!this.isInDesignViewer() && imgInTools != null)
		{
			g.drawImage(imgInTools, getX(), getY(), getWidth(), getHeight(), null);
		}
		else
		{
			g.setColor(Color.lightGray);
			g.drawString("ListView", getX() + 2, getY() + 14);
			//		g.drawRect(getX(), getY(), getWidth(), getHeight());
		}
	}

}
