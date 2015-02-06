/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * TextView.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.PropertiesPanel;
import org.droiddraw.property.ColorProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.resource.FontObjectFactory;
import org.droiddraw.resource.WidgetDefaultNPIconFactory;
import org.droiddraw.util.DisplayMetrics;
import org.jb2011.ninepatch4j.NinePatch;

import com.jb2011.drioduibuilder.util.Utils;

public class TextView extends AbstractWidget
{
	public static final String TAG_NAME = "TextView";
	public static final int START = 0;
	public static final int CENTER = 1;
	public static final int END = 2;

	int fontSize = 14;

	StringProperty text;
	StringProperty hint;
	StringProperty fontSz;
	SelectProperty face;
	SelectProperty style;
	SelectProperty align;
	ColorProperty textColor;

	int pad_x = 6;
	int pad_y = 4;

	PropertiesPanel p;
	Font f;
	BufferedImage _tempUsedForMessureStringWidth;

	boolean osx;
	
	/** 
	 * 本widget处于工具面板时绘制背景使用的NP图.
	 * 该背景仅为了出于美感而作，别无它用，并不对应于该widget在android真机上的效果哦.
	 */
	protected NinePatch bgInTools = null;

	public static final String[] propertyNames = new String[] { "android:hint",
			"android:textSize", "android:textStyle", "android:typeface",
			"android:textColor" };

	public TextView(String str)
	{
		super(TAG_NAME);

		text = new StringProperty("Text", "android:text", "");
		if (str != null)
		{
			text.setStringValue(str);
		}
		hint = new StringProperty("Default Text", "android:hint", "");
		fontSz = new StringProperty("Font Size", "android:textSize", fontSize
				+ "sp");
		face = new SelectProperty("Font Face", "android:typeface",
				new String[] { "normal", "sans", "serif", "monospace" }, 0);
		style = new SelectProperty("Font Style", "android:textStyle",
				new String[] { "normal", "bold", "italic", "bold|italic" }, 0);
		textColor = new ColorProperty("Text Color", "android:textColor", null);
		align = new SelectProperty("Text Alignment", "android:gravity",
				new String[] { "left", "center", "right" }, 2);
		props.add(text);
		props.add(hint);
		props.add(fontSz);
		props.add(face);
		props.add(style);
		props.add(textColor);
		props.add(align);

		osx = (System.getProperty("os.name").toLowerCase().contains("mac os x"));
		buildFont();

		_tempUsedForMessureStringWidth = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
		bgInTools = WidgetDefaultNPIconFactory.getInstance().getTextView_normal();
		
		apply();
	}

	protected void buildFont()
	{
		if(face.getStringValue() != null && face.getStringValue().equals("monospace"))
			f = FontObjectFactory.getDroidSansMono().deriveFont(Font.PLAIN, fontSize);
		else
			f = FontObjectFactory.getDroidSans().deriveFont(Font.PLAIN, fontSize);
//		if (osx)
//			f = new Font("Arial", Font.PLAIN, fontSize);
//		else
//			f = new Font(face.getStringValue(), Font.PLAIN, fontSize);
		
		if(style.getStringValue() != null
				&& style.getStringValue().contains("normal"))
			f = f.deriveFont(Font.PLAIN);
		else
		{
			if (style.getStringValue() != null
					&& style.getStringValue().contains("bold"))
				f = f.deriveFont(f.getStyle() | Font.BOLD);
			if (style.getStringValue() != null
					&& style.getStringValue().contains("italic"))
				f = f.deriveFont(f.getStyle() | Font.ITALIC);
		}
	}

	@Override
	public void apply()
	{
		super.apply();
		if (fontSz.getStringValue() != null
				&& fontSz.getStringValue().length() > 0)
		{
			fontSize = (DisplayMetrics.readSize(fontSz));
		}
		buildFont();
		this.readWidthHeight();
		this.baseline = fontSize + pad_y / 2;
	}

