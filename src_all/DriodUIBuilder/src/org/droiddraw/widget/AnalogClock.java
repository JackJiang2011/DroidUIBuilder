/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * AnalogClock.java at 2015-2-6 16:12:00, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;

import org.droiddraw.gui.ImageResources;

public class AnalogClock extends AbstractWidget
{
	public static final String TAG_NAME = "AnalogClock";

	BufferedImage face, face_scale;
	BufferedImage hour, hour_scale;
	BufferedImage minute, minute_scale;
	int offx;

	public AnalogClock()
	{
		super(TAG_NAME);
		apply();
		if (face == null)
		{
			Image img = ImageResources.instance().getImage("light/clock_dial");
			if (img != null)
			{
				face = new BufferedImage(img.getWidth(null), img
						.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
				face.getGraphics().drawImage(img, 0, 0, null);
				face_scale = face;
			}
			img = ImageResources.instance().getImage("light/clock_hand_hour");
			if (img != null)
			{
				hour = new BufferedImage(img.getWidth(null), img
						.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
				hour.getGraphics().drawImage(img, 0, 0, null);
				hour_scale = hour;
			}
			img = ImageResources.instance().getImage("light/clock_hand_minute");
			if (img != null)
			{
				minute = new BufferedImage(img.getWidth(null), img
						.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
				minute.getGraphics().drawImage(img, 0, 0, null);
				minute_scale = minute;
			}
			offx = 14;
		}
	}

	@Override
	protected int getContentHeight()
	{
		return 154;
	}

	@Override
	protected int getContentWidth()
	{
		return 154;
	}

	@Override
	public void apply()
	{
		super.apply();

		if (face != null)
		{
			double scale_x = getWidth() / 154.0;
			double scale_y = getHeight() / 154.0;
			AffineTransform at = AffineTransform.getScaleInstance(scale_x,
					scale_y);
			AffineTransformOp op = new AffineTransformOp(at,
					AffineTransformOp.TYPE_BILINEAR);
			face_scale = new BufferedImage(getWidth(), getHeight(),
					BufferedImage.TYPE_4BYTE_ABGR);
			op.filter(face, face_scale);
			hour_scale = new BufferedImage((int) (hour.getWidth() * scale_x),
					(int) (hour.getHeight() * scale_y),
					BufferedImage.TYPE_4BYTE_ABGR);
			op.filter(hour, hour_scale);
			minute_scale = new BufferedImage(
					(int) (minute.getWidth() * scale_x), (int) (minute
							.getHeight() * scale_y),
					BufferedImage.TYPE_4BYTE_ABGR);
			op.filter(minute, minute_scale);

			offx = (int) (14 * scale_x);
		}
	}

	private void drawAngleLine(Graphics g, double angle, double scale)
	{
		int dx = (int) (Math.cos(angle) * getWidth() * scale);
		int dy = (int) (Math.sin(angle) * getWidth() * scale);
		int cx = getX() + getWidth() / 2;
		int cy = getY() + getHeight() / 2;
		g.drawLine(cx, cy, cx + dx, cy - dy);
	}
	
	protected void paintNormalBorder(Graphics g)
	{
		// 本widget在未选中时不需要绘制border的哦
	}

	public void paint(Graphics g)
	{
		// 绘制背景
		super.paintBackground(g);
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int hrs = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		double hr_a = Math.toRadians(90 - (hrs + min / 60.0) * 30);
		double hr_m = Math.toRadians(90 - min * 6);
		if (face_scale == null)
		{
			g.setColor(Color.white);
			g.fillOval(getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.black);
			g.drawOval(getX(), getY(), getWidth(), getHeight());
			drawAngleLine(g, hr_a, 0.25);
			drawAngleLine(g, hr_m, 0.45);
		}
		else
		{
			g.drawImage(face_scale, getX(), getY(), null);
			g.drawImage(minute_scale, getX() + getWidth() / 2 - offx, getY(),null);
			g.drawImage(hour_scale, getX() + getWidth() / 2 - offx, getY(),null);
		}
	}
}
