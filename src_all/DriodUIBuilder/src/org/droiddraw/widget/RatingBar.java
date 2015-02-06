/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * RatingBar.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.IntProperty;

public class RatingBar extends ProgressBar
{
	/** 默认每颗星宽度 */
	public static final int DEFAULT_START_WIDTH = 42;
	Image star, star_half, start_off;
	IntProperty numStars;
	BooleanProperty indicator;
	// FloatProperty rating;
	// FloatProperty stepSize;

	public static final String TAG_NAME = "RatingBar";
	public static final String[] propertyNames = new String[] {
			"android:numStars", "android:isIndicator",
	// "android:rating",
	// "android:stepSize"
	};

	public RatingBar()
	{
		setTagName(TAG_NAME);
		star = ImageResources.instance().getImage("rate_star_big_on");//"rate_star_med_on");
		star_half = ImageResources.instance().getImage("rate_star_big_half");
		start_off = ImageResources.instance().getImage("rate_star_big_off");
		numStars = new IntProperty("Number of Stars", "android:numStars", 5);
		// rating = new FloatProperty("Rating", "android:rating", 5.0f);
		// stepSize = new FloatProperty("Step Size", "android:stepSize", 1.0f);
		indicator = new BooleanProperty("User Editable", "android:isIndicator", true);
		addProperty(numStars);
		addProperty(indicator);
		// addProperty(rating);
		// addProperty(stepSize);
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
		
		int width = 42;
		// 当位于工具箱时，为了美观，把全星和半星等机动组合绘制出来，好看一点
		if(!this.isInDesignViewer())
		{
			// 当总星数小于1时强制为1、当总星数小于2时，off星数为0，否则余下全为off星数
			int off_num = numStars.getIntValue()<1?1:numStars.getIntValue()<3?0:numStars.getIntValue() - 2;
			// 2个及以上星时
			if(numStars.getIntValue()>=2)
			{
				// 第一颗星是实星
				g.drawImage(star, getX(), getY(), width, getHeight(),null);
//				// 第二颗星是实星
//				g.drawImage(star, getX()+ width, getY(), width, getHeight(),null);
				// 第二颗星是半星
				g.drawImage(star_half, getX()+ width*1, getY(), width, getHeight(),null);
			}
			
			// 以下off星在本widget处于tools面板时就不需要绘制了，
			// 主要是占空间，不利于ui组件布局，w仅此而已
			if(this.isInDesignViewer())
			{
				// 余下全为off星
				for (int i = 0; i < numStars.getIntValue()-off_num; ++i)
					g.drawImage(start_off, getX() + width*(i+off_num), getY(), width, getHeight(),null);
			}
		}
		else
		{
			for (int i = 0; i < numStars.getIntValue(); ++i)
				g.drawImage(star, getX() + width * i, getY(), width, getHeight(),null);
		}
	}

	@Override
	protected Object clone()
		throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("Not yet...");
	}

	@Override
	protected int getContentHeight()
	{
		return Button.DEFAULT_CONTENT_HEIGHT;
	}

	@Override
	protected int getContentWidth()
	{
		int stars = 5;
		if (numStars != null)
			stars = numStars.getIntValue();
		return DEFAULT_START_WIDTH * stars;
	}
}
