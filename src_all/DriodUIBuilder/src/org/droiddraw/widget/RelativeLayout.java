/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * RelativeLayout.java at 2015-2-6 16:12:01, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.StringProperty;

public class RelativeLayout extends AbstractLayout
{

	public static final String TAG_NAME = "RelativeLayout";

	Hashtable<Widget, Vector<Relation>> relations;
	boolean repositioning = false;

	public static final int PADDING = 4;

	public static final String[] propNames = new String[] {
			"android:layout_toRightOf", "android:layout_toLeftOf",
			"android:layout_above", "android:layout_below",
			"android:layout_alignRight", "android:layout_alignLeft",
			"android:layout_alignTop", "android:layout_alignBottom",
			"android:layout_alignParentRight",
			"android:layout_alignParentLeft", "android:layout_alignParentTop",
			"android:layout_alignParentBottom",
			"android:layout_centerHorizontal", "android:layout_centerVertical",
			"android:layout_centerInParent", "android:layout_alignBaseline" };

	public static final RelationType[] rts = { RelationType.TO_RIGHT,
			RelationType.TO_LEFT, RelationType.ABOVE, RelationType.BELOW,
			RelationType.RIGHT, RelationType.LEFT, RelationType.TOP,
			RelationType.BOTTOM, RelationType.PARENT_RIGHT,
			RelationType.PARENT_LEFT, RelationType.PARENT_TOP,
			RelationType.PARENT_BOTTOM, RelationType.CENTER_HORIZONTAL,
			RelationType.CENTER_VERTICAL, RelationType.CENTER,
			RelationType.BASELINE };

	public RelativeLayout()
	{
		super(TAG_NAME);
		
		// 设置图标，方便ui设计时进行辨识
		this.setIconInTools(
				org.droiddraw.resource.IconFactory.getInstance()
					.getRelativeLayout_small_Icon().getImage());
				
		relations = new Hashtable<Widget, Vector<Relation>>();
	}

	protected boolean isRelatedTo(Widget w, Widget to)
	{
		Vector<Relation> rels = relations.get(w);
		if (rels == null)
		{
			return false;
		}
		if (w.equals(to))
		{
			return true;
		}
		else
		{
			boolean r1 = false;
			boolean r2 = false;
			if (rels.size() > 0)
				r1 = isRelatedTo(rels.get(0).getRelatedTo(), to);
			if (rels.size() > 1)
				r2 = isRelatedTo(rels.get(1).getRelatedTo(), to);
			return r1 || r2;
		}
	}

	protected static final String strip(String id)
	{
		int ix = id.indexOf("@");
		int ix2 = id.indexOf("+", ix);
		if (ix2 > ix)
		{
			return id.substring(ix2 + 1);
		}
		return id.substring(ix + 1);
	}

	public Widget findById(String id)
	{
		for (Widget w : widgets)
		{
			if (strip(w.getId()).equals(strip(id)))
			{
				return w;
			}
		}
		return null;
	}

	public void applyRelation(RelationType r, Widget w, Widget parent)
	{
		int x = w.getX();
		int y = w.getY();
		if (r == RelationType.ABOVE)
		{
			y = parent.getY() - PADDING - w.getHeight();
		}
		if (r == RelationType.BELOW)
		{
			y = parent.getY() + parent.getHeight() + PADDING;
		}
		if (r == RelationType.BOTTOM)
		{
			y = parent.getY() + parent.getHeight() - w.getHeight();
		}
		if (r == RelationType.TOP)
		{
			y = parent.getY();
		}
		if (r == RelationType.PARENT_TOP)
		{
			y = 0;
		}
		if (r == RelationType.PARENT_BOTTOM)
		{
			y = parent.getHeight() - w.getHeight();
		}
		if (r == RelationType.CENTER || r == RelationType.CENTER_VERTICAL)
		{
			y = parent.getHeight() / 2 - w.getHeight() / 2;
		}
		if (r == RelationType.BASELINE)
		{
			y = parent.getY() + parent.getBaseline() - w.getBaseline();
		}
		if (r == RelationType.LEFT)
		{
			x = parent.getX();
		}
		if (r == RelationType.RIGHT)
		{
			x = parent.getX() + parent.getWidth() - w.getWidth();
		}
		if (r == RelationType.TO_LEFT)
		{
			x = parent.getX() + w.getX() - w.getWidth();
		}
		if (r == RelationType.TO_RIGHT)
		{
			x = parent.getX() + parent.getWidth() + PADDING
					+ w.getMargin(Widget.LEFT);
		}
		if (r == RelationType.PARENT_LEFT)
		{
			x = 0;
		}
		if (r == RelationType.PARENT_RIGHT)
		{
			x = parent.getWidth() - w.getWidth();
		}
		if (r == RelationType.CENTER || r == RelationType.CENTER_HORIZONTAL)
		{
			x = parent.getWidth() / 2 - w.getWidth() / 2;
		}
		w.setPosition(x, y);
	}

