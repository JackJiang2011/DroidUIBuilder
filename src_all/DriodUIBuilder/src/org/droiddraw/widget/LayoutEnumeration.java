/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * LayoutEnumeration.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
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
