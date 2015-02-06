/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * RadioButton.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;

import com.jb2011.drioduibuilder.util.Utils;

public class RadioButton extends CompoundButton
{
	public static final String TAG_NAME = "RadioButton";
	Image on;
	Image off;

	public RadioButton(String text)
	{
		super(text);
		this.setTagName(TAG_NAME);

		pad_y = 6;

		apply();
	}

	@Override
	public void apply()
	{
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			off = ImageResources.instance().getImage("def/btn_radio_off");
			on = ImageResources.instance().getImage("def/btn_radio_on");
		}
		else if (theme.equals("light"))
		{
			off = ImageResources.instance().getImage(
					"light/radiobutton_off_background");
			on = ImageResources.instance().getImage(
					"light/radiobutton_on_background");
		}

		if (off != null)
		{
			pad_x = off.getWidth(null);
		}
		else
		{
			pad_x = 24;
		}
		super.apply();
	}

	@Override
	protected int getContentWidth()
	{
		return getContentWidthImpl();
	}
	
	@Override
	protected int getContentHeight()
	{
		if (off != null)
			return off.getHeight(null);
		else
			return super.getContentHeight();
	}

	@Override
	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		int off_x, off_y;

		if (off == null || on == null)
		{
			g.setColor(Color.white);
			g.fillOval(getX() + 2, getY() + 2, 16, 16);

			g.setColor(Color.black);
			g.drawOval(getX() + 2, getY() + 2, 16, 16);

			if ("true".equals(this.getPropertyByAttName("android:checked")
					.getValue()))
			{
				g.fillOval(getX() + 6, getY() + 6, 8, 8);
			}

			off_x = 20;
			off_y = 18;
		}
		else
		{
			Image img = off;
			if ("true".equals(this.getPropertyByAttName("android:checked")
					.getValue()))
			{
				img = on;
			}
			g.drawImage(img, getX(), getY(), null);
			g.setColor(Color.black);

			off_x = img.getWidth(null);
			off_y = img.getHeight(null);
		}

		baseline = (off_y + fontSize) / 2;

		setTextColor(g);
		g.setFont(f);
		// 开启字体绘制反走样
		Utils.setTextAntiAliasing((Graphics2D)g, true);
		g.drawString(text.getStringValue(), getX() + off_x, getY() + baseline - 4);
		// 绘制完成后并闭字体绘制反走样
		Utils.setTextAntiAliasing((Graphics2D)g, false);
	}
}
