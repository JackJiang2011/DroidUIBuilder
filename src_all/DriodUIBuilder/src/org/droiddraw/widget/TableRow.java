/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * TableRow.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.Vector;

import org.droiddraw.property.StringProperty;

public class TableRow extends LinearLayout
{

	public static final String TAG_NAME = "TableRow";

	Vector<Integer> widths;

	public TableRow()
	{
		this.setTagName(TAG_NAME);
		
		// 设置图标，方便ui设计时进行辨识
		this.setIconInTools(
				org.droiddraw.resource.IconFactory.getInstance()
					.getTableRow_small_Icon().getImage());
		
		this.orientation.setStringValue("horizontal");
		this.orientation.setEditable(false);
		this.widthProp.setStringValue("fill_parent");
		this.widthProp.setEditable(false);

		apply();
	}

	@Override
	public void positionWidget(Widget w)
	{
		super.positionWidget(w);
		if (parent instanceof TableLayout)
			parent.positionWidget(this);
	}

	@Override
	protected void repositionAllWidgetsInternal()
	{
		int y = 0;
		int x = 0;
		Vector<Widget> with_weight = new Vector<Widget>();
		int max_base = 0;

		for (Widget w : widgets)
		{
			if (!vertical)
			{
				if (!(w instanceof Layout) && w.getBaseline() > max_base)
				{
					max_base = w.getBaseline();
				}
			}

			StringProperty prop = (StringProperty) w
					.getPropertyByAttName("android:layout_weight");
			if (prop != null)
				with_weight.add(w);
			if (vertical)
				y += w.getPadding(TOP) + w.getHeight() + w.getPadding(BOTTOM);
			else
				x += w.getPadding(LEFT) + w.getWidth() + w.getPadding(RIGHT);
		}
		extra = 0;
		if (with_weight.size() > 0)
		{
			if (vertical)
			{
				extra = getHeight() - y;
			}
			else
			{
				extra = getWidth() - x;
			}
		}
		y = 0;
		x = 0;
		for (Widget w : widgets)
		{
			StringProperty prop = (StringProperty) w
					.getPropertyByAttName("android:layout_column");
			if (prop != null && widths != null)
			{
				int col = (Integer.parseInt(prop.getStringValue()));
				int ix = widgets.indexOf(w);
				while (ix < col)
				{
					x += widths.get(ix++);
				}
			}
			String gravity = "left";
			prop = (StringProperty) w
					.getPropertyByAttName("android:layout_gravity");
			if (prop != null)
				gravity = prop.getStringValue();

			if (vertical)
			{
				int width_w_pad = (w.getPadding(Widget.LEFT) + w.getWidth() + w
						.getPadding(Widget.RIGHT));
				if ("right".equals(gravity))
					x = getWidth() - width_w_pad;
				else if ("center_horizontal".equals(gravity)
						|| "center".equals(gravity))
					x = getWidth() / 2 - width_w_pad / 2;
				else
					x = w.getPadding(Widget.LEFT);
			}
			else
			{
				int height_w_pad = (w.getPadding(Widget.TOP) + w.getHeight() + w
						.getPadding(Widget.BOTTOM));
				if ("bottom".equals(gravity))
					y = getHeight() - height_w_pad;
				else if ("center_vertical".equals(gravity)
						|| "center".equals(gravity))
					y = (getHeight() - height_w_pad) / 2;
				else
				{
					if (w instanceof Layout)
					{
						y = w.getPadding(Widget.TOP);
					}
					else
					{
						y = max_base - w.getBaseline() + w.getPadding(TOP);
					}
				}
			}
			if (vertical)
			{
				y += w.getPadding(Widget.TOP);
			}
			else
			{
				x += w.getPadding(Widget.LEFT);
			}

			w.setPosition(x, y);
			if (vertical)
			{
				y += w.getHeight() + w.getPadding(Widget.BOTTOM);
			}
			else
			{
				x += w.getWidth() + w.getPadding(Widget.RIGHT);
			}
		}
	}

	public void setWidths(Vector<Integer> widths)
	{
		int ix = 0;
		for (Widget w : widgets)
		{
			StringProperty prop = (StringProperty) w
					.getPropertyByAttName("android:layout_column");
			if (prop != null)
			{
				ix = (Integer.parseInt(prop.getStringValue()));
			}
			prop = (StringProperty) w
					.getPropertyByAttName("android:layout_span");
			int wd = widths.get(ix++);
			if (prop != null)
			{
				int span = (Integer.parseInt(prop.getStringValue()));
				for (int i = 1; i < span; i++)
				{
					wd += widths.get(ix++);
				}
			}
			w.setSizeInternal(wd - w.getPadding(LEFT) - w.getPadding(RIGHT), w
					.getHeight());
		}
		this.widths = widths;
		repositionAllWidgetsInternal();
	}

	@Override
	public int getContentWidth()
	{
		if (widths != null && widths.size() > 0)
		{
			int res = 0;
			for (int w : widths)
				res += w;
			return res;
		}
		return super.getContentWidth();
	}

	@Override
	public void addEditableProperties(Widget w)
	{
		w.addProperty(new StringProperty("Column Span", "android:layout_span",
				"1"));
		super.addEditableProperties(w);
	}

	@Override
	public void removeEditableProperties(Widget w)
	{
		w.removeProperty(new StringProperty("Column Span",
				"android:layout_span", "1"));
		super.removeEditableProperties(w);
	}

}
