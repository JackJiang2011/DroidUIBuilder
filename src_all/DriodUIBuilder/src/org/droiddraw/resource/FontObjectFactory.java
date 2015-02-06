/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * FontObjectFactory.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.resource;

import java.awt.Font;

/**
 * 字体辅助类.
 * 
 * @author Jack Jiang, 2012-11-24
 * @version 1.0
 */
public class FontObjectFactory
{
	private static Font droidSans = null;
	private static Font driodSansMono = null;
	
	public static Font getDroidSans()
	{
		if(droidSans == null)
			droidSans = getFontFromFile("font/DroidSans.ttf");
		return droidSans;
	}
	
	public static Font getDroidSans_Bold()
	{
		if(droidSans == null)
			droidSans = getFontFromFile("font/DroidSans-Bold.ttf");
		return droidSans;
	}
	
	public static Font getDroidSansMono()
	{
		if(driodSansMono == null)
			driodSansMono = getFontFromFile("font/DroidSansMono.ttf");
		return driodSansMono;
	}
	
	private static Font getFontFromFile(String relativePatch)
	{
		Font f = null;
		try{
			f = Font.createFont(Font.TRUETYPE_FONT
					, FontObjectFactory.class.getResourceAsStream(relativePatch));
		}
		catch (Exception e){
			f = Font.getFont("Arial");
		}
		
		return f;
	}
}
