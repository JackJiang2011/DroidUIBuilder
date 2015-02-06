/*
 * Copyright (C) 2015 Jack Jiang(cngeeker.com) The DroidUIBuilder Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/DroidUIBuilder
 * Version 1.0
 * 
 * Jack Jiang PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * WidgetDefaultNPIconFactory.java at 2015-2-6 16:11:59, original version by Jack Jiang.
 * You can contact author with jb2011@163.com.
 */
package org.droiddraw.resource;

import org.jb2011.lnf.beautyeye.utils.NinePatchHelper;
import org.jb2011.lnf.beautyeye.utils.RawCache;
import org.jb2011.ninepatch4j.NinePatch;

/**
 * Object factory of NinePatch pictures(*.9.png).
 * 
 * @author Jack Jiang
 * @version 1.0
 */
public class WidgetDefaultNPIconFactory extends RawCache<NinePatch>
{
	/** root path(relative this NPIconFactory.class). */
	public final static String IMGS_ROOT="imgs/default/np";

	/** The instance. */
	private static WidgetDefaultNPIconFactory instance = null;

	/**
	 * Gets the single instance of __Icon9Factory__.
	 *
	 * @return single instance of __Icon9Factory__
	 */
	public static WidgetDefaultNPIconFactory getInstance()
	{
		if(instance==null)
			instance = new WidgetDefaultNPIconFactory();
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.jb2011.lnf.beautyeye.utils.RawCache#getResource(java.lang.String, java.lang.Class)
	 */
	@Override
	protected NinePatch getResource(String relativePath, Class baseClass)
	{
		return NinePatchHelper.createNinePatch(baseClass.getResource(relativePath), false);
	}

	/**
	 * Gets the raw.
	 *
	 * @param relativePath the relative path
	 * @return the raw
	 */
	public NinePatch getRaw(String relativePath)
	{
		return  getRaw(relativePath,this.getClass());
	}

	public NinePatch getButton_normal()
	{
		return getRaw(IMGS_ROOT+"/btn_default_normal.9.png");
	}
	
	public NinePatch getSpinner_normal()
	{
		return getRaw(IMGS_ROOT+"/btn_dropdown_normal.9.png");
	}
	
	public NinePatch getEditBox_normal()
	{
		return getRaw(IMGS_ROOT+"/editbox_background_normal.9.png");
	}
	
	public NinePatch getTab_normal()
	{
		return getRaw(IMGS_ROOT+"/tab_normal.9.png");
	}
	
	public NinePatch getTextView_normal()
	{
		return getRaw(IMGS_ROOT+"/text_view_in_tool_bg.9.png");
	}
	
	/** 一个圆角背景图 */
	public NinePatch getEacherRoundBg()
	{
		return getRaw(IMGS_ROOT+"/eacher_round_bg.9.png");
	}
}