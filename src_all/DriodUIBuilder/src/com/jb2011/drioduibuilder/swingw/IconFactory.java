/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * IconFactory.java at 2015-2-6 16:12:02, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package com.jb2011.drioduibuilder.swingw;

import javax.swing.ImageIcon;

import org.jb2011.lnf.beautyeye.utils.RawCache;

/**
 * 普通图片工厂类.
 * 
 * @author Jack Jiang
 * @version 1.0
 */
public class IconFactory extends RawCache<ImageIcon>
{
	/** 相对路径根（默认是相对于本类的相对物理路径）. */
	public final static String IMGS_ROOT="imgs";

	/** The instance. */
	private static IconFactory instance = null;

	/**
	 * Gets the single instance of __IconFactory__.
	 *
	 * @return single instance of __IconFactory__
	 */
	public static IconFactory getInstance()
	{
		if(instance==null)
			instance = new IconFactory();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.jb2011.lnf.beautyeye.utils.RawCache#getResource(java.lang.String, java.lang.Class)
	 */
	@Override
	protected ImageIcon getResource(String relativePath, Class baseClass)
	{
		return new ImageIcon(baseClass.getResource(relativePath));
	}
	
	/**
	 * Gets the image.
	 *
	 * @param relativePath the relative path
	 * @return the image
	 */
	public ImageIcon getImage(String relativePath)
	{
		return  getRaw(relativePath,this.getClass());
	}
	
	public ImageIcon getLaconicTipCloseIcon_normal()
	{
		return getImage(IMGS_ROOT+"/laconic_tip_close_icon_normal.png");
	}
	public ImageIcon getLaconicTipCloseIcon_rover()
	{
		return getImage(IMGS_ROOT+"/laconic_tip_close_icon_rover.png");
	}
	
	public ImageIcon getSwitchableBtnSelected_flag()
	{
		return getImage(IMGS_ROOT+"/switchable_btn_selected_flag.png");
	}
	
}
