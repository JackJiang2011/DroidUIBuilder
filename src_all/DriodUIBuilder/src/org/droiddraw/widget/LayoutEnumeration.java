package org.droiddraw.widget;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.TreeNode;

public class LayoutEnumeration implements Enumeration<TreeNode>
{
	Iterator<Widget> widgets;

	public LayoutEnumeration(Layout l)
	{
		this.widgets = l.getWidgets().iterator();
	}

	public boolean hasMoreElements()
	{
		return widgets.hasNext();
	}

	public TreeNode nextElement()
	{
		Widget w = widgets.next();
		if (w instanceof Layout)
		{
			return new LayoutTreeNode((Layout) w);
		}
		else
		{
			return new WidgetTreeNode(w);
		}
	}
}
