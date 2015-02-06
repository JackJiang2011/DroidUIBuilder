/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * AutoCompleteTextView.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import org.droiddraw.property.StringProperty;

public class AutoCompleteTextView extends EditView
{
	public static final String TAG_NAME = "AutoCompleteTextView";

	public AutoCompleteTextView(String txt)
	{
		super(txt);
		setTagName(TAG_NAME);

		props.add(new StringProperty("Completion Hint", "android:completionHint", ""));
		props.add(new StringProperty("Hint Size", "android:completionHintSize", ""));
		props.add(new StringProperty("Completion Threshold", "android:completionThreshold", ""));

		apply();
	}
}
