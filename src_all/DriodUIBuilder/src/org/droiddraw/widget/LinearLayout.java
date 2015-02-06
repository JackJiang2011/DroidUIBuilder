/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * LinearLayout.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.Vector;

import org.droiddraw.property.FloatProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;

public class LinearLayout extends AbstractLayout
{
	public static final String TAG_NAME = "LinearLayout";

	SelectProperty orientation;
	SelectProperty gravity;
	FloatProperty weightSum;

	boolean vertical;
	int max_base;

	public static int VERTICAL_PADDING = 2;
	public static int HORIZONTAL_PADDING = 2;

	int extra;
	private static final String[] OPTIONS = new String[] { "left", "right",
			"center_horizontal", "top", "bottom", "center_vertical" };

	public LinearLayout()
	{
		this(true);
	}

	public LinearLayout(boolean vertical)
	{
		super(TAG_NAME);
		
		// 设置图标，方便ui设计时进行辨识
		this.setIconInTools(
				org.droiddraw.resource.IconFactory.getInstance()
					.getLinearLayout_small_Icon().getImage());
				
		this.orientation = new SelectProperty("Orientation",
				"android:orientation",
				new String[] { "horizontal", "vertical" }, 0);
		this.orientation.setSelectedIndex(1);
		this.gravity = new SelectProperty("Gravity", "android:gravity",
				new String[] { "top", "bottom", "left", "right", "center" }, 0);
		this.weightSum = new FloatProperty("Weight Sum", "android:weightSum",
				-1.0f);
		addProperty(orientation);
		addProperty(gravity);
		addProperty(weightSum);

		extra = 0;
	}

	@Override
	public void apply()
	{
		super.apply();
		if (orientation != null)
			vertical = "vertical".equals(orientation.getStringValue());
	}

	@Override
	public void addWidget(Widget w)
	{
		if (!vertical)
		{
			if (!(w instanceof Layout) && w.getBaseline() > max_base)
			{
				max_base = w.getBaseline();
			}
		}
		super.addWidget(w);
	}

	@Override
	public void positionWidget(Widget w)
	{
		widgets.remove(w);
		if (vertical)
		{
			int y = w.getY();
			if (y >= -100)
			{
				int ix = 0;
				while (ix < widgets.size() && y > widgets.get(ix).getY())
					ix++;
				widgets.add(ix, w);
			}
			else
			{
				widgets.add(w);
			}
		}
		else
		{
			int x = w.getX();
			if (x >= 0)
			{
				int ix = 0;
				while (ix < widgets.size() && x > widgets.get(ix).getX())
					ix++;
				widgets.add(ix, w);
			}
			else
			{
				widgets.add(w);
			}
		}
		repositionAllWidgets();
	}

	@Override
	public void repositionAllWidgets()
	{
		for (Widget w : widgets)
		{
			w.apply();
		}
		repositionAllWidgetsInternal();
	}

