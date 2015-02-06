/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Layout.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.Vector;

import org.droiddraw.property.Property;

public interface Layout extends Widget
{
	public void addWidget(Widget w);

	public Vector<Widget> getWidgets();

	public void removeWidget(Widget w);

	public void positionWidget(Widget w);

	public void repositionAllWidgets();

	public void addOutputProperties(Widget w, Vector<Property> properties);

	public void addEditableProperties(Widget w);

	public void removeEditableProperties(Widget w);

	public void removeAllWidgets();

	public boolean containsWidget(Widget w);

	public int getScreenX();

	public int getScreenY();

	public void resizeForRendering();

	public void clearRendering();
}
