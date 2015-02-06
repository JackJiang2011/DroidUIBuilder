/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * View.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;

public class View extends AbstractWidget
{

	public static final String TAG_NAME = "View";

	public View()
	{
		super(TAG_NAME);
	}

	@Override
	protected int getContentHeight()
	{
		return 0;
	}

	@Override
	protected int getContentWidth()
	{
		return 0;
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		g.setColor(background.getColorValue());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
