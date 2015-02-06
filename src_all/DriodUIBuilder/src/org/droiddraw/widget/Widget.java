/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Widget.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import org.droiddraw.property.Property;

public interface Widget extends Cloneable
{
	public static final int TOP = 0;
	public static final int LEFT = 1;
	public static final int BOTTOM = 2;
	public static final int RIGHT = 3;

	public boolean clickedOn(int x, int y);

	public int getX();

	public int getY();

	public int getWidth();

	public int getHeight();

	public void setPosition(int x, int y);

	public void setSize(int width, int height);

	public void setWidth(int width);

	public void setHeight(int height);

	public void setSizeInternal(int w, int h);

	public void move(int dx, int dy);

	public void paint(Graphics g);

	public void apply();

	public Vector<Property> getProperties();

	public void setPropertyByAttName(String attName, String value);

	public Property getPropertyByAttName(String attName);

	public boolean propertyHasValueByAttName(String attName, Object value);

	public void addProperty(Property p);

	public void removeProperty(Property p);

	public void setPropertyChangeListener(PropertyChangeListener l);

	public String getTagName();

	public String getId();

	public Layout getParent();

	public void setParent(Layout w);

	public int getBaseline();

	public int getPadding(int which);

	public void setPadding(int pad);

	public void setPadding(int pad, int which);

	/**
	 * 该Widget是否可见.
	 * 
	 * @return true对应于Android中View的View.visible，false则对应于View.invisible或View.gone
	 */
	public boolean isVisible();
	
	/**
	 * 该Widget的getVisible()方法是否返回的是View.gone属性.
	 * 
	 * @return true表示该Widget是View.gone，否则可能是View.visible或View.invisible
	 */
	public boolean isGone();

	public int getMargin(int which);

	public Widget copy();
	
	/**
	 * 仅用于ui设计器中，设置该widget是否是被选中状态(或者说是
	 * 获得焦点).
	 * 
	 * @param selected true表示该widget被选中，否则没有选中
	 */
	public void setSelect(boolean selected);
	/**
	 * 仅用于ui设计器中，指明该widget是否是被选中状态(或者说是
	 * 获得焦点).
	 * 
	 * @return true表示该widget被选中，否则没有选中
	 */
	public boolean isSelected();
	
	/**
	 * 设置该widget对象是否是位于设计器的编辑面板中(而不是位于
	 * 工具面板上).
	 * 
	 * <p>位于工具面板上意味着它仅是作为工具使用，否则将即是位于真正的
	 * 设计器预览视图里.
	 * 
	 * <p>此标识的作用在于使得该 widget的paint方法可以
	 * 区分不同用途时它们的不同绘制实现，提高体验.
	 * 
	 * @param inDesignViewer true意味着该widget正位于设计器的编辑面板中
	 * (正常情况下都是处于这种情形之下)，否则意味着位于工具面板中
	 */
	public Widget setInDesignViewer(boolean inDesignViewer);
	
	/**
	 * 是否该widget对象是否是位于设计器的编辑面板中(而不是位于
	 * 工具面板上).
	 * 
	 * <p>此标识的作用在于使得该 widget的paint方法可以
	 * 区分不同用途时它们的不同绘制实现，提高体验.
	 * 
	 * @return true意味着该widget正位于设计器的编辑面板中
	 * (正常情况下都是处于这种情形之下)，否则意味着位于工具面板中
	 */
	public boolean isInDesignViewer();
	
}