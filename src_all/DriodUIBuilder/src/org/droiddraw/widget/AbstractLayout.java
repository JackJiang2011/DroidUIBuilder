/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * AbstractLayout.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Vector;

import org.droiddraw.AndroidEditor;
import org.droiddraw.MainPane;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.jb2011.ninepatch4j.NinePatch;

import com.jb2011.drioduibuilder.util.Utils;

/**
 * 所有布局widgets的父类.
 */
public abstract class AbstractLayout extends AbstractWidget implements Layout
{
	/** 默认宽度（用于缺省情况） */
	public static final int DEFAULT_WRAP_CONTENT_WIDTH = 116;
	/** 默认高度（用于缺省情况） */
	public static final int DEFAULT_WRAP_CONTENT_HEIGHT = 28;//24;//20
	
	/** 
	 * 用于UI设计时该布局组件的图标(包括它位于设计器的编辑面板和工具面板上).
	 * 
	 * <p>目的是方便用户便识这个组件是哪个layout，仅此而已 
	 */
	private Image iconInTools = null;
	
	/** 本布局中的所有子widgets集合 */
	protected Vector<Widget> widgets;

	public AbstractLayout(String tagName)
	{
		super(tagName);
		this.widgets = new Vector<Widget>();
		apply();
	}

	@Override
	public String toString()
	{
		return getTagName();
	}

	public void addWidget(Widget w)
	{
		assert (w != this);
		widgets.add(w);
		w.setParent(this);
		addEditableProperties(w);
		positionWidget(w);
		this.readWidthHeight();
		if (getParent() != null)
		{
			getParent().repositionAllWidgets();
		}
	}

	public Vector<Widget> getWidgets()
	{
		return widgets;
	}

	public void removeWidget(Widget w)
	{
		widgets.remove(w);
		removeEditableProperties(w);
		repositionAllWidgets();
	}

	public void removeAllWidgets()
	{
		for (Widget w : widgets)
		{
			removeEditableProperties(w);
		}
		widgets.clear();
	}

	/**
	 * 把绘制设计时的文本的方法单独提出来，以便子类进行自定义设置，
	 * 比如用空方法覆盖本方法从而实现不绘制显示文本的能力。
	 * 
	 * @param g
	 * @param iconInTools
	 */
	protected void paintText(Graphics g, Image iconInTools)
	{
		g.drawString(getTagName()
			, getX() 
				+ (AndroidEditor.instance().isRootLayout(this) // 当该是AndroidEditor的根布局时
					||iconInTools == null?					   // 或者该widget的左显示图标为空时
						// 水平不需要偏移的情况主要用于：根layout或者没有显示图标的情况
						0:iconInTools.getWidth(null)) + 12
			, getY() 
				+ (!this.isInDesignViewer()?// 当位于工具面板时显示文本垂直居中，否则居顶
					Utils.getFontCenterY(g.getFont(), getHeight()):17));
	}

	/**
	 * {@inheritDoc}
	 */
	protected void paintBackground(Graphics g)
	{
		if(this.isInDesignViewer())
			super.paintBackground(g);
		else
			// 位于工具面板时背景单独绘制一个好看一点的（并不
			// 是android真机上的效果哦，只是为了好看而已）
			WidgetDefaultNPIconFactory.getInstance().getEacherRoundBg()
				.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
		
		// 该布局没有子组件时才需要绘制以下内容
		if (widgets.size() == 0)
		{
			Image img = null;
			// 绘制用于UI设计时该布局组件的图标(包括它位于设计器的编辑面板和工具面板上).
			// 目的是方便用户便识这个组件是哪个layout，仅此而已
			if(!AndroidEditor.instance().isRootLayout(this) // 如果该layout是根布局，则不用绘制这个图标会好看点
					&& this.getIconInTools() != null)
			{
				img = this.getIconInTools();
				// 注意：paintBackground中的g坐标原点是没有移动到getX()和getY()的，
				// 否则绘制将错误地从(0,0)开始绘制，而非该 widget真正的坐标原点哦
				g.drawImage(img
						, getX() + 5
						// 垂直居中
						, getY() + (this.getHeight() - img.getHeight(null))/2
						, img.getWidth(null), img.getHeight(null), null);
			}
		}
	}

	// 注意：布局中的此方法，似乎是仅在Layouts 的wigets工具中用的，
	// 真正用于Viewer中时，是由它指定的painter类来实现的？？！！
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		// 绘制背景
		paintBackground(g);
		
