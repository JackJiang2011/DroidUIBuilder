/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * ImageResources.java at 2015-2-6 16:12:03, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.gui;

import java.awt.Image;
import java.util.Hashtable;

public class ImageResources
{
	private Hashtable<String, Image> images;
	private static ImageResources res = null;

	private ImageResources()
	{
		images = new Hashtable<String, Image>();
	}

	public void addImage(Image img, String name)
	{
		images.put(name, img);
	}

	public Image getImage(String name)
	{
		return images.get(name);
	}

	public static ImageResources instance()
	{
		if (res == null)
		{
			res = new ImageResources();
		}
		return res;
	}
}
