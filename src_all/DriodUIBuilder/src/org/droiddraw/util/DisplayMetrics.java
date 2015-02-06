/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * DisplayMetrics.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.util;

import org.droiddraw.property.StringProperty;

public class DisplayMetrics
{
	public static float density = 1.0f;
	public static float scaledDensity = 1.0f;
	public static float xdpi = 160;
	public static float ydpi = 160;

	public static final float MM_TO_IN = 0.0393700787f;
	public static final float PT_TO_IN = 1 / 72.0f;

	public static int readSize(StringProperty prop)
	{
		return readSize(prop.getStringValue());
	}

	public static int readSize(String sz)
	{
		if (sz == null)
			return -1;
		try
		{
			float size;
			if (sz.endsWith("dip"))
				size = Float.parseFloat(sz.substring(0, sz.length() - 3));
			else
				size = Float.parseFloat(sz.substring(0, sz.length() - 2));

			if (sz.endsWith("px"))
			{
				return (int) size;
			}
			else if (sz.endsWith("in"))
			{
				return (int) (size * xdpi);
			}
			else if (sz.endsWith("mm"))
			{
				return (int) (size * MM_TO_IN * xdpi);
			}
			else if (sz.endsWith("pt"))
			{
				return (int) (size * PT_TO_IN * xdpi);
			}
			else if (sz.endsWith("dp") || sz.endsWith("dip"))
			{
				return (int) (size * density);
			}
			else if (sz.endsWith("sp"))
			{
				return (int) (size * scaledDensity);
			}
			else
			{
				return Integer.parseInt(sz);
			}
		}
		catch (NumberFormatException ex)
		{
			return -1;
		}
	}
}
