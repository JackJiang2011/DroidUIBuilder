/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * LayoutTreeNode.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public class LayoutTreeNode extends WidgetTreeNode
{
	Layout layout;

	public LayoutTreeNode(Layout layout)
	{
		super(layout);
		this.layout = layout;
	}

	public Enumeration<TreeNode> children()
	{
		return new LayoutEnumeration(layout);
	}

	public boolean getAllowsChildren()
	{
		return true;
	}

	public TreeNode getChildAt(int childIndex)
	{
		Widget w = layout.getWidgets().elementAt(childIndex);
		if (w instanceof Layout)
		{
			return new LayoutTreeNode((Layout) w);
		}
		else
		{
			return new WidgetTreeNode(w);
		}
	}

	public int getChildCount()
	{
		return layout.getWidgets().size();
	}

	public int getIndex(TreeNode node)
	{
		if (node instanceof WidgetTreeNode)
		{
			Widget w = ((WidgetTreeNode) node).getWidget();
			return layout.getWidgets().indexOf(w);
		}
		return -1;
	}

	public boolean isLeaf()
	{
		return true;
	}
}
