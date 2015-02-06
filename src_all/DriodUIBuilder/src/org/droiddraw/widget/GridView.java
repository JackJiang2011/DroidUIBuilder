/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * GridView.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.IntProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.property.WidthProperty;

public class GridView extends AbstractWidget
{

	public static final String TAG_NAME = "GridView";

	IntProperty columnCount;
	StringProperty columnWidth;
	WidthProperty hSpacing, vSpacing;
	/** 该wdiget位于工具面板时的效果图 */
	private static Image imgInTools = null;
	
	public static final String[] propertyNames = { "android:numColumns",
			"android:columnWidth", "android:horizontalSpacing",
			"android:verticalSpacing" };

	public GridView()
	{
		super(TAG_NAME);
		columnCount = new IntProperty("Columns", "android:numColumns", -1);
		columnCount.setIntValue(5);
		columnWidth = new StringProperty("Column Width", "android:columnWidth",
				"");
		columnWidth.setStringValue("20"
				+ AndroidEditor.instance().getScreenUnit());
		hSpacing = new WidthProperty("Horiz. Spacing",
				"android:horizontalSpacing", 0);
		vSpacing = new WidthProperty("Vert. Spacing",
				"android:verticalSpacing", 0);

		addProperty(columnCount);
		addProperty(columnWidth);
		addProperty(hSpacing);
		addProperty(vSpacing);
		
		if(imgInTools == null)
			imgInTools = ImageResources.instance().getImage("gridview_small");

		apply();
	}

	@Override
	protected int getContentHeight()
	{
		return 50;
	}

	@Override
	protected int getContentWidth()
	{
		int cols = columnCount.getIntValue();
		int w = removeUnit(columnWidth.getStringValue());
		if (cols * w > 50)
		{
			return cols * w;
		}
		else
			return 50;
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
			g.drawString("GridView", getX() + 3, getY() + 16);

			g.setColor(Color.lightGray);
			for (int i = 1; i < columnCount.getIntValue(); i++)
			{
				int x = getX() + i * removeUnit(columnWidth.getStringValue());
				g.drawLine(x, getY(), x, getY() + getHeight());
				if (hSpacing.getIntValue() > 0)
				{
					x += hSpacing.getIntValue();
					g.drawLine(x, getY(), x, getY() + getHeight());
				}
			}
		}
	}

	private int removeUnit(String propertyValue)
	{
		int ix = propertyValue.indexOf("px");
		if (ix == -1)
		{
			ix = propertyValue.indexOf("dp");
		}
		if (ix == -1)
		{
			ix = propertyValue.indexOf("dip");
		}
		if (ix != -1)
		{
			propertyValue = propertyValue.substring(0, ix);
		}
		return Integer.parseInt(propertyValue);
	}
}
