package org.droiddraw.widget;

import java.util.Vector;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.Property;
import org.droiddraw.property.StringProperty;

public class AbsoluteLayout extends AbstractLayout
{
	public static final String TAG_NAME = "AbsoluteLayout";

	public AbsoluteLayout()
	{
		super(TAG_NAME);
		
		// 设置图标，方便ui设计时进行辨识
		this.setIconInTools(
				org.droiddraw.resource.IconFactory.getInstance().getAbsoluteLayout_small_Icon().getImage());
	}

	@Override
	public void positionWidget(Widget w)
	{
		apply();
	}

	@Override
	public void repositionAllWidgets()
	{
		apply();
	}

	public void addOutputProperties(Widget w, Vector<Property> properties)
	{
		String unit = AndroidEditor.instance().getScreenUnit();
		addOrUpdateProperty(properties, new StringProperty("X Position",
				"android:layout_x", w.getX() + unit, false));
		addOrUpdateProperty(properties, new StringProperty("Y Position",
				"android:layout_y", w.getY() + unit, false));
	}

	public void addEditableProperties(Widget w)
	{
	}

	public void removeEditableProperties(Widget w)
	{
	}

	@Override
	public void apply()
	{
		super.apply();
	}
}
