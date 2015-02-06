/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * EditView.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.jb2011.ninepatch4j.NinePatch;

public class EditView extends TextView
{
	public static final String TAG_NAME = "EditText";

	BooleanProperty password;
	SelectProperty numeric;
	BooleanProperty phone;
	BooleanProperty autoText;
	SelectProperty capitalize;
	StringProperty digits;

//	NineWayImage img;
	NinePatch img_base;

	public static final String[] propertyNames = new String[] {
			"android:password", "android:capitalize", "android:numeric",
			"android:phoneNumber", "android:autoText", "android:digits" };

	public EditView(String txt)
	{
		super(txt);
		this.setTagName(TAG_NAME);

		password = new BooleanProperty("Password", "android:password", false);
		capitalize = new SelectProperty("Capitalize", "android:capitalize",
				new String[] { "sentences", "words" }, 0);
		numeric = new SelectProperty("Number Format", "android:numeric",
				new String[] { "integer", "signed", "decimal" }, 0);
		phone = new BooleanProperty("Phone Number", "android:phoneNumber",
				false);
		autoText = new BooleanProperty("Correct Spelling", "android:autoText",
				false);
		digits = new StringProperty("Valid Characters", "android:digits", "");

		props.add(password);
		props.add(numeric);
		props.add(phone);
		props.add(autoText);
		props.add(capitalize);
		props.add(digits);

		img_base = null;

		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default"))
		{
			fontSz.setStringValue("14sp");
			fontSize = 14;
			
			img_base = WidgetDefaultNPIconFactory.getInstance().getEditBox_normal();
//			img_base = ImageResources.instance().getImage(
//					"mdpi/textfield_default.9");
//			if (img_base != null)
//			{
//				this.img = new NineWayImage(img_base, 11, 6);
//			}
			pad_x = 20;
			pad_y = 0;
		}
//		else if (theme.equals("light"))
//		{
//			img_base = ImageResources.instance().getImage(
//					"light/editbox_background_normal.9");
//			if (img_base != null)
//			{
//				this.img = new NineWayImage(img_base, 10, 10);
//			}
//			pad_x = 18;
//			pad_y = 0;
//		}

		apply();
	}

	@Override
	public Vector<Property> getProperties()
	{
		Vector<Property> ret = super.getProperties();
		if (digits.getStringValue() == null
				|| digits.getStringValue().length() < 1)
			ret.remove(digits);
		return ret;
	}

	@Override
	public int getContentWidth()
	{
		if (password != null && password.getBooleanValue())
		{
			String s = "";
			for (int i = 0; i < text.getStringValue().length(); i++)
				s = s + '\245';
			return stringLength(s) + pad_x;
		}
		else
		{
			return super.getContentWidth();
		}
	}

	@Override
	public int getContentHeight()
	{
//		// int sup = super.getContentWidth();
//		// if (sup > fontSize) {
//		// return sup;
//		// }
//		// else {
//		if (img_base != null)
//		{
//			String theme = AndroidEditor.instance().getTheme();
//			if (theme == null || theme.equals("default"))
//				return img_base.getHeight(null) - 5;
//			else if (theme.equals("light"))
//				return img_base.getHeight(null) - 5;
//		}
//		return fontSize;
//		// }
		return Button.DEFAULT_CONTENT_HEIGHT;
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
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
				g.setColor(Color.darkGray);
				g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
			}
			else
			{
				img_base.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
//				img.paint(g, getX(), getY(), getWidth(), getHeight());
				g.setColor(Color.darkGray);
			}
		}
		
		g.setFont(f);
		String s;
		if (password.getBooleanValue())
		{
			s = "";
			for (int i = 0; i < text.getStringValue().length(); i++)
				s = s + '\245';
		}
		else
			s = text.getStringValue();
		g.setColor(textColor.getColorValue());
		// g.drawString(s, getX()+pad_x/2, getY()+fontSize+pad_y/2-1);
		this.drawText(g, 0, (fontSize + getHeight()) / 2 - 2);
//		g.setColor(Color.black);
		
		// 绘制一个fucos状态下的光标样式，仅用于装饰哦
		g.setColor(Color.gray);
		g.fillRect(getX() + pad_x / 2 - 4, getY() + (getHeight() - fontSize)
				/ 2 - 2, 1, fontSize + 4);
	}
}