	protected int closestVertical(Widget w, Widget wd, int[] dist)
	{
		int y = wd.getY();
		if (wd == w.getParent())
		{
			y = 0;
		}
		dist[0] = Math.abs(w.getY() - y);
		dist[1] = Math.abs(w.getY() + w.getHeight() - (y + wd.getHeight()));
		dist[2] = Math.abs(w.getY() + w.getHeight() - y);
		dist[3] = Math.abs(w.getY() - (y + wd.getHeight()));
		dist[4] = Math.abs(w.getY() + w.getBaseline() - (y + wd.getBaseline()));
		int min = dist[0];
		int mode = 0;
		for (int i = 1; i < dist.length; i++)
		{
			if (dist[i] < min)
			{
				min = dist[i];
				mode = i;
			}
			if (wd == w.getParent() && i == 1)
				break;
		}
		// Test for centering
		if (wd == w.getParent()
				&& Math.abs(w.getY() + (w.getHeight()) / 2
						- (wd.getHeight() / 2)) < min)
		{
			min = Math.abs((w.getY() + w.getHeight()) / 2
					- (wd.getHeight() / 2));
			mode = dist.length;
		}
		dist[0] = min;
		return mode;
	}

	protected int closestHorizontal(Widget w, Widget wd, int[] dist)
	{
		int x = wd.getX();
		if (wd == w.getParent())
		{
			x = 0;
		}
		dist[0] = Math.abs(w.getX() - x);
		dist[1] = Math.abs(w.getX() + w.getWidth() - (x + wd.getWidth()));
		dist[2] = Math.abs(w.getX() + w.getWidth() - x);
		dist[3] = Math.abs(w.getX() - (x + wd.getWidth()));
		dist[4] = Integer.MAX_VALUE;
		int min = dist[0];
		int mode = 0;
		for (int i = 1; i < dist.length; i++)
		{
			if (dist[i] < min)
			{
				min = dist[i];
				mode = i;
			}
			if (wd == w.getParent() && i == 1)
				break;
		}
		// Test for centering
		if (wd == w.getParent()
				&& Math.abs(w.getX() + w.getWidth() / 2 - (wd.getWidth() / 2)) < min)
		{
			min = Math.abs((w.getX() + w.getWidth()) / 2 - (wd.getWidth() / 2));
			mode = dist.length;
		}
		dist[0] = min;
		return mode;
	}

