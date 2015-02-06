/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ViewerListener.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.droiddraw.AndroidEditor;
import org.droiddraw.MainPane;
import org.droiddraw.property.StringProperty;
import org.droiddraw.widget.AbsoluteLayout;
import org.droiddraw.widget.AnalogClock;
import org.droiddraw.widget.AutoCompleteTextView;
import org.droiddraw.widget.Button;
import org.droiddraw.widget.CheckBox;
import org.droiddraw.widget.DatePicker;
import org.droiddraw.widget.DigitalClock;
import org.droiddraw.widget.EditView;
import org.droiddraw.widget.FrameLayout;
import org.droiddraw.widget.Gallery;
import org.droiddraw.widget.GridView;
import org.droiddraw.widget.ImageButton;
import org.droiddraw.widget.ImageSwitcher;
import org.droiddraw.widget.ImageView;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.LinearLayout;
import org.droiddraw.widget.ListView;
import org.droiddraw.widget.MapView;
import org.droiddraw.widget.ProgressBar;
import org.droiddraw.widget.RadioButton;
import org.droiddraw.widget.RadioGroup;
import org.droiddraw.widget.RatingBar;
import org.droiddraw.widget.RelativeLayout;
import org.droiddraw.widget.ScrollView;
import org.droiddraw.widget.Spinner;
import org.droiddraw.widget.TabHost;
import org.droiddraw.widget.TabWidget;
import org.droiddraw.widget.TableLayout;
import org.droiddraw.widget.TableRow;
import org.droiddraw.widget.TextView;
import org.droiddraw.widget.Ticker;
import org.droiddraw.widget.TimePicker;
import org.droiddraw.widget.ToggleButton;
import org.droiddraw.widget.Widget;

