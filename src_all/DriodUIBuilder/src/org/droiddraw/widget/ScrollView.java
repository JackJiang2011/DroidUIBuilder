/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ScrollView.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;

public class ScrollView extends FrameLayout
{
	public static final String TAG_NAME = "ScrollView";
	StringProperty scrollbar_size;
	StringProperty scrollbar_fade;
	SelectProperty scrollbars;
	NineWayImage field;

	public ScrollView()
	{
		this.setTagName(TAG_NAME);
		
		// 设置图标，方便ui设计时进行辨识
		this.setIconInTools(
				org.droiddraw.resource.IconFactory.getInstance()
					.getScrollView_small_Icon().getImage());
		
		scrollbar_size = new StringProperty("Scrollbar Size",
				"android:scrollbarSize", "");
		scrollbar_fade = new StringProperty("Scrollbar Fade Duration",
				"android:scrollbarFadeDuration", "");
		scrollbars = new SelectProperty("Scrollbars", "android:scrollbars",
				new String[] { "none", "horizontal", "vertical" }, 0);
		props.add(scrollbar_size);
		props.add(scrollbar_fade);
		props.add(scrollbars);
		field = new NineWayImage(ImageResources.instance().getImage(
				"scrollfield.9"), 1, 1);
	}

	@Override
	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		// 绘制该 widget名称
		super.paintText(g, this.getIconInTools());
		
		field.paint(g, getX() + getWidth() - 10, getY(), 10, getHeight());
	}

}
