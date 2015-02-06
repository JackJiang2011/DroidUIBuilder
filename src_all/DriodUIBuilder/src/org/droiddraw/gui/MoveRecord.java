/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * MoveRecord.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import org.droiddraw.widget.Widget;

import javax.swing.undo.AbstractUndoableEdit;

public class MoveRecord extends AbstractUndoableEdit
{
	private static final long serialVersionUID = 1L;
	int sx, sy;
	int nx, ny;
	Widget w;

	public MoveRecord(int sx, int sy, int nx, int ny, Widget w)
	{
		this.sx = sx;
		this.sy = sy;
		this.nx = nx;
		this.ny = ny;
		this.w = w;
	}

	@Override
	public boolean canRedo()
	{
		return true;
	}

	@Override
	public boolean canUndo()
	{
		return true;
	}

	@Override
	public void redo()
	{
		w.setPosition(nx, ny);
	}

	@Override
	public void undo()
	{
		w.setPosition(sx, sy);
	}

}
