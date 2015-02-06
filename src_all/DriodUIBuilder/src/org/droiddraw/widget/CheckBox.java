/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * CheckBox.java at 2015-2-6 16:12:03, original version by Jack Jiang.
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

public class CheckBox extends CompoundButton
{
	public static final String TAG_NAME = "CheckBox";
	Image off;
	Image on;

	public CheckBox(String text)
	{
		super(text);
		setTagName(TAG_NAME);

		apply();
	}

	@Override
	public void apply()
	{
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			off = ImageResources.instance().getImage("def/btn_check_off");
			on = ImageResources.instance().getImage("def/btn_check_on");
		}
		else if (theme.equals("light"))
		{
			off = ImageResources.instance().getImage(
					"light/checkbox_off_background");
			on = ImageResources.instance().getImage(
					"light/checkbox_on_background");
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
		super.paintBackground(g);// 注意：不能调用super.paint()哦
		
		Image img;
		int off_x;
		int off_y;

		if (on == null || off == null)
		{
			g.setColor(Color.white);
			g.fillRect(getX() + 2, getY() + 2, 16, 16);

			g.setColor(Color.black);
			g.drawRect(getX() + 2, getY() + 2, 16, 16);

			if ("true".equals(this.getPropertyByAttName("android:checked")
					.getValue()))
			{
				g.drawLine(getX() + 2, getY() + 2, getX() + 18, getY() + 18);
				g.drawLine(getX() + 2, getY() + 18, getX() + 18, getY() + 2);
			}
			off_x = 20;
			off_y = 18;
		}
		else
		{
			if ("true".equals(this.getPropertyByAttName("android:checked")
					.getValue()))
			{
				img = on;
			}
			else
			{
				img = off;
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
