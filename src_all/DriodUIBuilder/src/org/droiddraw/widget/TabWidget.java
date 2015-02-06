/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * TabWidget.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.StringProperty;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.jb2011.ninepatch4j.NinePatch;

import com.jb2011.drioduibuilder.util.Utils;

/**
 * The TabWidget.java class will be used by the TabHost.java widget via a
 * LinearLayout widget.
 * 
 * @author rey malahay <a href="mailto:reymalahay@gmail.com">Rey Malahay</a>
 * 
 */
public class TabWidget extends AbstractWidget
{

	private NinePatch tab;
//	private NineWayImage img;
//	public static final String IMAGE_NAME = "def/tab_selected.9";
	public static final String TAG_NAME = "TabWidget";
	private static final String ANDROID_ID = "@android:id/tabs"; /*
																 * This is the
																 * id that is
																 * displayed in
																 * the final xml
																 * file.
																 */

	/**
	 * Default constructor
	 */
	public TabWidget()
	{
		super(TAG_NAME);
		((StringProperty) this.getProperties().get(
				this.getProperties().indexOf(this.id)))
				.setStringValue(ANDROID_ID);
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			tab = WidgetDefaultNPIconFactory.getInstance().getTab_normal();
//				ImageResources.instance().getImage(IMAGE_NAME);
//			img = new NineWayImage(tab, 10, 10);
		}
		apply();
	}

	public void apply()
	{
		super.apply();
		this.baseline = 22;
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		if (tab == null)
		{
			g.setColor(Color.white);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);

			g.setColor(Color.black);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		}
		else
		{
			tab.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
//			img.paint(g, getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.black);
			final String showName = "Tab Widget";
			g.drawString(showName, getX() + (getWidth() - Utils.getStrPixWidth(g.getFont(), showName))/2
					, getY() + Utils.getFontCenterY(g.getFont(), getHeight()));
		}
	}

	@Override
	protected int getContentHeight()
	{
//		if (tab != null)
//			return tab.getHeight(null);
//		else
//			return 10;
		return Button.DEFAULT_CONTENT_HEIGHT;
	}

	@Override
	protected int getContentWidth()
	{
		return (tab != null) ? 97 : 90;
	}
}