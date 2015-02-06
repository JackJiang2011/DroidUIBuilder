/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Spinner.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.jb2011.ninepatch4j.NinePatch;

import com.jb2011.drioduibuilder.util.Utils;

public class Spinner extends AbstractWidget
{
	public static final String TAG_NAME = "Spinner";
//	NineWayImage img;
//	NineWayImage arrows;
	NinePatch image_base;
//	Image arrs;

	BooleanProperty onTop;
	Font f;

	public Spinner()
	{
		super(TAG_NAME);

		image_base = null;

		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			image_base = WidgetDefaultNPIconFactory.getInstance().getSpinner_normal();
//				ImageResources.instance().getImage(
//					"def/spinner_normal.9");
//			img = new NineWayImage(image_base, 10, 10);
//			arrs = ImageResources.instance().getImage(
//					"def/btn_dropdown_neither.9");
//			arrows = null;
		}
//		else if (theme.equals("light"))
//		{
//			image_base = ImageResources.instance().getImage(
//					"light/spinnerbox_background_focus_yellow.9");
//			if (image_base != null)
//			{
//				img = new NineWayImage(image_base, 10, 10, 28, 10);
//				arrs = ImageResources.instance().getImage(
//						"light/spinnerbox_arrow_middle.9");
//				arrows = new NineWayImage(arrs, 1, 1, 22, 1);
//			}
//		}

		onTop = new BooleanProperty("Selector on Top",
				"android:drawSelectorOnTop", false);
		props.add(onTop);

		f = new Font("Arial", Font.PLAIN, 14);

		apply();
	}

	@Override
	protected int getContentHeight()
	{
		return Button.DEFAULT_CONTENT_HEIGHT;//image_base.getHeight();//.getHeight(null);
	}

	@Override
	protected int getContentWidth()
	{
		return 90;
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		if (image_base != null)
		{
//			img.paint(g, getX(), getY(), getWidth(), getHeight());
			image_base.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
//			if (arrows != null)
//				arrows.paint(g, getX(), getY(), getWidth(), getHeight());
//			else
//				g.drawImage(arrs, getX() + getWidth() - 38, getY()
//						+ getHeight() / 2 - arrs.getHeight(null) / 2, null);
		}
		g.setColor(Color.black);
		g.setFont(f);
		// 开启字体绘制反走样
		Utils.setTextAntiAliasing((Graphics2D)g, true);
//		if (arrows != null)
			g.drawString("Spinner", getX() + 10, 
					getY()+Utils.getFontCenterY(f, getHeight())// Y轴居中绘制
					);
		// 绘制完成后并闭字体绘制反走样
		Utils.setTextAntiAliasing((Graphics2D)g, false);
	}

}