		// 绘制widget名称：
		// 当该布局内没有组件时，绘制该布局的名称，比如“LinnerLayout”：
		// 以便即时让用户看到该布局是哪一种布局，方便进行ui设计，仅此而已，
		// 有关布局的背景等的绘制实现请参见它WidgetRegistry中注册的它的painter实现类
		if (widgets.size() == 0)
			paintText(g, this.getIconInTools());
////	g.drawString(getTagName(), 2, 15);
		
		// 移动坐标原点（该 原点是排除padding和margin之后的结果）
		g2d.translate(getX(), getY());
		g.setColor(Color.LIGHT_GRAY);
		
////		// 边框（用于widgets的tab中）
////		g.setColor(Color.red);
////		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
//		

		
		// 递归绘制子组件
		for (Widget w : widgets)
		{
//			if (w.isVisible())
//			if(!w.isGone())
				w.paint(g);
		}
		
		// 恢复坐标原点
		g2d.translate(-getX(), -getY());
	}

	@Override
	public int getContentWidth()
	{
		if (widgets.size() > 0)
		{
			int maxX = 0;
			for (Widget w : widgets)
			{
				/* LEFT padding already in X value */
				// w.apply();
				int width_w_pad = w.getWidth() + w.getPadding(RIGHT);
				if (w.getX() + width_w_pad > maxX)
					maxX = w.getX() + width_w_pad;
			}
			return maxX;
		}
		else
			return getDefaultWrapContentWidth();
	}

	@Override
	public int getContentHeight()
	{
		if (widgets.size() > 0)
		{
			int maxY = 0;
			for (Widget w : widgets)
			{
				/* TOP padding already in Y value */
				int height_w_pad = w.getHeight() + w.getPadding(BOTTOM);
				if (w.getY() + height_w_pad > maxY)
					maxY = w.getY() + height_w_pad;
			}
			return maxY;
		}
		else
			return getDefaultWrapContentHeight();
	}
	
	/**
	 * 获得默认宽度（用于缺省情况）.
	 * 
	 * @return
	 */
	// 提出此方法的目的是希望留给子类覆盖实现自已的特殊定义的机会
	protected int getDefaultWrapContentWidth()
	{
		return DEFAULT_WRAP_CONTENT_WIDTH;
	}
	
	/**
	 * 获得默认高度（用于缺省情况）.
	 * 
	 * @return
	 */
	// 提出此方法的目的是希望留给子类覆盖实现自已的特殊定义的机会
	protected int getDefaultWrapContentHeight()
	{
		return DEFAULT_WRAP_CONTENT_HEIGHT;
	}

	/*
	 * public void setPosition(int x, int y) { super.setPosition(x, y);
	 * //repositionAllWidgets(); //apply(); }
	 */
	public abstract void positionWidget(Widget w);

	public abstract void repositionAllWidgets();

	public int getScreenX()
	{
		if (parent != null && parent != this)
		{
			return (parent).getScreenX() + getX();
		}
		else
		{
			return getX();
		}
	}

	public int getScreenY()
	{
		if (parent != null && parent != this)
		{
			return parent.getScreenY() + getY();
		}
		else
		{
			return getY();
		}
	}

	public void resizeForRendering()
	{
		for (Widget w : widgets)
		{
			if (w instanceof Layout)
			{
				((Layout) w).resizeForRendering();
			}
		}
	}

	public void clearRendering()
	{
		for (Widget w : widgets)
		{
			w.apply();
			if (w instanceof Layout)
			{
				((Layout) w).clearRendering();
			}
		}
	}

	public boolean containsWidget(Widget w)
	{
		for (Widget wt : widgets)
		{
			if (wt.equals(w))
			{
				return true;
			}
			else if (wt instanceof Layout)
			{
				if (((Layout) wt).containsWidget(w))
				{
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Widget copy()
	{
		MainPane.getTipCom().warn("Cloning layouts is not supported (yet)");
		return null;
	}

	/**
	 * 获得用于UI设计时该布局组件的图标——目的是方便用户便识这
	 * 个组件是哪个layout，仅此而已.
	 * 
	 * @return
	 */
	public Image getIconInTools()
	{
		return iconInTools;
	}
	/**
	 * 设置用于UI设计时该布局组件的图标——目的是方便用户便识这
	 * 个组件是哪个layout，仅此而已.
	 * 
	 * @param iconInTools
	 */
	public void setIconInTools(Image iconInTools)
	{
		this.iconInTools = iconInTools;
	}
}
