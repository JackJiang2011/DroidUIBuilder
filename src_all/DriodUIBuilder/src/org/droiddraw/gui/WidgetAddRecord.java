/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * WidgetAddRecord.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import org.droiddraw.AndroidEditor;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

import javax.swing.undo.AbstractUndoableEdit;

public class WidgetAddRecord extends AbstractUndoableEdit
{
	private static final long serialVersionUID = 1L;
	Layout l;
	Widget w;

	public WidgetAddRecord(Layout l, Widget w)
	{
		this.l = l;
		this.w = w;
	}

	@Override
	public void undo()
	{
		l.removeWidget(w);
		if (AndroidEditor.instance().getSelected() == w)
		{
			AndroidEditor.instance().select(null);
		}
	}

	@Override
	public void redo()
	{
		l.addWidget(w);
	}

	@Override
	public boolean canUndo()
	{
		return true;
	}

	@Override
	public boolean canRedo()
	{
		return true;
	}
}
