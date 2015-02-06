/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * AbstractWidget.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.ColorProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.property.WidthProperty;
import org.droiddraw.util.DisplayMetrics;

public abstract class AbstractWidget implements Widget
{
	int x, y;
	int[] padding;

	int baseline;
	int width, height;
	private String tagName;
	Vector<Property> props;
	PropertyChangeListener listener;

	protected StringProperty id;
	protected static int widget_num = 0;
	Layout parent;

	WidthProperty widthProp;
	WidthProperty heightProp;
	StringProperty pad;
	StringProperty visibility;

	StringProperty marginBottom;
	StringProperty marginTop;
	StringProperty marginLeft;
	StringProperty marginRight;

	ColorProperty background;
	
	/**
	 * 仅用于ui设计器中，设置该widget是否是被选中状态(或者说是获得焦点). 
	 * 
	 * <p>true表示该widget被选中，否则没有选中.默认false.
	 */
	private boolean selected  = false;
	
	/**
	 * 设置该widget对象是否是位于设计器的编辑面板中(而不是位于
	 * 工具面板上).默认总是true.
	 * 
	 * <p>位于工具面板上意味着它仅是作为工具使用，否则将即是位于真正的
	 * 设计器预览视图里.
	 * 
	 * <p>true意味着该widget正位于设计器的编辑面板中
	 * (正常情况下都是处于这种情形之下)，否则意味着位于工具面板中.
	 * 
	 * <p>此标识的作用在于使得该 widget的paint方法可以
	 * 区分不同用途时它们的不同绘制实现，提高体验.
	 */
	private boolean inDesignViewer = true;
	
	public AbstractWidget(String tagName)
	{
		this.setTagName(tagName);
		this.props = new Vector<Property>();
		this.id = new StringProperty("Id", "android:id", "");
		this.id.setStringValue("@+id/widget" + (widget_num++));
		this.widthProp = new WidthProperty("Width", "android:layout_width", "");
		this.widthProp.setStringValue("wrap_content");
		this.heightProp = new WidthProperty("Height", "android:layout_height", "");
		this.heightProp.setStringValue("wrap_content");
		this.background = new ColorProperty("Background",//"Background Color",
				"android:background", null);
		this.pad = new StringProperty("Padding", "android:padding", "0dp");
		this.visibility = new SelectProperty("Visible", "android:visibility",
				new String[] { "visible", "invisible", "gone" }, 0);

		this.marginTop = new StringProperty("Top Margin",
				"android:layout_marginTop", "0dp");
		this.marginBottom = new StringProperty("Bottom Margin",
				"android:layout_marginBottom", "0dp");
		this.marginLeft = new StringProperty("Left Margin",
				"android:layout_marginLeft", "0dp");
		this.marginRight = new StringProperty("Right Margin",
				"android:layout_marginRight", "0dp");

		this.padding = new int[4];

		this.props.add(id);
		this.props.add(widthProp);
		this.props.add(heightProp);
		this.props.add(background);
		this.props.add(pad);
		this.props.add(visibility);

		this.props.add(marginTop);
		this.props.add(marginBottom);
		this.props.add(marginLeft);
		this.props.add(marginRight);

		this.baseline = 14;

		this.parent = null;
	}

	public void setPropertyChangeListener(PropertyChangeListener l)
	{
		this.listener = l;
	}

	public Layout getParent()
	{
		return parent;
	}

	public void setParent(Layout parent)
	{
		this.parent = parent;
		((AbstractWidget) parent).parentTest(this);
	}

