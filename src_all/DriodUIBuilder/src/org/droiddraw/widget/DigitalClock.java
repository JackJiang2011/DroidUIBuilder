/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * DigitalClock.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.widget;

import java.awt.Graphics;

public class DigitalClock extends TextView
{
	public static final String TAG_NAME = "DigitalClock";

	public DigitalClock()
	{
		super(null);//"HH:MM:SS pm");
		this.setTagName(TAG_NAME);
		setDate();
	}

	private void setDate()
	{
		text.setStringValue(
				new java.text.SimpleDateFormat("hh:mm:ss").
				// 因为使用的是DroidSans字体，而经此form后pm会被解析成“下午”等
				// 为了不出现乱码（DroidSans字体是英文字体），干脆就固定加个尾巴
				// 吧（虽不正确但ui设计应该是够用了）
                format(new java.util.Date())+" pm");
//				DateFormat.getTimeInstance(DateFormat.SHORT)
//				.format(new Date()));
	}

	@Override
	public void paint(Graphics g)
	{
		setDate();
		super.paint(g);
	}
}
