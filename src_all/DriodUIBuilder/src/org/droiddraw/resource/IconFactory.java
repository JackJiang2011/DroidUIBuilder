/*
 * Copyright (C) 2012 Jack Jiang The BeautyEye Project. 
 * All rights reserved.
 * Project URL:https://code.google.com/p/beautyeye/
 *  
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * __IconFactory__.java at 2012-10-13 9:02:19, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.resource;

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
	
	public ImageIcon getDAndCPane_Icon_left()
	{
		return getImage(IMGS_ROOT+"/d$cswitcher_bg_2_20121117.png");
	}
	public ImageIcon getDAndCPane_Icon_right()
	{
		return getImage(IMGS_ROOT+"/d$cswitcher_bg_1_20121117.png");
	}
	
	public ImageIcon getNavigateTool_hot_Icon()
	{
		return getImage(IMGS_ROOT+"/navigate_tool_hot.png");
	}
	public ImageIcon getNavigateTool_decoratedAndroidLogo_Icon()
	{
		return getImage(IMGS_ROOT+"/navigate_tool_decorated_android_logo.png");
	}
	
	public ImageIcon getMakeLayoutHeadFlag_Icon()
	{
		return getImage(IMGS_ROOT+"/make_layout_head_flag.png");
	}
	
	//------------------------------------------------------------------- layout preview small icon
	public ImageIcon getAbsoluteLayout_small_Icon()
	{
		return getImage(IMGS_ROOT+"/AbsoluteLayout.png");
	}
	public ImageIcon getFrameLayout_small_Icon()
	{
		return getImage(IMGS_ROOT+"/FrameLayout.png");
	}
	public ImageIcon getLinearLayout_small_Icon()
	{
		return getImage(IMGS_ROOT+"/VerticalLinearLayout.png");
	}
	public ImageIcon getRelativeLayout_small_Icon()
	{
		return getImage(IMGS_ROOT+"/RelativeLayout.png");
	}
	public ImageIcon getScrollView_small_Icon()
	{
		return getImage(IMGS_ROOT+"/ScrollView.png");
	}
	public ImageIcon getTableLayout_small_Icon()
	{
		return getImage(IMGS_ROOT+"/TableLayout.png");
	}
	
	public ImageIcon getTableRow_small_Icon()
	{
		return getImage(IMGS_ROOT+"/TableRow.png");
	}
	
//	public ImageIcon getGridView_small_Icon()
//	{
//		return getImage(IMGS_ROOT+"/GridView.png");
//	}
//	
//	public ImageIcon getListView_small_Icon()
//	{
//		return getImage(IMGS_ROOT+"/ListView.png");
//	}
	
}