public class ViewerListener implements MouseListener, MouseMotionListener,
		KeyListener
{
	int off_x, off_y;
	int sx, sy;
	int grid_x = 10;
	int grid_y = 10;
	boolean shift;

	int mode;

	private static final int NORMAL = 0;
	private static final int E = 1;
	private static final int SE = 2;
	private static final int S = 3;

	AndroidEditorViewer viewer;
	AndroidEditor app;
	JComboBox widgetType = new JComboBox(new String[] { AnalogClock.TAG_NAME,
			AutoCompleteTextView.TAG_NAME, Button.TAG_NAME, CheckBox.TAG_NAME,
			DigitalClock.TAG_NAME, EditView.TAG_NAME, ImageButton.TAG_NAME,
			ImageView.TAG_NAME, ListView.TAG_NAME, ProgressBar.TAG_NAME,
			RadioButton.TAG_NAME, RadioGroup.TAG_NAME, Spinner.TAG_NAME,
			TableRow.TAG_NAME, TextView.TAG_NAME, TimePicker.TAG_NAME,
			AbsoluteLayout.TAG_NAME, LinearLayout.TAG_NAME,
			RelativeLayout.TAG_NAME, TableLayout.TAG_NAME, Ticker.TAG_NAME,
			TabHost.TAG_NAME, TabWidget.TAG_NAME });

	MainPane ddp;
	private boolean dragging;

	public ViewerListener(AndroidEditor app, MainPane ddp, AndroidEditorViewer viewer)
	{
		this.ddp = ddp;
		this.app = app;
		this.viewer = viewer;

		if (!System.getProperty("os.name").toLowerCase().contains("mac os x"))
			widgetType.setLightWeightPopupEnabled(false);
	}

	public Component getWidgetSelector()
	{
		return widgetType;
	}

	public Widget createWidget()
	{
		String str = (String) widgetType.getSelectedItem();
		return createWidget(str);
	}

	public static Widget createWidget(String str)
	{
		// TODO(brendan) : Clean this up using reflection.
		if (str.equals(ToggleButton.TAG_NAME))
			return new ToggleButton("Toggle On", "Toggle Off");
		else if (str.equals(Button.TAG_NAME))
			return new Button(Button.TAG_NAME);
		else if (str.equals(CheckBox.TAG_NAME))
			return new CheckBox(CheckBox.TAG_NAME);
		else if (str.equals(EditView.TAG_NAME))
			return new EditView(EditView.TAG_NAME);
		else if (str.equals(TextView.TAG_NAME))
			return new TextView(TextView.TAG_NAME);
		else if (str.equals(AnalogClock.TAG_NAME))
			return new AnalogClock();
		else if (str.equals(DigitalClock.TAG_NAME))
			return new DigitalClock();
		else if (str.equals(ProgressBar.TAG_NAME))
			return new ProgressBar();
		else if (str.equals(LinearLayout.TAG_NAME))
			return new LinearLayout();
		else if (str.equals(AbsoluteLayout.TAG_NAME))
			return new AbsoluteLayout();
		else if (str.equals(RelativeLayout.TAG_NAME))
			return new RelativeLayout();
		else if (str.equals(RadioButton.TAG_NAME))
			return new RadioButton(RadioButton.TAG_NAME);
		else if (str.equals(RadioGroup.TAG_NAME))
			return new RadioGroup();
		else if (str.equals(TimePicker.TAG_NAME))
			return new TimePicker();
		else if (str.equals(ListView.TAG_NAME))
			return new ListView();
		else if (str.equals(Ticker.TAG_NAME))
			return new Ticker();
		else if (str.equals(Spinner.TAG_NAME))
			return new Spinner();
		else if (str.equals(ImageView.TAG_NAME))
			return new ImageView();
		else if (str.equals(ImageButton.TAG_NAME))
			return new ImageButton();
		else if (str.equals(AutoCompleteTextView.TAG_NAME))
			return new AutoCompleteTextView("AutoComplete");
		else if (str.equals(TableRow.TAG_NAME))
			return new TableRow();
		else if (str.equals(TableLayout.TAG_NAME))
			return new TableLayout();
		else if (str.equals(FrameLayout.TAG_NAME))
			return new FrameLayout();
		else if (str.equals(ScrollView.TAG_NAME))
			return new ScrollView();
		else if (str.equals(GridView.TAG_NAME))
			return new GridView();
		else if (str.equals(Gallery.TAG_NAME))
			return new Gallery();
		else if (str.equals(DatePicker.TAG_NAME))
			return new DatePicker();
		else if (str.equals(ImageSwitcher.TAG_NAME))
			return new ImageSwitcher();
		else if (str.equals(TabHost.TAG_NAME))
			return new TabHost();
		else if (str.equals(TabWidget.TAG_NAME))
			return new TabWidget();
		else if (str.equals(MapView.TAG_NAME))
			return new MapView();
		else if (str.equals(RatingBar.TAG_NAME))
		{
			return new RatingBar();
		}
		else
			return null;
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent e)
	{
		final int x = e.getX() - viewer.getOffX();
		final int y = e.getY() - viewer.getOffY();

		final MouseEvent ev = e;

		if (app.canSelect())
		{
			final Vector<Widget> ws = app.findWidgets(x, y);
			Widget w = null;
			if (ws.contains(app.getSelected()))
			{
				w = app.getSelected();
			}
			else
			{
				switch (ws.size())
				{
					case 0:
						break;
					case 1:
						w = ws.get(0);
						break;
					default:
						if (e.isControlDown()
								|| e.getButton() == MouseEvent.BUTTON3)
						{
							JPopupMenu menu = new JPopupMenu();
							JMenuItem it = new JMenuItem("Select a widget:");
							it.setEnabled(false);
							menu.add(it);
							menu.addSeparator();

							for (int i = 0; i < ws.size(); i++)
							{
								it = new JMenuItem(ws.get(i).getTagName());
								final int id = i;
								it.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent arg0)
									{
										doSelect(ws.get(id),
												ev.getClickCount(), x, y);
									}
								});
								menu.add(it);
							}
							menu.show(viewer, x, y);
						}
						else
						{
							w = ws.get(0);
						}
				}
			}
			doSelect(w, e.getClickCount(), x, y);
		}
		else
		{
			// Widget w = createWidget();
			// addWidget(w, x, y);
		}
	}

	protected void doSelect(Widget w, int clickCount, int x, int y)
	{
		if (clickCount > 1)
		{
			if (w != null)
			{
				if (w != app.getSelected())
					app.select(w);
				ddp.getPaneMakeLayout().editSelected();
			}
		}
		else if (mode == NORMAL)
		{
			if (w != null)
			{
				off_x = (w.getParent() != null ? w.getParent().getScreenX() : 0)
						+ w.getX() - x;
				off_y = (w.getParent() != null ? w.getParent().getScreenY() : 0)
						+ w.getY() - y;
			}
			app.select(w);
			viewer.requestFocus();
			viewer.repaint();
		}
	}

	public void addWidget(Widget ww, int xx, int yy)
	{
		final int x = xx;
		final int y = yy;
		final Widget w = ww;
		final Vector<Layout> ls = app.findLayouts(x, y);

		Layout l = null;

		switch (ls.size())
		{
			case 0:
				return;
			case 1:
				l = ls.get(0);
				break;
			default:
				JPopupMenu menu = new JPopupMenu();
				JMenuItem it = new JMenuItem("Select a layout:");
				it.setEnabled(false);
				menu.add(it);
				menu.addSeparator();

				for (int i = 0; i < ls.size(); i++)
				{
					it = new JMenuItem(ls.get(i).getTagName());
					final int id = i;
					it.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent arg0)
						{

							/*
							 * If the widget to be added is a TabWidget, then
							 * validate before adding it.
							 */
							if (!isTabWidgetValid(ls.get(id), w))
							{
								return;
							}

							/*
							 * Perform this check ONLY when using a FrameLayout
							 * in conjunction with a TabWidget, in order to
							 * customize the id that is displayed in the final
							 * xml file.
							 */
							if (FrameLayout.TAG_NAME.equals(w.getTagName())
									&& LinearLayout.TAG_NAME.equals(ls.get(id)
											.getTagName())
									&& isFrameLayoutForTab(ls.get(id), w))
							{
								((StringProperty) w
										.getProperties()
										.get(
												w
														.getProperties()
														.indexOf(
																w
																		.getPropertyByAttName("android:id"))))
										.setStringValue("@android:id/tabcontent");// getgetPropertyByAttName("Id")).setValue("@android:id/tabcontent");//setValue("@android:id/tabcontent");
																					// //getProperties().get(w.getProperties().indexOf(w.getId()))).setStringValue("@android:id/tabcontent");
							}

							addWidget(w, ls.get(id), x, y);
						}
					});
					menu.add(it);
				}
				menu.show(viewer, x, y);
		}
		if (l != null)
		{
			if (!isTabWidgetValid(l, w))
			{
				return;
			}
			addWidget(w, l, x, y);
		}
	}

	/**
	 * This method will check to see if the LinearLayout widget belongs to the
	 * root layout.
	 * 
	 * @param layout
	 * @param widget
	 * @return
	 */
	private boolean isFrameLayoutForTab(Layout layout, Widget widget)
	{

		boolean found = false;

		if (!TabHost.TAG_NAME.equals((app.getRootLayout().getTagName())))
		{
			MainPane.getTipCom().warn(
					"Please ensure that the root layout is a TabHost layout.");
			return false;
		}

		Vector<Widget> widgets = app.getRootLayout().getWidgets();

		for (Widget w : widgets)
		{
			if (LinearLayout.TAG_NAME.equals(w.getTagName()))
			{
				found = true;
				break;
			}
		}

		return found;
	}

	/**
	 * This method will check if the use of a TabWidget is valid in terms of
	 * what widgets should exist before the TabWidget is to be used.
	 * 
	 * @param layout
	 *            The layout that will contain the TabWidget directly.
	 * @param widget
	 *            The widget to be check, must be a TabWidget.
	 * @return
	 */
	private boolean isTabWidgetValid(Layout layout, Widget widget)
	{

		/*
		 * If the widget to be dropped into the Viewer is a TabWidget, then
		 * check if the root layout is a TabHost layout.
		 */
		if (!TabHost.TAG_NAME.equals(app.getRootLayout().getTagName())
				&& TabWidget.TAG_NAME.equals(widget.getTagName()))
		{
			MainPane.getTipCom().warn(
					"Please select TabHost as the root layout.");
			return false;
		}

		/*
		 * If a TabHost Layout exists, then check if the TabHost contains a
		 * LinearLayout, which will directly contain the TabWidget.
		 */
		if (!(LinearLayout.TAG_NAME.equals(layout.getTagName()))
				&& (TabWidget.TAG_NAME.equals(widget.getTagName())))
		{
			MainPane.getTipCom().warn(
					"First add a LinearLayout widget to the TabHost Layout.  "
							+ "Then add the TabWidget to that LinearLayout");
			return false;
		}
		return true;
	}

	public void addWidget(Widget w, Layout l, int x, int y)
	{
		boolean prefersGrid = AndroidEditor.instance().getPreferences()
				.getSnap();
		if (l instanceof AbsoluteLayout
				&& ((shift && !prefersGrid) || (!shift && prefersGrid)))
			w.setPosition((x / grid_x) * grid_x - l.getScreenX(), (y / grid_y)
					* grid_y - l.getScreenY());
		else
			w.setPosition(x - l.getScreenX(), y - l.getScreenY());
		l.addWidget(w);
		AndroidEditor.instance().queueUndoRecord(new WidgetAddRecord(l, w));
		AndroidEditor.instance().getTreeModel().addWidget(w);
		l.apply();
		app.select(w);
	}

	public void mouseReleased(MouseEvent e)
	{
		dragging = false;
		e.getComponent().setCursor(
				Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		Widget w = app.getSelected();
		if (w != null)
		{
			int nx = w.getX();
			int ny = w.getY();
			if (nx != sx || ny != sy)
			{
				app.queueUndoRecord(new MoveRecord(sx, sy, nx, ny, w));
			}
		}
	}

	public void mouseDragged(MouseEvent e)
	{
		int x = e.getX() - viewer.getOffX();
		int y = e.getY() - viewer.getOffY();
		if (x < 0)
		{
			x = 0;
		}
		if (y < 0)
		{
			y = 0;
		}
		if (x > app.getScreenX())
			x = app.getScreenX();
		if (y > app.getScreenY())
			y = app.getScreenY();

		Widget selected = app.getSelected();
		Vector<Widget> ws = app.findWidgets(x, y);

		if (!dragging)
		{
			if (!ws.contains(selected))
			{
				if (ws.size() > 0)
				{
					app.select(ws.get(0));
					selected = app.getSelected();
					Widget w = selected;
					off_x = (w.getParent() != null ? w.getParent().getScreenX()
							: 0)
							+ w.getX() - x;
					off_y = (w.getParent() != null ? w.getParent().getScreenY()
							: 0)
							+ w.getY() - y;
				}
				else
				{
					app.select(null);
					return;
				}
			}
		}
		dragging = true;

		if (selected != null)
		{
			Layout l = selected.getParent();
			Vector<Layout> ls = app.findLayouts(x, y);
			if (ls.size() > 0)
			{
				int ix = 0;
				do
				{
					l = ls.get(ix++);
				}
				while (l.equals(selected) && ix < ls.size());
			}
			else
				l = (selected.getParent());
			if (l != selected.getParent())
			{
				if (!(selected instanceof Layout)
						|| !((Layout) selected).containsWidget(l))
				{
					(selected.getParent()).removeWidget(selected);
					l.addWidget(selected);
				}
				else
				{
					l = selected.getParent();
				}
			}

			if (mode == NORMAL)
			{
				e.getComponent().setCursor(
						Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				int nx = (x + off_x);
				int ny = (y + off_y);
				boolean prefersGrid = AndroidEditor.instance().getPreferences()
						.getSnap();
				if (l instanceof AbsoluteLayout
						&& ((shift && !prefersGrid) || (!shift && prefersGrid)))
				{
					nx = nx / grid_x * grid_x;
					ny = ny / grid_y * grid_y;
				}
				selected.setPosition(nx - l.getScreenX(), ny - l.getScreenY());
			}
			else if (mode == E)
			{
				Widget w = AndroidEditor.instance().getSelected();
				w.setWidth(x - (l.getScreenX() + w.getX()));
			}
			else if (mode == SE)
			{
				Widget wd = AndroidEditor.instance().getSelected();
				int w = x - (l.getScreenX() + wd.getX());
				int h = y - (l.getScreenY() + wd.getY());
				if (shift)
				{
					if (w > h)
						h = w;
					else
						w = h;
				}
				wd.setSize(w, h);
			}
			else if (mode == S)
			{
				Widget w = AndroidEditor.instance().getSelected();
				w.setHeight(y - (l.getScreenY() + w.getY()));
			}
			l.positionWidget(selected);
			l.apply();
			viewer.repaint();
		}
	}

	public void mouseMoved(MouseEvent ev)
	{
		int ex = ev.getX();
		int ey = ev.getY();

		Widget selected = AndroidEditor.instance().getSelected();
		mode = 0;
		Cursor c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

		if (selected != null)
		{
			int x = selected.getParent().getScreenX() + selected.getX()
					+ viewer.getOffX();
			int y = selected.getParent().getScreenY() + selected.getY()
					+ viewer.getOffY();
			int distance_x = ex - (x + selected.getWidth());
			int distance_y = ey - (y + selected.getHeight());
			boolean close_r = distance_x > -8 && distance_x < -1 && ey > y
					&& ey < y + selected.getHeight();
			boolean close_b = distance_y > -8 && distance_y < -1 && ex > x
					&& ex < x + selected.getWidth();

			if (close_r)
			{
				if (close_b)
				{
					c = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
					mode = SE;
				}
				else
				{
					c = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
					mode = E;
				}
			}
			else if (close_b)
			{
				c = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
				mode = S;
			}
		}
		ev.getComponent().setCursor(c);
	}

	public void keyPressed(KeyEvent ev)
	{
		switch (ev.getKeyCode())
		{
			case KeyEvent.VK_SHIFT:
				shift = true;
				break;
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_BACK_SPACE:
				Widget w = app.getSelected();
				if (w != null)
				{
					Layout l = w.getParent();
					app.removeWidget(app.getSelected());
					app.queueUndoRecord(new WidgetDeleteRecord(l, w));
					viewer.repaint();
				}
				break;
		}
		Widget w = app.getSelected();
		if (w != null && w.getParent() instanceof AbsoluteLayout)
		{
			int dx = 0;
			int dy = 0;
			switch (ev.getKeyCode())
			{
				case KeyEvent.VK_UP:
					dy = -1;
					break;
				case KeyEvent.VK_DOWN:
					dy = 1;
					break;
				case KeyEvent.VK_LEFT:
					dx = -1;
					break;
				case KeyEvent.VK_RIGHT:
					dx = 1;
					break;
			}
			if (dx != 0 || dy != 0)
			{
				int sx = w.getX();
				int sy = w.getY();
				w.move(dx, dy);
				app
						.queueUndoRecord(new MoveRecord(sx, sy, sx + dx, sy
								+ dy, w));
			}
		}
	}

	public void keyReleased(KeyEvent ev)
	{
		switch (ev.getKeyCode())
		{
			case KeyEvent.VK_SHIFT:
				shift = false;
				break;
		}
	}

	public void keyTyped(KeyEvent ev)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		Widget w = app.getSelected();
		if (w != null)
		{
			int x = e.getX() - viewer.getOffX();
			int y = e.getY() - viewer.getOffY();

			sx = w.getX();
			sy = w.getY();

			off_x = (w.getParent() != null ? w.getParent().getScreenX() : 0)
					+ w.getX() - x;
			off_y = (w.getParent() != null ? w.getParent().getScreenY() : 0)
					+ w.getY() - y;
		}
	}
}
