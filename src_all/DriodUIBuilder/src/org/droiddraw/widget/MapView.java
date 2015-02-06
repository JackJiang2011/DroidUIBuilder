/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * MapView.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.StringProperty;

public class MapView extends AbstractWidget
{
	private static Image map = null;
	public static final String TAG_NAME = "MapView";

	BooleanProperty clickable = 
			new BooleanProperty("Clickable","android:clickable", true);
	StringProperty apiKey = 
			new StringProperty("API Key", "android:apiKey","none");

	public MapView()
	{
		super(TAG_NAME);
		clickable = new BooleanProperty("Clickable", "android:clickable", true);
		apiKey = new StringProperty("API Key", "android:apiKey", "none");
		clickable.setDefaulted(false);
		apiKey.setDefaulted(false);
		addProperty(clickable);
		addProperty(apiKey);
		widthProp.setStringValue("fill_parent");
		heightProp.setStringValue("fill_parent");
		apply();
		if (map == null)
		{
			map = ImageResources.instance().getImage("mapview");
//			map = new BufferedImage(img.getWidth(null), img.getHeight(null),
//					BufferedImage.TYPE_4BYTE_ABGR);
//			map.getGraphics().drawImage(img, 0, 0, null);
		}
	}

	@Override
	protected int getContentHeight()
	{
		return 100;
	}

	@Override
	protected int getContentWidth()
	{
		return 70;
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		g.drawImage(map, getX(), getY(), getWidth(), getHeight(), null);
	}

}
