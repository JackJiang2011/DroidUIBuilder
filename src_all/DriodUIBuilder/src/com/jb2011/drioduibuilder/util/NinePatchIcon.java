/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * NinePatchIcon.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package com.jb2011.drioduibuilder.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.Icon;

import org.jb2011.ninepatch4j.NinePatch;

/**
 * Android平台的9patch图实现的可自动拉伸的Icon类.
 * 
 * @author js, 2011-12-22
 * @version 1.0
 * @see NinePatch
 */
public class NinePatchIcon implements Icon
{
	/** 默认绘制区域的宽度,默认是16 */
    private int width = 0;
    /** 默认绘制区域的高度,默认是16 */
    private int height = 0;
    /** android平台类似的9patch图片实例 */
    private NinePatch ninePatch;
    
    public NinePatchIcon()
	{
	}
    
    public NinePatchIcon(int width, int height, URL imageFileUrl) throws Exception
	{
		this(width , height, NinePatch.load(imageFileUrl, false));
	}
    
	public NinePatchIcon(int width, int height, BufferedImage image)
	{
		this(width , height, NinePatch.load(image, true, false));
	}
	
	public NinePatchIcon(int width, int height, NinePatch mPatch)
	{
		this.width = width;
		this.height = height;
		this.ninePatch = mPatch;
	}
    
    public int getIconHeight()
	{
		return height;
	}

	public int getIconWidth()
	{
		return width;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		if(ninePatch != null)
		{
			Graphics2D g2 = (Graphics2D) g;
			ninePatch.draw(g2, x, y, getIconWidth(), getIconHeight());
		}
	}

	public NinePatch getNinePatch()
	{
		return ninePatch;
	}
	public void setNinePatch(NinePatch ninePatch)
	{
		this.ninePatch = ninePatch;
	}
	public void setNinePatch(URL imageFileUrl) throws Exception
	{
		this.ninePatch = NinePatch.load(imageFileUrl, false);
	}
	public void setNinePatch(BufferedImage image)
	{
		this.ninePatch = NinePatch.load(image, true, false);
	}

	public void setIconWidth(int width)
	{
		this.width = width;
	}

	public void setIconHeight(int height)
	{
		this.height = height;
	}
}
