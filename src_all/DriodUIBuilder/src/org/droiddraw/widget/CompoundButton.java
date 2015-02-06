/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * CompoundButton.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import org.droiddraw.property.BooleanProperty;

public class CompoundButton extends Button
{
	public CompoundButton(String text)
	{
		super(text);
		addProperty(new BooleanProperty("Checked", "android:checked", false));
	}
}