	@Override
	public void positionWidget(Widget w)
	{
		Vector<Relation> v = relations.get(w);
		if (v == null)
		{
			v = new Vector<Relation>();
			relations.put(w, v);
		}
		v.clear();
		boolean positioned = false;

		for (int i = 0; i < propNames.length; i++)
		{
			Property p = w.getPropertyByAttName(propNames[i]);
			if (p != null)
			{
				if (p instanceof StringProperty)
				{
					String id = ((StringProperty) p).getStringValue();
					Widget parent = findById(id);
					if (parent == null)
					{
						parent = this;
					}
					v.add(new Relation(w, parent, rts[i]));
					applyRelation(rts[i], w, parent);
					w.removeProperty(p);
					positioned = true;
				}
			}
		}
		if (positioned)
			return;

		/*
		 * if (widgets.size() == 1) { w.setPosition(0,0); return; }
		 */

		Widget closestTop = null;
		Widget closestLeft = null;
		int closeVert = Integer.MAX_VALUE;
		int closeHorz = Integer.MAX_VALUE;
		int modeVert = 0;
		int modeHorz = 0;

		int dist[] = new int[5];

		if (w.getParent() != null)
		{
			modeVert = closestVertical(w, w.getParent(), dist);
			if (modeVert < 2 || modeVert == dist.length)
			{
				closeVert = dist[0];
				closestTop = w.getParent();
			}
			else
			{
				modeVert = 0;
			}
			modeHorz = closestHorizontal(w, w.getParent(), dist);
			if (modeHorz < 2 || modeHorz == dist.length)
			{
				closeHorz = dist[0];
				closestLeft = w.getParent();
			}
			else
			{
				modeHorz = 0;
			}
		}

		for (Widget wd : widgets)
		{
			if (isRelatedTo(wd, w) || wd == w)
				continue;
			int mode = closestVertical(w, wd, dist);
			if (dist[0] < closeVert)
			{
				closeVert = dist[0];
				closestTop = wd;
				modeVert = mode;
			}
			mode = closestHorizontal(w, wd, dist);
			if (dist[0] < closeHorz)
			{
				closeHorz = dist[0];
				closestLeft = wd;
				modeHorz = mode;
			}
		}
		if (closestTop == null || closestLeft == null)
		{
			w.setPosition(0, 0);
			return;
		}

		int x, y;
		x = w.getX();
		y = w.getY();
		if (closestTop == w.getParent())
		{
			switch (modeVert)
			{
				case 0:
					y = 0;
					v.add(new Relation(w, closestTop, RelationType.PARENT_TOP));
					break;
				case 1:
					y = closestTop.getHeight() - w.getHeight();
					v.add(new Relation(w, closestTop,
							RelationType.PARENT_BOTTOM));
					break;
				case 5:
					y = closestTop.getHeight() / 2 - w.getHeight() / 2;
					v.add(new Relation(w, closestTop,
							RelationType.CENTER_VERTICAL));
					break;
			}
		}
		else
		{
			switch (modeVert)
			{
				case 0:
					y = closestTop.getY();
					v.add(new Relation(w, closestTop, RelationType.TOP));
					break;
				case 1:
					y = closestTop.getY() + closestTop.getHeight()
							- w.getHeight();
					v.add(new Relation(w, closestTop, RelationType.BOTTOM));
					break;
				case 2:
					y = closestTop.getY() - w.getHeight() - PADDING;
					v.add(new Relation(w, closestTop, RelationType.ABOVE));
					break;
				case 3:
					y = closestTop.getY() + closestTop.getHeight() + PADDING;
					v.add(new Relation(w, closestTop, RelationType.BELOW));
					break;
				case 4:
					y = closestTop.getY() + closestTop.getBaseline()
							- w.getBaseline();
					v.add(new Relation(w, closestTop, RelationType.BASELINE));
					break;
			}
		}
		if (closestLeft == w.getParent())
		{
			switch (modeHorz)
			{
				case 0:
					x = 0;
					v
							.add(new Relation(w, closestLeft,
									RelationType.PARENT_LEFT));
					break;
				case 1:
					x = closestLeft.getWidth() - w.getWidth();
					v.add(new Relation(w, closestLeft,
							RelationType.PARENT_RIGHT));
					break;
				case 5:
					x = closestLeft.getWidth() / 2 - w.getWidth() / 2;
					v.add(new Relation(w, closestLeft,
							RelationType.CENTER_HORIZONTAL));
					break;
			}
		}
		else
		{
			switch (modeHorz)
			{
				case 0:
					x = closestLeft.getX();
					v.add(new Relation(w, closestLeft, RelationType.LEFT));
					break;
				case 1:
					x = closestLeft.getX() + closestLeft.getWidth()
							- w.getWidth();
					v.add(new Relation(w, closestLeft, RelationType.RIGHT));
					break;
				case 2:
					x = closestLeft.getX() - w.getWidth() - PADDING;
					v.add(new Relation(w, closestLeft, RelationType.TO_LEFT));
					break;
				case 3:
					x = closestLeft.getX() + closestLeft.getWidth() + PADDING;
					v.add(new Relation(w, closestLeft, RelationType.TO_RIGHT));
					break;
			}
		}
		w.setPosition(x, y);
		Collections.sort(widgets, new Comparator<Widget>()
		{
			public int compare(Widget w1, Widget w2)
			{
				if (isRelatedTo(w1, w2))
				{
					return -1;
				}
				else if (isRelatedTo(w2, w1))
				{
					return 1;
				}
				else
					return 0;
			}
		});
	}

	public Vector<Widget> getParents(Widget w)
	{
		Vector<Relation> rels = relations.get(w);
		Vector<Widget> parents = new Vector<Widget>();
		if (rels != null && rels.size() > 0)
		{
			for (Relation r : rels)
			{
				parents.add(r.getRelatedTo());
			}
		}
		return parents;
	}

