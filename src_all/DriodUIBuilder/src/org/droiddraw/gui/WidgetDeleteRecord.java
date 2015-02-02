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
