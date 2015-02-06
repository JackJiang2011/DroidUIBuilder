/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ToggleButton.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;
import org.droiddraw.property.StringProperty;

public class ToggleButton extends Button
{
	public static final String TAG_NAME = "ToggleButton";

	private StringProperty textOn;
	private StringProperty textOff;

	NineWayImage on; // TODO 要换成NP图方式！
	NineWayImage off;// TODO 要换成NP图方式！

	public ToggleButton(String txtOn, String txtOff)
	{
		super("");
		this.setTagName(TAG_NAME);

		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			Image img_base = ImageResources.instance().getImage(
					"def/btn_toggle_on.9");
			if (img_base != null)
			{
				this.on = new NineWayImage(img_base, 10, 5);
			}
			img_base = ImageResources.instance().getImage(
					"def/btn_toggle_off.9");
			if (img_base != null)
			{
				this.off = new NineWayImage(img_base, 5, 5);
			}
		}

		// Empty defaults, so always print.
		this.textOn = new StringProperty("Text when on", "android:textOn", "");
		this.textOn.setStringValue(txtOn);
		this.textOff = new StringProperty("Text when off", "android:textOff",
				"");
		this.textOff.setStringValue(txtOff);
		this.addProperty(textOn);
		this.addProperty(textOff);
	}

	@Override
	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		// 处于可见状态才填充内容
		if(this.isVisible())
		{
			if (img_base == null)
			{
				g.setColor(Color.white);
				g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);

				g.setColor(Color.black);
				g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
			}
			else
			{
//				img_base.paint(g, getX(), getY(), getWidth(), getHeight());
				img_base.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
				if (on != null)
					on.paint(g, getX() + 15, getY() + getHeight() - 15, getWidth() - 30, 5);
				g.setColor(Color.black);
			}
		}
		
		g.setFont(f);
		g.setColor(textColor.getColorValue());
		drawText(g, textOn.getStringValue(), 0, getHeight() / 2 + fontSize / 2 - 8, CENTER);
	}

	@Override
	protected int getContentWidth()
	{
		int l1 = (textOn == null ? 0 : stringLength(textOn.getStringValue())
				+ pad_x);
		int l2 = (textOff == null ? 0 : stringLength(textOff.getStringValue())
				+ pad_x);
		return Math.max(Math.max(l1, l2), 40);
	}
}
