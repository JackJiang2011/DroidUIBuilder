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