	protected void repositionAllWidgetsInternal()
	{
		int y;
		int x;
		Vector<Widget> with_weight = new Vector<Widget>();
		int max_base = 0;
		int max_base_bottom = 0;
		if (gravity == null)
			return;
		String lGrav = gravity.getStringValue();

		y = 0;
		x = 0;
		for (Widget w : widgets)
		{
			if (!vertical)
			{
				if (!(w instanceof Layout))
				{
					if (!w.propertyHasValueByAttName("android:layout_gravity",
							"bottom")
							|| (!gravity.isDefault() && lGrav.equals("bottom")))
					{
						if (w.getBaseline() > max_base)
						{
							max_base = w.getBaseline();
						}
					}
					else
					{
						if (w.getHeight() - w.getBaseline() > max_base_bottom)
						{
							max_base_bottom = w.getHeight() - w.getBaseline();
						}
					}
				}
			}

			StringProperty prop = (StringProperty) w
					.getPropertyByAttName("android:layout_weight");
			if (prop != null)
			{
				with_weight.add(w);
			}
			if (vertical)
				y += w.getPadding(TOP) + w.getHeight() + w.getPadding(BOTTOM)
						+ w.getMargin(TOP) + w.getMargin(BOTTOM);
			else
				x += w.getPadding(LEFT) + w.getWidth() + w.getPadding(RIGHT)
						+ w.getMargin(LEFT) + w.getMargin(RIGHT);
		}
		extra = 0;
		if (vertical)
		{
			extra = getHeight() - y;
		}
		else
		{
			extra = getWidth() - x;
		}
		y = 0;
		x = 0;
		for (Widget w : widgets)
		{
			String gravity = "left";
			StringProperty prop = (StringProperty) w
					.getPropertyByAttName("android:layout_gravity");
			int share = calculateShare(w);
			if (prop != null)
				gravity = prop.getStringValue();
			if (vertical)
			{
				int width_w_pad = (w.getPadding(Widget.LEFT) + w.getWidth() + w
						.getPadding(Widget.RIGHT));
				if (gravity.contains("right"))
					x = getWidth() - width_w_pad;
				else if ("center_horizontal".equals(gravity)
						|| "center".equals(gravity))
					x = getWidth() / 2 - width_w_pad / 2;
				else
					x = w.getPadding(Widget.LEFT) + w.getMargin(Widget.LEFT);
			}
			else
			{
				if (gravity.contains("bottom"))
					y = getHeight() - max_base_bottom - w.getBaseline();
				else if ("center_vertical".equals(gravity)
						|| "center".equals(gravity))
					y = getHeight() / 2 - w.getBaseline();
				else
				{
					if (w instanceof Layout)
					{
						y = w.getPadding(Widget.TOP) + w.getMargin(Widget.TOP);
					}
					else
					{
						y = max_base - w.getBaseline() + w.getPadding(TOP);
					}
				}
			}
			if (vertical)
			{
				y += w.getPadding(Widget.TOP) + w.getMargin(Widget.TOP);
			}
			else
			{
				x += w.getPadding(Widget.LEFT) + w.getMargin(Widget.LEFT);
			}
			w.setPosition(x, y);
			if (with_weight.size() == 0)
			{
				if (vertical)
				{
					if (lGrav.equals("bottom"))
					{
						w.setPosition(x, y + extra);
					}
				}
				else if (lGrav.equals("right"))
				{
					w.setPosition(x + extra, y);
				}
			}
			if (vertical)
			{
				y += w.getHeight() + w.getPadding(Widget.BOTTOM)
						+ w.getMargin(Widget.BOTTOM);
				if (with_weight.contains(w))
				{
					y += share;
				}
			}
			else
			{
				x += w.getWidth() + w.getPadding(Widget.RIGHT)
						+ w.getMargin(Widget.RIGHT);
				if (with_weight.contains(w))
				{
					x += share;
				}
			}
		}
	}

	@Override
	public void resizeForRendering()
	{
		boolean vertical = "vertical".equals(orientation.getStringValue());
		for (Widget w : widgets)
		{
			if (w instanceof Layout)
			{
				((Layout) w).resizeForRendering();
			}
			StringProperty prop = (StringProperty) w
					.getPropertyByAttName("android:layout_weight");
			if (prop != null)
			{
				int share = calculateShare(prop);
				if (vertical)
					w.setSizeInternal(w.getWidth(), w.getHeight() + share);
				else
					w.setSizeInternal(w.getWidth() + share, w.getHeight());
			}
		}
	}

	private int calculateShare(StringProperty prop)
	{
		String weightString = null;
		if (prop != null)
		{
			weightString = prop.getStringValue();
		}
		if (weightString == null)
		{
			System.err.println("Property is ill-defined!");
			return 0;
		}
		try
		{
			float share = Float.parseFloat(weightString);
			Float value = (Float) weightSum.getValue();
			if (value == null)
			{
				System.err.println("No weight!");
				return 0;
			}
			float total = value.floatValue();
			if (total == -1)
			{
				total = sumWeights();
			}
			float percent = share / total;
			return (int) (percent * extra);
		}
		catch (NumberFormatException ex)
		{
			ex.printStackTrace();
		}
		return 0;
	}

	private float sumWeights()
	{
		float sum = 0;
		for (Widget w : widgets)
		{
			StringProperty prop = (StringProperty) w
					.getPropertyByAttName("android:layout_weight");
			if (prop == null)
			{
				continue;
			}
			try
			{
				sum += Float.parseFloat(prop.getStringValue());
			}
			catch (NumberFormatException ex)
			{
				ex.printStackTrace();
			}
		}
		return sum;
	}

	private int calculateShare(Widget w)
	{
		return calculateShare((StringProperty) w
				.getPropertyByAttName("android:layout_weight"));
	}

	public void addOutputProperties(Widget w, Vector<Property> properties)
	{
	}

	public void addEditableProperties(Widget w)
	{
		w.addProperty(new StringProperty("Linear Layout Weight",
				"android:layout_weight", "0"));
		w.addProperty(new SelectProperty("Layout Gravity",
				"android:layout_gravity", OPTIONS, 0));
	}

	public void removeEditableProperties(Widget w)
	{
		w.removeProperty(new StringProperty("", "android:layout_weight", ""));
		w.removeProperty(new SelectProperty("", "android:layout_gravity",
				new String[] { "" }, 0));
	}
}
