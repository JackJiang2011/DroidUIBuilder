/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * WidgetDeleteRecord.java at 2015-2-6 16:12:01, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import org.droiddraw.AndroidEditor;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

public class WidgetDeleteRecord extends WidgetAddRecord
{
	private static final long serialVersionUID = 1L;

	public WidgetDeleteRecord(Layout l, Widget w)
	{
		super(l, w);
	}

	@Override
	public void undo()
	{
		super.redo();
		AndroidEditor.instance().select(w);
	}

	@Override
	public void redo()
	{
		super.undo();
	}
}
