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
