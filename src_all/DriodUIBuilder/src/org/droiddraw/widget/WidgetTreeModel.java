/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * WidgetTreeModel.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.droiddraw.AndroidEditor;

public class WidgetTreeModel implements TreeModel
{
	List<TreeModelListener> listeners;

	public WidgetTreeModel()
	{
		listeners = new ArrayList<TreeModelListener>();
	}

	public void addTreeModelListener(TreeModelListener listener)
	{
		listeners.add(listener);
	}

	public Object getChild(Object parent, int index)
	{
		if (parent instanceof Layout)
		{
			Layout l = (Layout) parent;
			return l.getWidgets().get(index);
		}
		return null;
	}

	public int getChildCount(Object node)
	{
		if (node instanceof Layout)
		{
			return ((Layout) node).getWidgets().size();
		}
		return 0;
	}

	public int getIndexOfChild(Object parent, Object child)
	{
		if (parent instanceof Layout)
		{
			return ((Layout) parent).getWidgets().indexOf(child);
		}
		return -1;
	}

	public Object getRoot()
	{
		return AndroidEditor.instance().getRootLayout();
	}

	public boolean isLeaf(Object node)
	{
		if (node instanceof Layout)
		{
			return ((Layout) node).getWidgets().size() == 0;
		}
		return true;
	}

	public void removeTreeModelListener(TreeModelListener listener)
	{
		listeners.remove(listener);
	}

	public void valueForPathChanged(TreePath path, Object newValue)
	{
		throw new IllegalArgumentException("Unsupported.");
	}

	public void setRoot(Layout l)
	{
		fireStructureChangedEvent(new TreeModelEvent(this, new Object[] { l }));
	}

	protected void fireStructureChangedEvent(TreeModelEvent event)
	{
		for (TreeModelListener listener : listeners)
		{
			listener.treeStructureChanged(event);
		}
	}

	protected Object[] findPath(Widget w)
	{
		ArrayList<Object> path = new ArrayList<Object>();
		while (w != null)
		{
			path.add(w);
			w = w.getParent();
		}
		Collections.reverse(path);
		return path.toArray();
	}

	public void removeWidget(Widget w)
	{
		Layout parent = w.getParent();
		Object[] path = findPath(parent);
		int index = parent.getWidgets().indexOf(w);
		fireNodeRemovedEvent(new TreeModelEvent(this, path,
				new int[] { index }, new Object[] { w }));
	}

	public void removeAllWidgets(Layout parent)
	{
		Object[] path = findPath(parent);
		int[] indices = new int[parent.getWidgets().size()];
		for (int i = 0; i < indices.length; ++i)
		{
			indices[i] = i;
		}
		Object[] nodes = parent.getWidgets().toArray();
		fireNodeRemovedEvent(new TreeModelEvent(this, path, indices, nodes));
	}

	protected void fireNodeRemovedEvent(TreeModelEvent event)
	{
		for (TreeModelListener listener : listeners)
		{
			listener.treeNodesRemoved(event);
		}
	}

	public void addWidget(Widget w)
	{
		Layout parent = w.getParent();
		Object[] path = findPath(parent);
		int index = parent.getWidgets().indexOf(w);
		fireNodeInsertedEvent(new TreeModelEvent(this, path,
				new int[] { index }, new Object[] { w }));
	}

	protected void fireNodeInsertedEvent(TreeModelEvent event)
	{
		for (TreeModelListener listener : listeners)
		{
			listener.treeNodesInserted(event);
		}
	}
}
