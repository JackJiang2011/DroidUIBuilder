/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * TabHost.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import org.droiddraw.property.StringProperty;

/**
 * This class will provide TabHost functionality by containing a TabWidget and
 * its corresponding FrameLayout.
 * 
 * @author rey malahay <a href="mailto:reymalahay@gmail.com">Rey Malahay</a>
 * 
 */
public class TabHost extends FrameLayout
{

	public static final String TAG_NAME = "TabHost";
	/**
	 * This is the id that is displayed in the final xml file.
	 */
	private static final String ANDROID_ID = "@android:id/tabhost"; 

	/**
	 * Default constructor
	 */
	public TabHost()
	{
		this.setTagName(TAG_NAME);
		((StringProperty) this.getProperties().get(
				this.getProperties().indexOf(
						this.getPropertyByAttName("android:id"))))
				.setStringValue(ANDROID_ID);
	}
}