	public void parentTest(Widget w)
	{
		try
		{
			if (w == this)
				throw new IllegalArgumentException("BAD!");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		if (getParent() != null)
		{
			((AbstractWidget) getParent()).parentTest(w);
		}
	}

	public String getId()
	{
		return id.getStringValue();
	}

	public void setId(String id)
	{
		this.id.setStringValue(id);
	}

	public Vector<Property> getProperties()
	{
		return props;
	}

	public void addProperty(Property p)
	{
		if (!props.contains(p))
			props.add(p);
		if (listener != null)
			listener.propertyChange(new PropertyChangeEvent(this, "properties",
					null, props));
	}

	public void removeProperty(Property p)
	{
		props.remove(p);
		if (listener != null)
			listener.propertyChange(new PropertyChangeEvent(this, "properties",
					null, props));
	}

	public Property getPropertyByAttName(String attName)
	{
		for (Property prop : props)
		{
			if (prop.getAtttributeName().equals(attName))
				return prop;
		}
		return null;
	}

	public boolean propertyHasValueByAttName(String attName, Object value)
	{
		for (Property prop : props)
		{
			if (prop.getAtttributeName().equals(attName)
					&& prop.getValue().equals(value))
				return true;
		}
		return false;
	}

	public void setPropertyByAttName(String attName, String value)
	{
		Property p = getPropertyByAttName(attName);
		if (p != null)
			p.setValue(value);
		else
		{
			StringProperty prop = new StringProperty(attName, attName, "");
			prop.setValue(value);
			props.add(prop);
		}
	}

	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public void setWidth(int width)
	{
		this.widthProp.setStringValue(width
				+ AndroidEditor.instance().getScreenUnit());
		apply();
	}

	public void setHeight(int height)
	{
		this.heightProp.setStringValue(height
				+ AndroidEditor.instance().getScreenUnit());
		apply();
	}

	public void setSize(int width, int height)
	{
		this.widthProp.setStringValue(width
				+ AndroidEditor.instance().getScreenUnit());
		this.heightProp.setStringValue(height
				+ AndroidEditor.instance().getScreenUnit());
		apply();
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public boolean clickedOn(int x, int y)
	{
		int off_x = 0;
		int off_y = 0;
		if (parent != null)
		{
			off_x = parent.getScreenX();
			off_y = parent.getScreenY();
		}
		return (x > this.getX() + off_x && x < this.getX() + off_x + getWidth()
				&& y > this.getY() + off_y && y < this.getY() + getHeight() + off_y);
	}

	public void move(int dx, int dy)
	{
		setPosition(this.x + dx, this.y + dy);
	}

	public String getTagName()
	{
		return tagName;
	}

	protected void readWidthHeight()
	{
		int w = DisplayMetrics.readSize(widthProp);
		int h = DisplayMetrics.readSize(heightProp);
		if (w < 0)
			w = getWidth();
		if (h < 0)
			h = getHeight();

		try{
			setPadding(DisplayMetrics.readSize(pad.getStringValue()));
		}
		catch (NumberFormatException ex){
		}

		if (widthProp.getStringValue().equals("wrap_content"))
			w = getContentWidth();
		if (heightProp.getStringValue().equals("wrap_content"))
			h = getContentHeight();

		if (widthProp.getStringValue().equals("fill_parent")
				|| widthProp.getStringValue().equals("match_parent"))
		{
			if (getParent() != null)
			{
				StringProperty prop = (StringProperty) parent
						.getPropertyByAttName("android:layout_width");
				if (prop.getStringValue().equals("wrap_content"))
					w = getContentWidth();
				else
				{
					// TODO(bburns) This is a little wonky, but we apply the
					// left margin in the layout.
					w = getParent().getWidth() - getMargin(RIGHT);
				}
			}
			else
			{
				w = AndroidEditor.instance().getScreenX() - AndroidEditor.OFFSET_X;
			}
			w = w - getX() - padding[RIGHT];
		}
		if (heightProp.getStringValue().equals("fill_parent")
				|| heightProp.getStringValue().equals("match_parent"))
		{
			if (getParent() != null)
			{
				StringProperty prop = (StringProperty) parent
						.getPropertyByAttName("android:layout_height");
				if (prop.getStringValue().equals("wrap_content"))
					h = getContentHeight();
				else
				{
					// TODO(bburns) This is a little wonky, but we apply the top
					// margin in the layout.
					h = getParent().getHeight() - getMargin(BOTTOM);
				}
			}
			else
				h = AndroidEditor.instance().getScreenY();
			h = h - getY() - padding[BOTTOM];
		}

		width = w;
		height = h;
	}

	public void apply()
	{
		readWidthHeight();
		if (getParent() == null)
			setPosition(getPadding(LEFT) + AndroidEditor.OFFSET_X,
					getPadding(TOP) + AndroidEditor.OFFSET_Y);
		if (widthProp.getStringValue().equals("fill_parent"))
			x = padding[LEFT];
		if (heightProp.getStringValue().equals("fill_parent")
				&& this.getParent() != null)
			y = padding[TOP];
		// if (getParent() != null) {
		// getParent().repositionAllWidgets();
		// }
	}

	public void setSizeInternal(int w, int h)
	{
		this.width = w;
		this.height = h;
	}

	public int getBaseline()
	{
		return baseline;
	}
	
	
	/**
	 * 该组件的边框绘制核心实现方法.
	 * 
	 * @param g
	 * @param c
	 */
	private void paintBorder(Graphics g, Color c)
	{
		// 如果该组件是根布局，则不用绘制这个border会好看点，否则普通组件和布局则无此限制
		if(!(this instanceof Layout && AndroidEditor.instance().isRootLayout(this))) 
		{
			Stroke oldStroke = ((Graphics2D)g).getStroke();
// 			处于gone状态的widget就不用绘制了
//			if(!isGone())
			{
				if (isVisible())
				{
					// do nothing
				}
				// 如果是invisible则用虚线进行绘制，从而方便告之用户，这些组件是invisible的
				else
				{
					// 虚线样式Button
					Stroke sroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
							BasicStroke.JOIN_BEVEL, 0, new float[]{2, 2}, 0);//实线，空白
					((Graphics2D)g).setStroke(sroke);
				}
			}

			//绘制边框
			g.setColor(c);
			g.drawRect(getX() - getPadding(LEFT), getY() - getPadding(TOP),
					getWidth() + getPadding(LEFT) + getPadding(RIGHT),
					getHeight() + getPadding(TOP) + getPadding(BOTTOM));

			// 撤销虚线设置
			if( !isVisible())
				((Graphics2D)g).setStroke(oldStroke);
		}
	}
	
	/**
	 * 绘制正常状态（当处于设计器中未被选中时）下的边框.
	 * 
	 * @param g
	 */
	protected void paintNormalBorder(Graphics g)
	{
		paintBorder(g, !isGone()?Color.LIGHT_GRAY:Color.red);
	}
	
	/**
	 * 绘制被选中状态（当处于设计器中被选中时）下的边框.
	 * 
	 * @param g
	 */
	protected void paintSelectedBorder(Graphics g)
	{
		paintBorder(g, !isGone()?Color.yellow:Color.pink);
	}
	
	/**
	 * 实现widget背景的绘制.
	 * 
	 * <p>内容包括填充可能的背景、绘制边框.
	 * 
	 * @param g
	 * @see #paintSelectedBorder(Graphics)
	 * @see #paintNormalBorder(Graphics)
	 */
	protected void paintBackground(Graphics g)
	{
		// 如果设置了该widget在andoird实机中运行的背景色时，也在设计器里
		// 将该颜色作为背景色，进行背景填充，尽量让设计器中的观感与实机一致
		if (background.getColorValue() != null)
		{
			g.setColor(background.getColorValue());
			g.fillRect(getX() - getPadding(LEFT), getY() - getPadding(TOP),
					getWidth() + getPadding(LEFT) + getPadding(RIGHT),
					getHeight() + getPadding(TOP) + getPadding(BOTTOM));
		}
		
		// 根据选中状态来决定边框的绘制实现
		if(isSelected())
			paintSelectedBorder(g);
		else
			paintNormalBorder(g);
		
//		layout的可见状态的ui绘制形态有等进一步确认。
//		
//		除layout之外的如Button它样的单widget，它们的选中与否ui绘制还没有实现，参考layout即可。
//		除layout之外的如Button它样的单widget，它们的invislble绘制可能也得参考layout的实现哦。
//		
//		好好合计全计：原作者的选中与否状态是在AndroidEditorViewer中实现，原layout的paint是在
//		LayoutPainter中实现的，Jack 现在是要统一它些绘制在widget的 paint里，从而使代码更规范！
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see #paintBackground(Graphics)
	 */
	public void paint(Graphics g)
	{
		// 绘制背景
		paintBackground(g);
	}

	public void setPadding(int pad)
	{
		setPadding(pad, TOP);
		setPadding(pad, LEFT);
		setPadding(pad, BOTTOM);
		setPadding(pad, RIGHT);
	}

	public void setPadding(int pad, int which)
	{
		padding[which] = pad;
	}

	public int getPadding(int which)
	{
		return padding[which];
	}

	public int getMargin(int which)
	{
		switch (which)
		{
			case TOP:
				return DisplayMetrics.readSize(marginTop);
			case BOTTOM:
				return DisplayMetrics.readSize(marginBottom);
			case LEFT:
				return DisplayMetrics.readSize(marginLeft);
			case RIGHT:
				return DisplayMetrics.readSize(marginRight);
			default:
				return 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isVisible()
	{
		return visibility.getStringValue().equals("visible");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isGone()
	{
		return visibility.getStringValue().equals("gone");
	}

	/** 该组件默认wrap_content时的宽度 */
	protected abstract int getContentWidth();
	/** 该组件默认wrap_content时的高度 */
	protected abstract int getContentHeight();

	/**
	 * @param tagName
	 *            the tagName to set
	 */
	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public Widget copy()
	{
		try
		{
			// StringWriter sw = new StringWriter();
			// PrintWriter pw = new PrintWriter(sw);
			// pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			// AndroidEditor.instance().generateWidget(this, pw);
			// AndroidEditor.instance().
			AbstractWidget w = (AbstractWidget) this.clone();
			w.setId("@+id/widget" + (widget_num++));
			return w;
		}
		catch (CloneNotSupportedException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	void addOrUpdateProperty(Vector<Property> properties, StringProperty prop)
	{
		Property found = null;
		for (Property p : properties)
		{
			if (p.getAtttributeName().equals(prop.getAtttributeName()))
			{
				found = p;
				break;
			}
		}
		if (found == null)
			properties.add(prop);
		else
		{
			try{
				((StringProperty) found).setStringValue(prop.getStringValue());
			}
			catch (ClassCastException ex){
				properties.remove(found);
				properties.add(prop);
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setSelect(boolean selected)
	{
		this.selected = selected;
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return this.selected;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isInDesignViewer()
	{
		return inDesignViewer;
	}
	/**
	 * {@inheritDoc}
	 */
	public Widget setInDesignViewer(boolean inDesignViewer)
	{
		this.inDesignViewer = inDesignViewer;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		return tagName + " - " + getId();
	}
}
