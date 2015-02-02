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
