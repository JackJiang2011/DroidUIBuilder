/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * WidgetTreeNode.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public class WidgetTreeNode implements TreeNode
{
	private Widget widget;

	public WidgetTreeNode(Widget w)
	{
		this.widget = w;
	}

	public Widget getWidget()
	{
		return widget;
	}

	public Enumeration<TreeNode> children()
	{
		return null;
	}

	public boolean getAllowsChildren()
	{
		return false;
	}

	public TreeNode getChildAt(int childIndex)
	{
		return null;
	}

	public int getChildCount()
	{
		return 0;
	}

	public int getIndex(TreeNode node)
	{
		return -1;
	}

	public TreeNode getParent()
	{
		return new LayoutTreeNode(widget.getParent());
	}

	public boolean isLeaf()
	{
		return false;
	}

	public boolean equals(Object o)
	{
		if (o instanceof WidgetTreeNode)
		{
			return ((WidgetTreeNode) o).widget.equals(widget);
		}
		return false;
	}
}
