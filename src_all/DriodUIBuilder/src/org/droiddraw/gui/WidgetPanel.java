/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * WidgetPanel.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.droiddraw.AndroidEditor;
import org.droiddraw.MainPane;
import org.droiddraw.widget.Widget;

/**
 * 用于工具箱中，作为各Widget的载体，以便显示于Swing界面上.
 * 
 * @author null, 2012-11-21
 */
public class WidgetPanel extends JPanel implements DragGestureListener,
		DragSourceListener
{
	private static final long serialVersionUID = 1L;
	Widget w;
	Dimension d;
	DragSource ds;
	BufferedImage img;
	int x, y;

	public WidgetPanel(Widget w)
	{
		this.w = w;
		// 设置标识，用于表明该 widget不是位于编辑面板中，而是位
		// 于工具箱上，此标识的作用在于使得该 widget的paint方法可以
		// 区分不同用途时它们的不同绘制实现，提高体验
		this.w.setInDesignViewer(false);
		d = new Dimension(w.getWidth()+1, w.getHeight()+1);// +1是位了修正border的绘制（否则会向右并向下多一个像素，就不好看了）
		this.w.setPosition(0, 0);
		this.setToolTipText(w.getTagName());

		this.ds = DragSource.getDefaultDragSource();
		this.ds.createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_COPY, this);

		this.img = new BufferedImage(w.getWidth(), w.getHeight(),
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		w.paint(g);

		this.addMouseListener(new MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() > 1)
				{
					AndroidEditor.instance().getRootLayout().addWidget(
							ViewerListener.createWidget(WidgetPanel.this.w
									.getTagName()));
					AndroidEditor.instance().setChanged(true);
				}
				else
				{
					if (WidgetPanel.this.w.getParent() != null)
					{
						AndroidEditor.instance().select(WidgetPanel.this.w);
					}
				}
			}

			public void mouseEntered(MouseEvent e)
			{
			}

			public void mouseExited(MouseEvent e)
			{
			}

			public void mousePressed(MouseEvent e)
			{
			}

			public void mouseReleased(MouseEvent e)
			{
			}
		});
	}

	@Override
	public Dimension getMinimumSize()
	{
		return d;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return d;
	}

	@Override
	public void paint(Graphics g)
	{
		w.paint(g);
	}

	public void dragGestureRecognized(DragGestureEvent e)
	{
		try
		{
			Transferable t = new StringSelection(w.getTagName());
			ds.startDrag(e, DragSource.DefaultCopyDrop, img, new Point(x, y),
					t, this);
			// ds.startDrag(e, DragSource.DefaultCopyDrop, t, this);
		}
		catch (InvalidDnDOperationException ex)
		{
			MainPane.getTipCom().warn(ex);
		}
	}

	public void dragDropEnd(DragSourceDropEvent e)
	{
	}

	public void dragEnter(DragSourceDragEvent e)
	{
	}

	public void dragExit(DragSourceEvent e)
	{
	}

	public void dragOver(DragSourceDragEvent e)
	{
	}

	public void dropActionChanged(DragSourceDragEvent arg0)
	{
	}
}