	public Set<Widget> getRoots(Widget w)
	{
		Vector<Widget> parents = getParents(w);
		Set<Widget> roots = new HashSet<Widget>();
		for (Widget parent : parents)
		{
			Set<Widget> parentRoots = getRoots(parent);
			for (Widget root : parentRoots)
			{
				roots.add(root);
			}
		}
		return roots;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void repositionAllWidgets()
	{
		if (!repositioning)
		{
			repositioning = true;
			Vector<Widget> ws = (Vector<Widget>) widgets.clone();
			widgets.clear();
			for (Widget w : ws)
			{
				addWidget(w);
			}
			repositioning = false;
		}
	}

	public void addEditableProperties(Widget w)
	{
	}

	public void removeEditableProperties(Widget w)
	{
	}

	public void addOutputProperties(Widget w, Vector<Property> properties)
	{
		Vector<Relation> rels = relations.get(w);
		if (rels != null)
		{
			for (Relation r : rels)
			{
				if (r.getRelation().equals(RelationType.LEFT))
				{
					properties.add(new StringProperty("relation",
							"android:layout_alignLeft", r.getRelatedTo()
									.getId(), false));
				}
				else if (r.getRelation().equals(RelationType.TO_LEFT))
				{
					properties.add(new StringProperty("relation",
							"android:layout_toLeftOf",
							r.getRelatedTo().getId(), false));
				}
				else if (r.getRelation().equals(RelationType.RIGHT))
				{
					properties.add(new StringProperty("relation",
							"android:layout_alignRight", r.getRelatedTo()
									.getId(), false));
				}
				else if (r.getRelation().equals(RelationType.TO_RIGHT))
				{
					properties.add(new StringProperty("relation",
							"android:layout_toRightOf", r.getRelatedTo()
									.getId(), false));
				}
				else if (r.getRelation().equals(RelationType.ABOVE))
				{
					properties.add(new StringProperty("relation",
							"android:layout_above", r.getRelatedTo().getId(),
							false));
				}
				else if (r.getRelation().equals(RelationType.BELOW))
				{
					properties.add(new StringProperty("relation",
							"android:layout_below", r.getRelatedTo().getId(),
							false));
				}
				else if (r.getRelation().equals(RelationType.TOP))
				{
					properties.add(new StringProperty("relation",
							"android:layout_alignTop",
							r.getRelatedTo().getId(), false));
				}
				else if (r.getRelation().equals(RelationType.BOTTOM))
				{
					properties.add(new StringProperty("relation",
							"android:layout_alignBottom", r.getRelatedTo()
									.getId(), false));
				}
				else if (r.getRelation().equals(RelationType.CENTER_VERTICAL))
				{
					properties.add(new BooleanProperty("relation",
							"android:layout_centerVertical", true, false));
				}
				else if (r.getRelation().equals(RelationType.CENTER_HORIZONTAL))
				{
					properties.add(new BooleanProperty("relation",
							"android:layout_centerHorizontal", true, false));
				}
				else if (r.getRelation().equals(RelationType.PARENT_BOTTOM))
				{
					properties.add(new BooleanProperty("relation",
							"android:layout_alignParentBottom", true, false));
				}
				else if (r.getRelation().equals(RelationType.PARENT_TOP))
				{
					properties.add(new BooleanProperty("relation",
							"android:layout_alignParentTop", true, false));
				}
				else if (r.getRelation().equals(RelationType.PARENT_LEFT))
				{
					properties.add(new BooleanProperty("relation",
							"android:layout_alignParentLeft", true, false));
				}
				else if (r.getRelation().equals(RelationType.PARENT_RIGHT))
				{
					properties.add(new BooleanProperty("relation",
							"android:layout_alignParentRight", true, false));
				}
				else if (r.getRelation().equals(RelationType.BASELINE))
				{
					properties.add(new StringProperty("relation",
							"android:layout_alignBaseline", r.getRelatedTo()
									.getId(), false));
				}
			}
		}
	}

	public static enum RelationType
	{
		TOP, ABOVE, BOTTOM, BELOW, LEFT, TO_LEFT, RIGHT, TO_RIGHT, BASELINE, CENTER_VERTICAL, CENTER_HORIZONTAL, CENTER, PARENT_TOP, PARENT_BOTTOM, PARENT_RIGHT, PARENT_LEFT
	}

	public static class Relation
	{
		RelationType relation;
		Widget parent;
		Widget widget;

		public Relation(Widget widget, Widget parent, RelationType relation)
		{
			this.widget = widget;
			this.parent = parent;
			this.relation = relation;
		}

		public RelationType getRelation()
		{
			return relation;
		}

		public Widget getRelatedTo()
		{
			return parent;
		}

		public Widget getWidget()
		{
			return widget;
		}
	}
}