	protected Vector<String> buildLineBreaks(String textVal)
	{
		Vector<String> res = new Vector<String>();
		if (textVal == null)
		{
			return res;
		}
		String str = textVal;
		int ix;
		do
		{
			ix = str.indexOf('\n');
			String txt = str;
			if (ix != -1)
			{ // && (ix1 == -1 || ix1 > ix2)) {
				txt = str.substring(0, ix);
				str = str.substring(ix + 1);
			}
			int width = getWidth();
			if (width < 0)
			{
				res.add(txt);
				return res;
			}

			int l = stringLength(txt);
			while (l > width)
			{
				int bk = 1;
				while (stringLength(txt.substring(0, bk)) < width)
					bk++;
				bk--;
				if (bk == 0)
				{
					return res;
				}
				String sub = txt.substring(0, bk);
				res.add(sub);
				txt = txt.substring(bk);
				l = stringLength(txt);
			}
			res.add(txt);
		}
		while (ix != -1);
		return res;
	}

	protected int stringLength(String str)
	{
		if (str == null)
			return 0;
		return _tempUsedForMessureStringWidth.getGraphics().getFontMetrics(f).stringWidth(str);
	}

	protected String getText()
	{
		String txt = text.getStringValue();
		if (txt == null || txt.length() == 0)
		{
			txt = hint.getStringValue();
		}
		return txt;
	}

	@Override
	protected int getContentWidth()
	{
		int l = stringLength(getText()) + pad_x;
		if (l > AndroidEditor.instance().getScreenX())
			l = AndroidEditor.instance().getScreenX() - getX();
		return l;
	}

	@Override
	protected int getContentHeight()
	{
		Vector<String> texts = buildLineBreaks(getText());
		if (texts.size() == 0)
			return fontSize + pad_y;
		int h = texts.size() * (fontSize + 1) + pad_y;
		return h;
	}

	protected void drawText(Graphics g, int x, int h)
	{
		int aln = START;
		if (align.getStringValue().equals("end"))
		{
			aln = END;
		}
		else if (align.getStringValue().equals("center"))
		{
			aln = CENTER;
		}
		this.drawText(g, x, h, aln);
	}

	protected void drawText(Graphics g, int dx, int h, int align)
	{
		String txt = getText();
		drawText(g, txt, dx, h, align);
	}

	protected void drawText(Graphics g, String txt, int dx, int h, int align)
	{
		int tx = 0;
		if (txt == null)
			return;
		for (String s : buildLineBreaks(txt))
		{
			int l = stringLength(s);
			if (align == END)
				tx = getX() + getWidth() - l - pad_x / 2 + dx;
			else if (align == CENTER)
				tx = getX() + getWidth() / 2 - l / 2 + dx;
			else
				tx = getX() + pad_x / 2 + dx;
			// 开启字体绘制反走样
			Utils.setTextAntiAliasing((Graphics2D)g, true);
			g.drawString(s, tx, getY() + h);
			// 绘制完成后并闭字体绘制反走样
			Utils.setTextAntiAliasing((Graphics2D)g, false);
			h += fontSize + 1;
			if (h > getHeight())
				break;
		}
	}

	protected void setTextColor(Graphics g)
	{
		Color c = textColor.getColorValue();
		String theme = AndroidEditor.instance().getTheme();
		Color def = null;
		if (theme == null || theme.equals("default"))
		{
			def = Color.white;
		}
		else if (theme.equals("light"))
		{
			def = Color.black;
		}
		if (c == null)
			c = def;
		g.setColor(c);
	}

	public void paint(Graphics g)
	{
		// 当处于工具面板时，绘制的字符串仅用于设计人员选择该widget使用，仅供参考
		if(!this.isInDesignViewer())
		{
			// 仅在本widget处于工具面板时绘制
			if (bgInTools != null)
				bgInTools.draw((Graphics2D)g, getX(), getY(), getWidth(), getHeight());
			
			if(getText() != null)
			{
				g.setColor(Color.black);
				g.setFont(f);
				// 开启字体绘制反走样
				Utils.setTextAntiAliasing((Graphics2D)g, true);
				// 垂直居中（为了与NP背景图配合，作了偏移处理）
				g.drawString(getText(), getX() + 6
						, getY()+Utils.getFontCenterY(g.getFont(), getHeight()));
				// 绘制完成关闭字体绘制反走样
				Utils.setTextAntiAliasing((Graphics2D)g, false);
			}
		}
		// 处于真正的设计编辑面板中时
		else
		{
			// 绘制背景
			super.paintBackground(g);
			
			if (getText() != null)
			{
				setTextColor(g);
				g.setFont(f);

				int h = fontSize + pad_y / 2;
				drawText(g, 0, h);
			}
		}
	}

	@Override
	public Vector<Property> getProperties()
	{
		return props;
	}
}
