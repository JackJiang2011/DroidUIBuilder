/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * FrameLayout.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.Vector;

import org.droiddraw.property.Property;

public class FrameLayout extends AbstractLayout
{

	public static final String TAG_NAME = "FrameLayout";

	public FrameLayout()
	{
		super(TAG_NAME);
		
		// 设置图标，方便ui设计时进行辨识
		this.setIconInTools(
				org.droiddraw.resource.IconFactory.getInstance()
					.getFrameLayout_small_Icon().getImage());
	}

	public void addEditableProperties(Widget w)
	{
	}

	@Override
	public void positionWidget(Widget w)
	{
		w.setPosition(0, 0);
	}

	public void removeEditableProperties(Widget w)
	{
	}

	@Override
	public void repositionAllWidgets()
	{
		for (Widget w : widgets)
		{
			w.setPosition(0, 0);
		}
	}

	public void addOutputProperties(Widget w, Vector<Property> properties)
	{
	}
}
