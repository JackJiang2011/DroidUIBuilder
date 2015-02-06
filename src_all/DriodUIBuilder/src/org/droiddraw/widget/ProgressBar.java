/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ProgressBar.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.StringProperty;

public class ProgressBar extends AbstractWidget
{
	public static final String[] propertyNames = new String[] {
			"android:indeterminate", "android:max" };
	public static final String TAG_NAME = "ProgressBar";
	BooleanProperty indeterminate;
	Image base;
	Image dot;
	Image indet;

	public ProgressBar()
	{
		super(TAG_NAME);
		indeterminate = new BooleanProperty("Indeterminate",
				"android:indeterminate", false);
		addProperty(indeterminate);
		addProperty(new StringProperty("Max. Value", "android:max", "100"));
		apply();
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			base = ImageResources.instance().getImage(
					"def/progress_wheel_medium");
		}
		else
		{
			base = ImageResources.instance().getImage(
					"light/progress_circular_background");
			dot = ImageResources.instance().getImage("light/progress_particle");
			indet = ImageResources.instance().getImage(
					"light/progress_circular_indeterminate");
		}
	}

	@Override
	protected int getContentHeight()
	{
		return 48;
	}

	@Override
	protected int getContentWidth()
	{
		return 48;
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		if (base == null)
		{
			g.setColor(Color.black);
			g.fillOval(getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.white);
			g.fillOval(getX() + getWidth() / 4, getY() + getHeight() / 4,
					getWidth() / 2, getHeight() / 2);
			g.fillOval(getX() + getWidth() / 3, getY(), getWidth() / 4 - 3,
					getHeight() / 4 - 3);
		}
		else
		{
			g.drawImage(base, getX(), getY(), getWidth(), getHeight(), null);
			if (indet != null)
			{
				if (indeterminate.getBooleanValue())
				{
					g.drawImage(indet, getX(), getY(), getWidth(), getHeight(),
							null);
				}
				else
				{
					g.drawImage(dot, getX(), getY(), getWidth(), getHeight(),
							null);
				}
			}
		}
	}
}
