/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Ticker.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

public class Ticker extends FrameLayout
{
	public static final String TAG_NAME = "Ticker";

	public Ticker()
	{
		this.setTagName(TAG_NAME);
		apply();
	}

	@Override
	public void addWidget(Widget w)
	{
		if (widgets.size() == 0)
			super.addWidget(w);
	}
}
