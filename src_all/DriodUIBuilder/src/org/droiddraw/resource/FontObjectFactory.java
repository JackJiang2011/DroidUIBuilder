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
